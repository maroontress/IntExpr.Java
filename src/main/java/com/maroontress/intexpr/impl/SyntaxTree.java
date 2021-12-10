package com.maroontress.intexpr.impl;

import java.util.ArrayDeque;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;

/**
    This class represents the tree of {@link SyntaxNode} to visualize
    instructions in Reverse Polish notation.
*/
public final class SyntaxTree {

    private final SyntaxNode root;

    /**
        Creates a new instance.

        @param all The instructions.
    */
    public SyntaxTree(Iterable<Instruction> all) {
        root = newRoot(all);
    }

    private static SyntaxNode newRoot(Iterable<Instruction> all) {
        var stack = new ArrayDeque<SyntaxNode>();
        for (var i : all) {
            i.accept(stack);
        }
        return stack.pop();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return root.toString("", "");
    }
}
