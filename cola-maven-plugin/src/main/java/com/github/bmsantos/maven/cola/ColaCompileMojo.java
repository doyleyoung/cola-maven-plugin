package com.github.bmsantos.maven.cola;

import static com.github.bmsantos.maven.cola.config.ConfigurationManager.config;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_TEST_CLASSES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;

import com.github.bmsantos.maven.cola.main.ColaMain;

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

/**
 * @description Process BDD scenarios and inject JUnit tests into Cola Tests.
 */
@Mojo(name = "compile", requiresProject = true, threadSafe = false, requiresDependencyResolution = TEST,
defaultPhase = PROCESS_TEST_CLASSES)
public class ColaCompileMojo extends BaseColaMojo {

    @Override
    public void execute() throws MojoExecutionException {
        try {
            final ColaMain main = new ColaMain(targetTestDirectory, getTestClassLoader(), ideBaseClass,
                ideBaseClassTest, getLog());

            main.execute(getClasses());
        } catch (final Throwable t) {
            throw new MojoExecutionException(config.error("mojo"), t);
        }
    }
}