package br.com.campos.interpreter.tokens;

/**
 * This class is responsible for ...
 */
public final class LiteralToken implements Token {
    private final String value;

    public LiteralToken(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return TEXT;
    }
}
