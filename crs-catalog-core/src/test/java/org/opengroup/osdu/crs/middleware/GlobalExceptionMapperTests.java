package org.opengroup.osdu.crs.middleware;

import org.eclipse.jetty.http.BadMessageException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.opengroup.osdu.core.common.logging.JaxRsDpsLog;
import org.opengroup.osdu.crs.util.AppError;
import org.opengroup.osdu.crs.util.AppException;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionMapperTests {

    @Mock
    private JaxRsDpsLog log;

    @InjectMocks
    private GlobalExceptionMapper sut;

    @Test
    public void should_useValuesInAppExceptionInResponse_When_AppExceptionIsHandledByGlobalExceptionMapper() throws Exception {
        AppException exception = new AppException(409, "any reason", "any message");

        ResponseEntity<AppError> response = sut.handleAppException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(409, body.getCode());
        assertEquals("any message", body.getMessage());
        assertEquals("any reason", body.getReason());
        Mockito.verify(log).error(Mockito.any(), Mockito.any(AppException.class));
    }

    @Test
    public void should_useBadMassageValues_When_AppExceptionIsHandledByGlobalExceptionMapper() {
        BadMessageException exception = new BadMessageException(400, "bad request reason");

        ResponseEntity<AppError> response = sut.handleBadMessageException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Please check the input type and format and try again.", body.getMessage());
        assertEquals("Bad input type or format.", body.getReason());
        Mockito.verify(log).error(Mockito.any(), Mockito.any(AppException.class));
    }

    @Test
    public void should_useGenericValuesInResponse_When_ExceptionIsHandledByGlobalExceptionMapper() {
        Exception exception = new Exception("any message");

        ResponseEntity<AppError> response = sut.handleGeneralException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getCode());
        assertEquals("An unknown error has occurred.", body.getMessage());
        assertEquals("Server error.", body.getReason());
        Mockito.verify(log).error(Mockito.any(), Mockito.any(AppException.class));
    }

    @Test
    public void shouldReturnBadRequest() {
        AppException exception = AppException.createBadRequest("Bad request");

        ResponseEntity<AppError> response = sut.handleAppException(exception);
        AppError body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getCode());
        assertEquals("Bad request", body.getMessage());
        assertEquals("Bad Request", body.getReason());
        Mockito.verify(log).error(Mockito.any(), Mockito.any(AppException.class));
    }
}
