
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;


/**
 * Created by Alpine Tree on 6/20/2017.
 */
public class Changefiles {
    public JProgressBar jpb;
    public Process proc;
    private String[] args;
    public Changefiles() {

    }
    public void encode(LinkedList<File> files){
        try {
            if(args == null){
                File ffmpeg = Main.ffmpeg;
                for (int x = 0; x < files.size(); x++) {
                    /*while(Gui.pause = true) {Todo: This stops the process completely, want a way to pause effectively
                        try {
                            Thread.sleep(500);
                       } catch (InterruptedException e) {

                        }
                    }*/
                    jpb.setString(x + " / " + jpb.getMaximum());
                    int nameLength = files.get(x).getAbsolutePath().length();
                    System.out.println(x);
                    String renamed = files.get(x).getAbsolutePath().substring(0, nameLength - 4) + "-c" + ".mp4";
                    String[] args = new String[]{ffmpeg.toString(), "-i", files.get(x).getAbsolutePath(),"-c:v", "libx265", "-preset", "superfast", "-x265-params", "crf=20", renamed};
                    ProcessBuilder pc = new ProcessBuilder(args);
                    pc.inheritIO();
                    proc = pc.start();
                    try {
                        proc.waitFor();
                    } catch (InterruptedException e) {

                    }
                    jpb.setValue(x+1);
                    jpb.setString(x+1 + " / " + jpb.getMaximum());

                }
                jpb.setString("Completed!");

            }

        } catch (IOException e) {
            System.out.println("Unable to send command");
        } catch (NullPointerException nl) {
            System.out.println("Last value reached");
            jpb.setString("Completed!");
        }
    }
    public void linkProgressBar(JProgressBar jProgress){
        jpb = jProgress;
    }
    public void setArgs(LinkedList<File> argslist){

    }
}