package ch4_structural.Gym6c_log_framework.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Minimal recursive-descent JSON parser (no external dependency).
// 把 JSON 字串解析成 Map / List / String / Number / Boolean / null 的 Java 原生結構。
public class MiniJsonParser {

    private final String text;
    private int pos = 0;

    public MiniJsonParser(String text) {
        this.text = text;
    }

    public static Object parse(String text) {
        MiniJsonParser parser = new MiniJsonParser(text);
        Object result = parser.parseValue();
        parser.skipWhitespace();
        if (parser.pos != text.length()) {
            throw new IllegalArgumentException("Unexpected trailing content at " + parser.pos);
        }
        return result;
    }

    private Object parseValue() {
        skipWhitespace();
        char c = peek();
        if (c == '{') return parseObject();
        if (c == '[') return parseArray();
        if (c == '"') return parseString();
        if (c == '-' || Character.isDigit(c)) return parseNumber();
        if (text.startsWith("true", pos))  { pos += 4; return Boolean.TRUE; }
        if (text.startsWith("false", pos)) { pos += 5; return Boolean.FALSE; }
        if (text.startsWith("null", pos))  { pos += 4; return null; }
        throw new IllegalArgumentException("Unexpected character at " + pos + ": '" + c + "'");
    }

    private Map<String, Object> parseObject() {
        Map<String, Object> result = new LinkedHashMap<>();
        expect('{');
        skipWhitespace();
        if (peek() == '}') { pos++; return result; }
        while (true) {
            skipWhitespace();
            String key = parseString();
            skipWhitespace();
            expect(':');
            Object value = parseValue();
            result.put(key, value);
            skipWhitespace();
            char c = peek();
            if (c == ',') { pos++; continue; }
            if (c == '}') { pos++; return result; }
            throw new IllegalArgumentException("Expected ',' or '}' at " + pos + ", got '" + c + "'");
        }
    }

    private List<Object> parseArray() {
        List<Object> result = new ArrayList<>();
        expect('[');
        skipWhitespace();
        if (peek() == ']') { pos++; return result; }
        while (true) {
            Object value = parseValue();
            result.add(value);
            skipWhitespace();
            char c = peek();
            if (c == ',') { pos++; continue; }
            if (c == ']') { pos++; return result; }
            throw new IllegalArgumentException("Expected ',' or ']' at " + pos + ", got '" + c + "'");
        }
    }

    private String parseString() {
        expect('"');
        StringBuilder sb = new StringBuilder();
        while (pos < text.length() && text.charAt(pos) != '"') {
            char c = text.charAt(pos++);
            if (c == '\\' && pos < text.length()) {
                char esc = text.charAt(pos++);
                switch (esc) {
                    case 'n':  sb.append('\n'); break;
                    case 't':  sb.append('\t'); break;
                    case 'r':  sb.append('\r'); break;
                    case '"':  sb.append('"');  break;
                    case '\\': sb.append('\\'); break;
                    case '/':  sb.append('/');  break;
                    default: throw new IllegalArgumentException("Unknown escape: \\" + esc);
                }
            } else {
                sb.append(c);
            }
        }
        expect('"');
        return sb.toString();
    }

    private Number parseNumber() {
        int start = pos;
        if (peek() == '-') pos++;
        while (pos < text.length()
                && (Character.isDigit(text.charAt(pos)) || text.charAt(pos) == '.')) {
            pos++;
        }
        String num = text.substring(start, pos);
        if (num.contains(".")) return Double.parseDouble(num);
        return Long.parseLong(num);
    }

    private void skipWhitespace() {
        while (pos < text.length() && Character.isWhitespace(text.charAt(pos))) pos++;
    }

    private char peek() {
        if (pos >= text.length()) {
            throw new IllegalArgumentException("Unexpected end of JSON at " + pos);
        }
        return text.charAt(pos);
    }

    private void expect(char c) {
        skipWhitespace();
        if (pos >= text.length() || text.charAt(pos) != c) {
            throw new IllegalArgumentException(
                    "Expected '" + c + "' at " + pos
                            + ", got '" + (pos < text.length() ? text.charAt(pos) : "EOF") + "'");
        }
        pos++;
    }
}
