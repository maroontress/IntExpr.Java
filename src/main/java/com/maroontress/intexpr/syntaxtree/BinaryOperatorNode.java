package com.maroontress.intexpr.syntaxtree;

import com.maroontress.intexpr.impl.Opcode;

/**
    The syntax node representing a binary operator.
*/
public final class BinaryOperatorNode implements SyntaxNode {

    private final Opcode opcode;
    private final SyntaxNode leftOperand;
    private final SyntaxNode rightOperand;

    /**
        Creates a new instance.

        @param opcode The opcode.
        @param leftOperand The left operand.
        @param rightOperand The right operand.
    */
    public BinaryOperatorNode(Opcode opcode, SyntaxNode leftOperand,
                              SyntaxNode rightOperand) {
        this.opcode = opcode;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /** {@inheritDoc} */
    @Override
    public String toString(String firstIndent, String indent) {
        var lf = System.lineSeparator();
        var verticalRight = indent + " \u251c ";
        var vertical = indent + " \u2502 ";
        var upRight = indent + " \u2514 ";
        var blank = indent + "   ";
        return firstIndent + opcode + lf
                + leftOperand.toString(verticalRight, vertical) + lf
                + rightOperand.toString(upRight, blank);
    }
}
