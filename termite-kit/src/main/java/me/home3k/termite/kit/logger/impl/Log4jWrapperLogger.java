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

/**
 * @author home3k
 */
public class Log4jWrapperLogger implements Logger {

    private org.apache.log4j.Logger LOG;

    public Log4jWrapperLogger(Class<?> clazz) {
        this.LOG = org.apache.log4j.Logger.getLogger(clazz);
    }

    public Log4jWrapperLogger(String name) {
        this.LOG = org.apache.log4j.Logger.getLogger(name);
    }

    @Override
    public void info(String msg) {
        LOG.info(msg);
    }

    @Override
    public void info(String msg, Object... params) {
    }

    @Override
    public void debug(String msg) {
        LOG.debug(msg);
    }

    @Override
    public void debug(String msg, Object... params) {
    }

    @Override
    public void warn(String msg) {
        LOG.warn(msg);
    }

    @Override
    public void warn(String msg, Object... params) {
    }

    @Override
    public void warn(String msg, Throwable ex) {
        LOG.warn(msg, ex);
    }

    @Override
    public void error(String msg) {
        LOG.error(msg);
    }

    @Override
    public void error(String msg, Object... params) {
    }

    @Override
    public void error(String msg, Throwable ex) {
        LOG.error(msg, ex);
    }
}
