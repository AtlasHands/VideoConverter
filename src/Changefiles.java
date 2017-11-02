
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;


/**
 * Created by Alpine Tree on 6/20/2017.
 */
public class Changefiles {
    public JProgressBar jpb;
    public Process proc;
    private String args;
    private String extension;
    public Changefiles() {

    }
    public void encode(LinkedList<File> files){
        try {
                File ffmpeg = Main.ffmpeg;
                for (int x = 0; x < files.size(); x++) {
                    /*while(Gui.pause = true) {Todo: This stops the process completely, want a way to pause effectively
                        try {
                            Thread.sleep(500);
                       } catch (InterruptedException e) {

                        }
                    }*/
                    System.out.println(args);
                    jpb.setString(x + " / " + jpb.getMaximum());
                    int nameLength = files.get(x).getAbsolutePath().length();
                    String[] originalExtension = files.get(x).getPath().split(".");
                    System.out.println(x);
                    String renamed = files.get(x).getAbsolutePath().substring(0, nameLength - 4) + "-c." + extension;
                    String tempargs = ffmpeg.toString() + ",-i," + files.get(x).getAbsolutePath() + "," +  args + ","  + renamed;
                    ProcessBuilder pc = new ProcessBuilder(tempargs.split(","));
                    pc.inheritIO();
                    pc.redirectOutput();
                    proc = pc.start();
                    try {
                        proc.waitFor();
                    } catch (InterruptedException e) {

                    }
                    File rename = new File(files.get(x).getAbsolutePath().substring(0,nameLength - 4) + "-o." + extension);
                    files.get(x).renameTo(rename);
                    jpb.setValue(x+1);
                    jpb.setString(x+1 + " / " + jpb.getMaximum());

                }
                jpb.setString("Completed!");

        } catch (IOException e) {
            System.out.println("Unable to send command");
        } catch (NullPointerException nl) {
            System.out.println(nl.getStackTrace());
            jpb.setString("Completed!");
        }
    }
    public void linkProgressBar(JProgressBar jProgress){
        jpb = jProgress;
    }
    public void setArgs(String argument){
        args = argument;
    }
    public void setExtension(String ext){
        extension = ext;
    }
}