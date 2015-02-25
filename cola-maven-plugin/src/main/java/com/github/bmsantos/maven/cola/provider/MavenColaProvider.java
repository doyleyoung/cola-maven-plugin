package com.github.bmsantos.maven.cola.provider;

import static com.github.bmsantos.core.cola.utils.ColaUtils.CLASS_EXT;
import static java.io.File.separator;
import static java.lang.System.getProperties;
import static java.util.Arrays.asList;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;

import com.github.bmsantos.core.cola.provider.IColaProvider;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
