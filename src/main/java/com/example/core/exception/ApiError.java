package com.example.core.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    @JsonProperty("error_code")
    private ErrorCode errorCode;
    @JsonProperty("http_code")
    private int httpCode;
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date timestamp;

    private List<ApiSubError> subErrors;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("additional_data")
    private JsonNode additionalData;

    @JsonCreator
    public ApiError(@JsonProperty("status") int status,
                    @JsonProperty("error_code") ErrorCode errorCode,
                    @JsonProperty("error_message") String errorMessage) {
        this.httpCode = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = Calendar.getInstance().getTime();
    }
}

abstract class ApiSubError {
}

@Data
@AllArgsConstructor
class ApiValidationError extends ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}