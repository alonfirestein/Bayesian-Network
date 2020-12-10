import java.util.ArrayList;
import java.util.List;

/**
 * This class was created so that the questions in the queries are accessible and stored in a organized fashion.
 *
 * @author - Alon Firestein
 */
public class QueryQuestion {
    List<Variable> VariablesInQuestion;
    int Algorithm; //1,2 or 3

    public QueryQuestion() {
        VariablesInQuestion = new ArrayList<>();
        Algorithm = 0;
    }
}