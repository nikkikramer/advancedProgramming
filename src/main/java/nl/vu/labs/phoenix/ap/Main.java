package nl.vu.labs.phoenix.ap;

import java.math.BigInteger;
import java.util.Scanner;
import java.io.PrintStream;

public class Main {
	
	PrintStream out;
	
	Main() {
		out = new PrintStream(System.out);
	}
	
	private void printResults(SetInterface<BigInteger> set) {
		SetInterface<BigInteger> copySet = set.copy();
		while(!copySet.isEmpty()) {
			out.print(copySet.get());
			out.print(" ");
			copySet.remove(copySet.get());
		}
		out.print("\n");
	}

	private void start() {
		InterpreterInterface<Set<BigInteger>> interpreter = new Interpreter<Set<BigInteger>>();
		
		//1. Create a scanner on System.in
		Scanner in = new Scanner(System.in);
		
		//2. call interpreter.eval() on each line
		while(in.hasNextLine()) {
			SetInterface<BigInteger> calculatorOutcome = interpreter.eval(in.nextLine()); 
			if(calculatorOutcome != null) {
				printResults(calculatorOutcome);
			}
		}	
	}
	
	public static void main(String[] args) {
		new Main().start();
	}
}
