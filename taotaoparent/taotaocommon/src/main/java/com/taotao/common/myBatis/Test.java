package com.taotao.common.myBatis;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.Arrays;

/**
 * @ Create by ostreamBaba on 18-12-7
 * @ 描述
 */
public class Test {

    PipedWriter writer = new PipedWriter();
    PipedReader reader = new PipedReader();

    public Test() {
        try {
            writer.connect(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(){
        try {
            writer.write("fuck you");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void read(){
        try {
            char[] bytes = new char[500];
            reader.read(bytes);
            System.out.println(Arrays.toString(bytes).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Test test = new Test();
        Thread writerThread = new Thread(test::write);
        Thread readThread = new Thread(test::read);
        writerThread.start();
        writerThread.join();
        readThread.start();
        readThread.join();

    }

}
