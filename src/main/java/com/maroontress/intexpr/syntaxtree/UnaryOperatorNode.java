package com.maroontress.intexpr.syntaxtree;

import com.maroontress.intexpr.impl.Opcode;

/**
    The syntax node representing a unary operator.
*/
public final class UnaryOperatorNode implements SyntaxNode {

    private final Opcode opcode;
    private final SyntaxNode operand;

    /**
        Creates a new instance.

        @param opcode The opcode.
        @param operand The operand.
    */
    public UnaryOperatorNode(Opcode opcode, SyntaxNode operand) {
        this.opcode = opcode;
        this.operand = operand;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(String firstIndent, String indent) {
        // CSOFF: AvoidEscapedUnicodeCharacters
        var lf = System.lineSeparator();
        var upRight = indent + " \u2514 ";
        var blank = indent + "   ";
        return firstIndent + opcode + lf
                + operand.toString(upRight, blank);
        // CSON: AvoidEscapedUnicodeCharacters
    }
}
