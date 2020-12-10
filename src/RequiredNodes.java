import java.util.*;

/**
 * This class was created in order to help answer the queries the were needed to be answered with Variable Elimination.
 * This class includes a function that create a list of all the necessary nodes that are needed to answer the query
 * and removes all the unnecessary nodes/variables.
 *
 * @author - Alon Firestein
 */
public class RequiredNodes {


    public static List<Variable> KeepOnlyNecessaryNodes(QueryQuestion Question, List<Variable> Graph) {

            Set<String> NecessaryNodes = new HashSet<>();
            Queue<Variable> VarQueue = new LinkedList<>(Question.VariablesInQuestion);

            while (!VarQueue.isEmpty()) {
                Variable CurrentVar = VarQueue.poll();
                VarQueue.addAll(CurrentVar.Parents);
                NecessaryNodes.add(CurrentVar.VarName);
            }

            List<Variable> VarList = new ArrayList<>();
            for (String node : NecessaryNodes) {
                for (Variable var : Graph) {
                    if(node.equals(var.VarName)) {
                        VarList.add(var);
                        break;
                    }
                }
            }
            return VarList;
        }

    }


