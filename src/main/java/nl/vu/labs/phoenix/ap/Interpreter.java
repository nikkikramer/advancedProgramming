package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * A set interpreter for sets of elements of type T
 */

public class Interpreter<T extends SetInterface<BigInteger>> implements InterpreterInterface<T> {

	private PrintStream out;
	private HashMap<IdentifierInterface, T> hashTable;
	private static final char LEFT_BRACKET = '{';
	private static final char RIGHT_BRACKET = '}';
	private static final char LEFT_BRACKET_CF = '(';
	private static final char RIGHT_BRACKET_CF = ')';
	private static final char UNION_SIGN = '+';
	private static final char DIFFERENCE_SIGN = '-';
	private static final char INTERSECTION_SIGN = '*';
	private static final char SYMMETRIC_DIFFERENCE_SIGN = '|';
	private static final char COMMA = ',';
	private static final char DOT = '.';
	private static final char ASSIGNMENT = '=';
	private static final char IF_EXPRESSION = '%';
	private static final char PRINT_VARIABLE = '?';
	private static final char COMMENT_VARIABLE = '/';
	private static final char BOOLEAN_OPERATOR_SMALLER = '<';
	private static final char BOOLEAN_OPERATOR_GREATER = '>';
	private static final char ZERO_CHAR = '0';

	public Interpreter() {
		out = new PrintStream(System.out);
		hashTable = new HashMap<IdentifierInterface, T>();
	}

	private char nextChar(Scanner input) {
		return input.next().charAt(0);
	}

	private boolean nextCharIs(Scanner input, char c) {
		return input.hasNext(Pattern.quote(c + ""));
	}

	private void parser(Scanner input, char c) throws APException { 
		if (!nextCharIs(input, c)) {
			if (c == LEFT_BRACKET) {
				throw new APException("Error: The Set does not start with " + LEFT_BRACKET);
			} else if (c == RIGHT_BRACKET) {
				throw new APException("Error: The Set does not end with " + RIGHT_BRACKET);
			} else {
				throw new APException("Error: Missing the character: " + c);
			}
		}
		nextChar(input);
	}
	
	private boolean nextCharSymbol(Scanner input) {
		return (nextCharIs(input, UNION_SIGN) | nextCharIs(input, DIFFERENCE_SIGN)
				| nextCharIs(input, INTERSECTION_SIGN) | nextCharIs(input, SYMMETRIC_DIFFERENCE_SIGN)
				| nextCharIs(input, LEFT_BRACKET_CF) | nextCharIs(input, RIGHT_BRACKET_CF)
				| nextCharIs(input, RIGHT_BRACKET) | nextCharIs(input, LEFT_BRACKET) | nextCharIs(input,BOOLEAN_OPERATOR_SMALLER) | nextCharIs(input,BOOLEAN_OPERATOR_GREATER));
	}

	private boolean nextCharIsDigit(Scanner input) {
		return input.hasNext("[0-9]");
	}

	private boolean positiveDigit(Scanner input) {
		return input.hasNext("[1-9]");
	}

	private boolean nextCharIsLetter(Scanner input) {
		return input.hasNext("[a-zA-Z]");
	}

	private void eoln(Scanner input) throws APException {
		if (input.hasNext()) {
			throw new APException("Error: No characters allowed at end of line");
		}
	}

	private void skipSpaces(Scanner input) {
		while (input.hasNext("\\s*")) {
			nextChar(input);
		}
	}

	@Override
	public T getMemory(String var) {
		T result = null;
		try {
			Scanner id = new Scanner(var);
			id.useDelimiter("");
			Identifier iden = identifier(id);
			result = hashTable.get(iden);
		} catch (APException e) {
			out.println(e.getMessage());
		}
		return result;
	}

	@Override
	public T eval(String s) {
		T sortOfStatement = null;
		Scanner inputRow = new Scanner(s);
		inputRow.useDelimiter("");
		try {
			sortOfStatement = statement(inputRow);
		} catch (APException e) {
			sortOfStatement = null;
			out.println(e.getMessage());
		}
		return sortOfStatement;
	}

	private T statement(Scanner input) throws APException {
		T kindOfStatement = null;
		skipSpaces(input);
		if (nextCharIs(input, PRINT_VARIABLE)) {
			kindOfStatement = print_statement(input);
			if (kindOfStatement == null) {
				throw new APException("Error: variable doesn't exist");
			}
		} else if (nextCharIs(input, COMMENT_VARIABLE)) {
			// do nothing
		} else if (nextCharIsLetter(input)) {
			assignment(input);
		} else {
			throw new APException(
					"Error: Statement should be either a print-statement, comment-line or an assignment starting with an '?','/' or a letter, respectively");
		}
		return kindOfStatement;
	}

