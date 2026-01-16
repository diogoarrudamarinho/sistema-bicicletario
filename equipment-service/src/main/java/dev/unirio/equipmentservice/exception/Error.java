package dev.unirio.equipmentservice.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Error(
    String mensagem,
    int status,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    LocalDateTime timestamp
) {

    public Error(String mensagem, int status) {
        this(mensagem, status, LocalDateTime.now());
    }
}
