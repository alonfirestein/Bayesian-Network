
public class Ex1 {

    public static void main(String[] args) {

        ReadInputFile read = new ReadInputFile();
        String FileName = read.ReadFileName();
        read.start(FileName);
        BayesGraph graph = new BayesGraph(read.FileList, read.VariablesIndexList, read.Variables);
        read.Query(graph.Graph);
        CartesianProduct cartesianProduct = new CartesianProduct(read.QuestionsList, graph.Graph);
        new PrintToOutputFile(cartesianProduct.AnswersList);

    }


}
