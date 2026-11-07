# Model and Limitations

## What is shown

The Windows app displays a 10 × 10 height field. Its initial ring-like relief includes a small ripple. Sixty later frames are precomputed using four-direction neighbor averaging.

For a grid point \((i,j)\), the next height is a blend of its current value and the average of available neighbors:

\[
h_{t+1}(i,j)=0.65h_t(i,j)+0.35\,\operatorname{avg}(N_4(i,j)).
\]

Only in-grid neighbors are included at the boundary. The slider selects a frame, and the timer plays the frames in order.

## What the app does not claim

- It does not numerically solve the Ricci-flow equation or another geometric PDE.
- It does not compute a metric tensor, Ricci tensor, or Gaussian curvature.
- It does not model a manifold or establish a change in topological genus.
- It does not simulate or control actuators, drivers, shift registers, sensors, power supplies, or a physical 100-motor surface.
- It has no physical calibration, scientific boundary-condition analysis, convergence study, or comparison with analytic solutions.

“Tangible Ricci Flow Surface” is the inspiration for this educational visualization, not a claim that it is a complete solver or physical device.
