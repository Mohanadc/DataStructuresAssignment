package wordle;

import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static wordle.Huffman.huffmanTree;
import static wordle.Wordle.letterFreqMap;

public class Wordle {

    String fileName = "wordle/resources/dictionary.txt";
    //String fileName = "wordle/resources/extended-dictionary.txt";
    static List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();
    static Map<String, Integer> letterFreqMap;
    static Map<String, Integer> wordFreqMap;
    static Map<String, Integer> guessWordFreqMap;

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

    Wordle() {

        dictionary = readDictionary(fileName);

        //System.out.println("dict length: " + this.dictionary.size());
        //System.out.println("dict: " + dictionary);

    }

    public static void main(String[] args) {

        Wordle game = new Wordle();

        letterFreqMap = getLetterFreqMap(dictionary);
        wordFreqMap = getWordFreqMap(dictionary);
        guessWordFreqMap = wordFreqMap;
        guessWordFreqMap = sortMapByValueAscending(guessWordFreqMap);

        String target = game.getRandomTargetWord();
        huffmanTree(dictionary);
        System.out.println(guessWordFreqMap);



        target = "abbey";
        //System.out.println("target: " + target);
        game.play(target);

    }

    public void play(String target) {
        // TODO
        // TODO: You have to fill in the code
        System.out.println(target);
        for(int i = 0; i < num_guesses; ++i) {
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
        Iterator<Map.Entry<String, Integer>> iterator = guessWordFreqMap.entrySet().iterator();
        String lastEntry;

        System.out.println(Arrays.toString(hint));
        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            String word = entry.getKey();
            boolean remove = false;


            if(!Arrays.toString(hint).equals(Arrays.toString(getHint(guess, word))))
                remove = true;

            if(Objects.equals(word, guess))
                remove = true;
            else {
                for (int i = 0; i < 5; i++) {
                    int[] guessLetterCount = checkLetter(guess, hint, guess.charAt(i)); // Get letter count info for the letter at position i for the guess
                    int wordLetterCount = checkLetter(word, guess.charAt(i)); // Get letter count info for the letter at position i for the current word

                    if(guessLetterCount[0] > wordLetterCount)
                    {
                        remove = true;
                        System.out.println("case 0 removal");
                        break;
                    }



                    if (Objects.equals(hint[i], "+") && guess.charAt(i) != word.charAt(i)) {
                        remove = true;
                        System.out.println("case 1 removal");
                        break;
                    } else if(Objects.equals(hint[i], "o")){
                         if (wordLetterCount != guessLetterCount[0] && guessLetterCount[1] == 1){
                            remove = true;
                            System.out.println("case 3 removal");
                            break;
                        }
                    } else {
                        if(wordLetterCount != guessLetterCount[0] && guessLetterCount[1] == 1 && guessLetterCount[0] > 0){
                            remove = true;
                            System.out.println("case 4 removal");
                            break;
                        } else if(guessLetterCount[0] == 0 && wordLetterCount != guessLetterCount[0]){
                            System.out.println("wordLetter count is " + wordLetterCount + "guessLetter count is " + guessLetterCount[0]);
                            remove = true;
                            break;
                        }
                    }

                }
            }

            if(remove){
                iterator.remove();
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




    private static Map<String, Integer> getWordFreqMap(List<String> words){

        wordFreqMap = new HashMap<>();

        int sumOfFreq = 0;

        for (String word : words) {
            for (char ch : word.toCharArray()) {
                sumOfFreq += letterFreqMap.get(Character.toString(ch));
            }
            wordFreqMap.put(word, sumOfFreq);
            sumOfFreq = 0;
        }
        return wordFreqMap;
    }
    private static Map<String, Integer> getLetterFreqMap(List<String> words) {
        Map<String, Integer> letterFreqMap = new HashMap<>();

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
    public static Map<String, Integer> sortMapByValueAscending(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

        // Sort the list based on the values in ascending order
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        // Create a new LinkedHashMap to maintain the insertion order while sorting
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }
    public static List<String> convertMapToList(Map<String, Integer> map) {
        List<String> list = new ArrayList<>();
        for (String key : map.keySet()) {
            list.add(key);
        }
        return list;
    }
}

