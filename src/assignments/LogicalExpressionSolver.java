package assignments;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

// Name: 		Inderpreet Dhillon
// UCID:  		10159608
// Class:  		CPSC 331
// Purpose:		This program is capable of solving logical expressions given that they are formatted correctly
// Usage: 		Enter an expression consisting of letters (A-z) and symbols (+, *, -, "(, ')') and the truth table will be printed
// 					Valid expression: 		((A+B)+(-C))
//					Invalid expression:	(A+B)+-C, this expression is missing outer parentheses and brackets around -C

public class LogicalExpressionSolver {

	// Declare global variables for storing input, independent variables and
	// sub-expressions
	private static String input;
	private static LinkedList<Character> variables;
	private static HashMap<String, String> expressions;

	// Declare final truth table and number of truth values per variable
	private static String[][] truthTable;
	private static int numTruthValues;

	// A unique identifier key for the sub-expressions in the HashMap
	private static final String IDENTIFIER = "LE";

	public static void main(String[] args) {
		// Get input string from the user
		getInput();

		// Find independent variables and sub-expressions
		parse();

		// Evaluate the sup-expressions and store the results in the truth table
		evaluate();

		// Print the independent variables
		printVariables();

		// Print the sub-expressions
		printExpressions();

		// Print the final truth table
		printTruthTable();
	}

	/*
	 * Gets a string from the user, this represents a logical expression
	 * 
	 * Pre-Condition:
	 * ~ input has been declared but not initialized
	 * 
	 * Post-Condition:
	 * ~ input has the user's value
	 */
	private static void getInput() {
		// Declare Scanner object for taking input
		Scanner keyb = new Scanner(System.in);

		// Prompt user for input and store upper-cased string
		System.out.print("Input string: ");
		input = keyb.nextLine().toUpperCase();

		// Close Scanner object
		keyb.close();
	}

	/*
	 * Finds all independent variables and sub-expressions present in the user's input
	 * 
	 * Pre-Condition:
	 * ~ variables, expressions, numTruthValues have been declared but not initialized
	 * ~ input, IDENTIFIER have values
	 * 
	 * Post-Condition:
	 * ~ variables is a set of independent variables
	 * ~ expressions is a set of sub-expressions
	 * ~ numTruthValues is 2^(number of independent variables)
	 * ~ input, IDENTIFIER have not changed value
	 */
	private static void parse() {
		// Initialize independent variables list
		variables = new LinkedList<Character>();

		// Initialize sub-expressions map
		expressions = new HashMap<String, String>();

		// Create stack to hold indices of all '(' in the expression
		Stack<Integer> indices = new Stack<Integer>();

		// Initialize counter for the number of sub-expressions
		int expressionCount = 0;

		// Traverse the input string
		for (int i = 0; i < input.length(); i++) {

			// Get the current character
			char c = input.charAt(i);

			// Handle the current character
			if (Character.isLetter(c)) {

				// Check if the list has the variable
				if (indexOf(variables, c) == -1) {
					// Add the character to the variables list
					variables.add(c);
				}
			} else if (c == '(') {
				// When the character is a '(', add the index to the stack
				indices.push(i);
			} else if (c == ')') {
				// Get the sub-expression from the last '(' to the current index
				String sub = input.substring(indices.pop() + 1, i);

				// Check if the map has the sub-expression
				if (!contains(expressions, sub)) {
					// Put the sub-expression in the map
					expressions.put(IDENTIFIER + expressionCount, sub);

					// Increment expression counter
					expressionCount++;
				}
			}
		}

		// Calculate the number of truth values based on the number of variables
		numTruthValues = (int) Math.pow(2, variables.size());
	}

	/*
	 * Constructs a truth table by evaluating the variables and expressions
	 * 
	 * Pre-Condition:
	 * ~ truthTable has been declared but not initialized
	 * ~ variables, expressions, numTruthValues, IDENTIFIER have values
	 * 
	 * Post-Condition:
	 * ~ truthTable is complete and correct
	 * ~ variables, expressions, numTruthValues, IDENTIFIER have not been modified
	 */
	private static void evaluate() {
		// Initialize the truth table with the correct dimensions
		truthTable = new String[1 + numTruthValues][variables.size() + expressions.size()];

		// Traverse each column of the truth table
		for (int i = 0; i < truthTable[0].length; i++) {

			// Declare empty truth values string
			String values = "";

			// Determine if the current column holds a variable or expression
			if (i < variables.size()) {
				// Get the current variable
				char variable = variables.get(i);

				// Set the current column header to variable at the index
				truthTable[0][i] = String.valueOf(variable);

				// Set the values to be those of the variable
				values = getTruthColumn(variable);
			} else {
				// Get the key for the expression
				String key = IDENTIFIER + (i - variables.size());

				// Set the column header to the expression at the index
				truthTable[0][i] = key;

				// Set values to be the evaluated truth values of the expression
				values = getTruthValues(expressions.get(key));
			}

			// Traverse each row of the truth table
			for (int j = 1; j < truthTable.length; j++) {

				// Get the current truth value
				char value = values.charAt(j - 1);

				// Store the value in the array
				truthTable[j][i] = String.valueOf(value);
			}
		}
	}

