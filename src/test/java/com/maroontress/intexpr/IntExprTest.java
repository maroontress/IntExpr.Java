package com.maroontress.intexpr;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public final class IntExprTest {

    private static final String LF = System.lineSeparator();

    @Test
    public void expr1() {
        var expr = "(1 - 2) * (3 + 4)";
        // 1 2 - 3 4 + *
        var v = IntExpr.eval(expr);
        assertThat(v, is(-7));
        var s = IntExpr.toTree(expr);
        var e = ""
            + "MUL" + LF
            + " ├ SUB" + LF
            + " │  ├ CONST 1" + LF
            + " │  └ CONST 2" + LF
            + " └ ADD" + LF
            + "    ├ CONST 3" + LF
            + "    └ CONST 4";
        assertThat(s, is(e));
    }

    @Test
    public void expr2() {
        var expr = "(1 - 2) * (-3 + 4)";
        // 1 2 - - 3 4 + *
        var v = IntExpr.eval(expr);
        assertThat(v, is(-1));
        var s = IntExpr.toTree(expr);
        var e = ""
                + "MUL" + LF
                + " ├ SUB" + LF
                + " │  ├ CONST 1" + LF
                + " │  └ CONST 2" + LF
                + " └ ADD" + LF
                + "    ├ NEG" + LF
                + "    │  └ CONST 3" + LF
                + "    └ CONST 4";
        assertThat(s, is(e));
    }

    @Test
    public void expr3() {
        var expr = "7 / 3 + 5 * (2 / 3) + 7 % 3";
        var v = IntExpr.eval(expr);
        assertThat(v, is(3));
    }

    @Test
    public void divByZero() {
        var expr = "(1 - 2) / 0";
        //          123456789
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:9: divided by zero: \"/\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void modByZero() {
        var expr = "1 % 0";
        //          123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:3: divided by zero: \"%\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void divIntMinByMinusOne() {
        var expr = "(-2147483647 - 1) / -1";
        //          1234567890123456789
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:19: overflow: \"/\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void negIntMin() {
        var expr = "-(-2147483647 - 1)";
        //          1
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:1: overflow: \"-\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulZeroOthers() {
        var list = List.of(
                "0 * (-2147483647 - 1)",
                "0 * 2147483647");
        for (var expr : list) {
            var v = IntExpr.eval(expr);
            assertThat(v, is(0));
        }
    }

    @Test
    public void mulOthersZero() {
        var list = List.of(
                "(-2147483647 - 1) * 0",
                "2147483647 * 0");
        for (var expr : list) {
            var v = IntExpr.eval(expr);
            assertThat(v, is(0));
        }
    }

    @Test
    public void mulIntMinMinusOne() {
        var expr = "(-2147483647 - 1) * -1";
        //          1234567890123456789
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:19: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulMinusOneIntMin() {
        var expr = "-1 * (-2147483647 - 1)";
        //          1234
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:4: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulOverflowPp() {
        var expr = "2 * 2147483647";
        //          123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:3: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulOverflowNn() {
        var expr = "-2147483647 * -2";
        //          1234567890123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:13: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulOverflowPn() {
        var expr = "2 * -2147483647";
        //          123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:3: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void mulOverflowNp() {
        var expr = "-2147483647 * 2";
        //          1234567890123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:13: overflow: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void addOverflowPp() {
        var expr = "2147483647 + 1";
        //          123456789012
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:12: overflow: \"+\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void addOverflowNn() {
        var expr = "-1 + (-2147483647 - 1)";
        //          1234
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:4: overflow: \"+\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void subOverflowPn() {
        var expr = "2147483647 - -1";
        //          123456789012
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:12: overflow: \"-\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void subOverflowNp() {
        var expr = "(-2147483647 - 1) - 1";
        //          1234567890123456789
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:19: overflow: \"-\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void subZeroIntMin() {
        var expr = "0 - (-2147483647 - 1)";
        //          123
        try {
            IntExpr.eval(expr);
        } catch (ArithmeticException e) {
            assertThat(e.getMessage(), is("L1:3: overflow: \"-\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void leftParenMismatch() {
        var expr = "(1 + 2";
        //          1
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("L1:1: mismatched parenthesis: \"(\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void rightParenMismatch() {
        var expr = "1 + 2)";
        //          123456
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(),
                    is("L1:6: mismatched parenthesis: \")\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void noRightOperandWithBinaryOperator() {
        var expr = "1+";
        //          12
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("L1:2: operand is missing: \"+\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void noOperandWithUnaryOperator() {
        var expr = "-";
        //          1
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("L1:1: operand is missing: \"-\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void unknownUnaryOperator() {
        var expr = "*";
        //          1
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("L1:1: unknown UNARY operator: \"*\""));
            return;
        }
        throw new AssertionError();
    }

    @Test
    public void unknownBinaryOperator() {
        var expr = "1!";
        //          12
        try {
            IntExpr.eval(expr);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("L1:2: unknown BINARY operator: \"!\""));
            return;
        }
        throw new AssertionError();
    }
}
