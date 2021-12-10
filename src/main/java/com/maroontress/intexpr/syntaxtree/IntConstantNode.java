package com.maroontress.intexpr.syntaxtree;

import com.maroontress.intexpr.impl.Opcode;

/**
    The syntax node representing an operand and an integer constant.
*/
public final class IntConstantNode implements SyntaxNode {

    private final int value;

    /**
        Creates a new instance.

        @param value The immediate value.
    */
    public IntConstantNode(int value) {
        this.value = value;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(String firstIndent, String indent) {
        return firstIndent + Opcode.CONST + " " + value;
    }
}
