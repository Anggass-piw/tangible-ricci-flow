using System.Drawing.Drawing2D;
using System.Windows.Forms;

namespace RicciSurfaceLab;

internal sealed class SurfaceView : Control
{
    private readonly SurfaceModel _model;
    private int _step;

    internal SurfaceView(SurfaceModel model)
    {
        _model = model;
        DoubleBuffered = true;
        ResizeRedraw = true;
        BackColor = Color.FromArgb(12, 22, 38);
        AccessibleName = "Discrete 10 by 10 surface mesh";
        AccessibleDescription = "Educational smoothing visualization, not a Ricci-flow solver.";
    }

    internal void SetStep(int step)
    {
        _step = Math.Clamp(step, 0, SurfaceModel.MaxSteps);
        Invalidate();
    }

    protected override void OnPaint(PaintEventArgs e)
    {
        base.OnPaint(e);
        var graphics = e.Graphics;
        graphics.SmoothingMode = SmoothingMode.AntiAlias;
        graphics.Clear(BackColor);

        var width = ClientSize.Width;
        var height = ClientSize.Height;
        if (width <= 0 || height <= 0) return;

        using var stemPen = new Pen(Color.FromArgb(76, 144, 180, 210), 1f);
        using var meshPen = new Pen(Color.FromArgb(205, 174, 214, 232), 1.15f);
        using var pointBrush = new SolidBrush(Color.FromArgb(235, 249, 255));
        var unit = Math.Min(width / 20.8f, height / 10.2f);
        var centerX = width * 0.5f;
        var baseY = height * 0.64f;
        var points = new PointF[SurfaceModel.GridSize, SurfaceModel.GridSize];

        for (var row = 0; row < SurfaceModel.GridSize; row++)
        {
            for (var col = 0; col < SurfaceModel.GridSize; col++)
            {
                var z = _model.HeightAt(_step, row, col);
                points[row, col] = Project(row, col, z, centerX, baseY, unit);
                var basePoint = Project(row, col, 0.0, centerX, baseY, unit);
                graphics.DrawLine(stemPen, basePoint, points[row, col]);
            }
        }

        for (var diagonal = 0; diagonal <= 2 * (SurfaceModel.GridSize - 2); diagonal++)
        {
            for (var row = 0; row < SurfaceModel.GridSize - 1; row++)
            {
                var col = diagonal - row;
                if (col < 0 || col >= SurfaceModel.GridSize - 1) continue;

                var h00 = _model.HeightAt(_step, row, col);
                var h01 = _model.HeightAt(_step, row, col + 1);
                var h11 = _model.HeightAt(_step, row + 1, col + 1);
                var h10 = _model.HeightAt(_step, row + 1, col);
                var polygon = new[]
                {
                    points[row, col], points[row, col + 1],
                    points[row + 1, col + 1], points[row + 1, col]
                };
                var mean = (h00 + h01 + h11 + h10) * 0.25;
                using var fill = new SolidBrush(ColorForHeight(mean));
                graphics.FillPolygon(fill, polygon);
                graphics.DrawPolygon(meshPen, polygon);
            }
        }

        var radius = Math.Max(2.3f, unit * 0.075f);
        for (var row = 0; row < SurfaceModel.GridSize; row++)
        {
            for (var col = 0; col < SurfaceModel.GridSize; col++)
            {
                var point = points[row, col];
                graphics.FillEllipse(pointBrush, point.X - radius, point.Y - radius, radius * 2, radius * 2);
            }
        }

        TextRenderer.DrawText(graphics, "GRID 10 × 10  ·  100 POINTS",
            new Font("Segoe UI Semibold", 9f, FontStyle.Bold), new Point(16, 14),
            Color.FromArgb(190, 209, 230));
        TextRenderer.DrawText(graphics, $"STEP {_step} / {SurfaceModel.MaxSteps}",
            new Font("Segoe UI Semibold", 9f, FontStyle.Bold),
            new Point(Math.Max(16, width - 130), 14), Color.FromArgb(190, 209, 230));
    }

    private static PointF Project(int row, int col, double z, float centerX, float baseY, float unit)
    {
        var center = (SurfaceModel.GridSize - 1) * 0.5f;
        var x = col - center;
        var y = row - center;
        return new PointF(
            centerX + (x - y) * unit,
            baseY + (x + y) * unit * 0.42f - (float)z * unit * 3.4f);
    }

    private static Color ColorForHeight(double height)
    {
        var t = Math.Clamp((height + 0.18) / 0.70, 0.0, 1.0);
        var red = (int)(45 + 45 * t);
        var green = (int)(106 + 105 * t);
        var blue = (int)(170 + 42 * t);
        return Color.FromArgb(225, red, green, blue);
    }
}
