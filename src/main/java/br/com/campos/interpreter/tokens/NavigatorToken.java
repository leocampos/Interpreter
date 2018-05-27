package br.com.campos.interpreter.tokens;

/**
 * This class is responsible for ...
 */
public class NavigatorToken implements Token {
    private final String value;

    public NavigatorToken(String value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getName() {
        return NAVIGATOR;
    }
}
