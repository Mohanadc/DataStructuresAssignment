package wordle;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HashtableQ {
    static String fileName = "wordle/resources/sample_txt.txt";
    static ChainHashMap<String, Integer> wordFreqMap;

    public static void main(String[] args){
        List<String> wordList = readFile(fileName);
        getWordFreqMap(wordList);

        for(int i = 0; i < 10; i++) {

            Entry<String, Integer> topWord = getMax(wordFreqMap);
            System.out.println(topWord + " "  + topWord.getValue());

        }
    }

    private static ChainHashMap<String, Integer> getWordFreqMap(List<String> words){

        wordFreqMap = new ChainHashMap<>();

        int sumOfFreq = 0;

        for (String word : words) {
            sumOfFreq = 0;

            for(String wordToCompare: words){

                if(word.equals(wordToCompare))
                    sumOfFreq++;
            }

            wordFreqMap.put(word, sumOfFreq);
        }
        //System.out.println(wordFreqMap.entrySet());
        return wordFreqMap;
    }


    public static List<String> readFile(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = HashtableQ.class.getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            // Read file space by space
            while ((strLine = reader.readLine()) != null) {
                // Split the line into words based on spaces
                String[] words = strLine.split(" ");
                for (String word : words) {

                    wordList.add(word.toLowerCase());
                }
            }
            // Close the input stream
            in.close();

        } catch (Exception e) { // Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }
    public static Entry<String, Integer> getMax(ChainHashMap<String, Integer> map) {
        Entry<String, Integer> highestFreq = null;
        for(Entry<String, Integer> c: map.entrySet()){
            if(highestFreq == null || c.getValue() >= highestFreq.getValue())
                highestFreq = c;
        }
        map.remove(highestFreq.getKey());
        return highestFreq;
    }
}
