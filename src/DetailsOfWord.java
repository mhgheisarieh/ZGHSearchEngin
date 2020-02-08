import java.util.HashMap;

/**
 * contains a word and indexes of documents which has the word as key and number of rematches in values
 */

public class DetailsOfWord {
    private String word;

    /**
     * A hash map to link indexes and numOfWords in doc
     */
    private HashMap<Integer, Integer> numOfWordInDocs;
    //todo for multiple occurrences in one doc can add ArrayList of Integer to save indexes
    /**
     * key: index of doc ; value: index of word in doc
     */
    private HashMap<Integer, Integer> indexInDoc;

    public DetailsOfWord(String word) {
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

    public HashMap<Integer, Integer> getIndexInDoc() {
        return indexInDoc;
    }
}