package utils;

public class RandomGen {
    private static final java.util.Random random = new java.util.Random();

    /**
     * Returns a random integer between 0 (inclusive) and the specified bound (exclusive).
     *
     * @param bound the upper bound (exclusive) for the random integer
     * @return a random integer between 0 (inclusive) and bound (exclusive)
     */
    public static int getRandomInt(int bound) {
        return random.nextInt(bound);
    }

    /**
     * Returns random coordinates within the grid size.
     *
     * @param gridSize the size of the grid
     * @return an array containing the row and column indices
     */
    public static int[] getRandomCoordinates(int gridSize) {
        int row = getRandomInt(gridSize);
        int col = getRandomInt(gridSize);
        return new int[]{row, col};
    }
}
