
import com.sun.org.apache.regexp.internal.RE;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

//contains a word and indexes of documents which has the word as key and number of rematches in values
class DetailOfWord {
    private String word;
    private HashMap<Integer, Integer> numOfWordInDocs; //A hash map to link indexes and numOfWords in doc
    //todo for multiple occurrences in one doc can add ArrayList of Integer to save indexes
    private HashMap<Integer, Integer> indexInDoc; // key: index of doc ; value: index of word in doc

    public DetailOfWord(String word) {
        this.word = word;
        this.numOfWordInDocs = new HashMap<>();
        this.indexInDoc = new HashMap<>();
    }

    public void addWordToDocIndex(int indexOfDoc, int number) {
        this.numOfWordInDocs.merge(indexOfDoc, number, Integer::sum);
    }

    public void addIndexOfWordInDoc(int indexOfDoc, int indexOfWord) {
        this.indexInDoc.put(indexOfDoc, indexOfWord);
    }

    public HashMap<Integer, Integer> getNumOfWordInDocs() {
        return numOfWordInDocs;
    }
}

//each founded document as a Result; index: index of document; score : score of document
class Result {
    private int index;
    private int score;

    public Result(int index, int score) {
        this.index = index;
        this.score = score;
    }

    public void changeScore(int change) {
        this.score += change;
    }

    public int getIndex() {
        return index;
    }

    public int getScore() {
        return score;
    }
}

class CSVFileReader {
    private static CSVFileReader instance;
    private ArrayList<String> documents = new ArrayList<>();

    public ArrayList<String> getDocuments() {
        return documents;
    }


    public static CSVFileReader getInstance() {
        if (instance == null) {
            instance = new CSVFileReader();
        }
        return instance;
    }

    public void readCSVFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String doc;
            int indexOfDoc = 1;
            while ((doc = br.readLine()) != null) {
                //Each line is a document
                doc = doc.split("\",\"")[1];
                doc = doc.substring(0, doc.length() - 1);
                documents.add(doc);
                indexOfDoc++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class PreProcessor {

    private static PreProcessor instance;

    private HashMap<String, DetailOfWord> invertedIndex;

    private PreProcessor() {
        invertedIndex = new HashMap<>();
    }

    public static PreProcessor getInstance() {
        if (instance == null) {
            instance = new PreProcessor();
        }
        return instance;
    }

    public HashMap<String, DetailOfWord> getDetailOfWords() {
        return invertedIndex;
    }

    public void preProcessDocs(ArrayList<String> docs) {
        int indexOfDoc = 0;
        for (String doc : docs) {
            preProcessDoc(doc, indexOfDoc);
            indexOfDoc++;
        }
    }

    private void preProcessDoc(String doc, int indexOfDoc) {
        String[] words = doc.split("[\\s.,()/\"#;'\\\\\\-:$&]+");
        int indexOfWord = 0;
        for (String word : words) {
            if (invertedIndex.get(word) == null) {
                DetailOfWord indexes = new DetailOfWord(word);
                indexes.addWordToDocIndex(indexOfDoc, 1);
                invertedIndex.put(word, indexes);
                indexes.addIndexOfWordInDoc(indexOfDoc, indexOfWord);
            } else {
                invertedIndex.get(word).addWordToDocIndex(indexOfDoc, 1);
            }
            indexOfWord++;
        }
    }
}

class Processor {
    // each doc which has all the words in query has a result
    private HashMap<Integer, Result> results; //HashMap to link doc indexes with results
    private ArrayList<Result> sortedResults;
    private static Processor instance;

    private Processor() {
        results = new HashMap<>();
        sortedResults = new ArrayList<>();
    }

    public void restartProcessor() {
        results = new HashMap<>();
        sortedResults = new ArrayList<>();
    }

    public static Processor getInstance() {
        if (instance == null) {
            instance = new Processor();
        }
        return instance;
    }

    public void processQuery(String query) {
        System.out.println("ZGH Search Engine\nSearch Results:");
        String[] wordsToFind = query.split("[\\s.,()/\"#;'\\\\\\-:$]+");
        findAllMatches(wordsToFind);
        setResultsScore(wordsToFind);
        sortedResults = new ArrayList<>(results.values());
        sortedResults.sort(Comparator.comparingInt(Result::getScore));
//        for (int i = 0; i < wordsToFind.length; i++) {
//            if (stringsToFind[i].equals("OR")) {
//                i++;
//                try {
//                    indexes.addAll(invertedIndex.get(stringsToFind[i]));
//                } catch (Exception ignored) {
//                }
//            } else {
//            if (invertedIndex.get(stringsToFind[i]) != null)
//                indexes.retainAll(invertedIndex.get(stringsToFind[i]));
//            }
//        }
    }

    private void setResultsScore(String[] wordsToFind) {
        for (String word : wordsToFind) {
            for (Integer docIndex : results.keySet()) {
                PreProcessor.getInstance().getDetailOfWords().get(word);
                int score = PreProcessor.getInstance().getDetailOfWords().get(word).getNumOfWordInDocs().get(docIndex);
                results.get(docIndex).changeScore(score);
            }
        }
    }

    private void findAllMatches(String[] wordsToFind) {
        ArrayList<Integer> foundDocIndexes = null;
        for (String s : wordsToFind) {
            if (foundDocIndexes == null && PreProcessor.getInstance().getDetailOfWords().get(s) != null)
                foundDocIndexes = new ArrayList<>(PreProcessor.getInstance().getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
            else if (foundDocIndexes != null && PreProcessor.getInstance().getDetailOfWords().get(s) != null)
                foundDocIndexes.retainAll(PreProcessor.getInstance().getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
        }
        if (foundDocIndexes == null)
            return;
        for (Integer foundDocIndex : foundDocIndexes) {
            results.put(foundDocIndex, new Result(foundDocIndex, 0));
        }
    }

    public void printResults() {
        sortedResults.forEach(result -> System.out.println(CSVFileReader.getInstance().getDocuments().get(result.getIndex())));
    }
}

public class ZGHSearchEngine {

    public static final String FILE_NAME = "English.csv";

    public static void main(String[] args) {
        CSVFileReader.getInstance().readCSVFile(FILE_NAME);
        PreProcessor.getInstance().preProcessDocs(CSVFileReader.getInstance().getDocuments());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String query = scanner.nextLine();
            Processor.getInstance().restartProcessor();
            Processor.getInstance().processQuery(query);
            Processor.getInstance().printResults();
        }
    }
}
