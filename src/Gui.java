import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.io.File;
import java.util.LinkedList;

/**
 * Created by Alpine Tree on 6/20/2017.
 */
public class Gui {
    private JButton chooseFileDirectoryButton;
    private JPanel root;
    private JProgressBar progressBar1;
    private JComboBox speedComboBox1;
    private JComboBox codecComboBox2;
    private JComboBox encodingComboBox3;
    private JTextField textField1;
    private JSlider slider1;
    private JLabel timeLabel;
    private JButton pauseButton;
    private JCheckBox mp4CheckBox;
    private JCheckBox movCheckBox;
    private JCheckBox webmCheckBox;
    private JCheckBox mkvCheckBox;
    private JTextField textField2;
    private JCheckBox aviCheckBox;
    private JButton chooseSingleFileButton;
    private JCheckBox mpegCheckBox;
    private JLabel speedLabel;
    private JTextArea textArea1;
    private JLabel currentFileLabel;
    private JFileChooser jf;
    private JFrame f;
    private Changefiles ch;
    private int seconds;
    private int minutes;
    private int hours;
    private Timer timer;
    private String[] filter;
    public Gui(){
        //Shutdown hook, will run when window is closed
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try{
               // if(Main.ffmpeg != null) {
               //     Main.ffmpeg.delete();//deletes extracted ffmpeg if it exists
               // }
                ch.proc.destroy();
                ch.proc.destroyForcibly();
            }catch(NullPointerException e){}
        }));
        /////////////////////////////////
        //Setting icon

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/icon.png").getPath());
        /////////////////////////////////
        //Setting windows look and field
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (UnsupportedLookAndFeelException e) {}
        catch (ClassNotFoundException e) {}
        catch (InstantiationException e) {}
        catch (IllegalAccessException e) {}
        /////////////////////////////////
        ch = new Changefiles();
        ch.linkProgressBar(progressBar1);
        ch.setTextArea(textArea1);
        ch.setCurrentFileLabel(currentFileLabel);
        DefaultCaret caret = (DefaultCaret)textArea1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textArea1.setBorder(BorderFactory.createCompoundBorder(
                root.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        //Setting Default Values

        textField1.setText(Integer.toString(slider1.getValue()));
        progressBar1.setMinimum(0);
        progressBar1.setStringPainted(true);
        f = new JFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setIconImage(icon.getImage());
        f.setTitle("ConvertAll");
        f.add(root);
        f.pack();
        f.setResizable(false);
        f.setVisible(true);
        /////////////////////////////////
        //JFileChooser
        encodingComboBox3.addActionListener((ActionEvent e) ->{
            int index = encodingComboBox3.getSelectedIndex();
            if(index == 3 || index == 2){
                speedLabel.setVisible(false);
                speedComboBox1.setVisible(false);
            }else{
                speedLabel.setVisible(true);
                speedComboBox1.setVisible(true);
            }
        });
        chooseFileDirectoryButton.addActionListener((ActionEvent e) -> {
            progressBar1.setValue(0);
            progressBar1.setString("");
            jf = new JFileChooser();
            jf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = jf.showOpenDialog(f);
            if(returnVal == JFileChooser.APPROVE_OPTION) {

                File[] listOfFiles = jf.getSelectedFile().listFiles();
                filterBuilder(); //builds the filter
                sendFiles(listOfFiles);
            }
        });
        chooseSingleFileButton.addActionListener((ActionEvent e) ->{
            progressBar1.setValue(0);
            progressBar1.setString("");
            FileChooser fc = new FileChooser();

            jf = new JFileChooser();
            jf.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = jf.showOpenDialog(f);
            if(returnVal == JFileChooser.APPROVE_OPTION) {

                File[] listOfFiles = {jf.getSelectedFile()};
                filterBuilder(); //builds the filter
                sendFiles(listOfFiles);
            }
        });
        /////////////////////////////////
        //crf Slider
        slider1.addChangeListener((ChangeEvent ce) -> {
                int val = ((JSlider) ce.getSource()).getValue();
                textField1.setText(Integer.toString(val));
        });
        ///////////////////////////////// Todo: Find a way to pause a thread different from this way, this just stops everything
        //Pause button
        /*pauseButton.addActionListener((ActionEvent e) -> {
            if(pause = false){
                pause = true;
                pauseButton.setText("Pausing...");
                pauseButton.setText("Resume");
            }
            else{
                pause = false;
                pauseButton.setText("Resumed");
                try{
                    Thread.sleep(1000);
                    pauseButton.setText("Pause");
                }catch(InterruptedException x)
                {

                }
            }

        });*/
        /////////////////////////////////
        //Time elapsed settings
        resetTimer();
        timer = new Timer(1000,(ActionEvent e)->{
            {
                seconds++;
                if(seconds / 60 == 1){
                    minutes ++;
                    seconds = 0;
                }
                if(minutes / 60 == 1){
                    hours++;
                    minutes = 0;
                }
                timeLabel.setText(hours + "h:" + minutes + "m:" + seconds + "s");

            }
        });
        /////////////////////////////////

    }
    public void resetTimer(){
        seconds = 0;
        minutes = 0;
        hours = 0;
    }
    public void filterBuilder(){
        String tempfilter = "";
        if(mp4CheckBox.isSelected()){
            tempfilter = tempfilter + ".mp4,";
        }
        if(mkvCheckBox.isSelected()){
            tempfilter = tempfilter + ".mkv,";
        }
        if(webmCheckBox.isSelected()){
            tempfilter = tempfilter + ".webm,";
        }
        if(mpegCheckBox.isSelected()){
            tempfilter = tempfilter + ".mpeg,";
        }
        System.out.println(textField2.getText());
        if(aviCheckBox.isSelected()){
            tempfilter = tempfilter + ".avi,";
        }
        if(movCheckBox.isSelected()){
            tempfilter = tempfilter + ".mov";
        }
        filter = tempfilter.split(",");
        for(String ff : filter){
            System.out.println(ff);
        }
    }

    /**
     * Argument builder sets up a String array for arguments to be sent to process builder from UI components
     */
    public void ArgumentBuilder(){
        if(encodingComboBox3.getSelectedIndex() == 3){//AV1
            String args = "-c:v,libaom-av1,-crf," + slider1.getValue() + ",-strict,experimental";
            ch.setArgs(args);
        }
        if(encodingComboBox3.getSelectedIndex() == 2){//vp9
            String args = "-c:v,libvpx-vp9,-crf," + slider1.getValue() + ",-b:v,0";
            ch.setArgs(args);
        }
        if(encodingComboBox3.getSelectedIndex() == 1){//h264
            String args = "-c:v,libx264,-params," + speedComboBox1.getSelectedItem().toString() +",-crf," +slider1.getValue();
            ch.setArgs(args);
        }
        if(encodingComboBox3.getSelectedIndex() == 0) {//h265
            String args = "-c:v,libx265,-preset," + speedComboBox1.getSelectedItem().toString() + ",-x265-params," + "crf=" + slider1.getValue() + ",-acodec,copy,-c:s,copy";
            ch.setArgs(args);
        }
        ch.setExtension(codecComboBox2.getSelectedItem().toString());
    }

    /**
     * Uses expandFiles and filterFiles and then sends the files over to changefiles to be used with ffmpeg
     * @param rawFiles
     */
    public void sendFiles(File[] rawFiles){
        ArgumentBuilder();
        LinkedList<File> converted = new LinkedList<>();
        for(int x = 0;x<rawFiles.length;x++){
            converted.add(rawFiles[x]);
        }
        LinkedList<File> temp = expandFiles(converted);
        LinkedList<File> temp2;
        while(true){
            temp2 = expandFiles(temp);
            if(temp2.size() == temp.size()){
                break;
            }
            temp = temp2;
        }
        LinkedList<File> filtered = filterFiles(temp);
        Thread t = new Thread(() -> {
            resetTimer();
            timer.start();
            ch.encode(filtered);
            timer.stop();
        });
        t.start();

    }
    @SuppressWarnings("Unchecked") //This is for the linked list warning
    /**
     * expands files (currently only 2 folders deep) so that you can select a tv show directory and it will go into
     * seasons
     * @param LinkedList
     * @return returns all files that are not directories
     */
   public LinkedList<File> expandFiles(LinkedList<File> f){
        LinkedList<File> files = new LinkedList<>();
        File[] recursion;
        int counter = 1;
        while(counter != 0){
            counter = 0;
            for(File fz : f){
                if(fz.isDirectory()){
                    LinkedList<File> recursionLinkedList = new LinkedList<File>();
                    recursion = fz.listFiles();
                    for(File fy : recursion){
                        recursionLinkedList.add(fy);
                    }
                    files.addAll(expandFiles(recursionLinkedList));
                    continue;
                }
                files.add(fz);
            }
            System.out.println(files.size());
        }
        return files;
    }
    @SuppressWarnings("Unchecked") //This is for the linked list warning
    /**
     * Filters files containing certain end filenames, currently mkv, mov, avi, mts and mp4
     * will add configuration for this later
     * @param LinkedList<File>
     * @return Files only containing set file extensions
     */
    public LinkedList<File> filterFiles(LinkedList<File> files) {
        int total = files.size();
        LinkedList<File> filtered = new LinkedList<>();
        for(int x = 0;x<total;x++){
            String filename = files.get(x).getName();
            for(String r : filter){
                    if (filename.substring(filename.length() - (r.length()), filename.length()).equals(r)) {
                        filtered.add(files.get(x));
                }
            }

        }
        progressBar1.setMaximum(filtered.size());
        System.out.println(filtered.size());
        return filtered;
    }
}
