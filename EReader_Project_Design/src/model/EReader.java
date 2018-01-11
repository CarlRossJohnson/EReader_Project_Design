package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;

/**
 * EReader is a class that models the functionality 
 * of an e-reader.
 */
public class EReader extends Observable {
	private String filename;
	private int linesPerPage;
	private int totalPages;
	private List<String> pages;
	private int currPageNumber;

	// Constructor
	public EReader(String filename, int linesPerPage) {
		this.filename = filename;
		this.linesPerPage = linesPerPage;
		pages = new ArrayList<String>();
		totalPages = 0;
		loadPages();
		currPageNumber = 1;
	}

	// Reads the input file, breaks the text up 
	// into page-sized strings, and adds them to 
	// our pages list
	private void loadPages() {
		Scanner inFile;

		try {
			inFile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			return;
		}

		while (inFile.hasNextLine()) {
			String currPage = "\n";
			int currLine = 0;
			while (currLine < linesPerPage && inFile.hasNextLine()) {
				currPage += "   " + inFile.nextLine() + "\n";
				currLine++;
			}
			pages.add(currPage);
			totalPages++;
		}

		if (inFile != null) {
			inFile.close();
		}

	}

	// Decrements the current page number
	public void prevPage() {
		if (currPageNumber > 1) {
			currPageNumber--;
			setChanged();
			notifyObservers();
		}
	}

	// Increments the current page number
	public void nextPage() {
		if (currPageNumber < totalPages) {
			currPageNumber++;
			setChanged();
			notifyObservers();
		}
	}

	// Changes the current page number to the spoecified page
	// Returns true if page is in range, otherwise false
	public boolean jumpToPage(int pageNumber) {
		if (pageNumber < 1 || pageNumber > totalPages)
			return false;

		currPageNumber = pageNumber;
		setChanged();
		notifyObservers();
		return true;
	}

	/**********
	 * Getters
	 **********/

	public String getCurrPage() {
		return pages.get(currPageNumber - 1);
	}

	public int getCurrPageNumber() {
		return currPageNumber;
	}

	public int getTotalPages() {
		return totalPages;
	}

}
