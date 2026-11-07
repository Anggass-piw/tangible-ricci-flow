using System.Drawing;
using System.Windows.Forms;

namespace RicciSurfaceLab;

internal sealed class MainForm : Form
{
    private static readonly Color Ink = Color.FromArgb(24, 37, 58);
    private static readonly Color Muted = Color.FromArgb(94, 109, 130);
    private static readonly Color Accent = Color.FromArgb(15, 118, 110);
    private static readonly Color Page = Color.FromArgb(245, 247, 250);

    private readonly SurfaceModel _model = new();
    private readonly SurfaceView _surface;
    private readonly TrackBar _stepSlider;
    private readonly Label _stepLabel;
    private readonly Button _playButton;
    private readonly System.Windows.Forms.Timer _timer;
    private int _currentStep;
    private bool _running;

    internal MainForm()
    {
        Text = "Ricci Surface Lab";
        StartPosition = FormStartPosition.CenterScreen;
        ClientSize = new Size(1240, 820);
        MinimumSize = new Size(1000, 700);
        BackColor = Page;
        Font = new Font("Segoe UI", 10f, FontStyle.Regular);
        AutoScaleMode = AutoScaleMode.Dpi;

        _surface = new SurfaceView(_model) { Dock = DockStyle.Fill };
        _stepLabel = MakeLabel("Discrete steps: 0 of 60", 10f, Muted, false);
        _playButton = MakeButton("Start simulation", primary: true);
        _stepSlider = new TrackBar
        {
            Minimum = 0,
            Maximum = SurfaceModel.MaxSteps,
            TickFrequency = 10,
            SmallChange = 1,
            LargeChange = 5,
            Value = 0,
            Dock = DockStyle.Fill,
            TickStyle = TickStyle.BottomRight
        };
        _timer = new System.Windows.Forms.Timer { Interval = 125 };

        var root = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            ColumnCount = 1,
            RowCount = 3,
            Padding = new Padding(28, 22, 28, 20),
            BackColor = Page
        };
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 112));
        root.RowStyles.Add(new RowStyle(SizeType.Percent, 100));
        root.RowStyles.Add(new RowStyle(SizeType.Absolute, 66));
        root.Controls.Add(BuildHeader(), 0, 0);
        root.Controls.Add(BuildBody(), 0, 1);
        root.Controls.Add(BuildDisclaimer(), 0, 2);
        Controls.Add(root);

        _stepSlider.ValueChanged += (_, _) => UpdateStep(_stepSlider.Value);
        _stepSlider.MouseDown += (_, _) => Pause();
        _playButton.Click += (_, _) => TogglePlayback();
        _timer.Tick += (_, _) => AdvanceOneStep();
        FormClosed += (_, _) => _timer.Dispose();
    }

    private Control BuildHeader()
    {
        var header = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            ColumnCount = 1,
            RowCount = 3,
            Margin = Padding.Empty,
            BackColor = Page
        };
        header.RowStyles.Add(new RowStyle(SizeType.Absolute, 22));
        header.RowStyles.Add(new RowStyle(SizeType.Absolute, 42));
        header.RowStyles.Add(new RowStyle(SizeType.Percent, 100));

        var eyebrow = MakeLabel("DIGITAL GEOMETRY EDUCATION", 9f, Accent, true);
        var title = MakeLabel("Tangible Ricci Flow Surface", 25f, Ink, true);
        var subtitle = MakeLabel(
            "Explore how a grid surface changes as the height of each point gradually smooths.",
            10.5f, Muted, false);
        header.Controls.Add(eyebrow, 0, 0);
        header.Controls.Add(title, 0, 1);
        header.Controls.Add(subtitle, 0, 2);
        return header;
    }

    private Control BuildBody()
    {
        var body = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            ColumnCount = 2,
            RowCount = 1,
            Margin = Padding.Empty,
            BackColor = Page
        };
        body.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 64f));
        body.ColumnStyles.Add(new ColumnStyle(SizeType.Percent, 36f));

        var surfaceCard = new Panel
        {
            Dock = DockStyle.Fill,
            BackColor = Color.FromArgb(12, 22, 38),
            Margin = new Padding(0, 0, 16, 0)
        };
        surfaceCard.Controls.Add(_surface);
        body.Controls.Add(surfaceCard, 0, 0);

        var controlsCard = new Panel
        {
            Dock = DockStyle.Fill,
            BackColor = Color.White,
            Padding = new Padding(22),
            Margin = new Padding(0)
        };
        controlsCard.Controls.Add(BuildControls());
        body.Controls.Add(controlsCard, 1, 0);
        return body;
    }

    private Control BuildControls()
    {
        var layout = new TableLayoutPanel
        {
            Dock = DockStyle.Fill,
            ColumnCount = 1,
            RowCount = 9,
            Margin = Padding.Empty,
            BackColor = Color.White
        };
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 40));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 66));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 28));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 66));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 58));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 34));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 108));
        layout.RowStyles.Add(new RowStyle(SizeType.Absolute, 30));
        layout.RowStyles.Add(new RowStyle(SizeType.Percent, 100));

        layout.Controls.Add(MakeLabel("Simulation controls", 16f, Ink, true), 0, 0);
        layout.Controls.Add(MakeLabel(
            "Use the slider to inspect a step, or play the smoothing animation.",
            10f, Muted, false), 0, 1);
        _stepLabel.Text = "Discrete steps: 0 of " + SurfaceModel.MaxSteps;
        layout.Controls.Add(_stepLabel, 0, 2);
        layout.Controls.Add(_stepSlider, 0, 3);

        var buttonRow = new FlowLayoutPanel
        {
            Dock = DockStyle.Fill,
            FlowDirection = FlowDirection.LeftToRight,
            WrapContents = false,
            Margin = Padding.Empty,
            Padding = Padding.Empty,
            BackColor = Color.White
        };
        _playButton.Width = 174;
        _playButton.Height = 46;
        var resetButton = MakeButton("Reset", primary: false);
        resetButton.Width = 92;
        resetButton.Height = 46;
        resetButton.Click += (_, _) => ResetSimulation();
        buttonRow.Controls.Add(_playButton);
        buttonRow.Controls.Add(resetButton);
        layout.Controls.Add(buttonRow, 0, 4);

        layout.Controls.Add(MakeLabel("What are you seeing?", 13f, Ink, true), 0, 5);
        layout.Controls.Add(MakeLabel(
            "At each step, every grid point moves toward the local average of its neighbors. " +
            "The animation shows a simple discrete smoothing process.",
            10f, Muted, false), 0, 6);
        layout.Controls.Add(MakeLabel("Model note", 11f, Color.FromArgb(130, 83, 17), true), 0, 7);
        layout.Controls.Add(MakeLabel(
            "This is a discrete educational height-field visualization, not a complete Ricci-flow solver. " +
            "It does not model a physical surface or control 100 motors.",
            9.5f, Color.FromArgb(112, 91, 55), false), 0, 8);
        return layout;
    }

    private Control BuildDisclaimer()
    {
        var note = new Panel
        {
            Dock = DockStyle.Fill,
            BackColor = Color.FromArgb(255, 247, 225),
            Padding = new Padding(14, 9, 14, 7),
            Margin = new Padding(0, 12, 0, 0)
        };
        note.Controls.Add(MakeLabel(
            "Educational discrete visualization only — not a full Ricci-flow solver or a physical 100-motor surface.",
            9.5f, Color.FromArgb(112, 91, 55), false));
        return note;
    }

    private void TogglePlayback()
    {
        if (_running)
        {
            Pause();
            return;
        }
        if (_currentStep >= SurfaceModel.MaxSteps) _stepSlider.Value = 0;
        _running = true;
        _playButton.Text = "Pause";
        _timer.Start();
    }

    private void AdvanceOneStep()
    {
        if (_currentStep >= SurfaceModel.MaxSteps)
        {
            Pause();
            return;
        }
        _stepSlider.Value = _currentStep + 1;
    }

    private void UpdateStep(int step)
    {
        _currentStep = Math.Clamp(step, 0, SurfaceModel.MaxSteps);
        _stepLabel.Text = $"Discrete steps: {_currentStep} of {SurfaceModel.MaxSteps}";
        _surface.SetStep(_currentStep);
        if (_currentStep >= SurfaceModel.MaxSteps && _running) Pause();
    }

    private void Pause()
    {
        _running = false;
        _timer.Stop();
        _playButton.Text = "Start simulation";
    }

    private void ResetSimulation()
    {
        Pause();
        _stepSlider.Value = 0;
    }

    private static Label MakeLabel(string text, float size, Color color, bool bold)
    {
        return new Label
        {
            Text = text,
            Dock = DockStyle.Fill,
            ForeColor = color,
            Font = new Font("Segoe UI", size, bold ? FontStyle.Bold : FontStyle.Regular),
            TextAlign = ContentAlignment.MiddleLeft,
            AutoEllipsis = true,
            UseMnemonic = false
        };
    }

    private static Button MakeButton(string text, bool primary)
    {
        var button = new Button
        {
            Text = text,
            FlatStyle = FlatStyle.Flat,
            BackColor = primary ? Accent : Color.FromArgb(232, 238, 244),
            ForeColor = primary ? Color.White : Ink,
            Font = new Font("Segoe UI Semibold", 10f, FontStyle.Bold),
            UseVisualStyleBackColor = false,
            Margin = new Padding(0, 0, 10, 0),
            Cursor = Cursors.Hand
        };
        button.FlatAppearance.BorderSize = 0;
        return button;
    }
}
