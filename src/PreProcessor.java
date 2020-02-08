import java.util.ArrayList;
import java.util.HashMap;

class PreProcessor {

    private HashMap<String, DetailsOfWord> detailsOfWordHashMap;

    PreProcessor(ArrayList<String> documents) {
        detailsOfWordHashMap = new HashMap<>();
        preProcessDocs(documents);
    }

    HashMap<String, DetailsOfWord> getDetailOfWords() {
        return detailsOfWordHashMap;
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
            if (detailsOfWordHashMap.get(word) == null) {
                DetailsOfWord detailsOfWord = new DetailsOfWord(word);
                setWordDetail(indexOfDoc, indexOfWord, detailsOfWord);
                detailsOfWordHashMap.put(word, detailsOfWord);
            } else {
                setWordDetail(indexOfDoc, indexOfWord, detailsOfWordHashMap.get(word));
            }
            indexOfWord++;
        }
    }

    private void setWordDetail(int indexOfDoc, int indexOfWord, DetailsOfWord detailsOfWord) {
        detailsOfWord.addWordToDocIndex(indexOfDoc);
        detailsOfWord.addIndexOfWordInDoc(indexOfDoc, indexOfWord);
    }
}