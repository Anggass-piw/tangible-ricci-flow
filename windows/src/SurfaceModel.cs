namespace RicciSurfaceLab;

/// <summary>
/// Discrete educational height-field model. This is not a numerical Ricci-flow solver.
/// </summary>
internal sealed class SurfaceModel
{
    internal const int GridSize = 10;
    internal const int MaxSteps = 60;

    private readonly double[,,] _frames = new double[MaxSteps + 1, GridSize, GridSize];

    internal SurfaceModel()
    {
        CreateInitialSurface();
        EvolveByLocalAveraging();
    }

    private void CreateInitialSurface()
    {
        for (var row = 0; row < GridSize; row++)
        {
            for (var col = 0; col < GridSize; col++)
            {
                var x = -1.0 + 2.0 * col / (GridSize - 1.0);
                var y = -1.0 + 2.0 * row / (GridSize - 1.0);
                var radius = Math.Sqrt(x * x + y * y);
                var ring = 0.48 * Math.Exp(-Math.Pow((radius - 0.56) / 0.23, 2.0));
                var ripple = 0.055 * Math.Sin(2.7 * x) * Math.Cos(2.3 * y);
                var shallowCenter = -0.10 * Math.Exp(-(x * x + y * y) / 0.12);
                _frames[0, row, col] = ring + ripple + shallowCenter;
            }
        }
    }

    private void EvolveByLocalAveraging()
    {
        for (var step = 1; step <= MaxSteps; step++)
        {
            for (var row = 0; row < GridSize; row++)
            {
                for (var col = 0; col < GridSize; col++)
                {
                    var neighborTotal = 0.0;
                    var neighborCount = 0;
                    if (row > 0) { neighborTotal += _frames[step - 1, row - 1, col]; neighborCount++; }
                    if (row + 1 < GridSize) { neighborTotal += _frames[step - 1, row + 1, col]; neighborCount++; }
                    if (col > 0) { neighborTotal += _frames[step - 1, row, col - 1]; neighborCount++; }
                    if (col + 1 < GridSize) { neighborTotal += _frames[step - 1, row, col + 1]; neighborCount++; }
                    var average = neighborTotal / neighborCount;
                    _frames[step, row, col] = 0.65 * _frames[step - 1, row, col] + 0.35 * average;
                }
            }
        }
    }

    internal double HeightAt(int step, int row, int col)
    {
        var safeStep = Math.Clamp(step, 0, MaxSteps);
        var safeRow = Math.Clamp(row, 0, GridSize - 1);
        var safeCol = Math.Clamp(col, 0, GridSize - 1);
        return _frames[safeStep, safeRow, safeCol];
    }
}
