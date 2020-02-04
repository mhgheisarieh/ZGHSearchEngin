
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

//contains a word and indexes of documents which has the word as key and number of rematches in values
class InvertedIndexWord {
    private String word;
    private HashMap<Integer, Integer> numOfWordInDocs; //A hash map to link indexes and numOfWords in doc
    //todo for multiple occurrences in one doc can add ArrayList of Integer to save indexes
    private HashMap<Integer, Integer> indexInDoc; // key: index of doc ; value: index of word in doc

    public InvertedIndexWord(String word) {
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


class IndexOfWord {
    private int indexOfDoc;
    private int indexInDoc;

    IndexOfWord(int indexOfDoc, int indexInDoc) {
        this.indexOfDoc = indexOfDoc;
        this.indexInDoc = indexInDoc;
    }


}

public class InvertedIndexFinder {

    public static void main(String[] args) {
        ArrayList<String> documents = new ArrayList<>();
        HashMap<String, InvertedIndexWord> invertedIndex = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("English.csv"))) {
            String line;
            int index = 1;
            while ((line = br.readLine()) != null) {
                //Each line in a document
                line = line.split("\",\"")[1];
                line = line.substring(0, line.length() - 1);
                documents.add(line);
                processLine(invertedIndex, line, index);
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String strToFind = scanner.nextLine();
            processQuery(documents, invertedIndex, strToFind);
        }
    }

    private static void processQuery(ArrayList<String> documents, HashMap<String, InvertedIndexWord> invertedIndex, String strToFind) {
        System.out.println("ZGH Search Engine\nSearch Results:");
        String[] stringsToFind = strToFind.split("[\\s.,()/\"#;'\\\\\\-:$]+");
        HashMap<Integer, Result> results = new HashMap<>(); //HashMap to link doc indexes with results
        ArrayList<Integer> foundDocIndexes = null;
        for (int i = 0; i < stringsToFind.length; i++) {
            if (foundDocIndexes == null && invertedIndex.get(stringsToFind[i]) != null)
                foundDocIndexes = new ArrayList<>(invertedIndex.get(stringsToFind[i]).getNumOfWordInDocs().keySet());
            else if (foundDocIndexes != null && invertedIndex.get(stringsToFind[i]) != null)
                foundDocIndexes.retainAll(invertedIndex.get(stringsToFind[i]).getNumOfWordInDocs().keySet());
        }
        System.out.println(foundDocIndexes);
        System.out.println(invertedIndex);
        for (int i = 0; i < foundDocIndexes.size(); i++) {
            Result result = new Result(foundDocIndexes.get(i) , 0);
            results.put(foundDocIndexes.get(i) , result);
        }
        
//        for (int i = 0; i < stringsToFind.length; i++) {
//            Result result = new Result();
//        }
//        if (invertedIndex.get(stringsToFind[0]) != null) {
//            InvertedIndexWord invertedIndexWord = invertedIndex.get(stringsToFind[0]);
//            for (Map.Entry<Integer, Integer> entry : invertedIndexWord.getNumOfWordInDocs().entrySet()) {
//                if (results.get(entry.getKey()) == null) {
//                    Result result = new Result(entry.getKey(), entry.getValue());
//                    results.put(0, result);
//                } else {
//                    Result result = results.get(entry.getKey());
//                    result.changeScore(entry.getValue());
//                }
//            }
//        }
        for (int i = 0; i < stringsToFind.length; i++) {
            HashMap<Integer, Result> results_of_now_word = new HashMap<>(); //HashMap to link doc indexes with results
            if (invertedIndex.get(stringsToFind[i]) != null) {
                InvertedIndexWord invertedIndexWord = invertedIndex.get(stringsToFind[i]);
                for (Map.Entry<Integer, Integer> entry : invertedIndexWord.getNumOfWordInDocs().entrySet()) {
                    if (results.get(entry.getKey()) == null) {
                        Result result = new Result(entry.getKey(), entry.getValue());
                        results.put(0, result);
                    } else {
                        Result result = results.get(entry.getKey());
                        result.changeScore(entry.getValue());
                    }
                }
            }
//            if (stringsToFind[i].equals("OR")) {
//                i++;
//                try {
//                    indexes.addAll(invertedIndex.get(stringsToFind[i]));
//                } catch (Exception ignored) {
//                }
////            } else {
//            if (invertedIndex.get(stringsToFind[i]) != null)
//                indexes.retainAll(invertedIndex.get(stringsToFind[i]));
//            }
        }
//        printResults(documents, indexes);
    }

    private static void printResults(ArrayList<String> documents, ArrayList<Integer> indexes) {
        ArrayList<String> foundDocuments = new ArrayList<>();
        if (indexes.size() == 0) {
            System.out.println("nothing not found");
            return;
        }
        for (int index : indexes) {
            System.out.println(index + " : " + documents.get(index - 1));
            foundDocuments.add(documents.get(index - 1));
        }
        System.out.println(indexes);
    }

    private static void processLine(HashMap<String, InvertedIndexWord> invertedIndex, String line, int indexOfDoc) {
        String[] words = line.split("[\\s.,()/\"#;'\\\\\\-:$&]+");
        int indexOfWord = 0;
        for (String word : words) {
            if (invertedIndex.get(word) == null) {
                InvertedIndexWord indexes = new InvertedIndexWord(word);
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
