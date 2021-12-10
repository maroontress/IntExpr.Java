package com.maroontress.intexpr.impl;

/**
    A tiny interpreter that is an implementation of a stack machine, retrieves
    instructions from an iterator, and executes them.

    <p>Each instruction has a specific operation to the stack, so performing
    it involves interaction with the stack. When the iterator gets empty, the
    interpreter stops and provides the remaining values at the top of the stack
    as the final solution.</p>
*/
public final class Interpreter {

    /** Prevents the class from being instantiated. */
    private Interpreter() {
        throw new AssertionError();
    }

    /**
        Executes the instructions retrieved from the specified iterator with
        the stack that has the specified size and returns the result value.

        @param stackSize The size of stack.
        @param all All the instructions in Reverse Polish notation.
        @return The result value.
    */
    public static int run(int stackSize, Iterable<Instruction> all) {
        var stack = new int[stackSize];
        var k = 0;
        for (var i : all) {
            k = i.apply(stack, k);
        }
        return stack[0];
    }
}
