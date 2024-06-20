package io.online.videosite;

import org.junit.jupiter.api.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class SpelExpressionTests {
    @Test
    public void testParseExpression() {
        try {
            SpelExpressionParser parser = new SpelExpressionParser();
            TemplateParserContext context = new TemplateParserContext();
            String expressionString = "#{ T(java.lang.Math).random() * 100.0 }";
            Expression expression = parser.parseExpression(expressionString, context);
            System.err.println(expression.getValueType());
            System.err.println(expression.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
