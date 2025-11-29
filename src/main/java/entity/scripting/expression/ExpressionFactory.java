package entity.scripting.expression;

import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.value.BooleanValue;
import entity.scripting.expression.value.NumericValue;
import entity.scripting.expression.variable.BooleanVariable;
import entity.scripting.expression.variable.NumericVariable;

import java.util.ArrayList;
import java.util.List;

public class ExpressionFactory {

    private static final ExpressionEntry[] REGISTRY = {
            new ExpressionEntry(BooleanValue.KEY_WORD, BooleanValue::parse),
            new ExpressionEntry(NumericValue.KEY_WORD, NumericValue::parse),
            new ExpressionEntry(BooleanVariable.KEY_WORD, BooleanVariable::parse),
            new ExpressionEntry(NumericVariable.KEY_WORD, NumericVariable::parse),
            new ExpressionEntry(AndExpression.KEY_WORD, AndExpression::parse),
            new ExpressionEntry(OrExpression.KEY_WORD, OrExpression::parse),
            new ExpressionEntry(NotExpression.KEY_WORD, NotExpression::parse),
            new ExpressionEntry(SimpleArithmeticOperation.KEY_WORD, SimpleArithmeticOperation::parse)
    };

    public static Expression<?> parse(String string) throws ParseSyntaxException {
        string = string.trim();
        
        int colonIndex = string.indexOf(':');
        if (colonIndex == -1) {
            throw new ParseSyntaxException("Invalid Syntax: Missing ':' in expression: " + string);
        }

        String keyword = string.substring(0, colonIndex).trim();

        int openParentheses = string.indexOf('(', colonIndex + 1);
        if (openParentheses == -1) {
            throw new ParseSyntaxException("Invalid Syntax: Missing '(' after keyword '" + keyword + "'");
        }

        int closeParentheses = findMatchingParentheses(string, openParentheses);
        if (closeParentheses == -1) {
            throw new ParseSyntaxException("Invalid Syntax: Unmatched '(' in expression: " + string);
        }

        String body = string.substring(openParentheses + 1, closeParentheses).trim();

        for (ExpressionEntry entry : REGISTRY) {
            if (entry.getKeyword().equals(keyword)) {
                return entry.getParser().parse(body);
            }
        }

        throw new ParseSyntaxException("Unknown keyword: " + keyword);
    }

    private static int findMatchingParentheses(String string, int openIndex) {
        int depth = 1;
        for (int i = openIndex + 1; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == '(') {
                depth++;
            }
            if (character == ')') {
                depth--;
            }
            if (depth == 0) return i;
        }
        return -1;
    }

    public static List<String> split(String string) {
        List<String> parts = new ArrayList<>();
        int depth = 0;
        int start = 0;

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);

            if (c == '(') {
                depth++;
            }
            else if (c == ')') {
                depth--;
            }

            // Split at comma only at depth 0
            else if (c == ',' && depth == 0) {
                parts.add(string.substring(start, i).trim());
                start = i + 1;
            }
        }

        // Add the last segment
        if (start < string.length()) {
            parts.add(string.substring(start).trim());
        }

        return parts;
    }

    @FunctionalInterface
    private interface ExpressionParser {
        Expression<?> parse(String body) throws ParseSyntaxException;
    }

    private static class ExpressionEntry {
        private final String keyword;
        private final ExpressionParser parser;

        public ExpressionEntry(String keyword, ExpressionParser parser){
            this.keyword = keyword;
            this.parser = parser;
        }

        public String getKeyword(){
            return keyword;
        }

        public ExpressionParser getParser(){
            return parser;
        }
    }


}
