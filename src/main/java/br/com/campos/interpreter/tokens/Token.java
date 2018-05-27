package br.com.campos.interpreter.tokens;

public interface Token {
    public static final String CONCATENATE = "CONCATENATE";
    public static final String TO_DATE = "TO_DATE";
    public static final String SYMBOL = "SYMBOL";
    public static final String NAVIGATOR = "NAVIGATOR";
    public static final String TEXT = "TEXT";
    public static final String EOF = "EOF";
    public static final String NUMBER = "NUMBER";


    Object getValue();

    String getName();

    default boolean isEOF() {
        return false;
    }
}
