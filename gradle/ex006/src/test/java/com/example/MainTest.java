package com.example;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Main}.
 *
 * @author user@example.com (Author)
 */
@RunWith(JUnit4.class)
public class MainTest {

    @Test
    public void thisAlwaysPasses() {
    }

    @Test
    @Ignore
    public void thisIsIgnored() {
    }

    @Test
    public void sayHello() {
        assertEquals(new Main().sayHello(), "Hello");
    }
}
