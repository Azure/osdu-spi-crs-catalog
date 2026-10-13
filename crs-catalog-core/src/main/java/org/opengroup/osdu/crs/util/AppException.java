// Copyright 2017-2019, Schlumberger
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.opengroup.osdu.crs.util;

import lombok.ToString;
import org.apache.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ToString
public class AppException extends RuntimeException {

    private AppError error;

    public AppError getError() {
        return this.error;
    }

    public AppException(int status, String reason, String message) {
        this.error = AppError.builder().code(status).reason(reason).message(message).build();
    }

    public AppException(int status, String reason, String message, String debuggingInfo) {
        this.error = AppError.builder().code(status).reason(reason).message(message).debuggingInfo(debuggingInfo).build();
    }

    public AppException(int status, String reason, String message, Exception originalException) {
        this.error = AppError.builder().code(status).reason(reason).message(message).originalException(originalException).build();
    }

    public static AppException createUnauthorized(String debuggingInfo) {
        return new AppException(HttpStatus.SC_UNAUTHORIZED, "Unauthorized", "The user is not authorized to perform this action", debuggingInfo);
    }

    public static AppException createBadRequest(String message) {
        return new AppException(HttpStatus.SC_BAD_REQUEST, "Bad Request", message);
    }

    public static AppException createNotFound(String message) {
        return new AppException(HttpStatus.SC_NOT_FOUND, "Not found", message);
    }
}
