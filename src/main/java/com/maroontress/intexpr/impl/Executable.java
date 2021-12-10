package com.maroontress.intexpr.impl;

import com.maroontress.clione.Token;

/**
    The code that each operator executes.
*/
@FunctionalInterface
public interface Executable {

    /**
        Interacts with the specified stack and returns the new offset of the
        stack.

        @param stack The stack of integers.
        @param offset The offset representing the top of the stack.
        @param token The token corresponding to the operator.
        @return The new offset.
        @throws DivideByZeroException If there is an attempt to divide an
            integer value by zero.
        @throws OverflowException If an arithmetic operation results in an
            overflow.
    */
    int execute(int[] stack, int offset, Token token);
}
