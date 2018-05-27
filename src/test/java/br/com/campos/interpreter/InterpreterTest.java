package br.com.campos.interpreter;

import br.com.campos.interpreter.expressions.*;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class InterpreterTest {
    private Any json = parseJson("{'a':{'b':{'c':'inner-value'}}}");

    @Test
    public void testNavigator() {
        Expression expression = Interpreter.of("a");

        assertTrue(expression instanceof NavigatorExpression);
    }

    @Test
    public void testNavigatorComposed() {
        Expression expression = Interpreter.of("a.b.c");

        assertTrue(expression instanceof NavigatorExpression);

        String eval = expression.eval(json).toString();

        assertEquals("inner-value", eval);
    }

    private Any parseJson(String json) {
        try {
            return JsonIterator.parse(json.replace('\'', '"')).readAny();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNumber() {
        Expression expression = Interpreter.of("1");

        assertTrue(expression instanceof LiteralExpression);
    }

    @Test
    public void testConcatenate() {
        Expression expression = Interpreter.of("CONCATENATE('a','b')");

        assertTrue(expression instanceof ConcatenateExpression);
        assertEquals("ab", expression.eval(null));
    }

    @Test
    public void testConcatenateWithNavigator() {
        Expression expression = Interpreter.of("CONCATENATE(a.b.c,'b')");

        assertTrue(expression instanceof ConcatenateExpression);
        assertEquals("inner-valueb", expression.eval(json));
    }

    @Test
    public void testBasicToDate() {
        Expression expression = Interpreter.of("TO_DATE('2018.05.22','yyyy.MM.dd','dd/MM/yyyy')");

        assertTrue(expression instanceof ToDateExpression);
        assertEquals("22/05/2018", expression.eval(json));
    }

    @Test
    public void testComplexExpression() {
        Expression expression = Interpreter.of("CONCATENATE(TO_DATE('2018.05.22','yyyy.MM.dd','MM/yyyy'), a.b.c)");

        assertEquals("05/2018inner-value", expression.eval(json));
    }


    @Test(expected = RuntimeException.class)
    public void testBadSintax() {
        //Concat is not an acceptable function. Navigator can't be followed by (
        Expression expression = Interpreter.of("CONCAT(TO_DATE('2018.05.22','yyyy.MM.dd','MM/yyyy'), a.b.c)");
    }
}