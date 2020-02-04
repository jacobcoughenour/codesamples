package edu.frostburg.cosc310;

/*
 * Depth First Search P10
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Jacob Coughenour
 */
public class JacobCoughenourP10 {

	// main method
	public static void main(String[] args) {

		// create the graph
		MyBunnyGraph graph = new MyBunnyGraph();

		// print welcome message
		graph.hello();

		// load the input file
		try {
			// input file name
			String fileName = "hideyholes0001.txt";

			System.out.println("Reading input from " + fileName + "...");
			BufferedReader bufferedReader =
					new BufferedReader(new FileReader(fileName));
			String line;

			// dump that bad boy into the graph
			while ((line = bufferedReader.readLine()) != null) {

				// trim whitespace from start and end of the line
				line = line.trim();

				// ignore comments
				// not these comments, i mean the ones from the input
				if (line.startsWith("#"))
					continue;

				// send the line to the graph for parsing
				graph.addVertexFromString(line);
			}

			// close reader
			bufferedReader.close();

		} catch (IOException e) {
			System.err.println("There was a problem reading this file.");
		}

		// start the search
		graph.go();

		// print exit message
		graph.exit();
	}
}
