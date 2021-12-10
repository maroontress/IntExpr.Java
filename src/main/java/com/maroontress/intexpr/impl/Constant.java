package com.maroontress.intexpr.impl;

import java.util.Deque;
import com.maroontress.clione.Token;
import com.maroontress.intexpr.syntaxtree.IntConstantNode;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;

/**
    This class provides the instruction that places an integer constant (as an
    operand) to a stack.
*/
public final class Constant implements Instruction {

    private final int value;

    /**
        Creates a new instance.

        @param token The token representing an integer constant.
    */
    public Constant(Token token) {
        value = Integer.parseInt(token.getValue());
    }

    /** {@inheritDoc} */
    @Override
    public void accept(Deque<SyntaxNode> stack) {
        stack.push(new IntConstantNode(value));
    }

    /** {@inheritDoc} */
    @Override
    public int apply(int[] stack, int offset) {
        stack[offset] = value;
        return offset + 1;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Constant[opcode=CONST" + ", "
                + "value=" + value + "]";
    }
}
