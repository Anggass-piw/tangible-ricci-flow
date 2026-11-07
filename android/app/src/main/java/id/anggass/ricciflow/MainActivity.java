package id.anggass.ricciflow;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private static final int INK = Color.rgb(24, 37, 58);
    private static final int MUTED = Color.rgb(94, 109, 130);
    private static final int ACCENT = Color.rgb(15, 118, 110);
    private static final int PAGE = Color.rgb(245, 247, 250);

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final SurfaceModel model = new SurfaceModel();
    private SurfaceMeshView surfaceView;
    private SeekBar stepSeekBar;
    private TextView stepLabel;
    private Button playButton;
    private int currentStep = 0;
    private boolean running = false;

    private final Runnable animationTick = new Runnable() {
        @Override
        public void run() {
            if (!running) {
                return;
            }
            if (currentStep >= SurfaceModel.MAX_STEPS) {
                stopAnimation();
                return;
            }
            stepSeekBar.setProgress(currentStep + 1);
            handler.postDelayed(this, 125L);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(PAGE);
        getWindow().setNavigationBarColor(PAGE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        buildInterface();
    }

    private void buildInterface() {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(PAGE);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(22), dp(20), dp(28));
        scrollView.addView(content, new ScrollView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView eyebrow = text("DIGITAL GEOMETRY EDUCATION", 11, ACCENT, true);
        eyebrow.setLetterSpacing(0.08f);
        content.addView(eyebrow);

        TextView title = text("Tangible Ricci Flow Surface", 25, INK, true);
        title.setPadding(0, dp(5), 0, 0);
        content.addView(title);

        TextView intro = text(
                "Explore how a grid surface changes as the height of each point gradually smooths.",
                15, MUTED, false);
        intro.setPadding(0, dp(7), 0, dp(14));
        content.addView(intro);

        TextView badge = text("DISCRETE EDUCATIONAL VISUALIZATION  •  GRID 10 × 10", 11, ACCENT, true);
        badge.setPadding(dp(11), dp(8), dp(11), dp(8));
        badge.setBackground(rounded(Color.rgb(220, 243, 239), 22));
        content.addView(badge, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        FrameLayout visualCard = new FrameLayout(this);
        visualCard.setBackground(rounded(Color.rgb(12, 22, 38), 20));
        visualCard.setClipToOutline(true);
        LinearLayout.LayoutParams visualParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(285));
        visualParams.topMargin = dp(14);
        visualCard.setLayoutParams(visualParams);

        surfaceView = new SurfaceMeshView(this, model);
        visualCard.addView(surfaceView, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TextView visualCaption = text("INITIAL SURFACE  ·  100 GRID POINTS", 10,
                Color.rgb(190, 209, 230), true);
        visualCaption.setLetterSpacing(0.04f);
        visualCaption.setPadding(dp(13), dp(12), dp(13), dp(8));
        FrameLayout.LayoutParams captionParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.START);
        visualCard.addView(visualCaption, captionParams);
        content.addView(visualCard, visualParams);

        LinearLayout controls = card();
        LinearLayout.LayoutParams controlParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        controlParams.topMargin = dp(16);
        content.addView(controls, controlParams);

        TextView controlTitle = text("Simulation controls", 17, INK, true);
        controls.addView(controlTitle);

        stepLabel = text("Discrete steps: 0 of " + SurfaceModel.MAX_STEPS, 13, MUTED, false);
        stepLabel.setPadding(0, dp(11), 0, dp(2));
        controls.addView(stepLabel);

        stepSeekBar = new SeekBar(this);
        stepSeekBar.setMax(SurfaceModel.MAX_STEPS);
        stepSeekBar.setProgressTintList(android.content.res.ColorStateList.valueOf(ACCENT));
        stepSeekBar.setThumbTintList(android.content.res.ColorStateList.valueOf(ACCENT));
        controls.addView(stepSeekBar, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(42)));
        stepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentStep = progress;
                stepLabel.setText("Discrete steps: " + currentStep + " of " + SurfaceModel.MAX_STEPS);
                surfaceView.setStep(currentStep);
                if (currentStep >= SurfaceModel.MAX_STEPS && running) {
                    stopAnimation();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                stopAnimation();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams buttonRowParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(48));
        buttonRowParams.topMargin = dp(8);
        controls.addView(buttons, buttonRowParams);

        playButton = button("Start simulation", ACCENT, Color.WHITE);
        buttons.addView(playButton, new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        View buttonGap = new View(this);
        buttons.addView(buttonGap, new LinearLayout.LayoutParams(dp(10), 1));

        Button resetButton = button("Reset", Color.rgb(232, 238, 244), INK);
        buttons.addView(resetButton, new LinearLayout.LayoutParams(dp(104),
                ViewGroup.LayoutParams.MATCH_PARENT));

        playButton.setOnClickListener(view -> {
            if (running) {
                stopAnimation();
                return;
            }
            if (currentStep >= SurfaceModel.MAX_STEPS) {
                stepSeekBar.setProgress(0);
            }
            running = true;
            playButton.setText("Pause");
            handler.postDelayed(animationTick, 80L);
        });
        resetButton.setOnClickListener(view -> {
            stopAnimation();
            stepSeekBar.setProgress(0);
        });

        TextView explanationTitle = text("What are you seeing?", 17, INK, true);
        explanationTitle.setPadding(0, dp(21), 0, dp(6));
        content.addView(explanationTitle);
        TextView explanation = text(
                "At each step, every grid point moves toward the local average of its neighbors. "
                        + "Drag the step control or press Start to explore the change from the initial surface.",
                14, MUTED, false);
        content.addView(explanation);

        LinearLayout note = card();
        note.setBackground(rounded(Color.rgb(255, 247, 225), 15));
        LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        noteParams.topMargin = dp(15);
        content.addView(note, noteParams);
        TextView noteTitle = text("Model note", 14, Color.rgb(130, 83, 17), true);
        note.addView(noteTitle);
        TextView noteBody = text(
                "This is a discrete educational height-field visualization, not a complete Ricci-flow solver. "
                        + "It does not control a physical surface or 100 motors.",
                13, Color.rgb(112, 91, 55), false);
        noteBody.setPadding(0, dp(5), 0, 0);
        note.addView(noteBody);

        setContentView(scrollView);
    }

    private void stopAnimation() {
        running = false;
        handler.removeCallbacks(animationTick);
        if (playButton != null) {
            playButton.setText("Start simulation");
        }
    }

    private TextView text(String value, float sizeSp, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
        view.setTextColor(color);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setTypeface(Typeface.create("sans-serif", bold ? Typeface.BOLD : Typeface.NORMAL));
        return view;
    }

    private Button button(String label, int backgroundColor, int textColor) {
        Button view = new Button(this);
        view.setText(label);
        view.setTextColor(textColor);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        view.setAllCaps(false);
        view.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
        view.setPadding(dp(10), 0, dp(10), 0);
        view.setBackground(rounded(backgroundColor, 14));
        return view;
    }

    private LinearLayout card() {
        LinearLayout view = new LinearLayout(this);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setPadding(dp(16), dp(15), dp(16), dp(15));
        view.setBackground(rounded(Color.WHITE, 18));
        return view;
    }

    private GradientDrawable rounded(int color, int radiusDp) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(dp(radiusDp));
        return drawable;
    }

    private int dp(float value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onStop() {
        stopAnimation();
        super.onStop();
    }
}
