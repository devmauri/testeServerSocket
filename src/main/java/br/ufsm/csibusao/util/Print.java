package br.ufsm.csibusao.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author mauri
 */
public class Print {

    private boolean show = true;
    private String log;
    private String instanceed;
    private String place;
    //private String caminho = "C:\\log" + File.separator;
    private String caminho = "/home/log" + File.separator;
    private String nome = "Log.txt";

    private Queue<String> buffer;
    private int maxBuffer;

    public Print(String i, String p) {
        this.log = "";
        this.place = p;
        this.instanceed = i;
        
        this.maxBuffer = 1;
        this.buffer = new LinkedList<String>();
        //new Thread(new Produtor()).start();
        new Thread(new Consumidor()).start();
    }

    public void Inform(String print) {
        this.addLog(print);
        DebugLN(print);
    }

    private void LogLugar(String print, String lugar) {
        String tplace = this.place;
        this.place = lugar;
        this.addLog(print);
        this.place = tplace;
    }

    public void InformError(String print) {
        LogLugar(print, "\nError in " + this.place);
        System.err.println("\nErro em " + this.place + "\n\tErro: " + print);
    }

    public void printLog() {
        if (show) {
            System.out.print("\n======================================="
                    + "================================================");
            System.out.print("\nPrinting LOG\n");
            System.out.print("\n======================================="
                    + "================================================");
            System.out.println("\n\tLocal: " + this.instanceed
                    + this.log);
        }
    }

    public void addLog(String log) {
        this.log += "\n\t" + this.place + "->" + log;
    }

    private void DebugRaw(String print) {
        System.out.print(print);
    }

    public void DebugLN(String print) {
        if (show) {
            DebugRaw("\n\tDebug:" + print + "\n");
        }
    }

    public void InformLugar(String print, String lugar) {
        LogLugar(print, lugar + this.place);
        DebugLN(print);
    }

    public void GravarLog(String autor) {
        try {
            this.log+= "Log Sendo gravado por "+autor;
            new Thread(new Produtor()).start();
        } catch (Exception e) {
            System.err.println("Erro ao gravar aquivo de log ->" + e.toString());
        }

    }

    private class Produtor implements Runnable {

        @Override
        public void run() {
            try {                
                    synchronized (buffer) {

                        if (buffer.size() >= maxBuffer) {
                            buffer.wait();
                        }

                        synchronized (log) {
                            buffer.add(log);
                            log = "";
                        }

                        if (buffer.size() == 1) {
                            buffer.notify();
                        }
                    }                
            } catch (Exception e) {
                System.out.println(e);
            } finally {
            }
        }

    }

    private class Consumidor implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    String gravar = "";
                    synchronized (buffer) {
                        if (buffer.size() <= 0) {
                            buffer.wait();
                        }

                        gravar += buffer.poll();

                        if (buffer.size() >= (maxBuffer - 1)) {
                            buffer.notify();
                        }
                    }

                    File file = null;

                    FileInputStream fin = null;
                    DataInputStream din = null;

                    FileOutputStream fout = null;
                    DataOutputStream dout = null;

                    try {
                        file = new File(caminho + nome);
                        if (file.exists()) {
                            fin = new FileInputStream(file);
                            din = new DataInputStream(fin);
                            gravar = din.readUTF();
                            fin.close();
                            din.close();
                        } else {
                            file = new File(caminho);
                            file.mkdirs();
                            file = new File(caminho + nome);
                        }

                        fout = new FileOutputStream(file);
                        dout = new DataOutputStream(fout);

                        dout.writeUTF(gravar);

                    } catch (Exception e) {
                        System.err.println("Erro ao gravar aquivo de log ->" + e.toString());
                    } finally {
                        fin.close();
                        din.close();
                        fout.close();
                        dout.close();
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
            }
        }
    }

}
