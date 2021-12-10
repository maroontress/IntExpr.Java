package com.maroontress.intexpr.impl;

import com.maroontress.clione.Token;
import java.util.function.IntSupplier;

/**
    This class provides the utility methods for operations.
*/
public final class Operations {

    /** Prevents the class from being instantiated. */
    private Operations() {
        throw new AssertionError();
    }

    /**
        Performs an operation.

        @param t The token corresponding to the operator.
        @param s The supplier that represents the operation consisting of both
            an operator and its operand(s), supplying the result value.
        @return The result of evaluating {@code s}.
        @throws ArithmeticException If there is an attempt to divide an
            integer value by zero, or if an arithmetic operation results in
            an overflow.
    */
    public static int perform(Token t, IntSupplier s) {
        try {
            return s.getAsInt();
        } catch (OverflowException e) {
            throw new ArithmeticException(Messages.of(t, "overflow"));
        } catch (DivideByZeroException e) {
            throw new ArithmeticException(Messages.of(t, "divided by zero"));
        }
    }
}
