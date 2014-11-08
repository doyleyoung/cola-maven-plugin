package com.github.bmsantos.maven.cola;

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

import static java.util.Arrays.asList;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.DirectoryScanner;
import org.sonatype.plexus.build.incremental.BuildContext;

public abstract class BaseColaMojo extends AbstractMojo {

    @Component
    protected BuildContext context;

    /**
     * Used to enable cola logging.
     */
    @Parameter(property = "project.build.testOutputDirectory")
    protected String targetTestDirectory;

    /**
     * Cola Test Base Class.
     * This is required in order to allow Cola Tests to run from IDEs such as Eclipse.
     */
    @Parameter
    protected String ideBaseClass;

    /**
     * Cola Test Base Class Test.
     * The test name to be removed.
     */
    @Parameter
    protected String ideBaseClassTest;

    /**
     * Inlude filters
     */
    @Parameter
    protected String[] includes;

    /**
     * Exclude filters
     */
    @Parameter
    protected String[] excludes;

    /**
     * Used to enable cola logging.
     */
    @Parameter(defaultValue = "true")
    protected Boolean log;

    protected ClassLoader getTestClassLoader() throws MalformedURLException {
        final URL[] urls = { new File(targetTestDirectory).toURI().toURL() };
        final ClassLoader classLoader = new URLClassLoader(urls);
        return classLoader;
    }

    @SuppressWarnings("unchecked")
    protected List<String> getClasses() {

        if (context != null) {
            final List<String> deltas = (List<String>) context.getValue("colaDeltas");
            if (deltas != null) {
                return deltas;
            }
        }

        final DirectoryScanner scanner = new DirectoryScanner();
        if (includes != null && includes.length > 0) {
            scanner.setIncludes(includes);
        }
        if (excludes != null && excludes.length > 0) {
            scanner.setExcludes(excludes);
        }
        scanner.setBasedir(targetTestDirectory);
        scanner.setCaseSensitive(true);
        scanner.scan();

        return new ArrayList<String>(asList(scanner.getIncludedFiles()));
    }
}