package br.com.campos.interpreter.expressions;

import br.com.campos.interpreter.exception.SyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * This class is responsible for converting a String representing a date in the FromFormat to a new String representation of a date in the ToFormat.
 */
public class ToDateExpression implements Expression {
    private final Expression expression;
    private final Expression fromFormat;
    private final Expression toFormat;

    @Override
    public Object eval(Object input) {
        String evaluated = expression.eval(input).toString();
        SimpleDateFormat fromUser = new SimpleDateFormat(this.fromFormat.eval(input).toString());
        SimpleDateFormat myFormat = new SimpleDateFormat(this.toFormat.eval(input).toString());

        try {
            return myFormat.format(fromUser.parse(evaluated));
        } catch (ParseException e) {
            throw new SyntaxException(e);
        }
    }

    public ToDateExpression(Expression expression, Expression fromFormat, Expression toFormat) {
        this.expression = expression;
        this.fromFormat = fromFormat;
        this.toFormat = toFormat;
    }
}
