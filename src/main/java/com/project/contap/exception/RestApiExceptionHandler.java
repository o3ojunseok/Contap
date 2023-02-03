package com.project.contap.exception;


import com.project.contap.common.util.AddLog;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleApiRequestException(
            HttpServletRequest request,
            Exception ex) throws Exception {
        RestApiException restApiException = new RestApiException();
        restApiException.setResult("fail");

        if (ex instanceof ContapException) // 처리한 예외이면
        {
            restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
            restApiException.setErrorMessage(((ContapException) ex).getErrorCode().getMessage());
        } else // 아니면
        {
            restApiException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            restApiException.setErrorMessage("서버에 문의해주세요");
            Sentry.captureException(ex);
        }
        return new ResponseEntity(
                restApiException,
                HttpStatus.OK
        );
    }
}