	/*
	 * Prints the independent variables
	 * 
	 * Pre-Condition:
	 * ~ variables has values
	 * 
	 * Post-Condition:
	 * ~ variables has not been modified
	 * ~ all of the values in variables have been printed
	 */
	private static void printVariables() {
		// Print the independent variables
		System.out.println("Output:\nSet of independent variables:");

		// Get the variables list's iterator
		Iterator<Character> iterator = variables.iterator();

		// Loop until the iterator runs out of items
		while (iterator.hasNext()) {

			// Print the next variable
			System.out.println(iterator.next());
		}
	}

	/*
	 * Prints the sub-expressions
	 * 
	 * Pre-Condition:
	 * ~ expressions, IDENTIFER have values
	 * 
	 * Post-Condition:
	 * ~ expressions, IDENTIFER have not been modified
	 * ~ all of the values of expressions have been printed
	 */
	private static void printExpressions() {
		// Print the sub-expressions
		System.out.println("Set of logical subexpressions and logical expression:");

		// Loop through the expressions
		for (int i = 0; i < expressions.size(); i++) {

			// Get the key from the current index
			String key = IDENTIFIER + i;

			// Print the key and sub-expression
			System.out.printf("%s, %s\n", key, expressions.get(key));
		}
	}

	/*
	 * Prints the truth table
	 * 
	 * Pre-Condition:
	 * ~ truthTable has values
	 * 
	 * Post-Condition:
	 * ~ truthTable has not been modified
	 * ~ truthTable has been printed
	 */
	private static void printTruthTable() {
		// Print the truth table
		System.out.println("Truth Table:");

		// Iterate through the rows
		for (int i = 0; i < truthTable.length; i++) {

			// Iterate through the columns
			for (int j = 0; j < truthTable[i].length; j++) {

				// Check which row we are on
				if (i == 0) {
					// Print the current element
					System.out.print(truthTable[i][j] + "\t");
				} else {
					// Create a char array of half the size of the column header
					char[] pad = new char[truthTable[0][j].length() / 2];

					// Create a string from the array and replace null
					// terminator character with spaces
					String padding = new String(pad).replace('\0', ' ');

					// Print the padding and current element
					System.out.print(padding + truthTable[i][j] + "\t");
				}
			}
			// Go to the next line
			System.out.println();
		}
	}

	/*
	 * Finds an object's position in a List
	 * 
	 * Pre-Condition:
	 * ~ list is a List
	 * ~ object is of the same type as list's values
	 * 
	 * Post-Condition
	 * ~ list, object have not been modified
	 * ~ returns object position in list if found, -1 otherwise
	 */
	private static <E> int indexOf(List<E> list, E object) {
		// Initialize index
		int index = -1;

		// Get iterator from list
		Iterator<E> iterator = list.iterator();

		// Traverse list until the object is found or all elements are used
		while (iterator.hasNext()) {
			// Increment index
			index++;
			
			// Compare next element to the object
			if (iterator.next().equals(object)) {
				// Return object index when found
				return index;
			}
		}
		
		// Return -1 when the object is not found
		return -1;
	}

	/*
	 * Detects if an object is in a Map
	 * 
	 * Pre-Condition:
	 * ~ map is a Map
	 * ~ object has the same type as the map's values
	 * 
	 * Post-Condition:
	 * ~ map, object have not been modified
	 * ~ true is returned if the object is in map, false otherwise
	 */
	private static <K, V> boolean contains(Map<K, V> map, V object) {
		// Get iterator from map's keys
		Iterator<K> iterator = map.keySet().iterator();

		// Traverse map until the object is found or all elements are used
		while (iterator.hasNext()) {

			// Compare value of next key to the object
			if (map.get(iterator.next()).equals(object)) {
				// Return true when the object is found
				return true;
			}
		}

		// Return false if the object has not been found
		return false;
	}

	/*
	 * Converts an infix expression to post-fix notation
	 * 
	 * Pre-Condition:
	 * ~ input is a string with balanced brackets
	 * 
	 * Post-Condition:
	 * ~ input has not been changed
	 * ~ a post-fix version of input is returned
	 */
	private static String toPostfix(String input) {
		// Initialize output string
		String output = "";

		// Initialize stack for operators
		Stack<Character> operators = new Stack<Character>();

		// Iterate through input string
		for (char c : input.toCharArray()) {

			// Determine what to do with the character
			if (Character.isLetter(c)) {
				// Append character to output when it is a letter
				output += c;
			} else if (c == ')') {
				// Append the operand from the stack if a ')' is found
				output += operators.pop();
			} else if (c != '(') {
				// Push the character to the top of the stack
				operators.push(c);
			}
		}

		// Loop until the stack is empty
		while (!operators.isEmpty()) {

			// Append the top element of the stack to output
			output += operators.pop();
		}

		// Return the output
		return output;
	}

