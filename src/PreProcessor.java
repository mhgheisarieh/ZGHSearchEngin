import java.util.ArrayList;
import java.util.HashMap;

public class PreProcessor {

    private static PreProcessor instance;

    private HashMap<String, DetailsOfWord> invertedIndex;

    private PreProcessor() {
        invertedIndex = new HashMap<>();
    }

    public static PreProcessor getInstance() {
        if (instance == null) {
            instance = new PreProcessor();
        }
        return instance;
    }

    public HashMap<String, DetailsOfWord> getDetailOfWords() {
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
                DetailsOfWord indexes = new DetailsOfWord(word);
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