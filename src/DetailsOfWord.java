import java.util.HashMap;

//contains a word and indexes of documents which has the word as key and number of rematches in values

public class DetailsOfWord {
    private String word;
    private HashMap<Integer, Integer> numOfWordInDocs; //A hash map to link indexes and numOfWords in doc
    //todo for multiple occurrences in one doc can add ArrayList of Integer to save indexes
    private HashMap<Integer, Integer> indexInDoc; // key: index of doc ; value: index of word in doc

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