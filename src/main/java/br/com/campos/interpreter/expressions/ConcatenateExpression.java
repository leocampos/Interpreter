package br.com.campos.interpreter.expressions;

/**
 * This class is responsible for evaluating Concatenate expressions.
 *
 * It accepts any number of parameters, evaluates them and turns them into strings and concatenate them.
 */
public class ConcatenateExpression implements Expression {
    private final Expression[] params;

    public ConcatenateExpression(Expression...params) {
        this.params = params;
    }

    @Override
    public String eval(Object o) {
        StringBuilder builder = new StringBuilder();
        for(Expression e : params) {
            builder.append(e.eval(o).toString());
        }

        return builder.toString();
    }
}
