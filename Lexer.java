import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Lexer {
    
    public static enum TokenType {
        LEFTPAREN("\\("), IDENTIFIER("[A-Za-z]+"), INTEGER("-?[0-9]+"), RIGHTPAREN("\\)"),
            WHITESPACE("[\\s]+"), PLUSSIGN("[+]"), DIVIDESIGN("[/]");
        //NUMBER("-?[0-9]+"), BINARYOP("[*|/|+|-]"), WHITESPACE(" \t\f\r\n]+");
        
        public final String pattern;
        
        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }
    
    public static class Token {
        public String tokenNum;
        public int lineNum;
        public int columnNum;
        public TokenType type;
        public String data;
        
        public Token(String tokenNum, int lineNum, int columnNum, TokenType type, String data) {
            this.tokenNum = tokenNum;
            this.lineNum = lineNum;
            this.columnNum = columnNum;
            this.type = type;
            this.data = data;
        }
        
        @Override
        public String toString() {
            return String.format("(%s %s %s %s %s)", tokenNum, lineNum, columnNum, type.name(), data);
        }
    }
    
    public static ArrayList<Token> lex(String input, int lineNum, int columnNum) {
        // The tokens that will be returned.
        ArrayList<Token> tokens = new ArrayList<Token>();
        
        // Lexer logic.
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values()) 
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
        
        // Begin matching tokens.
        Matcher matcher = tokenPatterns.matcher(input);
        
        int tokenMatch = 0;
        String match = "";
        while (matcher.find()) {
            if (matcher.group(TokenType.INTEGER.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.INTEGER, 
                        matcher.group(TokenType.INTEGER.name())));
                lineNum += (matcher.group(TokenType.INTEGER.name()).length());
                continue;
            } else if (matcher.group(TokenType.PLUSSIGN.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.PLUSSIGN, 
                        matcher.group(TokenType.PLUSSIGN.name())));
                lineNum++;
                continue;
            } else if (matcher.group(TokenType.DIVIDESIGN.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.DIVIDESIGN,
                        matcher.group(TokenType.DIVIDESIGN.name())));
                lineNum++;
                continue;
            } else if (matcher.group(TokenType.WHITESPACE.name()) != null) {
                lineNum++;
                continue;
            } else if (matcher.group(TokenType.RIGHTPAREN.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.RIGHTPAREN, 
                        matcher.group(TokenType.RIGHTPAREN.name())));
                lineNum++;
                continue;
            } else if (matcher.group(TokenType.LEFTPAREN.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.LEFTPAREN,
                        matcher.group(TokenType.LEFTPAREN.name())));
                lineNum++;
                continue;
            } else if (matcher.group(TokenType.IDENTIFIER.name()) != null) {
                tokenMatch++;
                match = "tokenNode " + tokenMatch;
                tokens.add(new Token(match, lineNum, columnNum, TokenType.IDENTIFIER, 
                        matcher.group(TokenType.IDENTIFIER.name())));
                lineNum += (matcher.group(TokenType.IDENTIFIER.name()).length());
                continue;
            }
        }
        return tokens;
    }

    public static void main(String[] args) {
        //String input = "11 + 22 - 33";
        String input = "(sum + 47) / total";
        int lineNum = 1, columnNum = 1;
        
        // Create tokens and print them.
        ArrayList<Token> tokens = lex(input, lineNum, columnNum);
        
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println(tokens.get(i));
        }
    }
}
