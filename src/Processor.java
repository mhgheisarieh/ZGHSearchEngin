import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Processor {
    // each doc which has all the words in query has a result
    private HashMap<Integer, Result> results; //HashMap to link doc indexes with results
    private ArrayList<Result> sortedResults;
    private static Processor instance;

    private Processor() {
        results = new HashMap<>();
        sortedResults = new ArrayList<>();
    }

    public void restartProcessor() {
        results = new HashMap<>();
        sortedResults = new ArrayList<>();
    }

    public static Processor getInstance() {
        if (instance == null) {
            instance = new Processor();
        }
        return instance;
    }

    public void processQuery(String query) {
        System.out.println("ZGH Search Engine\nSearch Results:");
        String[] wordsToFind = query.split("[\\s.,()/\"#;'\\\\\\-:$]+");
        findAllMatches(wordsToFind);
        setResultsScore(wordsToFind);
        proximityFilter(wordsToFind);
        sortedResults = new ArrayList<>(results.values());
        sortedResults.sort(Comparator.comparingInt(Result::getScore).reversed());
//        for (int i = 0; i < wordsToFind.length; i++) {
//            if (stringsToFind[i].equals("OR")) {
//                i++;
//                try {
//                    indexes.addAll(invertedIndex.get(stringsToFind[i]));
//                } catch (Exception ignored) {
//                }
//            } else {
//            if (invertedIndex.get(stringsToFind[i]) != null)
//                indexes.retainAll(invertedIndex.get(stringsToFind[i]));
//            }
//        }
    }

    private void proximityFilter(String[] words) {
        ArrayList<Integer> toBeRemovedDocs = new ArrayList<>();
        int maxDistance = 5;
        for (Integer docIndex : results.keySet()) {
            for (int i = 0; i < words.length - 1; i++) {
                int firstIndex = PreProcessor.getInstance().getDetailOfWords().get(words[i]).getIndexInDoc().get(docIndex);
                int secondIndex = PreProcessor.getInstance().getDetailOfWords().get(words[i + 1]).getIndexInDoc().get(docIndex);
                if (Math.abs(firstIndex - secondIndex) > maxDistance){
                    toBeRemovedDocs.add(docIndex);
                }
            }
        }
        toBeRemovedDocs.forEach(docIndex -> results.remove(docIndex));

    }

    private void setResultsScore(String[] wordsToFind) {
        for (String word : wordsToFind) {
            for (Integer docIndex : results.keySet()) {
                PreProcessor.getInstance().getDetailOfWords().get(word);
                int score = PreProcessor.getInstance().getDetailOfWords().get(word).getNumOfWordInDocs().get(docIndex);
                results.get(docIndex).changeScore(score);
            }
        }
    }

    private void findAllMatches(String[] wordsToFind) {
        ArrayList<Integer> foundDocIndexes = null;
        for (String s : wordsToFind) {
            if (foundDocIndexes == null && PreProcessor.getInstance().getDetailOfWords().get(s) != null)
                foundDocIndexes = new ArrayList<>(PreProcessor.getInstance().getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
            else if (foundDocIndexes != null && PreProcessor.getInstance().getDetailOfWords().get(s) != null)
                foundDocIndexes.retainAll(PreProcessor.getInstance().getDetailOfWords().get(s).getNumOfWordInDocs().keySet());
        }
        if (foundDocIndexes == null)
            return;
        for (Integer foundDocIndex : foundDocIndexes) {
            results.put(foundDocIndex, new Result(foundDocIndex, 0));
        }
    }

    public void printResults() {
        sortedResults.forEach(result -> System.out.println(result.getScore() + "     " + CSVFileReader.getInstance().getDocuments().get(result.getIndex())));
    }
}