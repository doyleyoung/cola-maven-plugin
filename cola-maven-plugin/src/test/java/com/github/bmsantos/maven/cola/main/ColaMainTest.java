package com.github.bmsantos.maven.cola.main;

import static com.github.bmsantos.maven.cola.config.ConfigurationManager.config;
import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ColaMainTest {

    private static final String TARGET_DIR = "target/test-classes";

    @Mock
    private Log logger;

    private ColaMain uut;

    private List<String> classes;

    private final String testClass = "cola/ide/AnotherColaTest.class";

    @Before
    public void setUp() {
        initMocks(this);

        classes = new ArrayList<>();
        classes.add(testClass);

        uut = new ColaMain(TARGET_DIR, getClass().getClassLoader(), null, null, logger);
    }

    @Test
    public void shouldNotProcessOnNullList() throws MojoExecutionException {
        // When
        uut.execute(null);

        // Then
        assertTrue(true);
    }

    @Test
    public void shouldNotProcessOnEmptyList() throws MojoExecutionException {
        // When
        uut.execute(Collections.<String> emptyList());

        // Then
        assertTrue(true);
    }

    @Test
    public void shouldNotProcessMissingIdeBaseClassTest() throws MojoExecutionException {
        // When
        uut.execute(classes);

        // Then
        verify(logger).warn(config.warn("missing.ide.test"));
    }

    @Test
    public void shouldNotProcessMissingIdeBaseClass() throws MojoExecutionException {
        // When
        uut.execute(classes);

        // Then
        verify(logger).info(config.info("missing.ide.class"));
    }

    @Test
    public void shouldNotProcessDefaultIdeBaseClass() throws MojoExecutionException {
        // Given
        final File ideClass = new File(TARGET_DIR + "/" + config.getProperty("default.ide.class") + ".class");
        final File renamedIdeClass = new File(TARGET_DIR + "/" + config.getProperty("default.ide.class") + "_renamed");
        ideClass.renameTo(renamedIdeClass);

        // When
        uut.execute(classes);
        renamedIdeClass.renameTo(ideClass);

        // Then
        verify(logger).info(config.info("missing.default.ide.class"));
    }

    @Test
    public void shouldProcessDefaultIdeBaseClass() throws MojoExecutionException {
        // When
        uut.execute(classes);

        // Then
        verify(logger).info(config.info("found.default.ide.class"));
    }

    @Test
    public void shouldFindProvidedIdeBaseClass() throws MojoExecutionException {
        // Given
        final String ideClass = config.getProperty("default.ide.class");
        uut = new ColaMain(TARGET_DIR, getClass().getClassLoader(), ideClass, null, logger);

        // When
        uut.execute(classes);

        // Then
        verify(logger).info(config.info("processing") + TARGET_DIR + "/" + ideClass + ".class");
    }

    // In order to have the following test pass the class has to be recompiled.
    @Test
    public void shouldFindProvidedIdeBaseClassTest() throws MojoExecutionException {
        // Given
        final String ideClass = config.getProperty("default.ide.class");
        final File testClassFile = new File(TARGET_DIR + "/" + ideClass + ".class");
        final long initialSize = testClassFile.length();
        uut = new ColaMain(TARGET_DIR, getClass().getClassLoader(), ideClass, "toBeRemoved", logger);

        // When
        uut.execute(classes);
        final long finalSize = testClassFile.length();

        // Then
        assertThat(initialSize > finalSize, is(true));
    }

    @Test
    public void shouldProcessTestClasses() throws MojoExecutionException {
        // Given
        final File testClassFile = new File(TARGET_DIR + "/" + testClass);
        final long initialSize = testClass.length();

        // When
        uut.execute(classes);
        final long finalSize = testClassFile.length();

        // Then
        assertThat(initialSize < finalSize, is(true));
    }

    @Test(expected = MojoExecutionException.class)
    public void shouldThrowMojExecutionExceptionOnInvalidClasses() throws MojoExecutionException {
        // Given
        classes.add("this/path/takes/to/nowhere/NotFountTest.class");

        // When
        uut.execute(classes);

        // Then
        fail("Should have thrown an exception");
    }

    @Test
    public void shouldCollectFailureHistory() {
        // Given
        classes.add("this/path/takes/to/nowhere/NotFountTest1.class");
        classes.add("this/path/takes/to/nowhere/NotFountTest2.class");
        classes.add("this/path/takes/to/nowhere/NotFountTest3.class");

        // When
        try {
            uut.execute(classes);
        } catch (final Exception e) {
        }

        // Then
        assertThat(uut.getFailures().size(), is(3));
        verify(logger).error(format(config.error("failed.tests"), 3, 4));
    }
}
