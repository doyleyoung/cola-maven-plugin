package com.github.bmsantos.maven.cola;

import static org.apache.maven.plugins.annotations.LifecyclePhase.PROCESS_TEST_CLASSES;
import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ASM4;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.github.bmsantos.maven.cola.injector.InjectorClassVisitor;
import com.github.bmsantos.maven.cola.injector.MethodRemoverClassVisitor;

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
@Mojo(name = "compile", requiresProject = true, threadSafe = false, requiresDependencyResolution = COMPILE, defaultPhase = PROCESS_TEST_CLASSES)
public class ColaCompileMojo extends BaseColaMojo {

    private ClassLoader classLoader;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            classLoader = getTestClassLoader();

            final List<String> classes = getClasses();

            validateIdeBaseClass();
            processIdeBaseClass(classes);


            for (final String className : classes) {
                final ClassWriter classWritter = new ClassWriter(COMPUTE_MAXS);
                final InjectorClassVisitor injectorClassVisitor = new InjectorClassVisitor(ASM4, classWritter);
                processClass(className, classWritter, injectorClassVisitor);
            }
        } catch (final MalformedURLException e) {
            e.printStackTrace();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final Throwable t) {
            t.printStackTrace();
        }
    }

    private void processIdeBaseClass(final List<String> classes) throws IOException {
        if (ideBaseClassTest == null || ideBaseClassTest.isEmpty()) {
            getLog().warn("ideBaseClassTest method not set. Will look for default JUnit Test named 'iWillBeRemoved' method and remove if availble.");
            ideBaseClassTest = "iWillBeRemoved";
        }

        ideBaseClass = ideBaseClass.replace(".", "/");
        if (!ideBaseClass.endsWith(".class")) {
            ideBaseClass += ".class";
        }

        classes.remove(ideBaseClass);

        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        final MethodRemoverClassVisitor remover = new MethodRemoverClassVisitor(ASM4, cw, ideBaseClassTest);

        processClass(ideBaseClass, cw, remover);
    }

    private void validateIdeBaseClass() throws IOException {
        if (ideBaseClass == null || ideBaseClass.isEmpty()) {
            getLog().warn("ideBaseClass not set. Looking for default class.");
            ideBaseClass = "cola/ide/base/class/BaseColaTest.class";
            final File baseClass = new File(targetTestDirectory + "/" + ideBaseClass);
            if (baseClass.exists()) {
                getLog().info("Found default ideBaseClass class. Proceeding...");
            } else {
                final String msg = "Default class not found. Please set ideBaseClass property or create cola/ide/base/class/BaseColaTest test class.";
                getLog().error(msg);
                throw new IOException(msg);
            }
        }
    }

    private void processClass(final String className, final ClassWriter cw, final ClassVisitor classVisitor) throws IOException, FileNotFoundException {
        final InputStream in = classLoader.getResourceAsStream(className);
        final ClassReader classReader = new ClassReader(in);

        classReader.accept(classVisitor, 0);

        final String filePath = targetTestDirectory + "/" + className;
        getLog().info("Processing Cola Test: " + filePath);
        final DataOutputStream dout = new DataOutputStream(new FileOutputStream(new File(filePath)));

        dout.write(cw.toByteArray());
        dout.close();
    }

}
