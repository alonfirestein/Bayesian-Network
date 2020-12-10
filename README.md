# Bayesian Network

This project is an implementation of a Bayesian Network and different probabilistic deduction algorithms in java. In the project, three algorithms are implemented and each algorithm is built to answer queries in the form: P(A|B,C).

- The first algorithm is a simple probalistic deduction using the Bayes' theorem.
- The second algorithm is the [Variable Elimination Algorithm](https://en.wikipedia.org/wiki/Variable_elimination).
- The third algorithm is the same as the second algorithm, meaning that it also uses the variable elimination algorithm, with the only difference being that the order of the factors is determined in a heuristic method so that the complexity of the algorithm would be more efficient.


--------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Running the program:
To run the program, you need a text file that is written in the same way as all the input files in the input folder of the repository.
The text file includes the bayesian network and all of its variables and each variable has its corresponding data (values, parents, CPT).
At the end of the text file there is a list of queries that the program will answer and execute and will create an output.txt file that contains answer to each query.
*This program requires that the amounts of spaces, commas and other small punctuation marks be written in the same fashion as the rest of the input files.*

To officially execute the program, just run the Ex1 class, enter the input file name, and the program does the rest.


--------------------------------------------------------------------------------------------------------------------------------------------------------------------

This picture is an example of a bayesian netowrk also in a graph form:

![Bayesian-Netowrk](https://user-images.githubusercontent.com/57404551/101762131-b1369c00-3ae5-11eb-97b6-3cada57c90f4.png)

