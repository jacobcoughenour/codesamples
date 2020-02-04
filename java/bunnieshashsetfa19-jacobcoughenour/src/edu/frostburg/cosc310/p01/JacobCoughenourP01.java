package edu.frostburg.cosc310.p01;

import edu.frostburg.cosc310.datastructures.hashset.JCHashSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * COSC 310
 * Hash Set Database
 *
 * @author Jacob Coughenour
 */


public class JacobCoughenourP01 {

	private static JCHashSet hashSet = new JCHashSet(64);

	/**
	 * Run your program.
	 *
	 * @param args command line input
	 */
	public static void main(String[] args) {

		// http://www.patorjk.com/software/taag/#p=display&f=Ivrit&t=B.O.B.C.A.T.
		System.out.println(
				"\n    ____   ___   ____   ____    _   _____ \n" +
						"   | __ ) / _ \\ | __ ) / ___|  / \\ |_   _|\n" +
						"   |  _ \\| | | ||  _ \\| |     / _ \\  | |  \n" +
						"   | |_) | |_| || |_) | |____/ ___ \\_| | _ \n" +
						"  -|____(_)___(_)____(_)___(_)/   \\(_)_|(_)-\n" +
						" --- Bureau Of Bunny Catching Ain't Tough --- \n" +
						"  -----    Bunny Population Database   -----\n");


		// load the input file
		try {
			String fileName = "BunnyNames.txt";
			System.out.println("== Loading Database from " + fileName + "...");

			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader =
					new BufferedReader(fileReader);
			String line;

			// dumb that bad boy into the hash boy
			while ((line = bufferedReader.readLine()) != null)
				hashSet.add(line);

			bufferedReader.close();
		} catch (IOException e) {
			System.err.println("There was a problem reading this file.");
		}

		// print load completed message
		System.out.println("== Successfully loaded " + hashSet.size() + " bunnies");
		System.out.println();

		// print welcome message
		System.out.println("Welcome to the B.O.B.C.A.T. Bunny Population Database!");
		System.out.println("Enter a command below (do ? for a list of commands)");

		// create our input scanner to start reading user input from
		Scanner read = new Scanner(System.in);

		// some underrated code right here
		do {
			// put the little prompt thing before the user input
			System.out.print("\n> ");
			// keep doing that until processCommand returns false
		} while (processCommand(read.nextLine()));

		// exit the program now
		System.out.println("Exiting...");
	}

	/**
	 * Processes and runs commands from the input
	 *
	 * @param input input string straight from nextLine()
	 * @return false if the EXIT command is used
	 */
	private static boolean processCommand(String input) {

		// trim off trailing whitespace
		input = input.trim();

		// the input is empty
		if (input.length() == 0) {
			// print the list of commands
			HelpCommand("");
			return true;
		}

		// grab the first index of a space from the input
		int firstSpace = input.indexOf(" ");
		// the substring from 0 to that first space should be the command
		String commandName = firstSpace == -1 ? input : input.substring(0, firstSpace);
		// the rest of the string should be parameters
		String params = firstSpace == -1 ? "" : input.substring(firstSpace + 1);

		// case-insensitive search and run the command
		switch (commandName.toUpperCase()) {
			case "ADD":
				AddCommand(splitNames(params));
				break;
			case "DEL":
				DeleteCommand(splitNames(params));
				break;
			case "STATS":
				StatsCommand();
				break;
			case "?":
				HelpCommand(params);
				break;
			case "EXIT":
				return false;
			// assume that they are entering a name
			default:
				CheckNameCommand(splitNames(input));
				break;
		}

		// return true since the input wasn't EXIT
		return true;
	}

	/**
	 * Splits input names that are separated by \
	 *
	 * @param input raw input from System.in
	 * @return array of names from input
	 */
	private static String[] splitNames(String input) {

		// create a list of string to populate with the names
		ArrayList<String> names = new ArrayList<>();

		for (String s : input.trim().split("\\\\")) {
			// trim whitespace
			s = s.trim();
			// if the string isn't empty
			if (s.length() > 0)
				// add it
				names.add(s);
		}

		// convert our array list to an array and return it
		return names.toArray(new String[]{});
	}

	// some fancy unicode to spice up messages
	private static final String ErrorGlyph = " \u001B[31m[X]\u001B[0m ";
	private static final String CheckGlyph = "\033[0;92m" + " [\u2713]\u001B[0m ";


	/**
	 * Add command usage information
	 */
	private static final String AddCommandUsage = "ADD <NAME>  - add name to database";

