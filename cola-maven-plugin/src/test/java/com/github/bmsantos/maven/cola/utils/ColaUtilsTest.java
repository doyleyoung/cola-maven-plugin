package com.github.bmsantos.maven.cola.utils;

import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryFileExists;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryToOS;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryToOsClass;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.classToBinary;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.isSet;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.osToBinary;
import static java.io.File.separator;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ColaUtilsTest {

    @Test
    public void shoulBeSet() {
        // Given
        final String value = "something";

        // When / Then
        assertThat(isSet(value), is(true));
    }

    @Test
    public void shouldNotBeSetOnNull() {
        // Given
        final String value = null;

        // When / Then
        assertThat(isSet(value), is(false));
    }

    @Test
    public void shouldNotBeSetOnEmpty() {
        // Given
        final String value = "";

        // When / Then
        assertThat(isSet(value), is(false));
    }

    @Test
    public void shouldBeClassFile() {
        // Given
        final String className = "MyClass.class";

        // When / Then
        assertThat(ColaUtils.isClassFile(className), is(true));
    }

    @Test
    public void shouldNotBeClassFile() {
        // Given
        final String className = "MyClass";

        // When / Then
        assertThat(ColaUtils.isClassFile(className), is(false));
    }

    @Test
    public void shouldConvertToOSPathToBinaryFormat() {
        // Given
        final String path = toOSPath("com", "github", "bmsantos", "MyClass");
        final String expected = path.replace(separator, ".");

        // When
        final String result = osToBinary(path);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldConvertToClassPathToBinaryFormat() {
        // Given
        final String path = toOSPath("com", "github", "bmsantos", "MyClass.class");
        final String expected = path.replace(separator, ".").replace(".class", "");

        // When
        final String result = classToBinary(path);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldConvertToOSPath() {
        // Given
        final String path = toBinary("com", "github", "bmsantos", "MyClass");
        final String expected = path.replace(".", separator);

        // When
        final String result = binaryToOS(path);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldConvertToOSClass() {
        // Given
        final String path = toBinary("com", "github", "bmsantos", "MyClass");
        final String expected = path.replace(".", separator) + ".class";

        // When
        final String result = binaryToOsClass(path);

        // Then
        assertThat(result, is(expected));
    }

    @Test
    public void shouldFindFile() {
        // Given
        final String targetDir = toOSPath("target", "test-classes");
        final String className = getClass().getName();

        // When - Then
        assertThat(binaryFileExists(targetDir, className), is(true));
    }

    @Test
    public void shouldNotFindFile() {
        // Given
        final String targetDir = toOSPath("target", "test-classes");
        final String className = "invalid";

        // When - Then
        assertThat(binaryFileExists(targetDir, className), is(false));
    }

    private String toOSPath(final String... parts) {
        String result = parts[0];
        for (int i = 1; i < parts.length; i++) {
            result += separator + parts[i];
        }
        return result;
    }

    private String toBinary(final String... parts) {
        String result = parts[0];
        for (int i = 1; i < parts.length; i++) {
            result += "." + parts[i];
        }
        return result;
    }
}
