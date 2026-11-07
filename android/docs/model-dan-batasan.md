# Model and Limitations

## What the app models

The app creates a 10 × 10 height field. Each grid point stores one height value. The initial surface is a ring-like relief with a small ripple. Sixty later frames are precomputed using four-direction neighbor averaging.

For a grid point \((i,j)\), the next height is a blend of the current height and the average of its available neighbors:

\[
h_{t+1}(i,j)=0.65h_t(i,j)+0.35\,\operatorname{avg}(N_4(i,j)).
\]

At the grid edges, the average uses only neighbors inside the grid. The slider selects a precomputed frame; the play control advances through frames in order. The renderer projects those points to an angled mesh on screen.

## Why this is a discrete educational visualization

The original idea describes a tangible surface whose pillars illustrate changes in geometry. This app translates that idea into a screen visualization and a simple smoothing rule. The 10 × 10 grid is a visual sample, not a physical actuator simulation.

## What it does not do

- It does not solve the Ricci-flow equation or a geometric PDE numerically.
- It does not calculate a metric tensor, Ricci tensor, or Gaussian curvature.
- It does not fully model a manifold or topology; the relief is not evidence of a topological hole.
- It does not simulate motors, drivers, a power supply, multiplexing, sensors, or hardware communication.
- It does not provide physical units, scientific boundary conditions, convergence analysis, or validation against an analytic solution.

“Tangible Ricci Flow Surface” names the concept that inspired the app; it is not a claim that this app is a Ricci-flow solver or a tangible device.
