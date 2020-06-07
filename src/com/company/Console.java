package com.company;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Console extends Thread {

    PipedReader PR;
    PipedWriter PW;

    public PipedWriter getWriteStream(){
        return PW;
    }
    public PipedReader getReadStream(){
        return PR;
    }

    @Override
    public void run() {
        for(;MySingleton.pack.work;) {
            char[] buffer = new char[100];
            try {
                PR.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int j;
            for (j = 0; j < 100; j++) {
                if (buffer[j + 1] == '\u0000')
                    break;
            }
            char[] buf = new char[j + 1];
            for (int i = 0; i <= j; i++) {
                buf[i] = buffer[i];
            }

            String comand = new StringBuilder().append(buf).toString();
            String pe = new String("getPerson\n");
            String le = new String("getLaw\n");

            if (comand.equals(pe)) {
                try {
                    PW.write(Integer.toString(MySingleton.pack.numbersPerson));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (comand.equals(le)) {
                try {
                    PW.write(Integer.toString(MySingleton.pack.numdersLaw));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    PW.write("NO COMAND");
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
    }

    Console(PipedReader pipedReader, PipedWriter pipedWriter){
        try {
            PR = new PipedReader(pipedWriter);
            PW = new PipedWriter(pipedReader);
        }catch(IOException exy){
            exy.printStackTrace();
        }
    }

}
