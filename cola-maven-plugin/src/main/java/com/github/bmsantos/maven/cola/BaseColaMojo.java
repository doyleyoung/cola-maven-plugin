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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.sonatype.plexus.build.incremental.BuildContext;

public abstract class BaseColaMojo extends AbstractMojo {

    @Component
    protected MavenProject project;

    @Component
    protected BuildContext context;

    /**
     * Used to enable cola logging.
     */
    @Parameter(property = "project.build.testOutputDirectory")
    protected String targetTestDirectory;

    /**
     * Base Class for IDE enabling.
     * This is required in order to allow Cola Tests to run from IDEs such as Eclipse.
     */
    @Parameter
    protected String ideBaseClass;

    /**
     * Base Class Test method for IDE enabling.
     * The JUnit test method name to be removed.
     */
    @Parameter
    protected String ideTestMethod;

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
}