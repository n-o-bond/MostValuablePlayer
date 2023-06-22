package com.example.mostvaluableplayer.exception;


public class FailedParsingFileException extends RuntimeException {
    public FailedParsingFileException(String message) {
        super(message);
    }

    public FailedParsingFileException(String message, Throwable cause) {
        super(message, cause);
    }
}