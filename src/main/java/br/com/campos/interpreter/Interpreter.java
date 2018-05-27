package br.com.campos.interpreter;

import br.com.campos.interpreter.exception.SyntaxException;
import br.com.campos.interpreter.expressions.*;
import br.com.campos.interpreter.tokens.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for Transforming a String into an Expression that can be evaluated.
 */
public final class Interpreter {
    private Interpreter() {}

    static Expression of(String command) {
        TokenInterator tokenInterator = LexicalTokenizer.parse(command);
        Expression e = null;

        if(tokenInterator.hasNext()) {
            Token token = tokenInterator.next();

            if(token.isEOF()) {
                return null;
            } else if (token.getName().equals(Token.CONCATENATE)) {
                e = consumeCocatenate(tokenInterator);
            } else if (token.getName().equals(Token.TO_DATE)) {
                e = consumeToDate(tokenInterator);
            } else if (token.getName().equals(Token.NAVIGATOR)) {
                e = new NavigatorExpression(token.getValue().toString());
            } else if (token.getName().equals(Token.TEXT)) {
                e = new LiteralExpression(token.getValue().toString());
            } else if (token.getName().equals(Token.NUMBER)) {
                e = new LiteralExpression(token.getValue().toString());
            } else if (token.getName().equals(Token.SYMBOL)) {
                throw  new SyntaxException(token.getValue() + " is unacceptable at this point.");
            }
        }

        if(tokenInterator.hasNext() && !tokenInterator.next().isEOF())
            throw  new SyntaxException(" is unacceptable at this point.");

        return e;
    }

    private static Expression consumeCocatenate(TokenInterator tokenInterator) {
        List<Expression> parameters = new ArrayList<>();
        //Next token must be '('
        consumeOpenParenthesis(tokenInterator);

        consumeParameter(tokenInterator, parameters);
        consumeNextParameter(tokenInterator, parameters);

        //next should be either another comma + param or a closing )
        if(!tokenInterator.hasNext()) throw new SyntaxException("Unclosed pair ()");

        while (tokenInterator.hasNext()) {
            Token token = tokenInterator.next();

            if (isClosingParenthesis(token)) {
                return new ConcatenateExpression(parameters.toArray(new Expression[]{}));
            } else {
                consumeNextParameter(tokenInterator, parameters);
            }
        }

        throw new SyntaxException("Concatenate is not well formed");
    }

    private static Expression consumeToDate(TokenInterator tokenInterator) {
        List<Expression> parameters = new ArrayList<>();
        //Next token must be '('
        consumeOpenParenthesis(tokenInterator);

        //From date
        consumeParameter(tokenInterator, parameters);
        consumeComma(tokenInterator);

        //From format
        consumeParameter(tokenInterator, parameters);
        consumeComma(tokenInterator);

        //To format
        consumeParameter(tokenInterator, parameters);

        Token token = tokenInterator.next();
        if (isClosingParenthesis(token)) {
            return new ToDateExpression(parameters.get(0), parameters.get(1), parameters.get(2));
        }

        throw new SyntaxException("ToDate is not well formed");
    }

    private static boolean isClosingParenthesis(Token token) {
        return token.getName().equals(Token.SYMBOL) && token.getValue().equals(')');
    }

    private static void consumeNextParameter(TokenInterator tokenInterator, List<Expression> parameters) {
        consumeComma(tokenInterator);
        consumeParameter(tokenInterator, parameters);
    }

    private static void consumeComma(TokenInterator tokenInterator) {
        Token token;
        //Next token must be ,
        if(!tokenInterator.hasNext()) throw new SyntaxException("CONCATENATE should have at least two params separated by ,");
        token = tokenInterator.next();
        if(!isComma(token)) throw new SyntaxException("CONCATENATE should have at least two params separated by ,");
    }

    private static void consumeOpenParenthesis(TokenInterator tokenInterator) {
        String message = "'(' is expected after a function";
        if(!tokenInterator.hasNext()) {
            throw new SyntaxException(message);
        }
        Token token = tokenInterator.next();
        if(!isOpenParenthesis(token)) throw new SyntaxException(message);
    }

    private static void consumeParameter(TokenInterator tokenInterator, List<Expression> parameters) {
        if(!tokenInterator.hasNext()) throw new SyntaxException("parameters should be either Navigator, Function or Text");
        Token token = tokenInterator.next();

        switch (token.getName()) {
            case Token.CONCATENATE:
                parameters.add(consumeCocatenate(tokenInterator));
                break;
            case Token.TO_DATE:
                parameters.add(consumeToDate(tokenInterator));
                break;
            case Token.NAVIGATOR:
                parameters.add(new NavigatorExpression(token.getValue().toString()));
                break;
            case Token.TEXT:
                parameters.add(new LiteralExpression(token.getValue().toString()));
                break;
            default:
                throw new SyntaxException("parameters should be either Navigator, Function or Text");
        }
    }

    private static boolean isComma(Token token) {
        return token.getName().equals(Token.SYMBOL) && token.getValue().equals(',');
    }

    private static boolean isOpenParenthesis(Token token) {
        return token.getName().equals(Token.SYMBOL) && token.getValue().equals('(');
    }
}
