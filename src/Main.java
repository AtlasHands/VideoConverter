import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Alpine Tree on 6/23/2017.
 */
public class Main {
        //Todo: Add buttons so you can swap to mkv and only select one option.  Improve GUI, Suppress JfileChooser Warnings
        private static File copyFfmpeg() {
            try{
                File f = new File(ExportResource("ffmpeg.exe"));
                return f;
            }catch(Exception e){

            }
            return null;
        }
    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
        static public String ExportResource(String resourceName) throws Exception {
            InputStream stream = null;
            OutputStream resStreamOut = null;
            String jarFolder;
            try {
                stream = Main.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
                if(stream == null) {
                    throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
                }

                int readBytes;
                byte[] buffer = new byte[4096];
                jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
                resStreamOut = new FileOutputStream(resourceName);
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                stream.close();
                resStreamOut.close();
            }

            return resourceName;
        }
        public static File ffmpeg;
        public static void main(String[] args) {
            ffmpeg = copyFfmpeg();
            Gui g = new Gui();
        }
}
