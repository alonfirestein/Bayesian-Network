import java.util.*;

/**
 * This class was created in order to implement a variable in the Bayesian Network.
 *
 * @author - Alon Firestein
 */
public class Variable {

    String VarName;
    List<String> Values;
    List<Variable> Parents;
    Hashtable<String,Double> CPT;
    String ValueForQuery;
    int NumberOfNeighbours;

    public Variable(String name) {
        VarName = name;
        Values = new ArrayList<>();
        Parents = new ArrayList<>();
        CPT = new Hashtable<>();
        ValueForQuery = null;
    }

    public Variable(Variable other, String value) {
        this.VarName = other.VarName;
        this.Values = other.Values;
        this.Parents = other.Parents;
        this.ValueForQuery = value;
        this.CPT = other.CPT;
    }

    public Variable(String name, int NumberOfNeighbours) {
        this.VarName = name;
        this.NumberOfNeighbours = NumberOfNeighbours;
    }


    @Override
    public String toString() {
        return "Variable{" +
                "VarName='" + VarName + '\'' +
                ", Values=" + Values +
                ", Parents=" + Parents +
                ", CPT=" + CPT +
                ", ValueForQuery='" + ValueForQuery + '\'' +
                '}';
    }







}
