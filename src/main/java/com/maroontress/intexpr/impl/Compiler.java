package com.maroontress.intexpr.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import com.maroontress.clione.LexicalParser;
import com.maroontress.clione.Token;
import com.maroontress.clione.TokenType;

import static com.maroontress.intexpr.impl.OperatorType.BINARY;
import static com.maroontress.intexpr.impl.OperatorType.UNARY;
import static java.util.Map.entry;

/**
    This class provides a tiny compiler that translates code written in the
    little language into {@link Instruction}s that the {@link Interpreter}
    class can execute.

    <p>The code is of integer expressions specified in infix notation. The
    expression has some similarities in the syntax to the C programming
    language, containing operators and operands. The operators specified in
    {@link OperatorSpec} and parentheses are available in the expressions.
    The numbers that the expression includes and represents are 32-bit signed
    integers in two's-complement notation. The constants are non-negative
    integers.</p>

    <p>The compiled {@link Instruction}s consist of {@link Constant}s and
    {@link Operator}s in Reverse Polish notation to execute with the stack
    machine.</p>

    @see <a href="https://en.wikipedia.org/wiki/Reverse_Polish_notation">
        Wikipedia, Reverse Polish notation</a>
    @see <a href="https://en.wikipedia.org/wiki/Shunting-yard_algorithm">
        Wikipedia, Shunting-yard algorithm</a>
*/
public final class Compiler {

    private static final Map<TokenType, Action> UNARY_MAP = newUnaryMap();
    private static final Map<TokenType, Action> BINARY_MAP = newBinaryMap();

    private final Deque<PendedOperator> stack = new ArrayDeque<>();
    private final List<Instruction> list = new ArrayList<>();
    private Map<TokenType, Action> currentMap;

    private Compiler() {
        currentMap = UNARY_MAP;
    }

    /**
        Creates a new list containing {@link Instruction} objects from the
        expression that the specified reader provides.

        <p>The list contains the operators and operands in Reverse Polish
        notation, so it does not include any parentheses.</p>

        @param reader The reader that provides the expression to evaluate.
        @return The new list containing {@link Instruction} objects.
        @throws IOException If an I/O error occurs with the {@code reader}.
        @throws IllegalArgumentException If the specified expression has
            syntax errors, unknown operators, illegal tokens, mismatched
            parentheses.
    */
    public static List<Instruction> toRpn(Reader reader) throws IOException {
        try (var parser = LexicalParser.of(reader, Collections.emptySet())) {
            var c = new Compiler();
            return c.getInstructions(parser);
        }
    }

    private static Map<TokenType, Action> newUnaryMap() {
        return Map.ofEntries(
                entry(TokenType.OPERATOR, newOperatorAction(UNARY)),
                entry(TokenType.PUNCTUATOR, Compiler::leftParen),
                entry(TokenType.NUMBER, Compiler::number),
                entry(TokenType.UNKNOWN, Compiler::unknown),
                entry(TokenType.DELIMITER, Compiler::nop),
                entry(TokenType.COMMENT, Compiler::nop));
    }

    private static Map<TokenType, Action> newBinaryMap() {
        return Map.ofEntries(
                entry(TokenType.OPERATOR, newOperatorAction(BINARY)),
                entry(TokenType.PUNCTUATOR, Compiler::rightParen),
                entry(TokenType.UNKNOWN, Compiler::unknown),
                entry(TokenType.DELIMITER, Compiler::nop),
                entry(TokenType.COMMENT, Compiler::nop));
    }

    private static Action newOperatorAction(OperatorType type) {
        return (c, token) -> {
            var maybeOperator = Operator.of(token, type);
            if (maybeOperator.isEmpty()) {
                throw new IllegalArgumentException(
                        Messages.of(token, "unknown operator"));
            }
            c.pushOperator(maybeOperator.get());
        };
    }

    private static void nop(Compiler c, Token token) {
    }

    private static void unknown(Compiler c, Token token) {
        throw new IllegalArgumentException(
                Messages.of(token, "unknown token"));
    }

    private List<Instruction> getInstructions(LexicalParser parser)
            throws IOException {
        if (currentMap.isEmpty()) {
            throw new IllegalStateException();
        }
        for (;;) {
            var maybeToken = parser.next();
            if (maybeToken.isEmpty()) {
                break;
            }
            var token = maybeToken.get();
            var type = token.getType();
            var action = currentMap.get(type);
            if (action == null) {
                throw new IllegalArgumentException(
                        Messages.of(token, "syntax error"));
            }
            action.accept(this, token);
        }
        for (;;) {
            var o = stack.pollFirst();
            if (o == null) {
                break;
            }
            var maybeOperator = o.toOperator();
            if (maybeOperator.isEmpty()) {
                throw new IllegalArgumentException(
                        Messages.of(o.getToken(), "mismatched parenthesis"));
            }
            list.add(maybeOperator.get());
        }
        currentMap = Map.of();
        return List.copyOf(list);
    }

    private void rightParen(Token token) {
        var value = token.getValue();
        if (!value.equals(")")) {
            throw new IllegalArgumentException(
                    Messages.of(token, "syntax error"));
        }
        for (;;) {
            var o = stack.pollFirst();
            if (o == null) {
                throw new IllegalArgumentException(
                        Messages.of(token, "mismatched parenthesis"));
            }
            var maybeOperator = o.toOperator();
            if (maybeOperator.isEmpty()) {
                break;
            }
            list.add(maybeOperator.get());
        }
        currentMap = BINARY_MAP;
    }

    private void number(Token token) {
        list.add(new Constant(token));
        currentMap = BINARY_MAP;
    }

    private void leftParen(Token token) {
        var value = token.getValue();
        if (!value.equals("(")) {
            throw new IllegalArgumentException(
                    Messages.of(token, "syntax error"));
        }
        stack.push(PendedOperator.newLeftParen(token));
        currentMap = UNARY_MAP;
    }

    private void pushOperator(Operator operator) {
        for (;;) {
            var o = stack.peekFirst();
            if (o == null) {
                break;
            }
            if (o.comparePrecedence(operator) > 0) {
                break;
            }
            stack.pop();
            list.add(o.toOperator().get());
        }
        stack.push(PendedOperator.of(operator));
        currentMap = UNARY_MAP;
    }

    @FunctionalInterface
    private interface Action {
        void accept(Compiler c, Token t);
    }
}
