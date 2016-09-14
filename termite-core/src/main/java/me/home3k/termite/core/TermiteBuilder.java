/*
 * Copyright (C) 2016-2020 The Termite Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package me.home3k.termite.core;

import me.home3k.termite.core.config.ConfigLoader;
import me.home3k.termite.core.meta.IocType;
import me.home3k.termite.core.meta.ServerType;

import java.util.*;

/**
 * @author home3k
 */
public final class TermiteBuilder {
    private TermiteBuilder() {
    }

    public static BuildStep newBuilder(String configFile) {
        return new TermiteSteps().config(configFile);
    }

    public static NameStep newBuilder() {
        return new TermiteSteps();
    }

    public interface NameStep {

        BuildStep defaultInfo();

        BuildStep defaultInfo(String name);

        ServerStep name(String name);

        ServerStep noName();
    }

    public interface ServerStep {

        IocStep defaultServer();

        ServerStep serverType(String type);

        ServerStep port(int port);

        ServerStep threadCount(int threadCount);

        IocStep noMoreServerConfig();

    }

    public interface IocStep {

        RouterStep defaultIoc();

        IocStep iocType(String type);

        IocStep container(Object object);

        RouterStep noMoreIocConfig();
    }

    public interface RouterStep {

        BuildStep noRouter();

        RouterStep router(Router router);

        BuildStep noMoreRouter();
    }

    public interface ConfigStep {
        BuildStep config(String configFile);
    }

    public interface BuildStep {
        Termite build();
    }


    private static class TermiteSteps implements ConfigStep, NameStep, ServerStep, IocStep, RouterStep, BuildStep {

        private Properties config;

        private String name;
        private String serverType;
        private int port;
        private int threadCount;
        private String iocType;
        private Object container;

        private List<Router> routers = new LinkedList<>();

        @Override
        public BuildStep defaultInfo() {
            return this;
        }

        @Override
        public BuildStep defaultInfo(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ServerStep name(String name) {
            this.name = name;
            return this;
        }

        @Override
        public ServerStep noName() {
            this.name = null;
            return this;
        }

        @Override
        public RouterStep defaultIoc() {
            return this;
        }

        @Override
        public IocStep container(Object object) {
            this.container = object;
            return this;
        }

        @Override
        public RouterStep noMoreIocConfig() {
            return this;
        }

        @Override
        public BuildStep noRouter() {
            this.routers.clear();
            return this;
        }

        @Override
        public RouterStep router(Router router) {
            this.routers.add(router);
            return this;
        }

        @Override
        public BuildStep noMoreRouter() {
            return this;
        }

        @Override
        public IocStep defaultServer() {
            return this;
        }

        @Override
        public ServerStep serverType(String type) {
            this.serverType = type;
            return this;
        }

        @Override
        public IocStep iocType(String type) {
            this.iocType = type;
            return this;
        }

        @Override
        public ServerStep port(int port) {
            this.port = port;
            return this;
        }

        @Override
        public ServerStep threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        @Override
        public IocStep noMoreServerConfig() {
            return this;
        }

        @Override
        public Termite build() {
            Termite termite;
            if (config != null) {
                return new Termite(config);
            } else {
                termite = new Termite();
            }
            if (present(this.name)) termite.setName(this.name);
            if (present(this.iocType)) termite.setIocType(IocType.valueOf(this.iocType.toLowerCase()));
            if (present(this.container)) termite.setIocContainer(this.container);
            if (present(this.port)) termite.setPort(this.port);
            if (present(this.serverType)) termite.setServerType(ServerType.valueOf(this.serverType.toLowerCase()));
            if (present(this.threadCount)) termite.setThreadCount(this.threadCount);
            if (present(this.routers)) termite.setRouters(this.routers);

            return termite;
        }

        @Override
        public BuildStep config(String configFile) {
            this.config = ConfigLoader.load(configFile);
            return this;
        }

        private static boolean present(Object object) {
            if (object == null) {
                return false;
            } else {
                if (object instanceof Collection<?>) {
                    return !((Collection<?>) object).isEmpty();
                } else {
                    return true;
                }
            }
        }
    }

}
