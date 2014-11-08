package com.github.bmsantos.maven.cola.processor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.bmsantos.maven.cola.annotations.Given;
import com.github.bmsantos.maven.cola.annotations.Then;
import com.github.bmsantos.maven.cola.annotations.When;

public class StoryProcessor {
    private static final Logger log = LoggerFactory.getLogger(StoryProcessor.class);
    
    private static final String NEW_LINE = "\n";
    
    public static void process(final String scenario, final String story, final Object instance)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        
        log.info("Scenario: " + scenario);

        final Method[] methods = instance.getClass().getMethods();
        final String[] lines = story.split(NEW_LINE);
        
        final List<Method> calls = new ArrayList<Method>();
        Method found = null;
        for (final String line : lines) {
            final int firstSpace = line.indexOf(" ");
            final String type = line.substring(0, firstSpace);
            final String step = line.substring(firstSpace + 1);
            found = findMethodWithAnnotation(type, step, methods);
            if (found != null) {
                calls.add(found);
            } else {
                final String message = "Failed to find step: " + line;
                log.error(message);
                throw new RuntimeException(message);
            }
        }
        
        final Object[] empty = {};
        for (int i = 0; i < calls.size(); i++) {
            log.info("-> " + lines[i]);
            final Method method = calls.get(i);
            method.invoke(instance, empty);
        }
    }
    
    private static Method findMethodWithAnnotation(final String type, final String step, final Method[] methods) {
        for (final Method method : methods) {
            if ( (Given.class.getName().endsWith(type) && method.isAnnotationPresent(Given.class) &&
                    method.getAnnotation(Given.class).value().equals(step)) ||
                    (When.class.getName().endsWith(type) && method.isAnnotationPresent(When.class) &&
                            method.getAnnotation(When.class).value().equals(step)) ||
                            (Then.class.getName().endsWith(type) && method.isAnnotationPresent(Then.class) &&
                                    method.getAnnotation(Then.class).value().equals(step))) {
                return method;
            }
        }
        return null;
    }
}
