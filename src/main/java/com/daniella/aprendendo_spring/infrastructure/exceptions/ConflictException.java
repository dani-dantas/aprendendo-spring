package com.daniella.aprendendo_spring.infrastructure.exceptions;

public class ConflictException extends RuntimeException {

    public ConflictException(String mensangem) {
        super(mensangem);
    }

    public ConflictException(String mensangem, Throwable throwable) {
        super(mensangem);
    }

}
