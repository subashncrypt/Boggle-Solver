//package com.company;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * Class Boggle that accepts a puzzle and a dictionary of words to be found and then
 * solves the words and returns back all the words if present with the path
 */
public class Boggle extends IOException{

    // Private Variables with in the class
    // dictionary words stores all the words to be found
    private ArrayList<Dictionary> dictionaryWords =  new ArrayList<>();

    // stores the entire puzzle
    private List<String[]> puzzle = new ArrayList<String[]>();

    // Hash map to store index of letters in the puzzle for easy mapping
    private HashMap<String, ArrayList<Dictionary>> letterIndex =  new HashMap<>();

    private boolean visited[][];        // Array to store visited nodes
    private int xCoOrdinate;            // To keep trace of X co-ordinate of a word in the puzzle
    private int yCoOrdinate;            // To keep track of Y co-ordinate in a word in the puzzle

    /**
     * Function that stores all the words to be found in the dictionary
     * @param stream
     * @return true when all the words are ready and ready to use by the puzzle solve method
     * @throws IOException
     */
    public boolean getDictionary(BufferedReader stream) throws IOException {

        // initializing local variables
        String line;    // line that stores each word in a stream
        Boolean status = true; // Return parameter

        //Try block to read the data from the buffer reader
        try {

            //Loop through to read words from the stream
            while ((line = stream.readLine()) != null) {
                // if there is an empty line stop the stream
                // end of words
                if ("".equals(line)) {
                    break;
                }
                // handle return false or true
                addValidWords(line);
            }
        }
        catch (NullPointerException e){
            // Throw an exception when Null is passed
            throw new NullPointerException(" Null passed as stream");
        }
        catch (IOException ioException){
            // Throw an exception when there is an Io exception
            throw new IOException("stream is closed while reading Input from the file");
        }

        // if there are no words in the dictionary then return False
        if(dictionaryWords.size() == 0){
            status = false;
        }

        // return true if there are word read and can be used to solve puzzle
        return status;
    }

    /**
     * Get Puzzle is a function that stores the puzzle and helps to find the words in the dictionary
     * @param stream
     * @return True when have a puzzle of nay size
     * @throws DataFormatException
     * @throws IOException
     * @throws NullPointerException
     */
    public boolean getPuzzle(BufferedReader stream) throws DataFormatException, IOException,NullPointerException {

        // local variables
        String line;        // Stores each line from the stream : Entire Row
        String[] row;       // Stores the row character by character
        int columns = 0;    // Holds the length if the first column

        // Try to read lines from the buffer reader
        try{

            // while we reach to the EOF get stream
            while((line = stream.readLine())!= null) {

                // Stop the stream when we encounter a Blank line
                if ("".equals(line)) {
                    break;
                }else{

                    row = line.split("");   // Split the line

                    // Store the column length for future validation
                    if(puzzle.size() == 0){
                        columns = row.length;
                        row = line.toLowerCase().split("");

                        for (int i = 0; i < row.length ; i++) {
                            // create and initialize the Hash map to map index with characters

                            // if the character exist
                            if(letterIndex.containsKey(row[i])){
                                Dictionary charCo = new Dictionary(row[i]);
                                ArrayList<Dictionary> charLi =  new ArrayList<>();

                                // Set the coordinates for a character
                                charCo.setXcordinate(puzzle.size());
                                charCo.setYcoordinate(i);

                                // Add to the array list of co-ordinates mapped to a character
                                charLi = letterIndex.get(row[i]);
                                charLi.add(charCo);

                            }else {
                                // create and initialize the Hash map to map index with characters
                                Dictionary charCo = new Dictionary(row[i]);
                                ArrayList<Dictionary> charLi =  new ArrayList<>();

                                // Set the coordinates for a character
                                charCo.setXcordinate(puzzle.size());
                                charCo.setYcoordinate(i);

                                // Add to the array list of co-ordinates mapped to a character
                                charLi.add(charCo);
                                letterIndex.put(row[i],charLi);

                            }
                        }
                        // add row to the puzzle
                        puzzle.add(row);
                    }
                    // if the column length does not match then throw an exception
                    else if(row.length != columns){
                        throw new DataFormatException("Wrong column length");
                    }
                    // add the row to the puzzle
                    else{
                        row = line.toLowerCase().split("");
                        for (int i = 0; i < row.length ; i++) {
                            // create and initialize the Hash map to map index with characters

                            // if the character exist
                            if(letterIndex.containsKey(row[i])){
                                Dictionary charCo = new Dictionary(row[i]);
                                ArrayList<Dictionary> charLi =  new ArrayList<>();

                                // Set the coordinates for a character
                                charCo.setXcordinate(puzzle.size());
                                charCo.setYcoordinate(i);

                                // Add to the array list of co-ordinates mapped to a character
                                charLi = letterIndex.get(row[i].toLowerCase());
                                charLi.add(charCo);
                            }else {
                                // create and initialize the Hash map to map index with characters
                                Dictionary charCo = new Dictionary(row[i]);
                                ArrayList<Dictionary> charLi =  new ArrayList<>();

                                // Set the coordinates for a character
                                charCo.setXcordinate(puzzle.size());
                                charCo.setYcoordinate(i);

                                // Add to the array list of co-ordinates mapped to a character
                                charLi.add(charCo);
                                letterIndex.put(row[i],charLi);
                            }
                        }
                        // add row to the puzzle
                        puzzle.add(row);
                    }
                }
            }


        }
        // Throw exceptions accordingly
        catch(DataFormatException dataFormatException){
            // Throw an exception when wrong column length is passed
            throw new DataFormatException("Wrong data format given for the Puzzle all columns must be uniform");
        }
        catch (NullPointerException nullPointerException){
            // Throw an exception when Null is passed as a stream
            throw new NullPointerException("Null passed as stream");
        }
        catch (IOException ioException){
            // Throw an exception when there is an Io exception
            throw new IOException("Stream closed while reading Input from the file");
        }
        return true;
    }

