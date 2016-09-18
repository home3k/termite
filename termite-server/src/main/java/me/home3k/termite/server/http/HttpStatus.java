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

package me.home3k.termite.server.http;

/**
 * @author home3k
 */
public enum HttpStatus {

    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "NO CONTENT"),
    NOT_MODIFIED(304, "NOT MODIFIED"),
    BAD_REQUEST(400, "BAD REQUEST"),
    UNAUTHORIED(401, "UNAUTHORIED"),
    FORBIDDEN(403, "FORBIDDEN"),
    NOT_FOUND(404, "NOT FOUND"),
    METHOD_NOT_ALLOWED(405, "METHOD NOT ALLOWED"),
    REQUEST_TIMEOUT(408, "REQUEST TIMEOUT"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
    BAD_GATEWAY(502, "BAD GATEWAY"),
    SERVICE_UNAVAILABLE(503, "SERVICE UNAVAILABLE"),
    GATEWAY_TIMEOUT(504, "GATEWAY TIMEOUT");

    private int status;

    private String content;

    private HttpStatus(int status, String content) {
        this.status = status;
        this.content = content;
    }
}
