import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

class Processor {
    /**
     * each document which has all the words in query has a result
     */

    private HashMap<String, DetailsOfWord> detailsOfWordHashMap;

    Processor(HashMap<String, DetailsOfWord> detailsOfWordHashMap) {
        System.out.println("ZGH Search Engine\nSearch Results:");
        this.detailsOfWordHashMap = detailsOfWordHashMap;
    }

    ArrayList<Result> processQuery(String query) {
        HashMap<Integer, Result> results = new HashMap<>();
        String[] wordsToFind = extractQueryWords(query);
        findAllMatches(wordsToFind, results);
        setResultsScore(wordsToFind, results);
        proximityFilter(wordsToFind, results);
        return getSortedResult(results);
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
        int maxDistance = 5;
        for (Integer docIndex : results.keySet()) {
            for (int i = 0; i < words.length - 1; i++) {
                int firstIndex = detailsOfWordHashMap.get(words[i]).getIndexInDoc().get(docIndex);
                int secondIndex = detailsOfWordHashMap.get(words[i + 1]).getIndexInDoc().get(docIndex);
                if (Math.abs(firstIndex - secondIndex) > maxDistance) {
                    toBeRemovedDocs.add(docIndex);
                }
            }
        }
        toBeRemovedDocs.forEach(results::remove);
    }

    private void setResultsScore(String[] wordsToFind, HashMap<Integer, Result> results) {
        for (String word : wordsToFind) {
            for (Integer docIndex : results.keySet()) {
                detailsOfWordHashMap.get(word);
                int score = detailsOfWordHashMap.get(word).getNumOfWordInDocs().get(docIndex);
                results.get(docIndex).changeScore(score);
            }
        }
    }

    private void findAllMatches(String[] wordsToFind, HashMap<Integer, Result> results) {
        ArrayList<Integer> foundDocIndexes = null;
        for (String word : wordsToFind) {
            if (getFoundDocsIndex(word) != null) {
                if (foundDocIndexes == null)
                    foundDocIndexes = new ArrayList<>(Objects.requireNonNull(getFoundDocsIndex(word)));
                else
                    foundDocIndexes.retainAll(getFoundDocsIndex(word));
            }
        }
        if (foundDocIndexes == null)
            return;
        for (Integer foundDocIndex : foundDocIndexes) {
            results.put(foundDocIndex, new Result(foundDocIndex, 0));
        }
    }


    private ArrayList<Integer> getFoundDocsIndex(String word) {
        if (detailsOfWordHashMap.get(word) != null)
            return new ArrayList<>(detailsOfWordHashMap.get(word).getNumOfWordInDocs().keySet());
        else
            return null;
    }
}