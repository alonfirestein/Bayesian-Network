import java.util.ArrayList;
import java.util.List;


/**
 * This class was created in order to answer the questions in the queries using a cartesian product.
 * For more information on Cartesian Products: https://en.wikipedia.org/wiki/Cartesian_product
 *
 * @author - Alon Firestein
 */
public class CartesianProduct {

    List<QueryQuestion> QuestionsList;
    List<Variable> Graph;
    List<String> AnswersList;



    public CartesianProduct(List<QueryQuestion> QuestionsList, List<Variable> Graph) {

        AnswersList = new ArrayList<>();
        this.QuestionsList = QuestionsList;
        this.Graph = Graph;
        ValuesPerVar();
    }


    private void CartesianProductBuilder(List<List<Variable>> TheQuestion, List<Variable> VariablesInQuestion, List<Variable> Graph) {

        List<Variable> VarsNotInQuestion = new ArrayList<>();
        for (Variable var : Graph) {
            boolean MissingVar = false;

            for (Variable LocatedVar : VariablesInQuestion) {
                if(var.VarName.equals(LocatedVar.VarName))
                    MissingVar = true;
            }
            if(MissingVar == false)
                VarsNotInQuestion.add(var);
        }
        List<Variable> AllVarsList = new ArrayList<>();
        AllVarsList.addAll(VariablesInQuestion);
        AllVarsList.addAll(VarsNotInQuestion);

        CombinePossibilities(VarsNotInQuestion, TheQuestion, AllVarsList, VariablesInQuestion.size(), 0);

    }


    private void CombinePossibilities(List<Variable> VarsNotInQuestion, List<List<Variable>> PossibilitiesList, List<Variable> AllVarsList, int size, int num) {

        if (size == AllVarsList.size()) {
            List<Variable> CopiedList = new ArrayList<>();
            for (Variable var : AllVarsList) {
                Variable newVar = new Variable(var, var.ValueForQuery);
                CopiedList.add(newVar);
            }
            PossibilitiesList.add(CopiedList);
        }
        else {
            for(int i = 0; i<VarsNotInQuestion.get(num).Values.size(); i++) {
                AllVarsList.get(size).ValueForQuery = VarsNotInQuestion.get(num).Values.get(i);
                CombinePossibilities(VarsNotInQuestion, PossibilitiesList, AllVarsList, size+1, num+1);
            }
        }
    }


    public void ValuesPerVar() {

        for (QueryQuestion question : QuestionsList) {

            Variable var = question.VariablesInQuestion.get(0);
            String answer = var.ValueForQuery;
            List<String> ValuesList = new ArrayList<>(var.Values);
            ValuesList.remove(var.ValueForQuery);
            List<List<Variable>> TheQuestion = new ArrayList<>();
            CartesianProductBuilder(TheQuestion, question.VariablesInQuestion, Graph);

            for (String value : ValuesList) {
                var.ValueForQuery = value;
                CartesianProductBuilder(TheQuestion, question.VariablesInQuestion, Graph);
            }
            int tag = TheQuestion.size()/2;
            String algo_1 = new Algorithms().BasicDeduction(TheQuestion, tag);
            String algo_2 = Algorithms.VariableElimination(question, Graph, 2, answer);
            String algo_3 = Algorithms.VariableElimination(question, Graph,3, answer);


            if (question.Algorithm == 1) {
                AnswersList.add(algo_1);
            }
            else if (question.Algorithm == 2) {
                AnswersList.add(algo_2);
            }
            else  {
                AnswersList.add(algo_3);
            }
        }
    }



}