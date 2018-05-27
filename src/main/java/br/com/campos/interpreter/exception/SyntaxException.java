package br.com.campos.interpreter.exception;

import java.text.ParseException;

public class SyntaxException extends RuntimeException {
    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(Exception e) {
        super(e);
    }
}
