# IntExpr

IntExpr is a Java library to evaluate `int` expressions. The expression has
some similarities in the syntax to the C programming language.

## Examples

[The following code](src/test/java/com/example/EvalDemo.java) evaluates the
string that represents an integer expression and prints the result:

```java
package com.example;

import com.maroontress.intexpr.IntExpr;

public final class EvalDemo {
    public static void main(String[] args) {
        System.out.println(IntExpr.eval(args[0]));
    }
}
```

The output would be as follows:

```plaintext
$ java com.example.EvalDemo '(1+2*3<<4)%5'
2
```

Internally, the specified expression is converted to the expression in
[_Reverse Polish notation_][wikipedia-rpn] (RPN) before evaluation. The
expressions in RPN do not contain parentheses.
[The following code](src/test/java/com/example/TreeDemo.java) prints the syntax
tree representing the expression in RPN, which corresponds to the specified
expression:

```java
package com.example;

import com.maroontress.intexpr.IntExpr;

public final class TreeDemo {
    public static void main(String[] args) {
        System.out.println(IntExpr.toTree(args[0]));
    }
}
```

The output would be as follows:

```plaintext
$ java com.example.TreeDemo '(1+2*3<<4)%5'
MOD
 ├ SHL
 │  ├ ADD
 │  │  ├ CONST 1
 │  │  └ MUL
 │  │     ├ CONST 2
 │  │     └ CONST 3
 │  └ CONST 4
 └ CONST 5
```

The opcodes that the tree includes (such as `ADD`, `MUL`, `CONST`, etc.) are
defined in
[`enum Opcode`](src/main/java/com/maroontress/intexpr/impl/Opcode.java).

## Numbers

Numbers are 32-bit signed integers in two's-complement notation (like values of
`int` type in Java and C#, `int32_t` type in C23, and so on). They can
represent integers from &minus;2147483648 to 2147483647.

## Expressions

A simple expression is just an integer constant.

Note that the minimum integer (&minus;2147483648) cannot be constant because
the expression `-2147483648` is a unary `-` operator followed by `2147483648`,
and the integer is greater than the maximum integer (2147483647). So, you have
to represent the minimum integer with `(-2147483647 - 1)`.

In the following descriptions of legal expressions, _expr_ refers to a complete
expression:

- `(` _expr_ `)`: The parentheses alter the standard precedence to force the
  evaluation of the expression to precede the operation outside of them.
- unary `+` _expr_: The result is the expression itself (that is, no
  operation).
- unary `-` _expr_: The result is the negation of the expression.
- unary `!` _expr_:  The result is 1 if _expr_ is 0, otherwise 0.
- unary `~` _expr_: The result is the logical negation on each bit, forming the
  ones' complement of the given binary value.
- _expr_ `*` _expr_: The result is the product of the two expressions.
- _expr_ `/` _expr_: The result is the quotient of the two expressions.
- _expr_ `%` _expr_: The result is the remainder of the two expressions.
- _expr_ `+` _expr_: The result is the sum of the two expressions.
- _expr_ `-` _expr_: The result is the difference between the two expressions.
- _expr1_ `<<` _expr2_: The result is the left arithmetic shift of _expr1_ by
  _expr2_.
- _expr_ `>>` _expr_: The result is the right arithmetic shift of _expr1_ by
  _expr2_.
- _expr1_ `<` _expr2_: The result is 1 if _expr1_ is strictly less than
  _expr2_, otherwise 0.
- _expr1_ `<=` _expr2_: The result is 1 if _expr1_ is less than or equal to
  _expr2_, otherwise 0.
- _expr1_ `>` _expr2_: The result is 1 if _expr1_ is strictly greater than
  _expr2_, otherwise 0.
- _expr1_ `>=` _expr2_: The result is 1 if _expr1_ is greater than or equal to
  expr2, otherwise 0.
- _expr1_ `==` _expr2_: The result is 1 if _expr1_ is equal to _expr2_,
  otherwise 0.
- _expr1_ `!=` _expr2_: The result is 1 if _expr1_ is not equal to _expr2_,
  otherwise 0.
- _expr_ `&&` _expr_: The result is 1 if both expressions are non-zero,
  otherwise 0.
- _expr_ `||` _expr_: The result is 1 if either expression is non-zero,
  otherwise 0.

The operator precedence is as follows:

| Precedence | Operator                     | Associativity |
| :---       | :---                         | :---          |
| highest    | unary `+` `-` `!` `~`        |               |
|            | `*` `/` `%`                  | Left-to-right |
|            | `+` `-`                      | Left-to-right |
|            | `<<` `>>`                    | Left-to-right |
|            | `<` `<=` `>` `>=`            | Left-to-right |
|            | `==` `!=`                    | Left-to-right |
|            | `&`                          | Left-to-right |
|            | `^`                          | Left-to-right |
|            | `\|`                         | Left-to-right |
|            | `&&`                         | Left-to-right |
| lowest     | `\|\|`                       | Left-to-right |

## Errors

Syntax errors (such as a mismatched or missing parenthesis, a stray token, an
unknown token) throw an `IllegalArgumentException`. The division by zero and
the overflow during the evaluation throw an `ArithmeticException`.

## API Reference

- [com.maroontress.intexpr][apiref-maroontress.intexpr] module

[wikipedia-rpn]: https://en.wikipedia.org/wiki/Reverse_Polish_notation
[apiref-maroontress.intexpr]:
  https://maroontress.github.io/IntExpr-Java/api/latest/html/index.html
