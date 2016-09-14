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

package me.home3k.termite.server;

import me.home3k.termite.server.core.*;

import java.io.IOException;

/**
 * @author home3k
 */
public class TermiteServer implements Server {

    private int workerCount = Runtime.getRuntime().availableProcessors() * 2;

    private int execThreadCount = 16;

    private int maxRequestSize = 1014 * 1024 * 10;

    private TermiteReactor reactor;

    private Dispatcher dispatcher;

    private int port;

    private Handler handler;

    public TermiteServer(int port, Handler handler) {
        this.port = port;
        this.handler = handler;
    }


    protected void init() throws IOException {
        dispatcher = new ThreadPoolDispatcher(execThreadCount);
        reactor = new TermiteReactor(dispatcher);
        TcpServerSocketChannel socketChannel = new TcpServerSocketChannel(port, handler);
        socketChannel.bind();
        reactor.registerChannel(socketChannel);
    }

    @Override
    public void start() {
        try {
            init();
            reactor.start();
        } catch (IOException e) {
            // TODO
        }
    }

    @Override
    public void stop() {

    }
}
