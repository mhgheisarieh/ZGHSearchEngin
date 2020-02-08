/**
 * each founded document as a Result;
 */

class Result {
    /**
     * @param index: index of document
     * @param score : score of document
     */

    private int index;
    private int score;

    Result(int index, int score) {
        this.index = index;
        this.score = score;
    }

    void changeScore(int change) {
        this.score += change;
    }

    int getIndex() {
        return index;
    }

    int getScore() {
        return score;
    }
}