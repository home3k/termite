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

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author home3k
 */
public class TermiteReactor {

    private final Selector selector;

    private final Dispatcher dispatcher;

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

    private void loop() throws IOException {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }

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

    private void process(SelectionKey key) {
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

    protected void onChannelAcceptable(SelectionKey acceptableKey) {
    }

    protected void onChannelReadable(SelectionKey readableKey) {
    }

    protected void onChannelWritable(SelectionKey writableKey) {
    }
}
