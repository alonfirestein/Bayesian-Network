import java.text.DecimalFormat;
import java.util.*;


/**
 * This class was created in order to implement the needed algorithms that were required to answer the questions of
 * the Bayesian Network. It includes 3 different algorithms, one using a basic deduction technique and the other two
 * using Variable Elimination, the only difference is that the third algorithm was implemented using a heuristic
 * technique that's different than the second algorithm. Each algorithm has its necessary helper methods and functions
 * in this class as well in order to properly utilize them and it was be easier to create the algorithm.
 *
 * @author - Alon Firestein
 */
public class Algorithms {

    private static int AddCounter;
    private static int MulCounter;
    static DecimalFormat df = new DecimalFormat("#0.00000");
    private double FinalAnswer;


    public String BasicDeduction(List<List<Variable>> TheQuestion, int QuerySize, int tag) {

        StringBuilder result = new StringBuilder();

        double TempAnswer;
        double Answer = 0;
        for (int row = 0; row < QuerySize; row++) {
            TempAnswer = 1.0;
            int size = TheQuestion.get(row).size();
            for (int j = 0; j < size; j++) {
                double temp = BasicDeductionHelper(TheQuestion.get(row), TheQuestion.get(row).get(j));
                TempAnswer *= temp;
            }
            Answer += TempAnswer;
        }
        double TempNormal;
        double Normal = 0;
        for (int row = QuerySize; row < TheQuestion.size(); row++) {
            TempNormal = 1;
            int size = TheQuestion.get(row).size();
            for (int j = 0; j < size; j++) {
                double temp = BasicDeductionHelper(TheQuestion.get(row), TheQuestion.get(row).get(j));
                TempNormal *= temp;
            }
            Normal += TempNormal;

        }
        AddCounter = TheQuestion.size() - 1;
        MulCounter = ((TheQuestion.get(0).size() - 1) * (AddCounter + 1));
        FinalAnswer = (Answer / (Answer+Normal));
        if (tag == -1) AddCounter = MulCounter = 0;
        String AnswerAsString = (df.format(FinalAnswer));
        result.append(AnswerAsString).append(",").append(AddCounter).append(",").append(MulCounter);
        return result.toString();
    }

    private static double BasicDeductionHelper(List<Variable> VariableList, Variable CurrentVar) {

        StringBuilder result = new StringBuilder();

        if(CurrentVar.Parents.size() != 0 && CurrentVar.Parents != null) {
            for (Variable parent : CurrentVar.Parents) {
                for (Variable var : VariableList) {
                    if (parent.VarName.equals(var.VarName)) {
                        result.append(var.VarName).append(",").append(var.ValueForQuery).append(",");
                        break;
                    }
                }
            }
        }
        result.append(CurrentVar.VarName).append(",").append(CurrentVar.ValueForQuery);
        return CurrentVar.CPT.get(result.toString());
    }


    public static String VariableElimination(QueryQuestion q, List<Variable> graph, int AlgoNum, String RequiredAnswer, int tag) {

        AddCounter = 0;
        MulCounter = 0;

        List<Variable> NecessaryNodesList = new ArrayList<>();
        NecessaryNodesList.addAll(RequiredNodes.KeepOnlyNecessaryNodes(q, graph));
        List<Hashtable<String, Double>> FactorsList = new ArrayList<>();

        List<Variable> VarsList = q.VariablesInQuestion;
        for (Variable var : NecessaryNodesList) {
            FactorsList.add(new Hashtable<>(var.CPT));
        }
        for (Hashtable<String, Double> factor : FactorsList) {
            Enumeration<String> keys = factor.keys();
            while (keys.hasMoreElements()) {
                String line = keys.nextElement();
                String[] arr = line.split(",");
                for (int i = 0; i < arr.length; i += 2) {
                    for (int j = 1; j < VarsList.size(); j++) {
                        if (arr[i].equals(VarsList.get(j).VarName) && !arr[i + 1].equals(VarsList.get(j).ValueForQuery))
                            factor.remove(line);
                    }
                }
            }
        }
        List<Variable> VarsToEliminate = new ArrayList<>();
        for (Variable var : NecessaryNodesList) {
            boolean MissingVar = false;
            for (Variable VarInQuestion : q.VariablesInQuestion)
                if (var.VarName.equals(VarInQuestion.VarName)) {
                    MissingVar = true;
                    break;
                }
            if (!MissingVar)
                VarsToEliminate.add(var);
        }
        //Using this helper method, we know which variables to eliminate (in alphabetical order),
        //and then we eliminate them.
        VarsToEliminate = DetermineEliminationOrder(VarsToEliminate, AlgoNum, NecessaryNodesList);
        for (Variable var : VarsToEliminate) {
            List<Hashtable<String, Double>> VarsInFactorsList = new ArrayList<>();

            for (Hashtable<String, Double> factors : FactorsList) {
                Enumeration<String> keys = factors.keys();
                String key = keys.nextElement();
                String[] temp = key.split(",");

                for (int i = 0; i < temp.length; i += 2) {
                    if (temp[i].equals(var.VarName)) {
                        VarsInFactorsList.add(factors);
                }
            }
        }
        FactorsList.removeAll(VarsInFactorsList);
        FactorsList.add(MultiplyingFactors(VarsInFactorsList, var));
        }
        while (FactorsList.size() > 1) {
            FactorsList.sort(Comparator.comparingInt(Hashtable::size)); //In order to sort the factors
            FactorsList.add(MultiplyingTwoFactors(FactorsList.get(0), FactorsList.get(1)));
            FactorsList.remove(0);
            FactorsList.remove(0);
            FactorsList.sort(Comparator.comparingInt(Hashtable::size));
        }

        double answer = NormalizeFactors(FactorsList.get(0), q.VariablesInQuestion, RequiredAnswer);
        if (tag == -1) AddCounter = MulCounter = 0;
        return String.valueOf(df.format(answer)) +
                ',' +
                AddCounter +
                ',' +
                MulCounter;

    }

