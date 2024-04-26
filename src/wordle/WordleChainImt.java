package wordle;

import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;
import project20280.hashtable.ChainHashMap;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static wordle.Huffman.huffmanTree;
import static wordle.Wordle.letterFreqMap;

public class WordleChainImt {

    //String fileName = "wordle/resources/dictionary.txt";
    String fileName = "wordle/resources/extended-dictionary.txt";
    static List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();
    static ChainHashMap<String, Integer> letterFreqMap = new ChainHashMap<>();

    static ChainHashMap<String, Integer> wordFreqMap;
    static ChainHashMap<String, Integer> guessWordFreqMap;

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

    WordleChainImt() {

        dictionary = readDictionary(fileName);

        //System.out.println("dict length: " + this.dictionary.size());
        //System.out.println("dict: " + dictionary);

    }

    public static void main(String[] args) {

        WordleChainImt game = new WordleChainImt();

        letterFreqMap = getLetterFreqMap(dictionary);
        System.out.println(letterFreqMap);
        wordFreqMap = getWordFreqMap(dictionary);
        guessWordFreqMap = wordFreqMap;

        String target = game.getRandomTargetWord();
        huffmanTree(dictionary);
        System.out.println("Number of collisons " + wordFreqMap.getNoOfCollisions());
        //System.out.println(guessWordFreqMap);

        target = "abbey";
        //System.out.println("target: " + target);
        game.play(target);

    }

