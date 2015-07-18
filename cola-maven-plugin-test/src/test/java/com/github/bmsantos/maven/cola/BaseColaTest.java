package com.github.bmsantos.maven.cola;

import org.junit.Test;

import com.github.bmsantos.core.cola.story.annotations.IdeEnabler;

public abstract class BaseColaTest {
    @IdeEnabler
    @Test
    public void iWillBeRemoved() {
        System.out.println("This is a simple test!");
    }
}
