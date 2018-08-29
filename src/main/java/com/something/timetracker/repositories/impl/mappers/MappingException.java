package com.something.timetracker.repositories.impl.mappers;

class MappingException extends RuntimeException {
    MappingException(String message) {
        super(message);
    }

    MappingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
