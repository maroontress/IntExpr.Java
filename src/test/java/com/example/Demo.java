package com.example;

import org.junit.jupiter.api.Test;

public final class Demo {

    @Test
    public void evalDemo() {
        EvalDemo.main(new String[] {"(1+2*3<<4)%5"});
    }

    @Test
    public void treeDemo() {
        TreeDemo.main(new String[] {"(1+2*3<<4)%5"});
    }
}
