import java.io.*;
import java.util.*;

/**
 * This class was created in order to start executing the program and to properly read and store all of the information
 * and the data in the file so that the program can answer the queries of the Bayesian Network.
 *
 * @author - Alon Firestein
 */
public class ReadInputFile {

    int FileRowPosition;
    int QueryPositionInFile;
    List<String> FileList;
    List<String> QueriesList;
    List<QueryQuestion> QuestionsList;
    List<Variable> BayesGraph;
    List<String> Variables;
    List<Integer> VariablesIndexList;

    /**
     * Resetting all the variables in the class.
     */
    public void reset() {
        FileRowPosition = 0;
        QueryPositionInFile = 0;
        BayesGraph = new ArrayList<>();
        FileList = new ArrayList<>();
        QueriesList = new ArrayList<>();
        Variables = new ArrayList<>();
        VariablesIndexList = new ArrayList<>();
    }

    /**
     * Starting and executing all necessary methods and functions in order to properly read the file
     * and store all of its info in several organized data structures.
     * @param FileName - the name of the file on which this program executes.
     */
    public void start(String FileName) {
        reset();
        FileList = ReadFileContent(FileName);
        FindNumberOfVariables();
        GetVariableIndexInFile();
        AddToQueryList();
    }

    /**
     * Receiving and Reading the input file name from the user.
     * @return - the file name with a .txt extension
     */
    public String ReadFileName() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter input file name: ");
        String FileName = scan.nextLine();
        if (!FileName.contains(".txt")) {
            FileName += ".txt";
        }
        scan.close();
        return FileName;
    }

    /**
     * Reading each line in the file and storing it in a ArrayList for easy access to all the information.
     * @param FileName
     * @return - ArrayList containing each line/row in the input file.
     */
    public List<String> ReadFileContent(String FileName) {


        try {
            File file = new File(FileName);
            FileReader fileReader = new FileReader("input/"+file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            FileList = new ArrayList<>();
            String NewLine;
            //Runs until EOF
            while((NewLine = bufferedReader.readLine()) != null) {
                FileList.add(NewLine.toLowerCase());
            }
            bufferedReader.close();


        } catch (FileNotFoundException e) {
            System.err.println("Can't find the file, please make sure you correctly wrote the file name and it does in fact exist.");
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("Can't read the file");
            e.printStackTrace();
        }

        return FileList;
    }

    /**
     * Finding the number of variables that are located in the file and storing only them in an ArrayList.
     */
    private void FindNumberOfVariables() {
        String Vars;
        FileRowPosition++; //Skipping the first line which contains "Network"

        if (FileList.get(FileRowPosition).contains("variables:")) {
            Vars = FileList.get(FileRowPosition).substring(11);
            String[] VariablesList = Vars.split(",");

            for (String var : VariablesList) {
                var = var.trim(); // Removing all unnecessary whitespace and leaving only the variable.
                Variables.add(var);
            }
            FileRowPosition++;
        }
    }

    /**
     * Finding the index of each variable and where it is located (according to the line number in the file).
     * Therefore we can jump to any variable we need and all of its relevant information.
     */
    private void GetVariableIndexInFile() {

        while (!FileList.get(FileRowPosition).contains("queries")) {

            if (FileList.get(FileRowPosition).contains("var")) {
                VariablesIndexList.add(FileRowPosition);
            }
            FileRowPosition++;
        }
        QueryPositionInFile = FileRowPosition; //Queries start here.
    }

    /**
     * Adding all of the queries in the file into its own data structure.
     */
    public void AddToQueryList() {
        QueryPositionInFile++; // In order to skip the first line that contains only "Queries".

        while (QueryPositionInFile < FileList.size()) {
            QueriesList.add(FileList.get(QueryPositionInFile));
            QueryPositionInFile++;
        }
    }

    /**
     * "Starting" the data structure which will contain the the questions located in the queries and receiving
     * the Bayes Graph info and data from the BayesGraph class.
     * @param BayesGraph
     */
    public void Query(List<Variable> BayesGraph) {

        QuestionsList = new ArrayList<>();
        this.BayesGraph = BayesGraph;
        SplittingTheQueries();
    }

    /**
     * Splitting the queries in order to receive and store all of it's relevant information so we can properly
     * utilize and answer the queries in the input file.
     */
    public void SplittingTheQueries() {

        for (String query : QueriesList) {
            QueryQuestion Question = new QueryQuestion();
            String[] QuestionLine = query.split("[()]+");
            String TheQuestion = QuestionLine[1];
            String[] SplitQuestion = TheQuestion.split("[=|, ]+");

            for(int i = 0; i < SplitQuestion.length; i+=2) {
                for (Variable var : this.BayesGraph) {
                    if(var.VarName.equals(SplitQuestion[i])) {
                        Variable v = new Variable(var, SplitQuestion[i + 1]);
                        Question.VariablesInQuestion.add(v);
                        break;
                    }
                }
            }
            if( QuestionLine[QuestionLine.length-1].contains("1")) {
                Question.Algorithm = 1;
            }
            else if(QuestionLine[QuestionLine.length-1].contains("2")) {
                Question.Algorithm = 2;
            }
            else {
                Question.Algorithm = 3;
            }
            QuestionsList.add(Question);
        }
    }


}