	private T print_statement(Scanner input) throws APException {
		parser(input, PRINT_VARIABLE);
		skipSpaces(input);
		if (!input.hasNext()) {
			throw new APException("Error: expression missing");
		}
		T printStatement = expression(input);
		skipSpaces(input);
		eoln(input);
		return printStatement;
	}

	private T expression(Scanner input) throws APException {
		T result = null;
		if (nextCharIs(input, IF_EXPRESSION)) {
			nextChar(input);
			result = ifExpression(input);
		} else {
			result = subexpression(input);
		}
		return result;
	}

	private T ifExpression(Scanner input) throws APException {
		T result = null;
		boolean booleanResult = false;
		skipSpaces(input);
		String checkInput = input.nextLine();
		Pattern pattern = Pattern.compile("if (.*) then (.*) else (.*)");
		Matcher match = pattern.matcher(checkInput);
		if(!match.find()) {
			throw new APException("Error: Invalid if-expression");
		}
		String ifEvaluation = match.group(1);
		String thenStatement = match.group(2);
		String elseStatement = match.group(3);

		Scanner ifStatement = new Scanner(ifEvaluation);
		Scanner thenCheck = new Scanner(thenStatement);
		Scanner elseCheck = new Scanner(elseStatement);
		booleanResult = booleanExpression(ifStatement);
		if (booleanResult) {
			thenCheck.useDelimiter("");
			result = factor(thenCheck);
		} else {
			elseCheck.useDelimiter("");
			result = factor(elseCheck);
		}
		return result;
	}

	private boolean booleanExpression(Scanner input) throws APException {
		T expression1 = null;
		T expression2 = null;
		boolean result = false;
		skipSpaces(input);
		input.useDelimiter("");
		parser(input, LEFT_BRACKET_CF);
		skipSpaces(input);
		expression1 = expression(input);
		skipSpaces(input);
		char booleanOperator = nextChar(input);
		skipSpaces(input);
		expression2 = expression(input);
		if (booleanOperator == ASSIGNMENT) {
			result = expression1.equalSets(expression2);
		} else if (booleanOperator == BOOLEAN_OPERATOR_SMALLER) {
			result = expression1.smallerSet(expression2);
		} else if (booleanOperator == BOOLEAN_OPERATOR_GREATER) {
			result = expression1.greaterSet(expression2);
		} else {
			throw new APException("Error: Invalid boolean operator");
		}
		return result;
	}

	private T subexpression(Scanner input) throws APException {
		skipSpaces(input);
		SetInterface<BigInteger> result = term(input);
		skipSpaces(input);

		while (nextCharIs(input, DIFFERENCE_SIGN) || nextCharIs(input, UNION_SIGN) || nextCharIs(input, SYMMETRIC_DIFFERENCE_SIGN)) {
			char operator = nextChar(input);
			if (operator == UNION_SIGN) {
				// determine union
				result = result.union(term(input));
				skipSpaces(input);
			} else if (operator == DIFFERENCE_SIGN) {
				// determine complement
				result = result.difference(term(input));
				skipSpaces(input);
			} else if (operator == SYMMETRIC_DIFFERENCE_SIGN) {
				// determine symmetric difference
				result = result.symmetricDifference(term(input));
				skipSpaces(input);
			} else {
				throw new APException("Error: Invalid operater: " + operator); 
			}
		}
		return (T) result;
	}

	private T term(Scanner input) throws APException {
		skipSpaces(input);
		SetInterface<BigInteger> result = factor(input);
		skipSpaces(input);
		while (nextCharIs(input, INTERSECTION_SIGN)) {
			input.next(); 
			result = result.intersection(factor(input));
			skipSpaces(input);
		}
		return (T) result;
	}

	private T factor(Scanner input) throws APException {
		skipSpaces(input);
		SetInterface<BigInteger> result = new Set<BigInteger>();
		if (nextCharIsLetter(input)) {
			result = hashTable.get(identifier(input));
			if (result == null) {
				throw new APException("Variable does not exist");
			}
			return (T) result;

		} else if (nextCharIs(input, LEFT_BRACKET_CF)) {
			parser(input, LEFT_BRACKET_CF);
			result = subexpression(input);
			parser(input, RIGHT_BRACKET_CF);
			return (T) result;
		} else if (nextCharIs(input, LEFT_BRACKET)) {
			result = set(input);
		} else {
			throw new APException("Error: The input is NOT a factor");
		}
		return (T) result;
	}