	/*
	 * Calculates a logical expression's truth values
	 * 
	 * Pre-Condition:
	 * ~ expression is a logic expression with balanced brackets
	 * 
	 * Post-Condition:
	 * ~ expression has not been modified
	 * ~ a string of truth values for the expression are returned
	 */
	private static String getTruthValues(String expression) {
		// Initialize stack for truth values
		Stack<String> truthValues = new Stack<String>();

		// Convert the expression to post fix notation
		String postfix = toPostfix(expression);

		// Iterate through post-fix string
		for (char c : postfix.toCharArray()) {

			// Determine what to do with the character
			if (Character.isLetter(c)) {
				// Get the truth values for the character
				String values = getTruthColumn(c);

				// Put the truth values on the stack
				truthValues.push(values);
			} else {
				// Initialize string for truth values
				String values = "";

				// Deal with the operator
				if (c == '-') {
					// Get a single truth value string from the stack
					String operand = truthValues.pop();

					// Negate the operand
					values = negate(operand);
				} else if (c == '+') {
					// Get two operands from the stack
					String operandA = truthValues.pop();
					String operandB = truthValues.pop();

					// OR the operands
					values = or(operandA, operandB);
				} else if (c == '*') {
					// Get two operands from the stack
					String operandA = truthValues.pop();
					String operandB = truthValues.pop();

					// AND the operands
					values = and(operandA, operandB);
				}

				// Place the values back on the stack
				truthValues.push(values);
			}
		}

		// Take the top value and return it
		return truthValues.pop();
	}

	/*
	 * Gets the truth values for a single independent variable
	 * 
	 * Pre-Condition:
	 * ~ c is an independent variable
	 * ~ variables is a list of independent variables
	 * ~ variable occurs in the list of variables
	 * ~ numTruthValues is defined
	 * 
	 * Post-Condition:
	 * ~ variable, variables, numTruthValues have not been changed
	 * ~ the truth values of c are returned
	 */
	private static String getTruthColumn(char variable) {
		// Initialize string for truth values
		String values = "";

		// Calculate the number of trues before switching to falses, based on
		// the variable number and the total truth values
		int numTrues = (int) (numTruthValues / Math.pow(2, indexOf(variables, variable) + 1));

		// Initialize counter for each value
		int counter = 0;

		// Initialize boolean for whether the value is true or not
		boolean isTrue = true;

		// Loop for the number of truth values
		for (int i = 0; i < numTruthValues; i++) {

			// Check if enough trues have been put in
			if (counter == numTrues) {
				// Reset counter
				counter = 0;

				// Toggle boolean
				isTrue = !isTrue;
			}

			// Prepend current truth value to values
			values += (isTrue) ? 'T' : 'F';

			// Increment counter
			counter++;
		}

		// Return the truth values
		return values;
	}

	/*
	 * Negates a string
	 * 
	 * Pre-Condition:
	 * ~ input is a string of values either "T" or "F"
	 * 
	 * Post-Condition:
	 * ~ input is unchanged
	 * ~ a negated version of input is returned
	 */
	private static String negate(String input) {
		// Initialize string for result
		String result = "";

		// Iterate through input string
		for (char c : input.toCharArray()) {

			// Add the negation of the character to the result
			result += (c == 'T') ? 'F' : 'T';
		}

		// Return the negated input
		return result;
	}

	/*
	 * ORs two strings
	 * 
	 * Pre-Condition:
	 * ~ input1, input2 are strings of values either "T" or "F"
	 * 
	 * Post-Condition:
	 * ~ input1, input2 are unchanged
	 * ~ a string containing the OR of string1 and string2 is returned
	 */
	private static String or(String input1, String input2) {
		// Initialize string for result
		String result = "";

		// Iterate through both strings
		for (int i = 0; i < input1.length(); i++) {

			// Get the current character from both strings
			char a = input1.charAt(i);
			char b = input2.charAt(i);

			// Add the OR of the character to the result
			result += (a == 'T' || b == 'T') ? 'T' : 'F';
		}

		// Return the OR of the inputs
		return result;
	}

	/*
	 * ANDs two strings
	 * 
	 * Pre-Condition:
	 * ~ input1, input2 are strings of values either "T" or "F"
	 * 
	 * Post-Condition:
	 * ~ input1, input2 are unchanged
	 * ~ a string containing the AND of string1 and string2 is returned
	 */
	private static String and(String input1, String input2) {
		// Initialize string for result
		String result = "";

		// Iterate through both strings
		for (int i = 0; i < input1.length(); i++) {

			// Get the current character from both strings
			char a = input1.charAt(i);
			char b = input2.charAt(i);

			// Add the AND of the character to the result
			result += (a == 'T' && b == 'T') ? 'T' : 'F';
		}

		// Return the AND of the inputs
		return result;
	}
}