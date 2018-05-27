package br.com.campos.interpreter.tokens;

/**
 * This class is responsible for ...
 */
public class SymbolToken implements Token {
    private final char actual;

    public SymbolToken(char actual) {
        this.actual = actual;
    }

    @Override
    public String getName() {
        return SYMBOL;
    }

    @Override
    public Object getValue() {
        return actual;
    }
}
