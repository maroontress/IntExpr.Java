package com.maroontress.intexpr;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import com.maroontress.intexpr.impl.Compiler;
import com.maroontress.intexpr.impl.Instruction;
import com.maroontress.intexpr.impl.Interpreter;
import com.maroontress.intexpr.impl.SyntaxTree;

/**
    A utility class to evaluate {@code int} expressions.

    <p>The expression has some similarities in the syntax to the C programming
    language.</p>

    <h2>Numbers</h2>

    <p>Numbers are 32-bit signed integers in two's-complement notation (like
    values of {@code int} type in Java and C#, {@code int32_t} type in C23,
    and so on). They can represent integers from &minus;2147483648 to
    2147483647.</p>

    <h2>Expressions</h2>

    <p>A simple expression is just an integer constant.</p>

    <p>Note that the minimum integer (&minus;2147483648) cannot be constant
    because the expression {@code -2147483648} is a unary {@code -} operator
    followed by {@code 2147483648}, and the integer is greater than the maximum
    integer (2147483647). So, you have to represent the minimum integer with
    {@code (-2147483647 - 1)}.</p>

    <p>In the following descriptions of legal expressions, <em>expr</em> refers
    to a complete expression:</p>

    <dl>
    <dt><code>(</code> <em>expr</em> <code>)</code></dt>
    <dd>The parentheses alter the standard precedence to force the evaluation
    of the expression to precede the operation outside of them.</dd>

    <dt>unary <code>+</code> <em>expr</em></dt>
    <dd>The result is the expression itself (that is, no operation).</dd>

    <dt>unary <code>-</code> <em>expr</em></dt>
    <dd>The result is the negation of the expression.</dd>

    <dt>unary <code>!</code> <em>expr</em></dt>
    <dd>The result is 1 if <em>expr</em> is 0, otherwise 0.</dd>

    <dt>unary <code>~</code> <em>expr</em></dt>
    <dd>The result is the logical negation on each bit, forming the ones'
    complement of the given binary value.</dd>

    <dt><em>expr</em> <code>*</code> <em>expr</em></dt>
    <dd>The result is the product of the two expressions.</dd>

    <dt><em>expr</em> <code>/</code> <em>expr</em></dt>
    <dd>The result is the quotient of the two expressions.</dd>

    <dt><em>expr</em> <code>%</code> <em>expr</em></dt>
    <dd>The result is the remainder of the two expressions.</dd>

    <dt><em>expr</em> <code>+</code> <em>expr</em></dt>
    <dd>The result is the sum of the two expressions.</dd>

    <dt><em>expr</em> <code>-</code> <em>expr</em></dt>
    <dd>The result is the difference between the two expressions.</dd>

    <dt><em>expr1</em> <code>&lt;&lt;</code> <em>expr2</em></dt>
    <dd>The result is the left arithmetic shift of <em>expr1</em> by
    <em>expr2</em>.</dd>

    <dt><em>expr</em> <code>&gt;&gt;</code> <em>expr</em></dt>
    <dd>The result is the right arithmetic shift of <em>expr1</em> by
    <em>expr2</em>.</dd>

    <dt><em>expr1</em> <code>&lt;</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is strictly less than <em>expr2</em>,
    otherwise 0.</dd>

    <dt><em>expr1</em> <code>&lt;=</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is less than or equal to
    <em>expr2</em>, otherwise 0.</dd>

    <dt><em>expr1</em> <code>&gt;</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is strictly greater than
    <em>expr2</em>, otherwise 0.</dd>

    <dt><em>expr1</em> <code>&gt;=</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is greater than or equal to expr2,
    otherwise 0.</dd>

    <dt><em>expr1</em> <code>==</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is equal to <em>expr2</em>,
    otherwise 0.</dd>

    <dt><em>expr1</em> <code>!=</code> <em>expr2</em></dt>
    <dd>The result is 1 if <em>expr1</em> is not equal to <em>expr2</em>,
    otherwise 0.</dd>

    <dt><em>expr</em> <code>&amp;&amp;</code> <em>expr</em></dt>
    <dd>The result is 1 if both expressions are non-zero, otherwise 0.</dd>

    <dt><em>expr</em> <code>||</code> <em>expr</em></dt>
    <dd>The result is 1 if either expression is non-zero, otherwise 0.</dd>
    </dl>

    <p>The operator precedence is as follows:</p>

    <table style="border-collapse: collapse;">
    <caption>Table 1. The operator precedence</caption>
    <thead>
    <tr>
    <th style="padding: 1ex; text-align: center; border: 1px solid;">
    Precedence</th>
    <th style="padding: 1ex; text-align: center; border: 1px solid;">
    Operator</th>
    <th style="padding: 1ex; text-align: center; border: 1px solid;">
    Associativity</th>
    </tr>
    </thead>
    <tbody>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    highest</td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    unary <code>+ - ! ~</code></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>* / %</code></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"
    rowspan="10">Left-to-right</td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>+ -</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>&lt;&lt; &gt;&gt;</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>&lt; &lt;= &gt; &gt;=</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>== !=</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>&amp;</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>^</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>|</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;"></td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>&amp;&amp;</code></td>
    </tr>
    <tr>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    lowest</td>
    <td style="padding: 1ex; text-align: center; border: 1px solid;">
    <code>||</code></td>
    </tr>
    </tbody>
    </table>
*/
public final class IntExpr {

