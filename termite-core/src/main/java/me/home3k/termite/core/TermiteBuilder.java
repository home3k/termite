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
        ServerStep type(String type);

    }

    public interface IocStep {
    }

    public interface RouterStep {
    }

    public interface ConfigStep {
        BuildStep config(String configFile);
    }

    public interface BuildStep {
        Termite build();
    }


    private static class TermiteSteps implements ConfigStep, NameStep, BuildStep {

        private String name;

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
            return null;
        }

        @Override
        public ServerStep noName() {
            return null;
        }

        @Override
        public Termite build() {
            Termite termite = new Termite();
            return null;
        }

        @Override
        public BuildStep config(String configFile) {
            return null;
        }
    }

}
