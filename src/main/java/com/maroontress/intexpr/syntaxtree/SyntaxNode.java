package com.maroontress.intexpr.syntaxtree;

import com.maroontress.intexpr.impl.SyntaxTree;

/**
    The node of {@link SyntaxTree}.
*/
public interface SyntaxNode {

    /**
        Returns the string visualizing the syntax tree.

        @param firstIndent The string to indent the first line.
        @param indent The string to indent the second and subsequent lines.
        @return The string visualizing the syntax tree.
    */
    String toString(String firstIndent, String indent);
}