	/**
	 * Add name to database command
	 *
	 * @param input array of names to add
	 */
	private static void AddCommand(String[] input) {

		// input array is empty
		if (input.length == 0) {
			// print the help message for this command
			HelpCommand("ADD");
			return;
		}

		// for each input string
		for (String s : input)
			// try adding it to the hash set
			if (hashSet.add(s))
				System.out.println(CheckGlyph + "Added: " + s);
			else
				System.out.println(ErrorGlyph + "Name already exits: " + s);
	}

	/**
	 * Delete command usage information
	 */
	private static final String DeleteCommandUsage = "DEL <NAME>  - deletes name from database";

	/**
	 * Delete name from database command
	 *
	 * @param input array of names to delete
	 */
	private static void DeleteCommand(String[] input) {

		// input array is empty
		if (input.length == 0) {
			// print the help message for this command
			HelpCommand("DEL");
			return;
		}

		// for each input string
		for (String s : input)
			// try removing it from the hash set
			if (hashSet.remove(s))
				System.out.println(CheckGlyph + "Removed: " + s);
			else
				System.out.println(ErrorGlyph + "Not found: " + s);
	}

	/**
	 * Name command usage information
	 */
	private static final String CheckNameCommandUsage = "<NAME>      - confirms if the name is in the database";

	/**
	 * Check name in database command
	 *
	 * @param input array of names to check
	 */
	private static void CheckNameCommand(String[] input) {

		// input array is empty
		if (input.length == 0) {
			// print the help message for this command
			HelpCommand("<NAME>");
			return;
		}

		// for each input string
		for (String s : input)
			// try finding it in the hash set
			if (hashSet.find(s))
				System.out.println(CheckGlyph + "found name: " + s);
			else
				System.out.println(ErrorGlyph + "could not find name: " + s);

	}

	/**
	 * Stats command usage information
	 */
	private static final String StatsCommandUsage = "STATS       - prints hash table info";

	/**
	 * Database stats command
	 */
	private static void StatsCommand() {
		System.out.println("----------- Database Stats -----------");

		// using a hashmap to make the stats look pretty owo
		Map<String, Object> stats = new HashMap<>();

		stats.put("Total Entries", hashSet.size());
		stats.put("Table Size", hashSet.tableSize());
		stats.put("Load", hashSet.stats_currentLoad());
		stats.put("Total Collisions", hashSet.stats_collisions());
		stats.put("Total Rehashes", hashSet.stats_rehashCount());
		stats.put("Load Rehashes", hashSet.stats_rehashesFromLoadFactor());
		stats.put("Probe Rehashes", hashSet.stats_rehashesFromProbing());

		// get the max key length
		int maxKeyLength = 0;
		for (String key : stats.keySet())
			maxKeyLength = Math.max(maxKeyLength, key.length());

		// print all our stats out
		for (String key : stats.keySet()) {
			System.out.print("\t");
			// align keys to the right
			for (int i = maxKeyLength - key.length(); i >= 0; i--)
				System.out.print(" ");
			System.out.print(key);
			System.out.print(" : ");
			System.out.print(stats.get(key));
			System.out.print("\n");
		}


	}

	/**
	 * Help command usage information
	 */
	private static final String HelpCommandUsage = "? <COMMAND> - prints help for specific command";

	/**
	 * Help command
	 *
	 * @param commandName print help message for a specific command
	 */
	private static void HelpCommand(String commandName) {

		// case-insensitive
		// print out usage info if command is specified
		switch (commandName.toUpperCase()) {
			case "ADD":
				System.out.println(AddCommandUsage);
				break;
			case "DEL":
				System.out.println(DeleteCommandUsage);
				break;
			case "<NAME>":
				System.out.println(CheckNameCommandUsage);
				break;
			case "STATS":
				System.out.println(StatsCommandUsage);
				break;
			case "?":
				System.out.println(HelpCommandUsage);
				break;
			case "EXIT":
				System.out.println("EXIT - exits program");
				break;
			// no command specified
			// so just print all of them out
			default:
				if (commandName.length() > 0)
					System.out.println("unknown command");
				System.out.println("List of Commands:");
				System.out.println("\t" + AddCommandUsage);
				System.out.println("\t" + DeleteCommandUsage);
				System.out.println("\t" + CheckNameCommandUsage);
				System.out.println("\t" + StatsCommandUsage);
				System.out.println("\t" + HelpCommandUsage);
				System.out.println("\tEXIT        - exits program");
		}
	}


}
