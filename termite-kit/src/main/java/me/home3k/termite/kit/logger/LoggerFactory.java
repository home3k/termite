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

package me.home3k.termite.kit.logger;

/**
 * @author home3k
 */
public class LoggerFactory {

    private static final boolean slf4jAvailable = present("org.slf4j.Logger");
    private static final boolean log4jAvailable = present("org.apache.log4j.Logger");

    public static boolean present(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    public static Logger getLogger(Class clazz) {
        if (slf4jAvailable) {
            return new Slf4jWrapperLogger(clazz);
        } else if (log4jAvailable) {
            return new Log4jWrapperLogger(clazz);
        }
        return new GenericLogger(clazz);
    }


}
