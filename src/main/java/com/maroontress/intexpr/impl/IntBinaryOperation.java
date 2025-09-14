package com.maroontress.intexpr.impl;

/**
    A function representing operation with a binary operator.
*/
@FunctionalInterface
public interface IntBinaryOperation extends Operation {

    /**
        Returns the value operated with the specified operands.

        @param left The left operand.
        @param right The right operand.
        @return The operation result.
    */
    int apply(int left, int right);

    /** {@inheritDoc} */
    @Override
    default Executable toExecutable() {
        return (s, n, t) -> {
            if (n < 2) {
                var m = Messages.of(t, "operand is missing");
                throw new IllegalArgumentException(m);
            }
            var k = n - 1;
            var left = s[k - 1];
            var right = s[k];
            s[k - 1] = Operations.perform(t, () -> apply(left, right));
            return k;
        };
    }
}