	private Identifier identifier(Scanner input) throws APException {
		Identifier iden = new Identifier();
		iden.init(nextChar(input));
		while (input.hasNext()) {
			if (nextCharIsLetter(input) || nextCharIsDigit(input)) {
				iden.addElement(nextChar(input));
				if (nextCharIs(input, ASSIGNMENT)) {
					// do nothing
					return iden;
				} else if (input.hasNext(" ")) {
					skipSpaces(input);
					if (nextCharIs(input, ASSIGNMENT)) {
						// do nothing
						return iden;
					} else if (nextCharSymbol(input)) {
						return iden;
					} else if (!input.hasNext()) {
						return iden;
					} else {
						throw new APException("Error: Identifier can not contain any spaces");
					}
				}
			} else if (input.hasNext(" ")) {
				skipSpaces(input);
				if (!input.hasNext()) {
					return iden;
				} else if (nextCharIs(input, ASSIGNMENT)) {
					// do nothing: assignment
					return iden;
				} else if (nextCharSymbol(input)) {
					return iden;
				} else {
					throw new APException("Error: Identifier can not contain any spaces");
				}
			} else if (nextCharIs(input, ASSIGNMENT) | nextCharSymbol(input)) {
				// do nothing
				return iden;
			} else {
				throw new APException("Error: Identifier may only contain letters or numbers");
			}
		}
		return iden;
	}

	private void assignment(Scanner input) throws APException {
		skipSpaces(input);
		Identifier iden = identifier(input);
		skipSpaces(input);
		parser(input, ASSIGNMENT); 
		skipSpaces(input);
		if (!input.hasNext()) {
			throw new APException("Error: expression missing");
		}
		T expressionValue = expression(input);
		eoln(input);
		hashTable.put(iden, expressionValue);
	}

	private T set(Scanner input) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		parser(input, LEFT_BRACKET);
		result = row_natural_numbers(input);
		return (T) result;
	}

	private T row_natural_numbers(Scanner input) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		skipSpaces(input);
		if (nextCharIs(input, RIGHT_BRACKET)) {
			parser(input, RIGHT_BRACKET);
		} else {
			result = natural_number_term(input);
			skipSpaces(input);
			while (nextCharIs(input, COMMA)) {
				nextChar(input);
				skipSpaces(input);
				SetInterface<BigInteger> result_terms = new Set<BigInteger>();
				result_terms = natural_number_term(input).copy();
				while(!result_terms.isEmpty()) {
					result.add(result_terms.get());
					result_terms.remove(result_terms.get());
				}
			}
			skipSpaces(input);
			parser(input, RIGHT_BRACKET);
		}
		return (T) result;
	}

private T natural_number_term(Scanner input) throws APException {
		SetInterface<BigInteger> result = new Set<BigInteger>();
		BigInteger min = natural_number(input);
		result.add(min);
		if(nextCharIs(input,DOT)){
			StringBuffer sb = new StringBuffer();
			sb.append(min);
			Integer min2 = Integer.valueOf(sb.toString());
			nextChar(input);
			if(nextCharIs(input,DOT)) {
				nextChar(input);
				StringBuffer sb2 = new StringBuffer();
				sb2.append(natural_number(input));
				Integer max = Integer.valueOf(sb2.toString());
				if(min2 > max) {
					result.remove(min);
					return (T) result;
				}
				for(int i = min2; i<=max; i++) {
					StringBuffer sb3 = new StringBuffer();
					sb3.append(i);
					BigInteger result2 = new BigInteger(sb3.toString());
					result.add(result2);
				}
				return (T) result;	
			} else {
				throw new APException("Error: Invalid set");
			}
		}
		return (T) result;

	}

	private BigInteger natural_number(Scanner input) throws APException {
		StringBuffer sb = new StringBuffer();
		if (positiveDigit(input)) {
			sb.append(numbers(input));
		} else if (nextCharIs(input, ZERO_CHAR)) {
			sb.append(natural_zero(input));
			if (nextCharIsDigit(input)) {
				throw new APException("Error: Natural number zero should not be followed by digit");
			}
		} else {
			throw new APException("Error: Invalid number");
		}
		return new BigInteger(sb.toString());
	}

	private BigInteger natural_zero(Scanner input) throws APException {
		input.next();
		skipSpaces(input);
		if (!nextCharIsDigit(input)) {
			return BigInteger.ZERO;
		} else {
			throw new APException("Error: Input can not start with 0");
		}
	}

	private BigInteger numbers(Scanner input) throws APException {
		StringBuffer sb = new StringBuffer();
		while (nextCharIsDigit(input)) {
			sb.append(nextChar(input));
		}
		if (input.hasNext(" ")) {
			skipSpaces(input);
			if (nextCharIsDigit(input)) {
				throw new APException("Error: Spaces between natural numbers is not allowed.");
			}
		
		} else if (!nextCharIs(input, RIGHT_BRACKET) && !nextCharIs(input, COMMA) && !nextCharIs(input,DOT)) { 
			throw new APException("Error: Set can only contain integers");
		}
		return new BigInteger(sb.toString());
	}

}