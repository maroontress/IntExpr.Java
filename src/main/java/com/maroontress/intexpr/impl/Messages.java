package com.maroontress.intexpr.impl;

import com.maroontress.clione.Token;

/**
    Provides messages for exceptions that {@link Compiler} and
    {@link Interpreter} throw.
*/
public final class Messages {

    /** Prevents the class from being instantiated. */
    private Messages() {
        throw new AssertionError();
    }

    /**
        Returns a new message for the exception.

        @param token The token corresponding to the operator.
        @param m The message.
        @return The message for the exception.
    */
    public static String of(Token token, String m) {
        var v = token.getValue();
        var span = token.getSpan();
        return String.format("%1$s: %2$s: \"%3$s\"", span, m, v);
    }
}