    public void play(String target) {
        // TODO
        // TODO: You have to fill in the code
        System.out.println(target);
        for(int i = 0; i < num_guesses; ++i) {
            String highestFreq = getMax(guessWordFreqMap);
            System.out.println("Best guess: " + highestFreq);
            System.out.println("Load factor " + guessWordFreqMap.getLoadFactor());
            String guess = getGuess();

            if(guess == target) { // you won!
                win(target);
                return;
            }

            String[] hint = getHint(guess, target);

            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));


            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(Objects.equals(hint[k], "+")) num_green += 1;
            }
            if(num_green == 5) {
                win(target);
                System.out.println("in " + (i + 1) + " guesses");
                return;
            }
            System.out.println(wordleSolver(guess, hint));
        }

        lost(target);
    }
    public static String[] getHint(String guess, String target){
        String [] hint = {"_", "_", "_", "_", "_"};

        int[] matchTracker = new int[26];

        for (int k = 0; k < 5; k++) {
            if (guess.charAt(k) == target.charAt(k)) {
                hint[k] = "+";
                int ascii = ((int) guess.charAt(k)) - (int)'a';
                matchTracker[ascii]++;
                //System.out.println("matchTracker " + matchTracker[ascii] + " at " + ascii + " guessatchar is = " + guess.charAt(k) + " with ascii " + ascciiii);
            }
        }
        for (int k = 0; k < 5; k++) {
            // Check if the current guess character matches any character in the target word
            for (int j = 0; j < 5; j++) {
                // Check if the guess character matches the target character, and the hint at the target position is not "+" or "o"
                if (guess.charAt(k) == target.charAt(j) && Objects.equals(hint[k], "_")) {
                    // Check the frequency of the guess character in the guess word and the target character in the target word
                    int ascii = guess.charAt(k) - 'a';
                    int guessFreq = matchTracker[ascii];
                    int targetFreq = checkLetter(target, target.charAt(j));
                    //System.out.println("guessFreq = " + guessFreq + " targetFreq = " + targetFreq + " matchTracker " + matchTracker[ascii] + " guessatchar is = " + guess.charAt(k));

                    // If the guess frequency is less than or equal to the target frequency, set the hint to "o"
                    if (guessFreq < targetFreq) {
                        hint[k] = "o";
                        matchTracker[ascii]++;
                        break;
                    }
                }
            }
        }
        return hint;
    }
    public static String wordleSolver(String guess, String[] hint)
    {
        Iterator<Entry<String, Integer>> iterator = guessWordFreqMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, Integer> entry = iterator.next();
            String word = entry.getKey();
            boolean remove = false;

            if(!Arrays.toString(hint).equals(Arrays.toString(getHint(guess, word))))
                remove = true;

            if(Objects.equals(word, guess))
                remove = true;

            if (remove) {
                guessWordFreqMap.remove(word);
            }
        }




        return guessWordFreqMap.toString();
    }


    // method to check if a letter occurs in a String, returns the number of occurrences

    public static int[] checkLetter(String guess, String[] hint, char letterToCompare) {
        int[] count = new int[2];
        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == letterToCompare) {
                if (hint[i].equals("+"))
                    count[0]++;
            }
        }

        for (int i = 0; i < 5; i++) {
            if (guess.charAt(i) == letterToCompare) {
                if (hint[i].equals("_")) {
                    count[1] = 1; // Flag indicating the character appears in a "_"
                    break;
                } else if (hint[i].equals("o")) {
                    count[0]++;
                }
            }
        }
        //System.out.println("checkletter returned " + count[0] + " for hint " + Arrays.toString(hint) + " and word " + guess + " with letter " + letterToCompare);
        return count;
    }
    public static int checkLetter(String s, char c)
    {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }




    private static ChainHashMap<String, Integer> getWordFreqMap(List<String> words){

        wordFreqMap = new ChainHashMap<>();

        int sumOfFreq = 0;

        for (String word : words) {
            for (char ch : word.toCharArray()) {
                sumOfFreq += letterFreqMap.get(Character.toString(ch));
            }
            wordFreqMap.put(word, sumOfFreq);
            sumOfFreq = 0;
        }
        //System.out.println(wordFreqMap.entrySet());
        return wordFreqMap;
    }
    private static ChainHashMap<String, Integer> getLetterFreqMap(List<String> words) {
        ChainHashMap<String, Integer> letterFreqMap = new ChainHashMap<>();

        // Initialize the frequency map with zeros for all characters
        for (char ch = 'a'; ch <= 'z'; ch++) {

            letterFreqMap.put(Character.toString(ch), 0);
        }

        // Count the frequency of each character in the dictionary
        for (String word : words) {
            for (char ch : word.toCharArray()) {
                letterFreqMap.put(Character.toString(ch), letterFreqMap.get(Character.toString(ch)) + 1);
            }
        }
        //System.out.println(letterFreqMap);
        return letterFreqMap;
    }


    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        return userWord;
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }
    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }
    public static String getMax(ChainHashMap<String, Integer> map) {
        Entry<String, Integer> highestFreq = null;
        for(Entry<String, Integer> c: map.entrySet()){
            if(highestFreq == null || c.getValue() >= highestFreq.getValue())
                highestFreq = c;
        }
        return highestFreq.toString();
    }
    public static List<String> convertMapToList(ChainHashMap<String, Integer> map) {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            list.add(key);
        }
        return list;
    }

    public static void huffmanTree(List<String> dictionary) {


        HeapPriorityQueue<Integer, LinkedBinaryTree<String>> q = new HeapPriorityQueue<>();

        // Fill the heap with values from the frequency map
        for (Entry<String, Integer> entry : letterFreqMap.entrySet()) {
            LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
            t.addRoot(entry.getKey());
            q.insert(entry.getValue(), t);
        }


        // Build the Huffman tree
        while (q.size() > 1) {
            Entry<Integer, LinkedBinaryTree<String>> e1 = q.removeMin();
            Entry<Integer, LinkedBinaryTree<String>> e2 = q.removeMin();

            LinkedBinaryTree<String> t = new LinkedBinaryTree<>();
            t.addRoot(e1.getValue().root().getElement().concat(e2.getValue().root().getElement()));

            t.attach(t.root(), e1.getValue(), e2.getValue());

            q.insert(e1.getKey() + e2.getKey(), t);


        }


        System.out.println(q.min().getValue().toBinaryTreeString());
    }
}

