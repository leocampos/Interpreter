package br.com.campos.interpreter.tokens;

/**
 * This class is responsible for marking the end of the Stream of tokens.
 */
public class EOFToken implements Token {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public String getName() {
        return EOF;
    }

    @Override
    public boolean isEOF() {
        return true;
    }
}
