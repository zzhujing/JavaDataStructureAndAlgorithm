package com.concurrent.classloader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class EncryptUtils {

    public static final byte ENCRYPT_FACTORY = (byte) 0xff;

    public static void encrypt(String source, String target) {

        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(target)
        ) {
            int len = 0;
            while ((len = fis.read()) != -1) {
                fos.write(len ^ ENCRYPT_FACTORY);
            }
            fos.flush();
        } catch (IOException e) {
            //ignore
        }
    }

    public static void main(String[] args) {
//        EncryptUtils.encrypt("/Users/hujing/com/concurrent/classloader/MyObject.class","/Users/hujing/com/concurrent/classloader/MyObject1.class");

    }

}
