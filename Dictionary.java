//package com.company;

/**
 * Class that stores the word and its associated path and Co-ordinates
 * as well as used to store index for the puzzle
 */
public class Dictionary {

    // Initializing private variables
    private String word = "";                         // Stores the words
    private String path = "";                         // Stores the path of the word
    private boolean wordFound = false;                // if word is found
    private int rowIndex = Integer.MAX_VALUE;         // rowIndex or XCoOrdinate
    private int columnIndex = Integer.MAX_VALUE;      // columnIndex or YCoOrdinate
    
    // initializing a constructor
    public Dictionary(String word) {
        this.word = word;
    }

    /**
     * Getter function to get word back
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * Setter function to set the word
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Getter function that Gets path
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter function that sets path
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter functions for wordFound
     * @return True if found else not found
     */
    public boolean isWordFound() {
        return wordFound;
    }

    /**
     * Setter function to set the word
     * @param wordFound
     */
    public void setWordFound(boolean wordFound) {
        this.wordFound = wordFound;
    }

    /**
     * Getter function to get X coordinate
     * @return
     */
    public int getXcordinate() {
        return rowIndex;
    }

    /**
     * Setter function to set X coordinate
     * @param xcordinate
     */
    public void setXcordinate(int xcordinate) {
        this.rowIndex = xcordinate;
    }

    /**
     * Getter function to get Y coordinate
     * @return
     */
    public int getYcoordinate() {
        return columnIndex;
    }

    /**
     * Setter function to set Y co-ordinate
     * @param ycoordinate
     */
    public void setYcoordinate(int ycoordinate) {
        this.columnIndex = ycoordinate;
    }
}