    /**
     * Function that solves the words in the dictionary by finding them in the puzzle
     *
     * @returns a list of words find there starting co-ordinates with the traversal path
     */
    public List<String> solve(){

        // Local variables
        Set<String> foundWordsSet = new HashSet<String>();      // Stores unique set of words
        List<String> foundWords;                                // return parameter
        String word = new String();                             // holds each word along with its paths

        // Main logic that process and finds the word in the dictionary
        processDictionary();

        // Looping through to construct the string in a format and add it to the set
        for (int i =0; i<dictionaryWords.size(); i++){
            if(dictionaryWords.get(i).isWordFound()){
                word = dictionaryWords.get(i).getWord()+"\t"+dictionaryWords.get(i).getXcordinate()+"\t"+
                       dictionaryWords.get(i).getYcoordinate()+"\t"+dictionaryWords.get(i).getPath();
                foundWordsSet.add(word);
            }
        }

        // Moving the strings from a set to an Array list
        foundWords = new ArrayList<String>(foundWordsSet);

        // Now sort the words found in order
        Collections.sort(foundWords);

        // return the string
        return foundWords;
    }

    /**
     * Function that process a dictionary on the puzzle stored to find the words
     * does not return anything
     */
    private void processDictionary() {

        // Local variable initialization
        int rows = puzzle.size();               // Number of Rows initialization
        int columns = puzzle.get(1).length;     // Number of columns initialization
        visited = new boolean[rows][columns];   // Initialization of our visited matrix


        // Loops through to find out each word path
        for (int j=0; j<dictionaryWords.size(); j++){
            // Process each word
            processWord(dictionaryWords.get(j).getWord(),j,rows, columns);
        }
    }

    /**
     * Function that finds word based on the word to be found
     * @param word
     * @param wordPosition
     * @param rows
     * @param columns
     */
    private void processWord(String word,int wordPosition, int rows, int columns) {

        // initializing local variables
        ArrayList<Dictionary> coOrdinateList = new ArrayList<>();       // Array list of Co-ordinates
        char c = word.toLowerCase().charAt(0);                          // First character of the word


        // if that key first letter of the word exists
        if(letterIndex.containsKey(String.valueOf(c))){

            // Stores list of all the co-ordinates of a particular letter
            coOrdinateList = letterIndex.get(String.valueOf(c));

            // Loop through all the indexes of a starting letter to find the word from there
            for (int li = 0; li < coOrdinateList.size(); li++) {
                xCoOrdinate = coOrdinateList.get(li).getYcoordinate()+1;      // X coOrdinate calculated based on array index
                yCoOrdinate = Math.abs(coOrdinateList.get(li).getXcordinate()-puzzle.size());    // Y coOrdinate calculated based on array
                findWord(coOrdinateList.get(li).getXcordinate(),coOrdinateList.get(li).getYcoordinate(),0,word, wordPosition,"");
            }
        }

    }

