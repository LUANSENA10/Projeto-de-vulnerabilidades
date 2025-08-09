package com.security.security.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDTO {

    private Integer errorCode;
    private String messageError;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dateTimeError;

    public ErrorResponseDTO(Integer errorCode, String messageError) {
        this.errorCode = errorCode;
        this.messageError = messageError;
        this.dateTimeError = LocalDateTime.now();
    }
}
