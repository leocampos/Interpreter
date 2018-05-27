package br.com.campos.interpreter;

import br.com.campos.interpreter.exception.SyntaxException;
import br.com.campos.interpreter.tokens.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is responsible for transforming a String into an Iterator of Tokens.
 */
final class LexicalTokenizer {
    private LexicalTokenizer() {}

    static TokenInterator parse(String command) {
        AtomicInteger inc = new AtomicInteger(0);
        List<Token> tokens = new ArrayList<>();
        Token token;
        StringBuilder builder = new StringBuilder();

        while(inc.get() < command.length()) {
            char actual = command.charAt(inc.get());

            switch (actual) {
                case '"':
                case '\'':
                    inc.incrementAndGet();
                    consumeBuffer(builder, tokens);
                    tokens.add(consumeText(command, actual, inc));
                    break;
                case ',':
                case '(':
                case ')':
                    consumeBuffer(builder, tokens);
                    tokens.add(new SymbolToken(actual));
                    inc.incrementAndGet();
                    break;
                case ' ':
                case '\t':
                    inc.incrementAndGet();
                    break;
                case 'T':
                    token = tryToConsumeToDate(command, inc);
                    if(token != null) {
                        consumeBuffer(builder, tokens);

                        tokens.add(token);
                    } else {
                        saveInBuffer(builder, actual, inc);
                    }

                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    tokens.add(consumeNumber(command, inc));
                    break;
                case 'C':
                    token = tryToConsumeConcatenate(command, inc);
                    if(token != null) {
                        consumeBuffer(builder, tokens);

                        tokens.add(token);
                    } else {
                        saveInBuffer(builder, actual, inc);
                    }
                    break;
                default:
                    saveInBuffer(builder, actual, inc);
            }


        }
        consumeBuffer(builder, tokens);
        tokens.add(new EOFToken());

        return new TokenInterator(tokens);
    }

    private static LiteralToken consumeNumber(String command, AtomicInteger inc) {
        StringBuilder builder = new StringBuilder();
        while(inc.get() < command.length()) {
            char c = command.charAt(inc.get());

            if(isNumber(c)) {
                builder.append(c);
                inc.incrementAndGet();
            } else {
                break;
            }
        }

        return new LiteralToken(builder.toString());
    }

    private static boolean isNumber(char c) {
        return Character.getNumericValue(c) != -1 || c == '.';
    }

    private static void saveInBuffer(StringBuilder builder, char actual, AtomicInteger inc) {
        builder.append(actual);
        inc.incrementAndGet();
    }

    private static void consumeBuffer(StringBuilder builder, List<Token> tokens) {
        if(builder.length() > 0) {
            tokens.add(new NavigatorToken(builder.toString()));
        }

        builder.delete(0, builder.length());
    }

    private static Token tryToConsumeConcatenate(String command, AtomicInteger inc) {
        int concatenateLength = Token.CONCATENATE.length();
        int possibleFinal = inc.get() + concatenateLength;
        if(command.length() < possibleFinal) return null;

        if(command.substring(inc.get(), possibleFinal).equals(Token.CONCATENATE)) {
            inc.set(possibleFinal);

            return new FunctionToken(Token.CONCATENATE);
        }

        return null;
    }

    private static Token tryToConsumeToDate(String command, AtomicInteger inc) {
        int toDateLength = Token.TO_DATE.length();
        int possibleFinal = inc.get() + toDateLength;
        if(command.length() < possibleFinal) return null;

        if(command.substring(inc.get(), possibleFinal).equals(Token.TO_DATE)) {
            inc.set(possibleFinal);

            return new FunctionToken(Token.TO_DATE);
        }

        return null;
    }

    private static LiteralToken consumeText(String command, char c, AtomicInteger inc) {
        int ending = findStringEnding(command, c, inc.get());

        LiteralToken token = new LiteralToken(command.substring(inc.get(), ending));

        inc.getAndSet(ending + 1);

        return token;
    }

    private static int findStringEnding(String command, char c, int i) {
        int nextQuotation = command.indexOf(c, i + 1);
        if(nextQuotation == -1) {
            throw new SyntaxException("Unfinished String");
        }

        if('\\' == command.charAt(nextQuotation - 1)) {
            //Start over, it was not really the end...
            nextQuotation = findStringEnding(command, c, i + 1);
        }

        return nextQuotation;
    }
}
