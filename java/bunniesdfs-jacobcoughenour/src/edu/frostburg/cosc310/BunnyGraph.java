/*
    This interface defines the methods required by this assignment.  There isn't much
        here this time.  Utilize what you know to fill in the gaps and don't
        hesitate to refer to your textbook for assistance.
 */
package edu.frostburg.cosc310;

public interface BunnyGraph {

	/* -- Main -- */

	/**
	 * Runs the project.  (Create an instance of your lab and kick it off using this method).
	 * This will simply run your search and print out the results.
	 */
	public void go();

	/* -- Administrative -- */

	/**
	 * Returns the programmer's name
	 **/

	public String getMyName();

	/**
	 * Print a greeting message
	 **/
	public void hello();

	/**
	 * Print a message and perform any necessary cleanup.
	 **/
	public void exit();

}