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

package me.home3k.termite.core.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author home3k
 */
public class ConfigLoader {

    public static Properties load(String configFile) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream propIn = cl.getResourceAsStream(configFile);
        Properties prop = new Properties();
        try {
            prop.load(propIn);
        } catch (Throwable e) {
            throw new Error(e);
        }
        return prop;
    }
}
