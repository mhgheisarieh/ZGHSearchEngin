import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Processor {
    /**
     * each document which has all the words in query has a result
     *
     * @param results: to link doc indexes with results
     */
    private HashMap<Integer, Result> results;
    private ArrayList<Result> sortedResults;
    private PreProcessor preProcessor;

    public Processor(PreProcessor preProcessor) {
        this.results = new HashMap<>();
        this.sortedResults = new ArrayList<>();
        this.preProcessor = preProcessor;
    }

    private void restartProcessor() {
        this.results = new HashMap<>();
        this.sortedResults = new ArrayList<>();

    }

    public void processQuery(String query) {
        restartProcessor();
        System.out.println("ZGH Search Engine\nSearch Results:");
        String[] wordsToFind = query.split("[\\s.,()/\"#;'\\\\\\-:$]+");
        findAllMatches(wordsToFind);
        setResultsScore(wordsToFind);
        proximityFilter(wordsToFind);
        sortedResults = new ArrayList<>(results.values());
        sortedResults.sort(Comparator.comparingInt(Result::getScore).reversed());
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
            if (foundDocIndexes == null && preProcessor.getDetailOfWords().get(s) != null)
                foundDocIndexes = new ArrayList<>(preProcessor.getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
            else if (foundDocIndexes != null && preProcessor.getDetailOfWords().get(s) != null)
                foundDocIndexes.retainAll(preProcessor.getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
        }
        if (foundDocIndexes == null)
            return;
        for (Integer foundDocIndex : foundDocIndexes) {
            results.put(foundDocIndex, new Result(foundDocIndex, 0));
        }
    }

    public void printResults(ArrayList<String> documents) {
        sortedResults.forEach(result -> System.out.println(result.getScore() + "     " + documents.get(result.getIndex())));
    }
}