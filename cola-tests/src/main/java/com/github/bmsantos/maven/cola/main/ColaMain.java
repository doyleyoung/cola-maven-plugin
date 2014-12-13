package com.github.bmsantos.maven.cola.main;

import static com.github.bmsantos.maven.cola.config.ConfigurationManager.config;
import static com.github.bmsantos.maven.cola.formatter.FeaturesLoader.loadFeaturesFrom;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryFileExists;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.binaryToOsClass;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.classToBinary;
import static com.github.bmsantos.maven.cola.utils.ColaUtils.isSet;
import static java.io.File.separator;
import static java.lang.String.format;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.ASM4;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bmsantos.maven.cola.exceptions.ColaExecutionException;
import com.github.bmsantos.maven.cola.formatter.FeatureDetails;
import com.github.bmsantos.maven.cola.injector.InjectorClassVisitor;
import com.github.bmsantos.maven.cola.injector.MethodRemoverClassVisitor;

public class ColaMain {

    private static Logger log = LoggerFactory.getLogger(ColaMain.class);

    private final ClassLoader classLoader;
    private final String testClassDirectory;
    private String ideBaseClass;
    private String ideBaseClassTest;

    private List<String> failures;

    public ColaMain(final String testClassDirectory, final ClassLoader classLoader, final String ideBaseClass,
        final String ideBaseClassTest) {
        this.testClassDirectory = testClassDirectory.endsWith(separator) ? testClassDirectory : testClassDirectory + separator;
        this.classLoader = classLoader;
        this.ideBaseClass = ideBaseClass;
        this.ideBaseClassTest = ideBaseClassTest;
    }

    public List<String> getFailures() {
        return failures;
    }

    public void execute(final List<String> classes) throws ColaExecutionException {
        failures = new ArrayList<>();

        if (classes == null || classes.isEmpty()) {
            return;
        }

        if (isValidIdeBaseClass()) {
            try {
                ideBaseClass = processIdeBaseClass();
                classes.remove(ideBaseClass);
            } catch (final Throwable t) {
                log.info(config.error("failed.ide"), t);
            }
        }

        for (final String className : classes) {
            try {
                final Class<?> annotatedClass = classLoader.loadClass(classToBinary(className));

                final List<FeatureDetails> featureList = loadFeaturesFrom(annotatedClass);

                final ClassWriter classWritter = new ClassWriter(COMPUTE_MAXS);

                final InjectorClassVisitor injectorClassVisitor = new InjectorClassVisitor(ASM4, classWritter,
                    featureList);

                processClass(className, classWritter, injectorClassVisitor);
            } catch (final Throwable t) {
                log.error(format(config.error("failed.process.file"), className), t);
                failures.add(format(config.error("failed.processing"), className, t.getMessage()));
            }
        }

        if (!failures.isEmpty()) {
            log.error(format(config.error("failed.tests"), failures.size(), classes.size()));
            for (final String failure : failures) {
                log.error(failure);
            }

            throw new ColaExecutionException(config.error("processing"));
        }
    }

    private String processIdeBaseClass() throws IOException {

        ideBaseClass = binaryToOsClass(ideBaseClass);

        final ClassWriter cw = new ClassWriter(COMPUTE_MAXS);
        final MethodRemoverClassVisitor remover = new MethodRemoverClassVisitor(ASM4, cw, ideBaseClassTest);

        processClass(ideBaseClass, cw, remover);

        return ideBaseClass;
    }

    private boolean isValidIdeBaseClass() {
        if (!isSet(ideBaseClassTest)) {
            log.warn(config.warn("missing.ide.test"));
            ideBaseClassTest = config.getProperty("default.ide.test");
        }

        if (binaryFileExists(testClassDirectory, ideBaseClass)) {
            return true;
        } else {
            // Try default
            log.info(config.info("missing.ide.class"));

            ideBaseClass = config.getProperty("default.ide.class");
            if (binaryFileExists(testClassDirectory, ideBaseClass)) {
                log.info(config.info("found.default.ide.class"));
                return true;
            } else {
                log.info(config.info("missing.default.ide.class"));
            }
        }

        return false;
    }

    private void processClass(final String className, final ClassWriter cw, final ClassVisitor classVisitor)
        throws IOException, FileNotFoundException {

        final String filePath = testClassDirectory + className;
        log.info(config.info("processing") + filePath);

        final InputStream in = classLoader.getResourceAsStream(className);
        final ClassReader classReader = new ClassReader(in);
        classReader.accept(classVisitor, 0);

        final File file = new File(filePath);
        final DataOutputStream dout = new DataOutputStream(new FileOutputStream(file));

        dout.write(cw.toByteArray());
        dout.close();
    }
}
