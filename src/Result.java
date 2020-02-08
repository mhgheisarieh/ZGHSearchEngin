/**
 * each founded document as a Result;
 */

public class Result {
    /**
     * @param index: index of document
     * @param score : score of document
     */

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