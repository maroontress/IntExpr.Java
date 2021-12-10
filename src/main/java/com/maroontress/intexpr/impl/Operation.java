package com.maroontress.intexpr.impl;

/**
    This interface provides the factory of an {@link Executable} object.
*/
public interface Operation {

    /**
        Returns the {@link Executable} object.

        @return The {@link Executable} object.
    */
    Executable toExecutable();
}
