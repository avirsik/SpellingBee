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
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
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

//        // testing
//        words.add("e");
//        words.add("h");
//        words.add("i");
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        wordCombos("", letters);
    }

    public void wordCombos(String placeholder, String letters) {
        // Base case
        words.add(placeholder);
        for (int i = 0; i < letters.length(); i++) {
            wordCombos(placeholder + letters.substring(i, i+1), letters.substring(0, i) + letters.substring(i+1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // YOUR CODE HERE
        words = mergeSort(words, 0, words.size()-1);
    }

    // Recursive method Mergesort algorithm
    public ArrayList<String> mergeSort(ArrayList<String> words, int start, int end) {
        // Base case
        System.out.println(words);
        if (end-start == 1 || end-start == 0) {
            ArrayList<String> arr = new ArrayList<String>();
            return arr;
        }
        int mid = (start + end) / 2;
//        System.out.println("Array 1: " + words + ", start: " + start + " Mid: " + mid);
        ArrayList<String> arr1 = mergeSort(words, start, mid);
        ArrayList<String> arr2 = mergeSort(words, mid+1, end);
        return compare(arr1, arr2);
    }

    public ArrayList<String> compare(ArrayList<String> arr1, ArrayList<String> arr2) {
        ArrayList<String> arr = new ArrayList<String>();
        int idx1 = 0;
        int idx2 = 0;
        int count = 0;
        // While there is still stuff in the array
        while (idx1 < arr.size() && idx2 < arr.size()) {
            // If
            if (arr1.get(idx1).compareTo(arr2.get(idx2)) < 0) {
                arr.add(arr1.get(idx1++));
            }
            else {
                arr.add(arr2.get(idx2++));
            }
        }
        // Copy over remaining elements
        while (idx1 < arr1.size() && count < arr1.size()) {
            arr.add(arr1.get(idx1++));
        }
        while (idx2 < arr2.size()) {
            arr.add(arr1.get(idx2++));
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
            // If the word is in the dictionary go to the next word in the arraylist
            if (found(words.get(i), 0, DICTIONARY.length-1)) {
                break;
            }
            // If the word is not in the dictionary remove it from the arraylist
            else {
                words.remove(i);
            }
        }
    }

    // Recursive binary search algorithm
    public boolean found(String word, int start, int end) {
//        int mid = start + (end - start) / 2;
        int mid = (end - start) / 2;
        // Base case
        if (start > end) {
            return false;
        }
        if (DICTIONARY[mid].equals(word)) {
            return true;
        }
        if (word.compareTo(DICTIONARY[mid]) < 0) {
            return found(word, start,mid-1);
        }
        return found(word, mid+1, DICTIONARY.length);
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
