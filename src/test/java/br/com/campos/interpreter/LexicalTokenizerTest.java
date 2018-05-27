package br.com.campos.interpreter;

import br.com.campos.interpreter.tokens.Token;
import org.junit.Assert;
import org.junit.Test;

public class LexicalTokenizerTest {
    @Test
    public void testEmptyParserShouldGenerateATokenIteratorWithOnlyEOFToken() {
        TokenInterator tokenInterator = LexicalTokenizer.parse("");
        Assert.assertEquals(Token.EOF, tokenInterator.next().getName());
    }

    @Test
    public void testTextOnlyParserShouldGenerateATokenIteratorWithTextAndEOF() {
        TokenInterator tokenInterator = LexicalTokenizer.parse("'A String'");
        Token textToken = tokenInterator.next();
        Assert.assertEquals(Token.TEXT, textToken.getName());
        Assert.assertEquals("A String", textToken.getValue());
        Assert.assertEquals(Token.EOF, tokenInterator.next().getName());
    }

    @Test
    public void testTextWithEscape() {
        TokenInterator tokenInterator = LexicalTokenizer.parse("'A String \\' with escape'");
        Token textToken = tokenInterator.next();
        Assert.assertEquals(Token.TEXT, textToken.getName());
        Assert.assertEquals("A String \\' with escape", textToken.getValue());
    }

    @Test
    public void testToDate() {
        TokenInterator tokenInterator = LexicalTokenizer.parse(Token.TO_DATE);
        Token textToken = tokenInterator.next();
        Assert.assertEquals(Token.TO_DATE, textToken.getName());
    }

    @Test
    public void testConcatenate() {
        TokenInterator tokenInterator = LexicalTokenizer.parse(Token.CONCATENATE);
        Token textToken = tokenInterator.next();
        Assert.assertEquals(Token.CONCATENATE, textToken.getName());
    }

    @Test
    public void testSymbolsWithSpacesAndTabs() {
        TokenInterator tokenInterator = LexicalTokenizer.parse("(  ),   ");
        Assert.assertEquals('(', tokenInterator.next().getValue());
        Assert.assertEquals(')', tokenInterator.next().getValue());
        Assert.assertEquals(',', tokenInterator.next().getValue());
        Assert.assertEquals(Token.EOF, tokenInterator.next().getName());
    }

    @Test
    public void testNavigator() {
        TokenInterator tokenInterator = LexicalTokenizer.parse("a.b.c");
        Token navToken = tokenInterator.next();
        Assert.assertEquals( Token.NAVIGATOR, navToken.getName());
        Assert.assertEquals("a.b.c", navToken.getValue());
        Assert.assertEquals(Token.EOF, tokenInterator.next().getName());
    }

    @Test
    public void testSeveral() {
        TokenInterator tokenInterator = LexicalTokenizer.parse(") TO_DATE(CONCATENATE(a.b.c,d.e),\"dd/mm/yyyy\")");
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.TO_DATE, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.CONCATENATE, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.NAVIGATOR, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.NAVIGATOR, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.TEXT, tokenInterator.next().getName());
        Assert.assertEquals(Token.SYMBOL, tokenInterator.next().getName());
        Assert.assertEquals(Token.EOF, tokenInterator.next().getName());
    }
}