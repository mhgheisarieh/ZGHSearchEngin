import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

class Processor {
    /**
     * each document which has all the words in query has a result
     */


    public static int PROXIMITY_MAX_DISTANCE = 5;

    Processor() {
        System.out.println("ZGH Search Engine\nSearch Results:");
    }

    ArrayList<Result> processQuery(String query) {
        HashMap<Integer, Result> results = new HashMap<>();
        String[] wordsToFind = extractQueryWords(query);
        fillResults(wordsToFind, results);
        setResultsScore(wordsToFind, results);
        proximityFilter(wordsToFind, results);
        return getSortedResult(results);

    }

    private void fillResults(String[] wordsToFind, HashMap<Integer, Result> results) {
        ArrayList<Integer> foundDocs = findAllMatches(wordsToFind);
        if (foundDocs != null)
            foundDocs.forEach(docIndex -> results.put(docIndex, new Result(docIndex, 0)));
    }

    private ArrayList<Result> getSortedResult(HashMap<Integer, Result> results) {
        ArrayList<Result> result = new ArrayList<>(results.values());
        result.sort(Comparator.comparingInt(Result::getScore).reversed());
        return result;
    }

    private String[] extractQueryWords(String query) {
        return query.split("[\\s.,()/\"#;'\\\\\\-:$]+");
    }

    private void proximityFilter(String[] words, HashMap<Integer, Result> results) {
        ArrayList<Integer> toBeRemovedDocs = new ArrayList<>();
        HashMap<String,DetailsOfWord> details = PreProcessedData.getInstance().getDetailsOfWordHashMap();
        for (Integer docIndex : results.keySet()) {
            for (int i = 0; i < words.length - 1; i++) {
                int firstIndex = details.get(words[i]).getIndexInDoc().get(docIndex);
                int secondIndex = details.get(words[i + 1]).getIndexInDoc().get(docIndex);
                if (Math.abs(firstIndex - secondIndex) > PROXIMITY_MAX_DISTANCE) {
                    toBeRemovedDocs.add(docIndex);
                }
            }
        }
        toBeRemovedDocs.forEach(results::remove);
    }

    private void setResultsScore(String[] wordsToFind, HashMap<Integer, Result> results) {
        for (String word : wordsToFind) {
            for (Integer docIndex : results.keySet()) {
                int score = PreProcessedData.getInstance().getDetailsOfWordHashMap().get(word).getNumOfWordInDocs().get(docIndex);
                results.get(docIndex).changeScore(score);
            }
        }
    }

    private ArrayList<Integer> findAllMatches(String[] wordsToFind) {
        ArrayList<Integer> foundDocIndexes = null;
        for (String word : wordsToFind) {
            ArrayList<Integer> foundDocIndexesForWord = getFoundDocsIndexForWord(word);
            if (foundDocIndexesForWord != null) {
                if (foundDocIndexes == null)
                    foundDocIndexes = new ArrayList<>(foundDocIndexesForWord);
                else
                    foundDocIndexes.retainAll(foundDocIndexesForWord);
            }
        }
        return foundDocIndexes;
    }


    private ArrayList<Integer> getFoundDocsIndexForWord(String word) {
        DetailsOfWord detailsOfWord = PreProcessedData.getInstance().getDetailsOfWordHashMap().get(word);
        if (detailsOfWord != null)
            return new ArrayList<>(detailsOfWord.getNumOfWordInDocs().keySet());
        else
            return null;
    }
}