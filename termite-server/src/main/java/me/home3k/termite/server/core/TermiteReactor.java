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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author home3k
 */
public class TermiteReactor {

    private final Selector selector;

    private final Dispatcher dispatcher;

    private final Queue<Runnable> pendingCommands = new ConcurrentLinkedDeque<>();

    private final ExecutorService boss = Executors.newSingleThreadExecutor();

    public TermiteReactor(Dispatcher dispatcher) throws IOException {
        this.selector = Selector.open();
        this.dispatcher = dispatcher;
    }

    public void start() throws IOException {
        boss.execute(() -> {
            try {
                // TODO logging
                loop();
            } catch (IOException ex) {
                // TODO
            }
        });
    }

    public void stop() throws InterruptedException, IOException {
        boss.shutdownNow();
        selector.wakeup();
        boss.awaitTermination(3, TimeUnit.SECONDS);
        selector.close();
    }

    public TermiteReactor registerChannel(AbstractChannel channel) throws IOException {
        SelectionKey selectionKey = channel.getChannel().register(selector, channel.getInterestedOps());
        selectionKey.attach(channel);
        channel.setReactor(this);
        return this;
    }

    private void loop() throws IOException {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

            processPendingCommands();

            selector.select();

            Set<SelectionKey> selectedKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectedKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (!selectionKey.isValid()) {
                    iterator.remove();
                    continue;
                }
                process(selectionKey);
            }
            selectedKeys.clear();
        }
    }

    private void processPendingCommands() {
        Iterator<Runnable> iterator = pendingCommands.iterator();
        while (iterator.hasNext()) {
            Runnable command = iterator.next();
            command.run();
            iterator.remove();
        }
    }

    private void process(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            onChannelAcceptable(key);
        } else if (key.isReadable()) {
            onChannelReadable(key);
        } else if (key.isWritable()) {
            onChannelWritable(key);
        } else {
            // TODO
        }
    }

    protected void onChannelAcceptable(SelectionKey acceptableKey) throws IOException{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) acceptableKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        SelectionKey readKey = socketChannel.register(selector, SelectionKey.OP_READ);
        readKey.attach(acceptableKey.attachment());

    }

    protected void onChannelReadable(SelectionKey readableKey) {
        AbstractChannel channel = (AbstractChannel)readableKey.attachment();
        try {
            Object readObject = channel.read(readableKey);
            dispatcher.onChannelReadEvent(channel, readObject, readableKey);
        } catch (IOException e) {
            try {
                readableKey.channel().close();
            } catch (IOException ex) {
                // TODO
            }
        }
    }

    protected void onChannelWritable(SelectionKey writableKey) throws IOException {
        AbstractChannel channel = (AbstractChannel)writableKey.attachment();
        channel.flush(writableKey);
    }

    protected void changeOps(SelectionKey key, int interestedOps) {
        pendingCommands.add(new ChangeKeyOpsCommand(key, interestedOps));
        selector.wakeup();
    }

    class ChangeKeyOpsCommand implements Runnable {

        private SelectionKey key;
        private int interestedOps;

        public ChangeKeyOpsCommand(SelectionKey key, int interestedOps) {
            this.key = key;
            this.interestedOps = interestedOps;
        }

        public void run() {
            key.interestOps(interestedOps);
        }

    }
}
