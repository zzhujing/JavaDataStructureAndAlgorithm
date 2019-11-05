package com.concurrent.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PriorityChildClassLoader extends ClassLoader {

    private String classLoaderName;

    private static final String DEFAULT_LOAD_DIR = "/Users/hujing";

    private final String dir;

    public PriorityChildClassLoader() {
        super();
        this.classLoaderName = "CUSTOM_CLASSLOADER";
        this.dir = DEFAULT_LOAD_DIR;
    }

    public PriorityChildClassLoader(String classLoaderName, String dir) {
        this.classLoaderName = classLoaderName;
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    /**
     * xxx.xxx.AAA -> xxx/xxx/AAA.class
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name == null || name.isEmpty() || name.trim().isEmpty())
            throw new ClassNotFoundException();

        String result = name.replace(".", "/");
        File classFile = new File(dir, result + ".class");
        if (!classFile.exists())
            throw new ClassNotFoundException();

        byte[] classByteArray = doLoadClass(classFile);

        if (classByteArray == null || classByteArray.length == 0)
            throw new ClassNotFoundException("load class byte empty");

        return this.defineClass(name, classByteArray, 0, classByteArray.length);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (name.startsWith("java.")) {
            final ClassLoader system = ClassLoader.getSystemClassLoader();
            clazz = system.loadClass(name);
            if (clazz != null && resolve)
                resolveClass(clazz);
            return clazz;
        }
        try {
            clazz = findClass(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (clazz == null && getParent() != null) {
            getParent().loadClass(name);
        }
        return clazz;
    }

    private byte[] doLoadClass(File classFile) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             FileInputStream fis = new FileInputStream(classFile)
        ) {
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            baos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
