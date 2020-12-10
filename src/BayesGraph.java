import java.util.*;

/**
 * This class was created in order to implement a Bayes Graph.(see link below for a visual example)
 * Using this class, I stored the parents, values and the CPT of each variable in the network and
 * added those variables to the graph as "nodes".
 * Bayesian Network Graph Visual Example:
 * https://uol.de/en/lcs/probabilistic-programming/webchurch-and-openbugs/example-5-bayesian-network-student-model
 *
 * @author - Alon Firestein
 */
public class BayesGraph {

    List<String> FileList;
    List<Integer> VariableIndex;
    List<String> Variables;
    ArrayList<Variable> Graph;



    public BayesGraph(List<String> FileList, List<Integer> VariableIndex, List<String> Variables) {

        this.FileList = FileList;
        this.Variables = Variables;
        this.VariableIndex = VariableIndex;
        Graph = new ArrayList<>(Variables.size());
        AddVariablesToGraph();
    }

    /**
     * This function goes to each variable in the input file, and stores all of its values.
     */
    private void CollectVarValues() {

        for (int var : VariableIndex) {
            String CurrentVar = FileList.get(var).substring(4).trim();
            int VarValuesIndex = var + 1;
            Variable VarInGraph = Graph.get(Variables.indexOf(CurrentVar));

            if (CurrentVar.equals(VarInGraph.VarName)) {

                if (FileList.get(VarValuesIndex).contains("values:")) {
                    String ValuesOfCurrentVar = FileList.get(VarValuesIndex).substring(7);
                    String[] ValuesArray = ValuesOfCurrentVar.split(",");

                    for (String NewValue : ValuesArray) {
                        if (!VarInGraph.Values.contains(NewValue.trim())) {
                            VarInGraph.Values.add(NewValue.trim());
                        }
                    }
                }
            }
        }
    }


    /**
     * This function goes to each variable in the input file, and stores all of its parents(if it has any).
     */
    private void CollectVarParents() {

        for (int var : VariableIndex) {
            String CurrentVar = FileList.get(var).substring(4).trim();
            int VarParentsIndex = var + 2;
            Variable VarInGraph = Graph.get(Variables.indexOf(CurrentVar));

            if (CurrentVar.equals(VarInGraph.VarName)) {

                if (FileList.get(VarParentsIndex).contains("parents:")) {
                    String ParentsOfCurrentVar = FileList.get(VarParentsIndex).substring(8);
                    String[] ParentsArray = ParentsOfCurrentVar.split(",");

                    if (ParentsArray[0].contains("none")) {
                        continue;
                    }
                    else {
                        for (String parent : ParentsArray) {
                            parent = parent.trim();
                            for (Variable Node : Graph) {
                                if (Node.VarName.equals(parent)) {
                                    VarInGraph.Parents.add(Node);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * This function goes to each variable in the input file, and stores all of the data that is located
     * in the CPT section of the variable. Each variable's CPT section is very different, therefore for each
     * variable that has parents this function also stores each parents given value and its statistic probability value.
     * The given CPT data does not include whole data of the values that will sum to 100%, therefore this function
     * also stores the statistic probability value of the values that are not located in the CPT.
     */
    private void CollectVarCPT() {

        for (int var : VariableIndex) {
            String CurrentVar = FileList.get(var).substring(4).trim();
            int VarCPTindex = var + 3;
            Variable VarInGraph = Graph.get(Variables.indexOf(CurrentVar));

            if (CurrentVar.equals(VarInGraph.VarName)) {

                if (FileList.get(VarCPTindex).toLowerCase().contains("cpt:")) {
                    int CPTdata = VarCPTindex + 1;

                    while (!FileList.get(CPTdata).equals("")) {

                        String row = FileList.get(CPTdata).trim();
                        String[] CPTarray = row.split(",");

                        for (int value = 0; value < CPTarray.length; value++) {
                            CPTarray[value] = CPTarray[value].trim();

                            if (CPTarray[value].contains("=")) {
                                CPTarray[value] = CPTarray[value].substring(1);
                            }
                        }

                        int NumberOfParents = VarInGraph.Parents.size();
                        StringBuilder result = new StringBuilder();

                        for (int parent = 0; parent < NumberOfParents; parent++) {
                            result.append(VarInGraph.Parents.get(parent).VarName).append(",");
                            result.append(CPTarray[parent]).append(",");

                        }
                        result.append(VarInGraph.VarName).append(",");
                        double sum = 0;

                        for (int CurrentVarProb = NumberOfParents; CurrentVarProb < CPTarray.length; CurrentVarProb += 2) {
                            double probability = Double.parseDouble(CPTarray[CurrentVarProb + 1]);
                            sum += probability;
                            VarInGraph.CPT.put(result + CPTarray[CurrentVarProb], probability);
                        }
                        VarInGraph.CPT.put(result +
                                VarInGraph.Values.get(VarInGraph.Values.size() - 1), 1 - sum);
                        CPTdata++;
                    }
                }
            }
        }
    }


    /**
     * Adding each variable to the graph ("as a node") and executing the functions that collect all of their data
     * that is located in the input file.
     */
    public void AddVariablesToGraph() {

        for (String variable : Variables) {
            Variable var = new Variable(variable);
            Graph.add(var);
        }
        CollectVarValues();
        CollectVarParents();
        CollectVarCPT();

    }


    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        for (Variable node : Graph) {
            result.append(node.VarName).append("\n").append("Values: ");

            for (String value : node.Values) {
                result.append(value).append(" ");
            }

            result.append("\n" + "Parents:");

            for (Variable var : node.Parents) {
                result.append(var.VarName).append(" ");
            }

            result.append("\n" + "CPT:" + "\n");

            Enumeration<String> CPTdata = node.CPT.keys();
            while (CPTdata.hasMoreElements()) {
                String key = CPTdata.nextElement();
                result.append(key).append("  ").append(node.CPT.get(key)).append("\r").append("\n");
            }
        }
        return result.toString();
    }




}
