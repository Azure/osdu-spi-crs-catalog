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

package org.opengroup.osdu.crs.middleware;

import org.eclipse.jetty.http.BadMessageException;
import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.crs.util.AppError;
import org.opengroup.osdu.crs.util.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.inject.Inject;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionMapper extends ResponseEntityExceptionHandler {

    @Inject
    private JaxRsDpsLog log;

    @ExceptionHandler(AppException.class)
    protected ResponseEntity<AppError> handleAppException(AppException e) {
        return this.getErrorResponse(e);
    }

    @ExceptionHandler(BadMessageException.class)
    protected ResponseEntity<AppError> handleBadMessageException(BadMessageException e) {
        /*When invalid input types are passed(recordId or dataId = "%"), it causes BadMessageException.
         Handled it with generic error with code 400 (Bad Request).*/
        return this.getErrorResponse(new AppException(BAD_REQUEST.value(), "Bad input type or format.", "Please check the input type and format and try again."));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<AppError> handleGeneralException(Exception e) {
        return this.getErrorResponse(
                new AppException(INTERNAL_SERVER_ERROR.value(), "Server error.",
                        "An unknown error has occurred."));
    }

    private ResponseEntity<AppError> getErrorResponse(AppException e) {
        log.error(e.getMessage(), e);
        AppError appError = e.getError();
        return new ResponseEntity<>(appError, resolve(appError.getCode()));
    }
}
