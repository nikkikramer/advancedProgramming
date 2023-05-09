package nl.vu.labs.phoenix.ap;

/* [1] Set Specification
 * 	   -- Complete the specification for a set interface.
 * 		  See the List interface for inspiration
 * 
 * Elements: object of type T
 * Structure: None
 * Domain: The elements are same type and unique
 * 
 * Set(); //default constructor
 * PRE - (no precondition)
 * POST - A new Set-object has been made and contains the empty set
 */
public interface SetInterface<T extends Comparable<T>> {
	
	/* 
	 * [2] Mandatory methods. Make sure you do not modify these!
	 * 	   -- Complete the specifications of these methods
	 */
	
	boolean add(T element);
	/*
	 * PRE - 
	 * POST - True: Element was inserted
	 * 		  False: Element  was already present
	 */
	
	T get();
	/*
	 * PRE - The set is not empty
	 * POST - A ?(copy) of a random element of the set is returned
	 */
	
	boolean remove(T element);
	/*
	 * PRE - 
	 * POST - True: Element was removed
	 * 		  False: Element was not present and can't be removed
	 */
	
	int size();
	/*
	 *  PRE - 
	 *  POST - Number of elements in set is returned
	 */
	
	SetInterface<T> copy();
	/*
	 * PRE - 
	 * POST - A copy of the set with current object
	 */
	
	SetInterface<T> difference(SetInterface<T> set2);
	/*
	 * 	PRE		-
	 * 	POST	- The elements in set1 and not in set2 joined in a new Set
	 */
	
	SetInterface<T> intersection(SetInterface<T> set2);
	/*
	 * 	PRE		-
	 * 	POST	- The elements common to both sets are joined in a new Set
	 */
	
	SetInterface<T> union(SetInterface<T> set2);
	/*
	 * 	PRE		-
	 * 	POST	- The set of all elements from the both sets are joined in a new Set		  
	 */
	
	SetInterface<T> symmetricDifference(SetInterface<T> set2);
	/*
	 * 	PRE		-
	 * 	POST	- The elements different in both sets are joined in a new Set         
	 */
	
	SetInterface<T> init();
	
	/*
	 * PRE -
	 * POST - The set is empty
	 */
	
	boolean equalSets(SetInterface<T> set2);
	
	/*
	 * PRE -
	 * POST - True: The sets are equal
	 * 		  False: The sets are not equal
	 */
	boolean smallerSet(SetInterface<T> set2);
	
	/*
	 * PRE - 
	 * POST - True: All elements in the set are in set2
	 * 		  False: All elements in the set are not in set2
	 */
	boolean greaterSet(SetInterface<T> set2);
	
	/*
	 * 	PRE		-
	 * 	POST	- True: All elements of set2 are in the set
	 * 			  False: All elements of set2 are not in the set
	 */
	
	boolean isEmpty ();    
	/* 
	 * PRE - 
	 * POST - True: The amount of elements of the set equals 0.
	 * 		  False: The amount of elements of the set is greater than 0.   
	 */
	boolean isInSet(T element);
	/*
	 * PRE - 
	 * POST - True: identifier is in set
	 * 		  False: identifier is not in set
	 */
	
}
