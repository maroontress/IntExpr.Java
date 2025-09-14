package com.maroontress.intexpr.impl;

import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Function;
import com.maroontress.intexpr.syntaxtree.BinaryOperatorNode;
import com.maroontress.intexpr.syntaxtree.SyntaxNode;
import com.maroontress.intexpr.syntaxtree.UnaryOperatorNode;

/**
    This class represents the types of operators.
*/
public enum OperatorType {

    // CSOFF: NeedBraces
    /** Represents unary operators. */
    UNARY(o -> s -> {
        var operand = s.pop();
        var node = new UnaryOperatorNode(o, operand);
        s.push(node);
    }),
    // CSON: NeedBraces

    // CSOFF: NeedBraces
    /** Represents binary operators. */
    BINARY(o -> s -> {
        var right = s.pop();
        var left = s.pop();
        var node = new BinaryOperatorNode(o, left, right);
        s.push(node);
    });
    // CSON: NeedBraces

    private final Function<Opcode, Consumer<Deque<SyntaxNode>>> f;

    OperatorType(Function<Opcode, Consumer<Deque<SyntaxNode>>> f) {
        this.f = f;
    }

    /**
        Returns the {@link Consumer} of the stack of {@link SyntaxNode} objects
        associated with the specified {@link Opcode}.

        @param opcode The opcode to decide what the consumer performs with
            the stack.
        @return The consumer.
    */
    public Consumer<Deque<SyntaxNode>> apply(Opcode opcode) {
        return f.apply(opcode);
    }
}
