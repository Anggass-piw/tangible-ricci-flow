using RicciSurfaceLab;

var model = new SurfaceModel();
var changed = false;
for (var row = 0; row < SurfaceModel.GridSize; row++)
{
    for (var col = 0; col < SurfaceModel.GridSize; col++)
    {
        var initial = model.HeightAt(0, row, col);
        var later = model.HeightAt(SurfaceModel.MaxSteps, row, col);
        if (!double.IsFinite(initial) || !double.IsFinite(later))
            throw new InvalidOperationException("A grid value is not finite.");
        if (Math.Abs(initial - later) > 1e-6) changed = true;
    }
}

if (!changed) throw new InvalidOperationException("The surface did not evolve.");
if (!double.IsFinite(model.HeightAt(-5, -2, 99)))
    throw new InvalidOperationException("Index clamping failed.");

Console.WriteLine("PASS: all 100 grid points are finite, the surface evolves, and indices are clamped.");
