import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * This class was created in order to receive the answers created using the other classes in the project,
 * and to print them to a new output text file.
 *
 * @author - Alon Firestein
 */
public class PrintToOutputFile {

    List<String> answers;


    public PrintToOutputFile(List<String> answers) {

        this.answers = answers;
        PrintAnswersToFile();
    }

    public void PrintAnswersToFile() {

        try {

            FileWriter fileWriter = new FileWriter("output.txt");
            for (String str : answers) {
                fileWriter.write(str + "\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public PrintToOutputFile(String error) {

        try {
            FileWriter fileWriter = new FileWriter("output.txt");
            fileWriter.write(error+"\n");
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
