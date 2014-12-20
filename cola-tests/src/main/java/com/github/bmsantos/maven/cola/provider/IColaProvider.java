package com.github.bmsantos.maven.cola.provider;

import java.net.URLClassLoader;
import java.util.List;

public interface IColaProvider {

    public String getTargetDirectory();

    public URLClassLoader getTargetClassLoader() throws Exception;

    public List<String> getTargetClasses();
}
