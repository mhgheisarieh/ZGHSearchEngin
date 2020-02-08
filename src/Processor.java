import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

class Processor {
    /**
     * each document which has all the words in query has a result
     *
     * @param results: to link doc indexes with results
     */
    private HashMap<Integer, Result> results;
    private PreProcessor preProcessor;

    Processor(PreProcessor preProcessor) {
        this.results = new HashMap<>();
        this.preProcessor = preProcessor;
    }

    private void restartProcessor() {
        this.results = new HashMap<>();
    }

    ArrayList<Result> processQuery(String query) {
        restartProcessor();
        System.out.println("ZGH Search Engine\nSearch Results:");
        String[] wordsToFind = extractQueryWords(query);
        findAllMatches(wordsToFind);
        setResultsScore(wordsToFind);
        proximityFilter(wordsToFind);
        ArrayList<Result> result = new ArrayList<>(results.values());
        result.sort(Comparator.comparingInt(Result::getScore).reversed());
        return result;
    }

    private String[] extractQueryWords(String query) {
        return query.split("[\\s.,()/\"#;'\\\\\\-:$]+");
    }

    private void proximityFilter(String[] words) {
        ArrayList<Integer> toBeRemovedDocs = new ArrayList<>();
        int maxDistance = 5;
        for (Integer docIndex : results.keySet()) {
            for (int i = 0; i < words.length - 1; i++) {
                int firstIndex = preProcessor.getDetailOfWords().get(words[i]).getIndexInDoc().get(docIndex);
                int secondIndex = preProcessor.getDetailOfWords().get(words[i + 1]).getIndexInDoc().get(docIndex);
                if (Math.abs(firstIndex - secondIndex) > maxDistance) {
                    toBeRemovedDocs.add(docIndex);
                }
            }
        }
        toBeRemovedDocs.forEach(docIndex -> results.remove(docIndex));
    }

    private void setResultsScore(String[] wordsToFind) {
        for (String word : wordsToFind) {
            for (Integer docIndex : results.keySet()) {
                preProcessor.getDetailOfWords().get(word);
                int score = preProcessor.getDetailOfWords().get(word).getNumOfWordInDocs().get(docIndex);
                results.get(docIndex).changeScore(score);
            }
        }
    }

    private void findAllMatches(String[] wordsToFind) {
        ArrayList<Integer> foundDocIndexes = null;
        for (String s : wordsToFind) {
            ArrayList<Integer> numOfWordInDocs = new ArrayList<>(preProcessor.getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
            if (preProcessor.getDetailOfWords().get(s) != null) {
                if (foundDocIndexes == null)
                    foundDocIndexes = new ArrayList<>(numOfWordInDocs);
                else foundDocIndexes.retainAll(numOfWordInDocs);
            }
        }
        if (foundDocIndexes == null)
            return;
        for (Integer foundDocIndex : foundDocIndexes) {
            results.put(foundDocIndex, new Result(foundDocIndex, 0));
        }
    }


}