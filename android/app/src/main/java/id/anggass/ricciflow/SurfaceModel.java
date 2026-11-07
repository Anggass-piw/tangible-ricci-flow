package id.anggass.ricciflow;

/**
 * A small discrete height-field model for the educational visualization.
 * This is deliberately not a numerical Ricci-flow solver.
 */
public final class SurfaceModel {
    public static final int GRID_SIZE = 10;
    public static final int MAX_STEPS = 60;

    private final double[][][] frames = new double[MAX_STEPS + 1][GRID_SIZE][GRID_SIZE];

    public SurfaceModel() {
        createInitialSurface();
        evolveByLocalAveraging();
    }

    private void createInitialSurface() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                double x = -1.0 + 2.0 * col / (GRID_SIZE - 1.0);
                double y = -1.0 + 2.0 * row / (GRID_SIZE - 1.0);
                double radius = Math.sqrt(x * x + y * y);
                double ring = 0.48 * Math.exp(-Math.pow((radius - 0.56) / 0.23, 2.0));
                double ripple = 0.055 * Math.sin(2.7 * x) * Math.cos(2.3 * y);
                double shallowCenter = -0.10 * Math.exp(-(x * x + y * y) / 0.12);
                frames[0][row][col] = ring + ripple + shallowCenter;
            }
        }
    }

    private void evolveByLocalAveraging() {
        for (int step = 1; step <= MAX_STEPS; step++) {
            double[][] previous = frames[step - 1];
            double[][] next = frames[step];
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    double neighborTotal = 0.0;
                    int neighborCount = 0;
                    if (row > 0) {
                        neighborTotal += previous[row - 1][col];
                        neighborCount++;
                    }
                    if (row + 1 < GRID_SIZE) {
                        neighborTotal += previous[row + 1][col];
                        neighborCount++;
                    }
                    if (col > 0) {
                        neighborTotal += previous[row][col - 1];
                        neighborCount++;
                    }
                    if (col + 1 < GRID_SIZE) {
                        neighborTotal += previous[row][col + 1];
                        neighborCount++;
                    }
                    double neighborAverage = neighborTotal / neighborCount;
                    next[row][col] = 0.65 * previous[row][col] + 0.35 * neighborAverage;
                }
            }
        }
    }

    public double heightAt(int step, int row, int col) {
        int safeStep = Math.max(0, Math.min(MAX_STEPS, step));
        int safeRow = Math.max(0, Math.min(GRID_SIZE - 1, row));
        int safeCol = Math.max(0, Math.min(GRID_SIZE - 1, col));
        return frames[safeStep][safeRow][safeCol];
    }
}
