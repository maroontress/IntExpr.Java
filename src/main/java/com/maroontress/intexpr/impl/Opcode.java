package com.maroontress.intexpr.impl;

/**
    Provides constants of opcodes to visualize the syntax tree.

    @see <a href="https://en.wikipedia.org/wiki/Operators_in_C_and_C%2B%2B">
        Wikipedia, Operators in C and C++</a>
*/
public enum Opcode {

    /** Negation. */
    NEG,

    /** Positive (NOP). */
    POS,

    /** Bitwise NOT. */
    NOT,

    /** Logical negation. */
    LNOT,

    /** Multiplication. */
    MUL,

    /** Division. */
    DIV,

    /** Modulo. */
    MOD,

    /** Addition. */
    ADD,

    /** Subtraction. */
    SUB,

    /** Bitwise right shift. */
    SHR,

    /** Bitwise left shift. */
    SHL,

    /** Greater than. */
    LGT,

    /** Less than. */
    LLT,

    /** Greater than or equal to. */
    LGE,

    /** Less than or equal to. */
    LLE,

    /** Not equal to. */
    LNE,

    /** Equal to. */
    LEQ,

    /** Bitwise AND. */
    AND,

    /** Bitwise XOR. */
    XOR,

    /** Bitwise OR. */
    OR,

    /** Logical AND. */
    LAND,

    /** Logical OR. */
    LOR,

    /** Constant (immediate value). */
    CONST,
}
