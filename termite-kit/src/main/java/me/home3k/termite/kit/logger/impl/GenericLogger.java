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

package me.home3k.termite.kit.logger.impl;

import me.home3k.termite.kit.logger.Logger;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author home3k
 */
public class GenericLogger implements Logger {

    enum Level {
        INFO, DEBUG, WARN, ERROR
    }

    private String name;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private static PrintStream printStream = System.out;


    public GenericLogger(Class<?> clazz) {
        this.name = clazz.getName();
    }

    public GenericLogger(String name) {
        this.name = name;
    }

    @Override
    public void info(String msg) {
        log(Level.INFO, msg, null, null);
    }

    @Override
    public void info(String msg, Object... params) {
        log(Level.INFO, msg, null, params);
    }

    @Override
    public void debug(String msg) {
        log(Level.DEBUG, msg, null, null);
    }

    @Override
    public void debug(String msg, Object... params) {
        log(Level.DEBUG, msg, null, params);
    }

    @Override
    public void warn(String msg) {
        log(Level.WARN, msg, null, null);
    }

    @Override
    public void warn(String msg, Object... params) {
        log(Level.WARN, msg, null, params);
    }

    @Override
    public void warn(String msg, Throwable ex) {
        log(Level.WARN, msg, ex, null);
    }

    @Override
    public void error(String msg) {
        log(Level.ERROR, msg, null, null);
    }

    @Override
    public void error(String msg, Object... params) {
        log(Level.ERROR, msg, null, params);
    }

    @Override
    public void error(String msg, Throwable ex) {
        log(Level.ERROR, msg, ex, null);
    }

    private static String now() {
        return sdf.format(new Date());
    }

    private static boolean needFormat(String msg) {
        return msg.indexOf("{}") != -1;
    }

    private String format(String msg, Object... params) {
        if (msg == null) {
            return null;
        }
        if (params == null || params.length == 0) {
            if (needFormat(msg)) {
                return msg.replaceAll("\\{\\}", "");
            } else {
                return msg;
            }
        } else {
            String formatedString = msg;
            if (needFormat(msg)) {
                formatedString = msg.replaceAll("\\{\\}", "%s");
            }
            return String.format(formatedString, params);
        }
    }

    private void log(Level level, String msg, Throwable ex, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(now()).append(" ").append(level.name()).append("[")
                .append(Thread.currentThread().getName()).append("] ")
                .append(this.name).append(" ").append(format(msg, params));
        printStream.println(sb.toString());
        if (ex != null) {
            ex.printStackTrace(System.err);
            System.err.flush();
        }
    }
}