    /** Prevents the class from being instantiated. */
    private IntExpr() {
        throw new AssertionError();
    }

    /**
        Evaluates the specified string representing an expression and returns
        the evaluated value.

        <p>The expression is of 32-bit signed integer in two's-complement
        notation.</p>

        <p>For example, the invocation
        {@code IntExpr.eval("(1+2*3<<4)%5")} returns 2.</p>

        @param expr The expression to evaluate.
        @return The evaluated value.
        @throws IllegalArgumentException If the specified {@code expr} has
            syntax errors such as a mismatched or missing parenthesis, a stray
            token, an unknown token.
        @throws ArithmeticException If there is an attempt to divide an integer
            value by zero or to overflow.
    */
    public static int eval(String expr) {
        var list = toRpn(expr);
        return Interpreter.run(list.size(), list);
    }

    /**
        Returns the string to visualize the syntax tree representing the
        expression in Reverse Polish notation, which is equivalent to the
        specified expression.

        <p>The expression is of 32-bit signed integer in two's-complement
        notation.</p>

        <p>For example, the invocation:
        {@code IntExpr.toTree("(1+2*3<<4)%5")} returns as follows:</p>
        <pre>
        """
        MOD
        ├ SHL
        │  ├ ADD
        │  │  ├ CONST 1
        │  │  └ MUL
        │  │     ├ CONST 2
        │  │     └ CONST 3
        │  └ CONST 4
        └ CONST 5
        """</pre>

        The opcodes that the tree includes (such as {@code ADD}, {@code MUL},
        {@code CONST}, etc.) are defined in
        {@link com.maroontress.intexpr.impl.Opcode}.

        @param expr The expression to evaluate.
        @return The string to visualize the syntax tree.
        @throws IllegalArgumentException If the specified {@code expr} has
            syntax errors such as a mismatched or missing parenthesis, a stray
            token, an unknown token.
    */
    public static String toTree(String expr) {
        var list = toRpn(expr);
        var tree = new SyntaxTree(list);
        return tree.toString();
    }

    /**
        Create a new list of an {@link Instruction} object with the specified
        expression.

        @param expr The expression to evaluate.
        @return The new list of an {@link Instruction} object.
    */
    private static List<Instruction> toRpn(String expr) {
        try {
            var reader = new StringReader(expr);
            return Compiler.toRpn(reader);
        } catch (IOException e) {
            throw new AssertionError();
        }
    }
}
