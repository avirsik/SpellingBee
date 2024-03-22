import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, Annie Virsik
 *
 * Written on March 21, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE
        // Calls recursive method
        wordCombos("", letters);
    }

    // Recursive method that generates all combinations of words and adds them to the words array
    public void wordCombos(String placeholder, String letters) {
        // Base case
        words.add(placeholder);
        for (int i = 0; i < letters.length(); i++) {
            wordCombos(placeholder + letters.charAt(i), letters.substring(0, i) + letters.substring(i+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        // Calls recursive sorting algorithm
        words = mergeSort(words, 0, words.size()-1);
    }

    // Recursive method Mergesort algorithm
    public ArrayList<String> mergeSort(ArrayList<String> words, int start, int end) {
        // Base case - if there is one thing in the array add value to arr and return arr
        if (end-start == 0) {
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(words.get(start));
            return arr;
        }
        int mid = (start + end) / 2;
        ArrayList<String> arr1 = mergeSort(words, start, mid);
        ArrayList<String> arr2 = mergeSort(words, mid+1, end);
        return compare(arr1, arr2);
    }

    // The part of mergeSort that compares the values
    public ArrayList<String> compare(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> arr = new ArrayList<String>();
        int idx1 = 0;
        int idx2 = 0;
        // While there is still stuff in the array
        while (idx1 < arr1.size() && idx2 < arr2.size()) {
            if (arr1.get(idx1).compareTo(arr2.get(idx2)) < 0) {
                arr.add(arr1.get(idx1++));
            }
            else {
                arr.add(arr2.get(idx2++));
            }
        }
        // Copy over remaining elements
        while (idx1 < arr1.size()) {
            arr.add(arr1.get(idx1++));
        }
        while (idx2 < arr2.size()) {
            arr.add(arr2.get(idx2++));
        }
        return arr;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++) {
            // If the word is not in the dictionary remove it from the arraylist
            if (!found(words.get(i), 0, DICTIONARY.length-1)) {
                words.remove(i);
                i--;
            }
        }
    }

    // Recursive binary search algorithm
    public boolean found(String word, int start, int end) {
        int mid = start + (end - start) / 2;
        // Base cases
        // If the starting index is less then the ending index return false
        if (start > end) {
            return false;
        }
        // If the middle point is the word return true
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }
        // Runs found on either the first half or second half of the array
        if (word.compareTo(DICTIONARY[mid]) < 0) {
            return found(word, start,mid-1);
        }
        return found(word, mid+1, end);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