    //For normalizing the factors we won't count the number of multiplications that occurred, only the additions.
    private static double NormalizeFactors(Hashtable<String, Double> FactorsTable, List<Variable> VarList, String TrueAnswer) {

        double TrueElements = 0;
        double TotalSum = 0;
        double FinalResult = 0;
        int AdditionsInTable = FactorsTable.size() - 1;
        Enumeration<String> FactorKeys = FactorsTable.keys();

        while (FactorKeys.hasMoreElements()) {
            String KeyElement = FactorKeys.nextElement();
            String[] KeyArray = KeyElement.split(",");
            for (int key = 0; key < KeyArray.length; key += 2) {
                if (KeyArray[key+1].equals(TrueAnswer) && KeyArray[key].equals(VarList.get(0).VarName))
                    TrueElements = FactorsTable.get(KeyElement);
            }
            TotalSum += FactorsTable.get(KeyElement);
            FinalResult = (TrueElements / TotalSum);
        }
        AddCounter += AdditionsInTable;
        return FinalResult;
    }



    private static Hashtable<String, Double> MultiplyingFactors(List<Hashtable<String, Double>> VarsInFactorsList, Variable var) {

        VarsInFactorsList.sort(Comparator.comparingInt(Hashtable::size));
        while (VarsInFactorsList.size() > 1) {

            VarsInFactorsList.add(MultiplyingTwoFactors(VarsInFactorsList.get(0), VarsInFactorsList.get(1)));
            VarsInFactorsList.remove(0);
            VarsInFactorsList.remove(0);
            VarsInFactorsList.sort(Comparator.comparingInt(Hashtable::size));
        }
        Hashtable<String, Double> Factors = VarsInFactorsList.get(0);
        Hashtable<String, Double> FactorsAfterSummation = new Hashtable<>();
        List<String> FactorsToSubtract = new ArrayList<>();

        while (!Factors.isEmpty()) {
            Enumeration<String> keys = Factors.keys();
            String FirstKey = keys.nextElement();
            FactorsToSubtract.add(FirstKey);
            Enumeration<String> KeysToCompare = Factors.keys();

            while (KeysToCompare.hasMoreElements()) {
                String OtherKey = KeysToCompare.nextElement();
                if (!FirstKey.equals(OtherKey)) {
                    if (IsSummationPossible(FirstKey, OtherKey, var.VarName)) {
                        FactorsToSubtract.add(OtherKey);
                        AddCounter++;
                    }
                }
            }
            double sum = 0;
            for (String string : FactorsToSubtract) {
                sum += Factors.get(string);
            }
            String answer = FactorsToSubtract.get(0);
            String[] arr = answer.split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < arr.length; i += 2) {
                if (!arr[i].equals(var.VarName)) {
                    builder.append(arr[i]).append(",").append(arr[i + 1]).append(",");
                }
            }
            builder.deleteCharAt(builder.length() - 1);
            FactorsAfterSummation.put(builder.toString(), sum);
            for (String factor : FactorsToSubtract) {
                Factors.remove(factor);
            }
            FactorsToSubtract.removeAll(FactorsToSubtract);
        }
        return FactorsAfterSummation;
    }


    private static boolean IsSummationPossible(String FirstKey, String Key, String var) {

        String[] FirstKeyArray = FirstKey.split(",");
        String[] KeyArray = Key.split(",");

        for (int i = 0; i < FirstKeyArray.length; i += 2) {
            if (FirstKeyArray[i].equals(var)) { i += 2; }
            for (int j = 0; j < KeyArray.length; j += 2) {
                if (FirstKeyArray[i].equals(KeyArray[j]) &&
                        (!FirstKeyArray[i + 1].equals(KeyArray[j + 1]))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static Hashtable<String, Double> MultiplyingTwoFactors(Hashtable<String, Double> FirstFactor, Hashtable<String, Double> SecondFactor) {

        Hashtable<String,Double> NewFactor = new Hashtable<>();
        Enumeration<String> FirstFactorsKeys = FirstFactor.keys();

        while(FirstFactorsKeys.hasMoreElements()) {
            String FirstFactors = FirstFactorsKeys.nextElement();
            String[] FirstFactorsArray = FirstFactors.split(",");
            Enumeration<String> SecondFactorsKeys = SecondFactor.keys();

            while(SecondFactorsKeys.hasMoreElements()) {
                String SecondFactors = SecondFactorsKeys.nextElement();
                String[] SecondFactorsArray = SecondFactors.split(",");
                String match = FindValuesToMatch(FirstFactorsArray, SecondFactorsArray);

                if(match != null) {
                    NewFactor.put(match, FirstFactor.get(FirstFactors)*SecondFactor.get(SecondFactors));
                    MulCounter++;
                }
            }
        }
        return NewFactor;
    }


    private static String FindValuesToMatch(String[] ValuesArray1, String[] ValuesArray2) {

        List<String> ValuesList = new ArrayList<>();
        for (int i = 0; i < ValuesArray1.length; i += 2) {
            for (int j = 0; j < ValuesArray2.length; j += 2) {
                if (ValuesArray1[i].equals(ValuesArray2[j])) {
                    if (!ValuesArray1[i+1].equals(ValuesArray2[j+1])) { return null; }
                    else {
                        ValuesList.add(ValuesArray1[i]);
                        ValuesList.add(ValuesArray1[i + 1]);
                    }
                }
            }
        }
        for (int i = 0; i < ValuesArray1.length; i += 2) {
            if (!ValuesList.contains(ValuesArray1[i])) {
                ValuesList.add(ValuesArray1[i]);
                ValuesList.add(ValuesArray1[i + 1]);
            }
        }
        for (int i = 0; i < ValuesArray2.length; i += 2) {
            if (!ValuesList.contains(ValuesArray2[i])) {
                ValuesList.add(ValuesArray2[i]);
                ValuesList.add(ValuesArray2[i + 1]);
            }
        }
        StringBuilder result = new StringBuilder();
        for (String value : ValuesList) {
            result.append(value).append(",");
        }

        result.deleteCharAt(result.length()-1);
        return result.toString();

    }



    private static List<Variable> DetermineEliminationOrder(List<Variable> VarsToEliminate, int AlgoMethod, List<Variable> Graph) {
        //Determining order of elimination of the variables (in alphabetical order):
        if (AlgoMethod == 2) {

            VarsToEliminate.sort((var1, var2) -> { char[] Var1Arr = var1.VarName.toCharArray();
                                                   char[] Var2Arr = var2.VarName.toCharArray();
                                                   int min = Math.min(Var1Arr.length, Var2Arr.length);
                                                   for (int i = 0; i < min; i++) {
                                                       return Character.compare(Var1Arr[i], Var2Arr[i]);
                                                }
              return Integer.compare(Var1Arr.length, Var2Arr.length); });
        }
        //Using a heuristic method:
         else if (AlgoMethod == 3)
            VarsToEliminate = HeuristicMethod(VarsToEliminate, Graph);
         return VarsToEliminate;


    }

    //For the third algorithm, using a heuristic method we will reorganize the order of the variables by the
    //number of neighbours they have.
    public static List<Variable> HeuristicMethod(List<Variable> VarsToEliminate, List<Variable> Graph) {

        List<Variable> VarsList = new ArrayList<>();
        List<Variable> OrderByNeighbours = new ArrayList<>();

        for (Variable variable : VarsToEliminate) {
            VarsList.add(new Variable(variable.VarName, 0));
        }
        for (Variable variable : Graph) {
            for (Variable Parent : variable.Parents) {
                for (Variable VarNeighbour : VarsList) {
                    if (Parent.VarName.equals(VarNeighbour.VarName)) {
                        VarNeighbour.NumberOfNeighbours++;
                        break;
                    }
                }
            }
        }
        VarsList.sort(Comparator.comparingInt(var -> var.NumberOfNeighbours));

        for (Variable Neighbour : VarsList) {
            for (Variable variable : VarsToEliminate) {
                if (Neighbour.VarName.equals(variable.VarName)) {
                    OrderByNeighbours.add(variable);
                }
            }
        }
        return OrderByNeighbours;
    }



}



