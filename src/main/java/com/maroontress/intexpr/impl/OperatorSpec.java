package com.maroontress.intexpr.impl;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;

import static com.maroontress.intexpr.impl.OperatorType.BINARY;
import static com.maroontress.intexpr.impl.OperatorType.UNARY;
import static java.util.Map.entry;
import static java.util.function.Function.identity;

/**
    This class provides the class objects of operators.
*/
public final class OperatorSpec implements Consumer<Deque<SyntaxNode>> {

    private static final List<OperatorSpec> ALL = new Builder()
            .add("-", Opcode.NEG, o -> {
                if (o == Integer.MIN_VALUE) {
                    throw new OverflowException();
                }
                return -o;
            })
            .add("+", Opcode.POS, UNARY, (s, n, t) -> n)
            .add("~", Opcode.NOT, o -> ~o)
            .add("!", Opcode.LNOT, o -> (o != 0) ? 0 : 1)
            .nextPrecedence()
            .add("*", Opcode.MUL, (left, right) -> {
                if (left > 0 && right > 0) {
                    if (Integer.MAX_VALUE / left < right) {
                        throw new OverflowException();
                    }
                } else if (left < 0 && right < 0) {
                    if (Integer.MAX_VALUE / left > right) {
                        throw new OverflowException();
                    }
                } else if (left > 0) {
                    if (Integer.MIN_VALUE / left > right) {
                        throw new OverflowException();
                    }
                } else if (right > 0) {
                    if (Integer.MIN_VALUE / right > left) {
                        throw new OverflowException();
                    }
                }
                return left * right;
            })
            .add("/", Opcode.DIV, (left, right) -> {
                if (right == 0) {
                    throw new DivideByZeroException();
                }
                if (left == Integer.MIN_VALUE && right == -1) {
                    throw new OverflowException();
                }
                return left / right;
            })
            .add("%", Opcode.MOD, (left, right) -> {
                if (right == 0) {
                    throw new DivideByZeroException();
                }
                return left % right;
            })
            .nextPrecedence()
            .add("+", Opcode.ADD, (left, right) -> {
                if (left > 0 && right > 0) {
                    if (Integer.MAX_VALUE - left < right) {
                        throw new OverflowException();
                    }
                } else if (left < 0 && right < 0) {
                    if (Integer.MIN_VALUE - left > right) {
                        throw new OverflowException();
                    }
                }
                return left + right;
            })
            .add("-", Opcode.SUB, (left, right) -> {
                if (left >= 0 && right < 0) {
                    if (left > Integer.MAX_VALUE + right) {
                        throw new OverflowException();
                    }
                } else if (left < 0 && right > 0) {
                    if (left < Integer.MIN_VALUE + right) {
                        throw new OverflowException();
                    }
                }
                return left - right;
            })
            .nextPrecedence()
            .add(">>", Opcode.SHR, (a, b) -> a >> b)
            .add("<<", Opcode.SHL, (a, b) -> a << b)
            .nextPrecedence()
            .add(">", Opcode.LGT, (a, b) -> (a > b) ? 1 : 0)
            .add("<", Opcode.LLT, (a, b) -> (a < b) ? 1 : 0)
            .add(">=", Opcode.LGE, (a, b) -> (a >= b) ? 1 : 0)
            .add("<=", Opcode.LLE, (a, b) -> (a <= b) ? 1 : 0)
            .nextPrecedence()
            .add("!=", Opcode.LNE, (a, b) -> (a != b) ? 1 : 0)
            .add("==", Opcode.LEQ, (a, b) -> (a == b) ? 1 : 0)
            .nextPrecedence()
            .add("&", Opcode.AND, (a, b) -> a & b)
            .nextPrecedence()
            .add("^", Opcode.XOR, (a, b) -> a ^ b)
            .nextPrecedence()
            .add("|", Opcode.OR, (a, b) -> a | b)
            .nextPrecedence()
            .add("&&", Opcode.LAND, (a, b) -> (a != 0 && b != 0) ? 1 : 0)
            .nextPrecedence()
            .add("||", Opcode.LOR, (a, b) -> (a != 0 || b != 0) ? 1 : 0)
            .toList();

    private static final Map<OperatorType, Map<String, OperatorSpec>>
            TYPE_MAP = newOperatorClassMap();

    private final String symbol;
    private final int precedence;
    private final Opcode opcode;
    private final OperatorType type;
    private final Executable code;
    private final Consumer<Deque<SyntaxNode>> treeAction;

    private OperatorSpec(String symbol, int precedence, Opcode opcode,
                         OperatorType type, Executable code) {
        this.opcode = opcode;
        this.precedence = precedence;
        this.type = type;
        this.symbol = symbol;
        this.code = code;
        treeAction = type.apply(opcode);
    }

    private static Map.Entry<OperatorType, Map<String, OperatorSpec>>
            toEntry(OperatorType type) {
        return entry(type, newOperatorMap(type));
    }

    private static Map<OperatorType, Map<String, OperatorSpec>>
            newOperatorClassMap() {
        return Map.ofEntries(toEntry(UNARY), toEntry(BINARY));
    }

    private static Map<String, OperatorSpec>
            newOperatorMap(OperatorType type) {
        return ALL.stream()
                .filter(i -> i.type == type)
                .collect(Collectors.toMap(o -> o.symbol, identity()));
    }

    /**
        Returns the {@link OperatorSpec} object corresponding to the specified
        symbol and operator type.

        @param symbol The symbol representing the {@link OperatorSpec} object.
        @param type {@link OperatorType#UNARY} or {@link OperatorType#BINARY}.
        @return The {@link OperatorSpec} object if found, otherwise
            {@link Optional#empty()}.
    */
    public static Optional<OperatorSpec>
            query(String symbol, OperatorType type) {
        var map = TYPE_MAP.get(type);
        if (map == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(map.get(symbol));
    }

    /**
        Returns the executable code associated with this.

        @return The executable code.
    */
    public Executable getCode() {
        return code;
    }

    /**
        Returns the precedence of the operator.

        @return The precedence of the operator.
    */
    public int getPrecedence() {
        return precedence;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Deque<SyntaxNode> stack) {
        treeAction.accept(stack);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "OperatorSpec[opcode=" + opcode + ", " +
                "priority=" + precedence + ", " +
                "type=" + type + ", " +
                "symbol=" + symbol + ']';
    }

    private static final class Builder {
        private final List<OperatorSpec> list = new ArrayList<>();
        private int precedence = 1;

        private Builder() {
        }

        public List<OperatorSpec> toList() {
            return List.copyOf(list);
        }

        public Builder nextPrecedence() {
            ++precedence;
            return this;
        }

        public Builder add(String symbol, Opcode opcode, IntUnaryOperation o) {
            add(symbol, opcode, UNARY, o);
            return this;
        }

        public Builder add(String symbol, Opcode opcode, IntBinaryOperation o) {
            add(symbol, opcode, BINARY, o);
            return this;
        }

        public Builder add(String symbol, Opcode opcode, OperatorType type,
                           Executable code) {
            add(new OperatorSpec(symbol, precedence, opcode, type, code));
            return this;
        }

        private void add(String symbol, Opcode opcode, OperatorType type,
                         Operation o) {
            add(new OperatorSpec(symbol, precedence, opcode, type,
                    o.toExecutable()));
        }

        private void add(OperatorSpec s) {
            list.add(s);
        }
    }
}
