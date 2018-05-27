package br.com.campos.interpreter.expressions;

import com.jsoniter.any.Any;

/**
 * This class is responsible for Navigating into a Json object and retrieving its value.
 */
public class NavigatorExpression implements Expression {
    private final String value;


    public NavigatorExpression(String value) {
        this.value = value;
    }

    @Override
    public Object eval(Object input) {
        return evaluate(value, (Any)input);
    }

    private Any evaluate(String value, Any source) {
        int posNav = value.indexOf('.');
        if(posNav == -1) return source.get(value);

        String head = value.substring(0, posNav);
        String rest = value.substring(posNav + 1);

        return evaluate(rest, source.get(head));
    }
}
