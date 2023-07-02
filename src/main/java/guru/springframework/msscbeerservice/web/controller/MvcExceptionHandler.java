package guru.springframework.msscbeerservice.web.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class MvcExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrorSummary>> handleCve(MethodArgumentNotValidException manv) {
        List<FieldErrorSummary> fieldErrorSummaryList = manv.getFieldErrors().stream().map(FieldErrorSummary::new).collect(Collectors.toList());
        return new ResponseEntity<>(fieldErrorSummaryList, HttpStatus.BAD_REQUEST);
    }

    @NoArgsConstructor
    @Getter
    private static class FieldErrorSummary {
        private String fieldName;
        private String errorCode;
        private FieldErrorSummary(FieldError fieldError) {
            this.fieldName = fieldError.getField();
            this.errorCode = fieldError.getCode();
        }
    }
}
