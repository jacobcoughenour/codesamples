package edu.frostburg.cosc310.datastructures.hashset;

/**
 * Interface for this assignment.  HT refers to Hash Table.  Please do not alter this file.
 * 
 * Your project should implement this interface with a class named XYHashSet
 * 	where XY is your initials.
 * 
 * @author sdkennedy
 *
 */

public interface HashSet<T> {
	
	/** Standard HashSet behavior */
	public boolean 	add(T element);		// put element into HT
	public boolean	remove(T element);	// remove element
	public boolean find(T element);		// true when element exists in HT
	
	/** Accounting info */
	public int 		size(); 		// # of elements in this set
	public int		tableSize();	// size of HT
	public boolean 	isEmpty(); 		// true when size==0
	
	/** Return your name */
	public String 	myName();		// return your name as a String
}
