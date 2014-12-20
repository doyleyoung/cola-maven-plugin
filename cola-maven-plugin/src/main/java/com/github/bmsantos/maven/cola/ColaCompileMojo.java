package com.github.bmsantos.maven.cola;

import static com.github.bmsantos.maven.cola.config.ConfigurationManager.config;
import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_TEST_CLASSES;
import static org.apache.maven.plugins.annotations.ResolutionScope.TEST;

import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.slf4j.impl.StaticLoggerBinder;

import com.github.bmsantos.maven.cola.main.ColaMain;
import com.github.bmsantos.maven.cola.provider.IColaProvider;
import com.github.bmsantos.maven.cola.provider.MavenColaProvider;

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
    @SuppressWarnings("unchecked")
    public void execute() throws MojoExecutionException {
        try {
            StaticLoggerBinder.getSingleton().setMavenLog(getLog());

            List<String> deltas = null;
            if (context != null) {
                deltas = (List<String>) context.getValue("colaDeltas");
            }

            final IColaProvider provider = new MavenColaProvider(targetTestDirectory,
                project.getTestClasspathElements(), includes, excludes, deltas);

            try (final URLClassLoader classLoader = provider.getTargetClassLoader()) {
                final ColaMain main = new ColaMain(ideBaseClass, ideBaseClassTest);
                main.execute(provider);
            }
        } catch (final Throwable t) {
            throw new MojoExecutionException(config.error("mojo"), t);
        }
    }
}