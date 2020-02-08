import java.util.ArrayList;
import java.util.HashMap;

public class PreProcessor {

    private HashMap<String, DetailsOfWord> invertedIndex;

    public PreProcessor(ArrayList<String> documents) {
        invertedIndex = new HashMap<>();
        preProcessDocs(documents);
    }

    public HashMap<String, DetailsOfWord> getDetailOfWords() {
        return invertedIndex;
    }

    private void preProcessDocs(ArrayList<String> docs) {
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
                invertedIndex.get(word).addIndexOfWordInDoc(indexOfDoc, indexOfWord);
            }
            indexOfWord++;
        }
    }
}