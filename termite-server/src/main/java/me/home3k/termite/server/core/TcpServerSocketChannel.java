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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author home3k
 */
public class TcpServerSocketChannel extends AbstractChannel {

    private final int port;

    public TcpServerSocketChannel(int port, Handler handler) throws IOException {
        super(ServerSocketChannel.open(), handler);
        this.port = port;
    }

    @Override
    public int getInterestedOps() {
        return SelectionKey.OP_ACCEPT;
    }

    @Override
    public void bind() throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) this.getChannel();
        serverSocketChannel.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
        serverSocketChannel.configureBlocking(false);
    }

    @Override
    public ByteBuffer read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int read = socketChannel.read(buffer);
        buffer.flip();
        if(read == -1) {
            throw new IOException("Socket closed");
        }
        return buffer;
    }

    @Override
    protected void doWrite(Object pendingWrite, SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer)pendingWrite;
        ((SocketChannel)key.channel()).write(buffer);
    }
}