    /**
     * Recursive function with the main logic to find the puzzle using DFS approach
     * @param i index of a particular row
     * @param j index of a particular column
     * @param index index of word
     * @param word word to be found
     * @param wordPosition
     * @param path
     */
    private void findWord(int i, int j, int index, String word,int wordPosition, String path) {

        // if we find the word then update the X and Y coordinates with the smallest path
        if(index == (word.length()-1) &&
           word == dictionaryWords.get(wordPosition).getWord()){

            // if the word was already found
            if(dictionaryWords.get(wordPosition).isWordFound()){

                // if X is lesser comparatively
                if(xCoOrdinate <= dictionaryWords.get(wordPosition).getXcordinate()){

                    // if X is equal comparatively and difference in Y coordinate
                    if(xCoOrdinate == dictionaryWords.get(wordPosition).getXcordinate() &&
                    yCoOrdinate < dictionaryWords.get(wordPosition).getYcoordinate()){

                        // Update values
                        dictionaryWords.get(wordPosition).setPath(path);
                        dictionaryWords.get(wordPosition).setXcordinate(xCoOrdinate);
                        dictionaryWords.get(wordPosition).setYcoordinate(yCoOrdinate);

                    }else if(xCoOrdinate < dictionaryWords.get(wordPosition).getXcordinate()){
                        // Update values of X and Y coOrdinates along with the path
                        dictionaryWords.get(wordPosition).setPath(path);
                        dictionaryWords.get(wordPosition).setXcordinate(xCoOrdinate);
                        dictionaryWords.get(wordPosition).setYcoordinate(yCoOrdinate);
                    }

                }
            }else {

                // Set values of the dictionary when its first time
                dictionaryWords.get(wordPosition).setWordFound(true);
                dictionaryWords.get(wordPosition).setPath(path);
                dictionaryWords.get(wordPosition).setXcordinate(xCoOrdinate);
                dictionaryWords.get(wordPosition).setYcoordinate(yCoOrdinate);
            }
            // return after you have set and found the word
            return;
        }

        // if the index is out of bound or the character is not matching then return
        if(i >= puzzle.size() || i<0 || j>= puzzle.get(i).length || j<0 ||
           (word.charAt(index) != puzzle.get(i)[j].charAt(0)) || visited[i][j]){
            return;
        }

        // Set visited to be True
        visited[i][j] = true;

        // Recursion calls
        // when next letter matches and the direction is UP
        if(i-1 < puzzle.size() && i-1>=0 && j < puzzle.get(i).length &&
                j>=0 && word.charAt(index+1) == puzzle.get(i-1)[j].charAt(0)&&
                !visited[i-1][j]){
            findWord(i-1,j,index+1,word,wordPosition,path+"U");
        }

        // when next letter matches and the direction is Down
        if(i+1 < puzzle.size() && i+1>=0 && j < puzzle.get(i).length &&
                j>=0 && word.charAt(index+1) == puzzle.get(i+1)[j].charAt(0)&&
                !visited[i+1][j]){
            findWord(i+1,j,index+1,word,wordPosition,path+"D");
        }

        // when next letter matches and the direction is Right
        if(i < puzzle.size() && i>=0 && j+1 < puzzle.get(i).length &&
                j+1 >=0 && word.charAt(index+1) == puzzle.get(i)[j+1].charAt(0)&&
                !visited[i][j+1]){
            findWord(i,j+1,index+1,word,wordPosition,path+"R");
        }

        // when next letter matches and the direction is Left
        if(i < puzzle.size() && i>=0 && j-1 < puzzle.get(i).length &&
                j-1 >=0 && word.charAt(index+1) == puzzle.get(i)[j-1].charAt(0)&&
                !visited[i][j-1]){
            findWord(i,j-1,index+1,word,wordPosition,path+"L");
        }

        // when next letter matches and the direction is Diagonally up to the left
        if(i-1 < puzzle.size() && i-1>=0 && j-1 < puzzle.get(i).length &&
                j-1 >=0 && word.charAt(index+1) == puzzle.get(i-1)[j-1].charAt(0)&&
                !visited[i-1][j-1]){
            findWord(i-1,j-1,index+1,word,wordPosition,path+"N");
        }

        // when next letter matches and the direction is Diagonally up to the right
        if(i-1 < puzzle.size() && i-1>=0 && j+1 < puzzle.get(i).length &&
                j+1 >=0 && word.charAt(index+1) == puzzle.get(i-1)[j+1].charAt(0)&&
                !visited[i-1][j+1]){
            findWord(i-1,j+1,index+1,word,wordPosition,path+"E");
        }

        // when next letter matches and the direction is Diagonally down to the left
        if(i+1 < puzzle.size() && i+1>=0 && j-1 < puzzle.get(i).length &&
                j-1 >=0 && word.charAt(index+1) == puzzle.get(i+1)[j-1].charAt(0)&&
                !visited[i+1][j-1]){
            findWord(i+1,j-1,index+1,word,wordPosition,path+"W");
        }

        // when next letter matches and the direction is Diagonally down to the right
        if(i+1 < puzzle.size() && i+1>=0 && j+1 < puzzle.get(i).length &&
                j+1 >=0 && word.charAt(index+1) == puzzle.get(i+1)[j+1].charAt(0)&&
                !visited[i+1][j+1]){
            findWord(i+1,j+1,index+1,word,wordPosition,path+"S");
        }

        // Set visited to be false
        visited[i][j] = false;

        // Return the control to the calling function
        return;
    }


    /**
     * Function the returns a String of puzzle that can be printed to look similar to the input
     * @return String which contains puzzle each line seperated by "\n" character
     */
    public String print(){

        // Initialize the return string of the puzzle
        String puzzleString = new String();

        // Loop through and get the string appended
        for(int j =0; j<puzzle.size(); j++){
            for(int k=0; k<puzzle.get(j).length; k++){
                puzzleString+=puzzle.get(j)[k];
            }
                puzzleString+="\n";
        }

        // returns the puzzle string
        return puzzleString;
    }

    /**
     *
     * Function that validates if word can be used inside the dictionary
     * @param line
     * @return True if we can false if we cannt
     */
    private boolean addValidWords(String line){

        // if the length is less than 2 then it's not a valid word
        if(line.length() < 2){
            return false;
        }

        // Add words to the dictionary when it's a valid word after all the validation
        line = line.toLowerCase();
        Dictionary word = new Dictionary(line);
        dictionaryWords.add(word);

        // Return true after successful addition
        return true;
    }
}
