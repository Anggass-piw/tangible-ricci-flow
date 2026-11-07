package id.anggass.ricciflow;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SurfaceModelTest {
    @Test
    public void modelProducesFiniteValuesAndEvolves() {
        SurfaceModel model = new SurfaceModel();
        boolean changed = false;
        for (int row = 0; row < SurfaceModel.GRID_SIZE; row++) {
            for (int col = 0; col < SurfaceModel.GRID_SIZE; col++) {
                double initial = model.heightAt(0, row, col);
                double later = model.heightAt(SurfaceModel.MAX_STEPS, row, col);
                assertTrue(Double.isFinite(initial));
                assertTrue(Double.isFinite(later));
                if (Math.abs(initial - later) > 1.0e-6) {
                    changed = true;
                }
            }
        }
        assertTrue("The surface should change after discrete averaging", changed);
    }

    @Test
    public void requestedIndicesAreSafelyClamped() {
        SurfaceModel model = new SurfaceModel();
        assertTrue(Double.isFinite(model.heightAt(-10, -3, 90)));
        assertTrue(Double.isFinite(model.heightAt(1000, 200, -2)));
    }
}
