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

package me.home3k.termite.server.core;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author home3k
 */
public abstract class AbstractChannel {

    private final SelectableChannel channel;
    private final Handler handler;
    private TermiteReactor reactor;
    private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites = new ConcurrentHashMap<>();

    public AbstractChannel(SelectableChannel channel, Handler handler) {
        this.channel = channel;
        this.handler = handler;
    }

    public void setReactor(TermiteReactor reactor) {
        this.reactor = reactor;
    }

    public SelectableChannel getChannel() {
        return channel;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public abstract int getInterestedOps();

    public abstract void bind() throws IOException;

    public abstract Object read(SelectionKey key) throws IOException;


    protected void flush(SelectionKey key) throws IOException {
        Queue<Object> pendingWrites = channelToPendingWrites.get(key.channel());
        while (true) {
            Object pendingWrite = pendingWrites.poll();
            if (pendingWrite == null) {
                reactor.changeOps(key, SelectionKey.OP_READ);
                break;
            }

            doWrite(pendingWrite, key);
        }
    }


    protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;

        public void write(Object data, SelectionKey key) {
        Queue<Object> pendingWrites = this.channelToPendingWrites.get(key.channel());
        if (pendingWrites == null) {
            synchronized (this.channelToPendingWrites) {
                pendingWrites = this.channelToPendingWrites.get(key.channel());
                if (pendingWrites == null) {
                    pendingWrites = new ConcurrentLinkedQueue<>();
                    this.channelToPendingWrites.put(key.channel(), pendingWrites);
                }
            }
        }
        pendingWrites.add(data);
        reactor.changeOps(key, SelectionKey.OP_WRITE);
    }
}
