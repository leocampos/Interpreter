package br.com.campos.interpreter.tokens;

/**
 * This class is responsible for ...
 */
public class FunctionToken implements Token {
    private final String name;

    public FunctionToken(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getValue() {
        return null;
    }
}
