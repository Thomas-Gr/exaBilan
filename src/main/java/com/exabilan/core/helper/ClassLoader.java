package com.exabilan.core.helper;

public final class ClassLoader {

    private ClassLoader() {}

    public static java.lang.ClassLoader getClassLoader() {
        java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }

        return classLoader;
    }
}
