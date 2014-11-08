package com.github.bmsantos.maven.cola.injector;

import static java.util.Collections.emptyMap;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.Step;

import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class InjectorClassVisitor extends ClassVisitor {
    
    private final ClassWriter cw;
    private String stories;
    private Map<Scenario, List<Step>> scenarios = emptyMap();
    
    public InjectorClassVisitor(final int api, final ClassWriter cw) {
        super(api, cw);
        this.cw = cw;
    }
    
    @Override
    public FieldVisitor visitField(final int access, final String name,
            final String desc, final String signature, final Object value) {
        if (name.equals("stories")) {
            stories = (String) value;
            scenarios = StoryFormatter.parse(stories);
        }
        return super.visitField(access, name, desc, signature, value);
    }
    
    @Override
    public void visitEnd() {
        
        for (final Scenario scenario : scenarios.keySet()) {
            injectTestMethod(scenario, scenarios.get(scenario));
        }
        
        super.visitEnd();
    }
    
    private void injectTestMethod(final Scenario scenario, final List<Step> steps) {
        
        final String story = buildStory(steps);
        if (story == null) {
            return;
        }
        
        final MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, scenario.getName(), "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitLdcInsn(scenario.getName());
        mv.visitLdcInsn(story);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, "com/github/bmsantos/maven/cola/processor/StoryProcessor", "process", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)V", false);
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
