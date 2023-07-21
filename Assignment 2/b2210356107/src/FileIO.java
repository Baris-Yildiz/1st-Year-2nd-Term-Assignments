import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

/**
 * <p>
 *     FileIO class is the class that helps read files and write to files.
 * </p>
 * @version 1.0
 * */
public class FileIO {

    /**
     * Returns the contents of the file at the specified path in the form of a String array.
     * @param path the path of the file being read.
     * @return a string array that contains all lines.
     * @throws NoSuchFileException if <b>path</b> doesn't exist.
     * @author Nebi YILMAZ
     */
    public static String[] readFile(String path) throws NoSuchFileException {
        try {
            List<String> fileContents = Files.readAllLines(Paths.get(path));

            int length = fileContents.size();
            String[] results = new String[length];

            int i = 0;
            for (String fileContent : fileContents) {
                results[i++] = fileContent;
            }
            return results;
        } catch (IOException ioException) {
            return null;
        }
    }

    /**
     * Writes contents to the file at the specified path.
     * @param path the path of the file being written to.
     * @author Gorkem AKYILDIZ
     */
    public static void writeFile(String path, String content) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path));
            ps.print(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }
}
