package com.concurrent.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CustomClassLoader extends ClassLoader {

    private String classLoaderName;

    private static final String DEFAULT_LOAD_DIR = "/home/hujing";

    private  String dir;

    public CustomClassLoader() {
        super();
        this.classLoaderName = "CUSTOM_CLASSLOADER";
        this.dir = DEFAULT_LOAD_DIR;
    }

    public CustomClassLoader(ClassLoader parent) {
        super(parent);
        this.classLoaderName = "CUSTOM_CLASSLOADER";
        this.dir = DEFAULT_LOAD_DIR;
    }

    public CustomClassLoader(String classLoaderName, String dir) {
        this.classLoaderName = classLoaderName;
        this.dir = dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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

        //加载
        byte[] classByteArray = doLoadClass(classFile);

        if (classByteArray == null || classByteArray.length == 0)
            throw new ClassNotFoundException("load class byte empty");


        //解析
        return this.defineClass(name, classByteArray, 0, classByteArray.length);

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
