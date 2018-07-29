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
    private File logFile;
    private JTextArea jta;
    private JLabel jl;
    private boolean finished = false;
    public Changefiles() {
        logFile = new File("log.txt");
    }
    public void encode(LinkedList<File> files){
        try {
            finished = false;
            File ffmpeg = Main.ffmpeg;
            Thread t = new Thread(( )-> {
                int docCounter = 0;
                while(!finished){
                    try{
                        Thread.sleep(250);
                        FileReader fr = new FileReader(logFile);
                        BufferedReader br = new BufferedReader(fr);
                        int currentCount = 0;
                        br.skip(docCounter);
                        String k = br.readLine();
                        while(k != null){
                            if(currentCount < docCounter){
                                k=br.readLine();
                                currentCount++;
                            }else{
                                docCounter++;
                                jta.append(k + "\n");
                                k = br.readLine();
                            }
                        }
                    }catch(FileNotFoundException fnf){

                    }catch(IOException ioe){

                    }catch(InterruptedException ie){

                    }
                }
            });
            t.start();
            for (int x = 0; x < files.size(); x++) {
                /*while(Gui.pause = true) {Todo: This stops the process completely, want a way to pause effectively
                    try {
                        Thread.sleep(500);
                   } catch (InterruptedException e) {

                    }
                }*/
                jl.setText(files.get(x).getName().substring(0,50) + "...");
                String convertedRenamed = "";
                jpb.setString(x + " / " + jpb.getMaximum());
                String actualPath = files.get(x).getAbsolutePath();
                String actualName = files.get(x).getName();
                String changedextension = actualName.substring(0,actualName.length()-4) + "-c." + extension;
                String splitName[] = actualPath.split(actualName);
                System.out.println(actualName);
                System.out.println(actualPath);
                System.out.println(changedextension);
                convertedRenamed = splitName[0] + changedextension;
                System.out.println(splitName[0]);
                String localArgs = args;
                localArgs = ffmpeg.getName() + ",-analyzeduration,100M,-probesize,100M,-i," +  actualPath + "," +  args + ","  + convertedRenamed;
                ProcessBuilder pb =  new ProcessBuilder(localArgs.split(","));
                pb.redirectErrorStream(true);
                pb.redirectOutput(logFile);
                Process p = pb.start();
                try{
                    p.waitFor();
                }catch(InterruptedException IE){
                    System.out.println(IE.getMessage());
                }
                jpb.setValue(x+1);
                jpb.setString(x+1 + " / " + jpb.getMaximum());

            }
            finished = true;
            jpb.setString("Completed!");
            jl.setText("Completed!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
    public void setTextArea(JTextArea jt){
        jta=jt;
    }
    public void setCurrentFileLabel(JLabel l){
        jl = l;
    }
}