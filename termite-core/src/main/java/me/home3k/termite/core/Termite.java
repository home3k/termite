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

import me.home3k.termite.core.constant.Constants;
import me.home3k.termite.core.meta.IocType;
import me.home3k.termite.core.meta.ServerType;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author home3k
 */
public class Termite {

    private String name = "termite";

    private IocType iocType = IocType.termite;

    private Object iocContainer;

    private ServerType serverType = ServerType.termite;

    private int port = 8761;

    private int threadCount = Runtime.getRuntime().availableProcessors() * 2;

    private List<Router> routers = new LinkedList<>();

    Termite() {}

    Termite(Properties config) {
        this.name = config.getProperty(Constants.NODE_NAME, this.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IocType getIocType() {
        return iocType;
    }

    public void setIocType(IocType iocType) {
        this.iocType = iocType;
    }

    public Object getIocContainer() {
        return iocContainer;
    }

    public void setIocContainer(Object iocContainer) {
        this.iocContainer = iocContainer;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public List<Router> getRouters() {
        return routers;
    }

    public void setRouters(List<Router> routers) {
        this.routers = routers;
    }
}
