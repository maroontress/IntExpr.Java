package com.maroontress.intexpr.impl;

import java.util.Deque;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;

/**
    The instruction that the {@link Interpreter} runs.
*/
public interface Instruction {

    /**
        Interacts with the specified stack of the {@link SyntaxNode} object to
        build a {@link SyntaxTree} object.

        @param stack The stack of a {@link SyntaxNode} object.
    */
    void accept(Deque<SyntaxNode> stack);

    /**
        Interacts with the specified stack of an integer.

        @param stack The stack of an integer.
        @param offset The position of the top of the stack.
        @return The new offset of the top of the stack.
    */
    int apply(int[] stack, int offset);
}
