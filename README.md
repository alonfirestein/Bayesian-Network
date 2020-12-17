# Bayesian Network

This project is an implementation of a Bayesian Network and different probabilistic deduction algorithms in java. In the project, three algorithms are implemented and each algorithm is built to answer queries in the form: P(A|B,C).

- The first algorithm is a simple probalistic deduction.
- The second algorithm is the [Variable Elimination Algorithm](https://en.wikipedia.org/wiki/Variable_elimination).
- The third algorithm is the same as the second algorithm, meaning that it also uses the variable elimination algorithm, with the only difference being that the order of the factors is determined in a heuristic method so that the complexity of the algorithm would be more efficient.


--------------------------------------------------------------------------------------------------------------------------------------------------------------------


# Text Files:
To run the program, you need a text file that is written in the same way as all the input files in the input folder of the repository.
The text file includes the bayesian network and all of its variables and each variable has its corresponding data (values, parents, CPT).
At the end of the text file there is a list of queries that the program will answer and execute and will create an output.txt file that contains an answer to each query. On each line of the output file, there are 3 numbers seperated by commas, the first number is the answer of the relevant query, the second and third numbers are the amount of additions and multiplications the program had to compute to get the answer.  
  
*This program requires that the amounts of spaces, commas and other small punctuation marks be written in the same fashion as the rest of the input files.*  
  
  
--------------------------------------------------------------------------------------------------------------------------------------------------------------------
  
    
# Running the program:

Clone the project to your current workspace directory:  
`git clone https://github.com/alonfirestein/Bayesian-Network.git`
  
Open your terminal and navigate to the directory where the project is located, make sure the input files and the src folder are in the same directory.

Run the command to compile the program: `javac Ex1.java`

Run the command to run the program: `java Ex1`
  
And then enter the input file you wish the program to compute.
  
For Example:  
  
![example](https://user-images.githubusercontent.com/57404551/102539488-6f45c100-40b6-11eb-9fb4-79f2e6e19b45.png)

*The program will always print out the answers to the same output.txt file.*




--------------------------------------------------------------------------------------------------------------------------------------------------------------------

This picture is an example of a bayesian network (and also the first input.txt file):

<img src="https://user-images.githubusercontent.com/57404551/101762956-c2cc7380-3ae6-11eb-98f1-7a1b265a657b.jpg" height="400" width="800">

