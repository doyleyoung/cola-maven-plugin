package com.github.bmsantos.maven.cola.utils;

import static java.io.File.separator;

import java.io.File;

public final class ColaUtils {

    public static boolean isSet(final String value) {
        return value != null && !value.isEmpty();
    }

    public static boolean isClassFile(final String name) {
        return name.endsWith(".class");
    }

    public static String osToBinary(final String path) {
        return path.replace(separator, ".");
    }

    public static String classToBinary(final String path) {
        return osToBinary(path.replace(".class", ""));
    }

    public static String binaryToOS(final String binaryFormat) {
        return binaryFormat.replace(".", separator);
    }

    public static String binaryToOsClass(final String binaryFormat) {
        String result = binaryToOS(binaryFormat);
        if (!isClassFile(result)) {
            result += ".class";
        }
        return result;
    }

    public static boolean binaryFileExists(final String dir, final String clazz) {
        return isSet(dir) && isSet(clazz) && new File(dir + separator + binaryToOsClass(clazz)).exists();
    }
}
