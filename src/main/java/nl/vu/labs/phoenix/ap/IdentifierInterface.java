package nl.vu.labs.phoenix.ap;

/* Elements: character of type Char
 * Structure: linear
 * Domain: rows of alphanumeric characters starting with a letter.
 * 
 * Identifier(); //default constructor
 * PRE -
 * POST - a new identifier object has been made and contains a placeholder
 * 
 * Identifier(Identifier src); //copy constructor
 * 	PRE - 
 * 	POST - A new Identifier-object has been made and contains a copy of the chars.
 */
public interface IdentifierInterface {
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
	String value();	
	/* PRE -
	 * POST - A string object with all the characters is returned
	 * 
	 * [3] Add anything else you think belongs to this interface 
	 */
	
	char getCharacter(int idx);
	/*
	 * PRE - index should [0,length)
	 * POST - The char at index idx is returned.
	 */
	
	void init(char c);
	/* PRE - The c contains a letter
	 * POST - The identifier only contains c
	 */
	
	void addElement(char c);
	/* PRE - c contains a alphanumeric character
	 * POST - c is added to the end of a Identifier
	 */
	
	int length();
	/* PRE -
	 * POST - The number of characters in the identifier is returned
	 */
	
	boolean equals(Object identifier);
	/*
	 * PRE - 
	 * POST - True: The elements of the identifiers are equal
	 * 		  False: The elements of the identifiers are not equal
	 */
	
	int hashCode();
	/* PRE - 
	 * POST - a hashCode for the identifier has been returned
	 */
	
}
