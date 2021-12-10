package com.maroontress.intexpr.impl;

/**
    A function representing operation with a unary operator.
*/
@FunctionalInterface
public interface IntUnaryOperation extends Operation {

    /**
        Returns the value operated with the specified operand.

        @param operand The operand.
        @return The operation result.
    */
    int apply(int operand);

    /** {@inheritDoc} */
    @Override
    default Executable toExecutable() {
        return (s, n, t) -> {
            var k = n - 1;
            var operand = s[k];
            s[k] = Operations.perform(t, () -> apply(operand));
            return n;
        };
    }
}
