import java.util.ArrayList;
import java.util.HashMap;

class PreProcessor {

    public static HashMap<String, DetailsOfWord> preProcess(ArrayList<String> docs) {
        HashMap<String, DetailsOfWord> detailsOfWordHashMap = new HashMap<>();
        int indexOfDoc = 0;
        for (String doc : docs) {
            preProcessDoc(doc, indexOfDoc, detailsOfWordHashMap);
            indexOfDoc++;
        }
        return detailsOfWordHashMap;
    }

    private static void preProcessDoc(String doc, int indexOfDoc, HashMap<String, DetailsOfWord> detailsOfWordHashMap) {
        String[] words = Splitter.split(doc);
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

    private static void setWordDetail(int indexOfDoc, int indexOfWord, DetailsOfWord detailsOfWord) {
        detailsOfWord.addWordToDocIndex(indexOfDoc);
        detailsOfWord.addIndexOfWordInDoc(indexOfDoc, indexOfWord);
    }
}