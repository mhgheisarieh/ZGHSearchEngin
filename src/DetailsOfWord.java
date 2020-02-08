import java.util.HashMap;

/**
 * contains a word and indexes of documents which has the word as key and number of rematches in values
 */

class DetailsOfWord {
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

    DetailsOfWord(String word) {
        this.word = word;
        this.numOfWordInDocs = new HashMap<>();
        this.indexInDoc = new HashMap<>();
    }

    void addWordToDocIndex(int indexOfDoc) {
        this.numOfWordInDocs.merge(indexOfDoc, 1, Integer::sum);
    }

    void addIndexOfWordInDoc(int indexOfDoc, int indexOfWord) {
        this.indexInDoc.put(indexOfDoc, indexOfWord);
    }

    HashMap<Integer, Integer> getNumOfWordInDocs() {
        return numOfWordInDocs;
    }

    HashMap<Integer, Integer> getIndexInDoc() {
        return indexInDoc;
    }
}