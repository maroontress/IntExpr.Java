package com.maroontress.intexpr.impl;

import java.util.Deque;
import java.util.Optional;
import com.maroontress.clione.Token;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;

/**
    The instruction that pops the operand(s) from the stack, executes the
    function with the operand(s), and pushes the return value to the stack.
*/
public final class Operator implements Instruction {

    private final OperatorSpec spec;
    private final Token token;

    private Operator(OperatorSpec spec, Token token) {
        this.spec = spec;
        this.token = token;
    }

    /**
        Creates a new {@link Operator} object corresponding to the specified
        token and {@link OperatorType}.

        @param token The token corresponding to the operator.
        @param type The operator type ({@link OperatorType#UNARY} or
            {@link OperatorType#BINARY}).
        @return The new operator.
    */
    public static Optional<Operator> of(Token token, OperatorType type) {
        var value = token.getValue();
        return OperatorSpec.query(value, type)
                .map(s -> new Operator(s, token));
    }

    /**
        Returns the {@link OperatorSpec} object of this operator.

        @return The {@link OperatorSpec} object.
    */
    public OperatorSpec getSpec() {
        return spec;
    }

    public Token getToken() {
        return token;
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Deque<SyntaxNode> stack) {
        spec.accept(stack);
    }

    /** {@inheritDoc} */
    @Override
    public int apply(int[] stack, int offset) {
        return spec.getCode().execute(stack, offset, token);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Operator[spec=" + spec + ", token=" + token + "]";
    }
}
