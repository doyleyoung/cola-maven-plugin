package com.github.bmsantos.maven.cola.injector;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import com.github.bmsantos.maven.cola.formatter.FeatureDetails;
import com.github.bmsantos.maven.cola.formatter.FeatureFormatter;

public class InjectorClassVisitor extends ClassVisitor {

    private final ClassWriter cw;
    private String stories;
    private List<FeatureDetails> features;

    public InjectorClassVisitor(final int api, final ClassWriter cw, final List<FeatureDetails> features) {
        super(api, cw);
        this.cw = cw;
        this.features = features;
    }

    @Override
    public FieldVisitor visitField(final int access, final String name,
        final String desc, final String signature, final Object value) {
        if ((features == null || features.isEmpty()) && name.equals("stories")) {
            stories = (String) value;
            features = new ArrayList<>();
            features.add(FeatureFormatter.parse(stories, "/from/junit/stories/field"));
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {

        for (final FeatureDetails feature : features) {
            for (final Scenario scenario : feature.getScenarios().keySet()) {
                final List<Step> story = new ArrayList<>(feature.getBackgroundSteps());
                story.addAll(feature.getScenarios().get(scenario));
                injectTestMethod(feature.getFeature(), scenario, story);
            }
        }
        super.visitEnd();
    }

    private void injectTestMethod(final Feature feature, final Scenario scenario, final List<Step> steps) {

        final String story = buildStory(steps);
        if (story == null) {
            return;
        }

        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, feature.getName() + " : " + scenario.getName(), "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(feature.getName());
        mv.visitLdcInsn(scenario.getName());
        mv.visitLdcInsn(story);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, "com/github/bmsantos/maven/cola/story/processor/StoryProcessor", "process", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V", false);
        mv.visitInsn(RETURN);
        mv.visitAnnotation("Lorg/junit/Test;", true);
        mv.visitEnd();
        mv.visitMaxs(0, 0);
    }

    private String buildStory(final List<Step> steps) {
        String story = "";
        for (final Step step : steps) {
            story += step.getKeyword() + step.getName() + "\n";
        }
        return story;
    }
}
