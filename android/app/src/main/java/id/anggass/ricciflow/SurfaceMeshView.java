package id.anggass.ricciflow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public final class SurfaceMeshView extends View {
    private final SurfaceModel model;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int step = 0;

    public SurfaceMeshView(Context context, SurfaceModel model) {
        super(context);
        this.model = model;
        setContentDescription("Discrete smoothing visualization of a 10 by 10 height-field grid");
        setWillNotDraw(false);
    }

    public void setStep(int step) {
        this.step = Math.max(0, Math.min(SurfaceModel.MAX_STEPS, step));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }

        RectF bounds = new RectF(0, 0, width, height);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(12, 22, 38));
        canvas.drawRoundRect(bounds, dp(20), dp(20), paint);

        canvas.save();
        Path clip = new Path();
        clip.addRoundRect(bounds, dp(20), dp(20), Path.Direction.CW);
        canvas.clipPath(clip);

        float unit = Math.min(width / 20.8f, height / 10.2f);
        float centerX = width * 0.5f;
        float baseY = height * 0.62f;

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(Math.max(1f, dp(1)));
        paint.setColor(Color.argb(70, 144, 180, 210));
        for (int row = 0; row < SurfaceModel.GRID_SIZE; row++) {
            for (int col = 0; col < SurfaceModel.GRID_SIZE; col++) {
                float value = (float) model.heightAt(step, row, col);
                float[] top = project(row, col, value, centerX, baseY, unit);
                float[] base = project(row, col, 0f, centerX, baseY, unit);
                canvas.drawLine(base[0], base[1], top[0], top[1], paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
        for (int diagonal = 0; diagonal <= 2 * (SurfaceModel.GRID_SIZE - 2); diagonal++) {
            for (int row = 0; row < SurfaceModel.GRID_SIZE - 1; row++) {
                int col = diagonal - row;
                if (col < 0 || col >= SurfaceModel.GRID_SIZE - 1) {
                    continue;
                }
                float h00 = (float) model.heightAt(step, row, col);
                float h01 = (float) model.heightAt(step, row, col + 1);
                float h11 = (float) model.heightAt(step, row + 1, col + 1);
                float h10 = (float) model.heightAt(step, row + 1, col);
                Path face = new Path();
                addPoint(face, project(row, col, h00, centerX, baseY, unit), true);
                addPoint(face, project(row, col + 1, h01, centerX, baseY, unit), false);
                addPoint(face, project(row + 1, col + 1, h11, centerX, baseY, unit), false);
                addPoint(face, project(row + 1, col, h10, centerX, baseY, unit), false);
                face.close();

                float average = (h00 + h01 + h11 + h10) * 0.25f;
                float level = Math.max(0f, Math.min(1f, (average + 0.18f) / 0.70f));
                float[] hsv = new float[] { 205f - 72f * level, 0.76f, 0.48f + 0.30f * level };
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(Color.HSVToColor(225, hsv));
                canvas.drawPath(face, paint);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Math.max(1f, dp(0.8f)));
                paint.setColor(Color.argb(190, 174, 214, 232));
                canvas.drawPath(face, paint);
            }
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(235, 249, 255));
        float nodeRadius = Math.max(dp(1.7f), unit * 0.075f);
        for (int row = 0; row < SurfaceModel.GRID_SIZE; row++) {
            for (int col = 0; col < SurfaceModel.GRID_SIZE; col++) {
                float value = (float) model.heightAt(step, row, col);
                float[] point = project(row, col, value, centerX, baseY, unit);
                canvas.drawCircle(point[0], point[1], nodeRadius, paint);
            }
        }
        canvas.restore();
    }

    private float[] project(int row, int col, float z, float centerX, float baseY, float unit) {
        float center = (SurfaceModel.GRID_SIZE - 1) * 0.5f;
        float x = col - center;
        float y = row - center;
        float screenX = centerX + (x - y) * unit;
        float screenY = baseY + (x + y) * unit * 0.42f - z * unit * 3.4f;
        return new float[] { screenX, screenY };
    }

    private float dp(float value) {
        return value * getResources().getDisplayMetrics().density;
    }

    private void addPoint(Path path, float[] point, boolean first) {
        if (first) {
            path.moveTo(point[0], point[1]);
        } else {
            path.lineTo(point[0], point[1]);
        }
    }
}
