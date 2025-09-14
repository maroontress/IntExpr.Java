package com.maroontress.intexpr.impl;

import java.util.Optional;
import com.maroontress.clione.Token;

/**
    This interface represents the operator wrapper temporally created while
    the {@link Compiler} creates the list of {@link Instruction} objects in
    Reverse Polish notation.

    @see Compiler
*/
public interface PendedOperator {

    /**
        Returns the operator that this wraps if this is not a left parenthesis,
        otherwise {@link Optional#empty()}.

        @return The operator that this wraps or {@link Optional#empty()}.
    */
    Optional<Operator> toOperator();

    /**
        Compares the precedences of this and the specified operator.

        @param that The operator to compare the precedence of.
        @return A negative integer, zero, or a positive integer as the
            precedence of this is less than, equal to, or greater than one of
            {@code that}.
    */
    int comparePrecedence(Operator that);

    /**
        Returns the token if this is a left parenthesis or throws
        {@link IllegalStateException} otherwise.

        @return The token.
    */
    Token getToken();

    /**
        Returns a new {@link PendedOperator} object that represents a left
        parenthesis.

        @param token The token representing a left parenthesis.
        @return The new {@link PendedOperator} object.
    */
    static PendedOperator newLeftParen(Token token) {
        return new PendedOperator() {
            @Override
            public int comparePrecedence(Operator that) {
                return 1;
            }

            @Override
            public Optional<Operator> toOperator() {
                return Optional.empty();
            }

            @Override
            public Token getToken() {
                return token;
            }
        };
    }

    /**
        Returns a new {@link PendedOperator} object that represents the
        specified operator.

        @param operator The operator to wrap.
        @return The new {@link PendedOperator} object.
    */
    static PendedOperator of(Operator operator) {
        return new PendedOperator() {
            @Override
            public int comparePrecedence(Operator that) {
                return operator.getSpec().getPrecedence()
                        - that.getSpec().getPrecedence();
            }

            @Override
            public Optional<Operator> toOperator() {
                return Optional.of(operator);
            }

            @Override
            public Token getToken() {
                return operator.getToken();
            }
        };
    }
}
