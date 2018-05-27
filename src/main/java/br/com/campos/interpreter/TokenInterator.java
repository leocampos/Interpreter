package br.com.campos.interpreter;

import br.com.campos.interpreter.tokens.Token;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class is responsible for ...
 */
public final class TokenInterator implements Iterator<Token> {
    private final List<Token> tokens;
    private int index = 0;

    TokenInterator(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean hasNext() {
        return index < tokens.size()-1;
    }

    @Override
    public Token next() {
        if(index >= tokens.size()) throw new NoSuchElementException();
        return tokens.get(index++);
    }
}
