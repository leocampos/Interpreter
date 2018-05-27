package br.com.campos.interpreter.expressions;

/**
 * This class is responsible for Evaluating numbers and text.
 */
public class LiteralExpression implements Expression {
    private final String literalValue;

    public LiteralExpression(String literalValue) {
        this.literalValue = literalValue;
    }

    @Override
    public Object eval(Object input) {
        return literalValue;
    }
}
