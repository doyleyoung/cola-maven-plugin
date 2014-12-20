package com.github.bmsantos.maven.cola.provider;

import static com.github.bmsantos.maven.cola.utils.ColaUtils.CLASS_EXT;
import static java.io.File.separator;
import static java.lang.System.getProperties;
import static java.util.Arrays.asList;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

public class MavenColaProvider implements IColaProvider {

    private final String targetDirectory;
    private final List<String> classpathElements, deltas;
    private final String[] includes, excludes;

    public MavenColaProvider(final String targetDirectory, final List<String> classpathElements,
        final String[] includes, final String[] excludes, final List<String> deltas) {

        this.targetDirectory = targetDirectory.endsWith(separator) ? targetDirectory : targetDirectory + separator;
        this.classpathElements = classpathElements;
        this.includes = includes;
        this.excludes = excludes;
        this.deltas = deltas;
    }

    @Override
    public String getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public URLClassLoader getTargetClassLoader() throws Exception {
        final List<URL> urls = new ArrayList<>();

        for (final String path : classpathElements) {
            urls.add(new File(path).toURI().toURL());
        }

        return new URLClassLoader(urls.toArray(new URL[urls.size()]), MavenColaProvider.class.getClassLoader());
    }

    @Override
    public List<String> getTargetClasses() {
        if (deltas != null) {
            return deltas;
        }

        final String[] resolvedIncludes = resolveIncludes();

        final DirectoryScanner scanner = new DirectoryScanner();
        if (resolvedIncludes != null && resolvedIncludes.length > 0) {
            scanner.setIncludes(resolvedIncludes);
        }
        if (excludes != null && excludes.length > 0) {
            scanner.setExcludes(excludes);
        }
        scanner.setBasedir(targetDirectory);
        scanner.setCaseSensitive(true);
        scanner.scan();

        return new ArrayList<String>(asList(scanner.getIncludedFiles()));
    }

    protected String[] resolveIncludes() {
        final List<String> list = new ArrayList<>();
        final String test = getProperties().getProperty("test");
        final String itTest = getProperties().getProperty("it.test");

        if (test != null) {
            list.add(test.endsWith(CLASS_EXT) ? test : test + CLASS_EXT);
        }
        if (itTest != null) {
            list.add(itTest.endsWith(CLASS_EXT) ? itTest : itTest + CLASS_EXT);
        }
        if (!list.isEmpty()) {
            return list.toArray(new String[list.size()]);
        }

        if (includes != null && includes.length > 0) {
            return includes;
        }

        return null;
    }
}
