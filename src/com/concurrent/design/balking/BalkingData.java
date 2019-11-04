package com.concurrent.design.balking;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 当数据已经被处理的时候，则退出
 * 使用追加文件来模拟
 */
public class BalkingData {

    private final String fileName;
    private String content;
    private boolean changed;

    public BalkingData(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.changed = true;
    }

    //有新内容要添加
    public synchronized void change(String newContent) {
        this.content = newContent;
        this.changed = true;
    }

    public synchronized void save() throws IOException {
        //如果已经被处理则退出
        if (!changed) {
            return;
        }
        doSave();
        this.changed = false;
    }

    private void doSave() throws IOException {
        try (Writer writer = new FileWriter(fileName, true)) {
            writer.write(content);
            writer.write("\n");
            writer.flush();
        }
    }
}
