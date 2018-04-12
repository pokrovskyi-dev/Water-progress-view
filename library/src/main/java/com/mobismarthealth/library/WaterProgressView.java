package com.mobismarthealth.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by sergey on 02.06.2017.
 */

public class WaterProgressView extends View implements View.OnClickListener{

    // DEFAULT VALUES
    private final static boolean DEFAULT_MAIN_ARC_ENABLE = true;
    private final static float DEFAULT_MAIN_ARC_START_ANGLE = 270.0f;
    private final static int DEFAULT_MAIN_ARC_PROGRESS = 50;
    private final static float DEFAULT_MAIN_ARC_WIDTH = 20;
    private final static int DEFAULT_MAIN_ARC_COLOR = Color.parseColor("#FF0000");
    private final static boolean DEFAULT_MAIN_ARC_BG_ENABLE = true;
    private final static float DEFAULT_MAIN_ARC_BG_WIDTH = 10;
    private final static int DEFAULT_MAIN_ARC_BG_COLOR = Color.parseColor("#ce93d8");

    private final static boolean DEFAULT_CENTER_TEXT_ENABLE = true;
    private final static float DEFAULT_CENTER_TEXT_SIZE = 16;
    private final static int DEFAULT_CENTER_TEXT_COLOR = Color.parseColor("#8C000000");

    private final static boolean DEFAULT_WAVE_ENABLE = true;
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.1f;
    private static final float DEFAULT_AMPLITUDE_VALUE = 50.0f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;
    private static final int DEFAULT_WAVE_PROGRESS_VALUE = 50;
    private static final int DEFAULT_WAVE_COLOR = Color.parseColor("#212121");
    private static final int DEFAULT_WAVE_BACKGROUND_COLOR = Color.parseColor("#00000000");

    // MIN and MAX values
    private final static float MAX_ANGLE = 360.0f;
    private final static float MIN_ANGLE = 0.0f;
    private final static float MAX_PROGRESS = 100;
    private final static float MIN_PROGRESS = 0;

    // Main Arc
    boolean mMainArcEnable;
    float mMainArcWidth;
    int mMainArcColor;
    float mMainArcStartAngle;
    float mMainArcProgress;
    boolean mMainArcBgEnable;
    int mMainArcBgColor;
    float mMainArcBgWidth;
    RectF mainArcRectF;
    Path mainArcPath;
    Path mainArcBgPath;

    // Center text
    float mCenterTextSize;
    int mCenterTextColor;
    boolean mCenterTextEnable;

    // Waves
    boolean mWaveEnable;
    private float mAmplitudeRatio;
    private int mWaveBgColor;
    private int mWaveColor;
    // Properties
    private float mDefaultWaterLevel;
    private float mWaterLevelRatio = 1f;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;
    private int mWaveProgressValue = DEFAULT_WAVE_PROGRESS_VALUE;
    // Object used to draw.
    // Shader containing repeated waves.
    private BitmapShader mWaveShader;
    private Bitmap bitmapBuffer;
    // Shader matrix.
    private Matrix mShaderMatrix;
    // Paint to draw wave.
    private Paint mWavePaint;
    //Paint to draw waveBackground.
    private Paint mWaveBgPaint;
    // Point to draw title.
    // Animation.
    private ObjectAnimator waveShiftAnim;
    private AnimatorSet mAnimatorSet;

    private Context mContext;

    // View size, for square shape
    int viewSize;

    // Text Rect
    private Rect textRect;

    // Main Arc Progress Paint
    private Paint mainArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG){
        {
            setDither(true);
            setStyle(Style.STROKE);
        }
    };
    // Main Arc BG Paint
    private Paint mainArcBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG){
        {
            setDither(true);
            setStyle(Style.STROKE);
        }
    };
    // Second Arc Progress Paint
    private Paint secondArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG){
        {
            setDither(true);
            setStyle(Style.STROKE);
        }
    };
    // Text Paint
    private Paint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG){
        {
            setDither(true);
            setTextAlign(Align.LEFT);
        }
    };

    public WaterProgressView(Context context){
        this(context, null);
        setOnClickListener(this);
        init();
    }

    public WaterProgressView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
        setOnClickListener(this);
        init();
    }

    public WaterProgressView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterProgressView);
        try {
            setMainArcEnable(typedArray.getBoolean(
                    R.styleable.WaterProgressView_wpv_main_arc_enable,
                    DEFAULT_MAIN_ARC_ENABLE));
            setMainArcStartAngle(typedArray.getInteger(
                    R.styleable.WaterProgressView_wpv_main_arc_start_angle,
                    (int) DEFAULT_MAIN_ARC_START_ANGLE));
            setMainArcProgress(typedArray.getInteger(
                    R.styleable.WaterProgressView_wpv_main_arc_progress,
                    DEFAULT_MAIN_ARC_PROGRESS));
            setMainArcWidth(typedArray.getFloat(
                    R.styleable.WaterProgressView_wpv_main_arc_width,
                    (int)DEFAULT_MAIN_ARC_WIDTH));
            setMainArcColor(typedArray.getColor(
                    R.styleable.WaterProgressView_wpv_main_arc_color,
                    DEFAULT_MAIN_ARC_COLOR));
            setMainArcBgEnable(typedArray.getBoolean(
                    R.styleable.WaterProgressView_wpv_main_arc_bg_enable,
                    DEFAULT_MAIN_ARC_BG_ENABLE));
            setMainArcBgWidth(typedArray.getFloat(
                    R.styleable.WaterProgressView_wpv_main_arc_bg_width,
                    (int)DEFAULT_MAIN_ARC_BG_WIDTH));
            setMainArcBgColor(typedArray.getColor(
                    R.styleable.WaterProgressView_wpv_main_arc_bg_color,
                    DEFAULT_MAIN_ARC_BG_COLOR));

            setCenterTextEnable(typedArray.getBoolean(
                    R.styleable.WaterProgressView_wpv_center_text_enable,
                    DEFAULT_CENTER_TEXT_ENABLE));
            setCenterTextSize(typedArray.getDimensionPixelSize(
                    R.styleable.WaterProgressView_wpv_center_text_size,
                    (int)DEFAULT_CENTER_TEXT_SIZE));
            setCenterTextColor(typedArray.getColor(
                    R.styleable.WaterProgressView_wpv_center_text_color,
                    DEFAULT_CENTER_TEXT_COLOR));

            setWaveEnable(typedArray.getBoolean(
                    R.styleable.WaterProgressView_wpv_wave_enable,
                    DEFAULT_WAVE_ENABLE));
            setWaveColor(typedArray.getColor(
                    R.styleable.WaterProgressView_wpv_wave_color,
                    DEFAULT_WAVE_COLOR));
        } finally {
            typedArray.recycle();
        }

        setOnClickListener(this);

        init();

        mContext = context;
    }

    private void init(){
        mainArcRectF = new RectF();
        mainArcPath = new Path();
        mainArcBgPath = new Path();

        textRect = new Rect();

        // Init Wave.
        mShaderMatrix = new Matrix();
        mWavePaint = new Paint();
        // The ANTI_ALIAS_FLAG bit AntiAliasing smooths out the edges of what is being drawn,
        // but is has no impact on the interior of the shape.
        mWavePaint.setAntiAlias(true);
        mWaveBgPaint = new Paint();
        mWaveBgPaint.setAntiAlias(true);
        // Init Animation
        initAnimation();

        // Init Wave
        mWaveBgColor = DEFAULT_WAVE_BACKGROUND_COLOR;
        mWaveBgPaint.setColor(mWaveBgColor);

        // Init AmplitudeRatio
        float amplitudeRatioAttr = DEFAULT_AMPLITUDE_VALUE / 1000;
        mAmplitudeRatio = (amplitudeRatioAttr > DEFAULT_AMPLITUDE_RATIO) ? DEFAULT_AMPLITUDE_RATIO : amplitudeRatioAttr;
    }

    // Main Arc

    public void setMainArcEnable(boolean mainArcEnable){
        mMainArcEnable = mainArcEnable;
    }

    public boolean getMainArcEnable(){
        return mMainArcEnable;
    }

    /**
     * Set up progress of Main(inner) circle
     * @param progress of Main(inner) circle. from 1 to 100
     */
    public void setMainArcProgress(@FloatRange(from = MIN_PROGRESS, to = MAX_PROGRESS) final float progress){
        mMainArcProgress = (int) Math.max(MIN_PROGRESS, Math.min(progress, MAX_PROGRESS));
        mMainArcProgress = mMainArcProgress * (float)3.6;
        setProgressValue((int)progress);
        postInvalidate();
    }

    public int getMainArcProgress(){
        return (int)(mMainArcProgress / 3.6);
    }

    public void setMainArcStartAngle(@FloatRange(from = MIN_ANGLE, to = MAX_ANGLE) float mainStartAngle){
        mMainArcStartAngle = Math.max(MIN_ANGLE, Math.min(mainStartAngle, MAX_ANGLE));
        postInvalidate();
    }

    public int getMainArcStartAngle(){
        return (int)mMainArcStartAngle;
    }

    public void setMainArcWidth(float mainArcWidth){
        mMainArcWidth = mainArcWidth;
        requestLayout();
    }

    public int getMainArcWidth(){
        return (int)mMainArcWidth;
    }

    public void setMainArcColor(int mainColor){
        mMainArcColor = mainColor;
    }

    public int getMainArcColor(){
        return mMainArcColor;
    }

    public void setMainArcBgEnable(boolean mainArcBgEnable){
        mMainArcBgEnable = mainArcBgEnable;
    }

    public boolean getMainArcBgEnable(){
        return mMainArcBgEnable;
    }

    public void setMainArcBgWidth(float mainArcBgWidth){
        mMainArcBgWidth = mainArcBgWidth;
        requestLayout();
    }

    public int getMainArcBgWidth(){
        return (int)mMainArcBgWidth;
    }

    public void setMainArcBgColor(int mainArcBgColor){
        mMainArcBgColor = mainArcBgColor;
    }

    public int getMainArcBgColor(){
        return mMainArcBgColor;
    }

    // Center text

    public void setCenterTextEnable(boolean centerTextEnable){
        mCenterTextEnable = centerTextEnable;
    }

    public boolean getCenterTextEnable(){
        return mCenterTextEnable;
    }

    public void setCenterTextSize(float centerTextSize){
        //mCenterTextSize = centerTextSize * getResources().getDisplayMetrics().scaledDensity;
        mCenterTextSize = centerTextSize;
        postInvalidate();
    }
    public float getCenterTextSize(){
        return mCenterTextSize;
    }

    public void setCenterTextColor(int centerTextColor){
        mCenterTextColor = centerTextColor;
    }
    public int getCenterTextColor(){
        return mCenterTextColor;
    }

    // Waves

    public void setWaveEnable(boolean waveEnable){
        mWaveEnable = waveEnable;
    }

    public boolean getWaveEnable(){
        return mWaveEnable;
    }

    public void setWaveColor(int color) {
        mWaveColor = color;
        updateWaveShader();
        invalidate();
    }

    public int getWaveColor() {
        return mWaveColor;
    }

    /**
     * Set vertical size of wave according to amplitudeRatio.
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     */
    public void setAmplitudeRatio(int amplitudeRatio) {
        if (mAmplitudeRatio != (float) amplitudeRatio / 1000) {
            mAmplitudeRatio = (float) amplitudeRatio / 1000;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Water level increases from 0 to the value of WaveView.
     *
     * @param progress Default to be 50.
     */
    public void setProgressValue(int progress) {
        mWaveProgressValue = progress;
        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(this, "waterLevelRatio", mWaterLevelRatio, ((float) mWaveProgressValue / 100));
        waterLevelAnim.setDuration(1000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        AnimatorSet animatorSetProgress = new AnimatorSet();
        animatorSetProgress.play(waterLevelAnim);
        if (isInEditMode()){
            return;
        }
        animatorSetProgress.start();
    }

    public int getProgressValue() {
        return mWaveProgressValue;
    }

    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        if (mWaveEnable){
            startAnimation();
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mWaveEnable){
            cancelAnimation();
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View v){
        //
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        // For square shape
        if (width > height){
            viewSize = height;
        } else {
            viewSize = width;
        }

        // Set main arc dimens
        mainArcRectF.set(
                mMainArcWidth,
                mMainArcWidth,
                viewSize - mMainArcWidth,
                viewSize - mMainArcWidth
        );

        setMeasuredDimension(viewSize, viewSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateWaveShader();
    }

    /**
     * Implement this to do your drawing
     * @param canvas -  the canvas on which the background will be drawn
     *               https://developer.android.com/reference/android/graphics/Canvas.html
     */
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.save();

        // Draw Wave.
        if (mWaveEnable){
            // Modify paint shader according to mShowWave state.
            if (mWaveShader != null){
                // First call after mShowWave, assign it to our paint.
                if (mWavePaint.getShader() == null) {
                    mWavePaint.setShader(mWaveShader);
                }

                // Sacle shader according to waveLengthRatio and amplitudeRatio.
                // This decides the size(waveLengthRatio for width, amplitudeRatio for height) of waves.
                mShaderMatrix.setScale(1, mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO, 0, mDefaultWaterLevel);
                // Translate shader according to waveShiftRatio and waterLevelRatio.
                // This decides the start position(waveShiftRatio for x, waterLevelRatio for y) of waves.
                mShaderMatrix.postTranslate(mWaveShiftRatio * getWidth(),
                        (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());

                // Assign matrix to invalidate the shader.
                mWaveShader.setLocalMatrix(mShaderMatrix);

                // viewSize - mMainArcWidth
                float radius = (viewSize / 2) - mMainArcWidth;
                // Draw background
                canvas.drawCircle(viewSize / 2f, viewSize / 2f, radius, mWaveBgPaint);
                canvas.drawCircle(viewSize / 2f, viewSize / 2f, radius, mWavePaint);


            } else {
                mWavePaint.setShader(null);
            }
        }

        // Draw Main progress
        if (mMainArcEnable){
            if (mMainArcBgEnable){
                mainArcBgPaint.setStrokeWidth(mMainArcBgWidth);
                mainArcBgPaint.setColor(mMainArcBgColor);
                mainArcBgPaint.setStrokeCap(Paint.Cap.ROUND);
                mainArcBgPaint.setStrokeJoin(Paint.Join.ROUND);
                mainArcBgPaint.setAlpha(255);
                mainArcBgPath.reset();
                mainArcBgPath.addArc(mainArcRectF, mMainArcStartAngle, 360);
                canvas.drawPath(mainArcBgPath, mainArcBgPaint);
            }
            mainArcProgressPaint.setStrokeWidth(mMainArcWidth);
            mainArcProgressPaint.setColor(mMainArcColor);
            mainArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);
            mainArcProgressPaint.setStrokeJoin(Paint.Join.ROUND);
            mainArcProgressPaint.setAlpha(255);
            mainArcPath.reset();
            mainArcPath.addArc(mainArcRectF, mMainArcStartAngle, mMainArcProgress);
            canvas.drawPath(mainArcPath, mainArcProgressPaint);
        }

        // Draw center text
        if (mCenterTextEnable){
            canvas.getClipBounds(textRect);
            String centerText = String.valueOf((int)(mMainArcProgress / 3.6));
            centerText += "%";
            // text size
            textPaint.setTextSize(mCenterTextSize);
            // text color
            textPaint.setColor(mCenterTextColor);
            // Getting values to center
            int height = textRect.height();
            int width = textRect.width();
            textPaint.setTextAlign(Paint.Align.LEFT);
            textPaint.getTextBounds(centerText, 0, centerText.length(), textRect);
            float xPos = height / 2f - textRect.width() / 2f - textRect.left;
            float yPos = width / 2f + textRect.height() / 2f - textRect.bottom;
            canvas.drawText(centerText, xPos, yPos, textPaint);
        }

        canvas.restore();
    }

    // Wave Animation

    private void updateWaveShader(){
        if (bitmapBuffer == null){
            //mMainArcWidth - mSecondArcWidth
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();

            if (width > 0 && height > 0) {
                double defaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / width;
                float defaultAmplitude = height * DEFAULT_AMPLITUDE_RATIO;
                mDefaultWaterLevel = height * DEFAULT_WATER_LEVEL_RATIO;
                float defaultWaveLength = width;

                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);

                Paint wavePaint = new Paint();
                wavePaint.setStrokeWidth(2);
                wavePaint.setAntiAlias(true);

                // Draw default waves into the bitmap.
                // y=Asin(ωx+φ)+h
                final int endX = width + 1;
                final int endY = height + 1;

                float[] waveY = new float[endX];

                wavePaint.setColor(adjustAlpha(mWaveColor, 0.3f));
                for (int beginX = 0; beginX < endX; beginX++) {
                    double wx = beginX * defaultAngularFrequency;
                    float beginY = (float) (mDefaultWaterLevel + defaultAmplitude * Math.sin(wx));
                    canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);
                    waveY[beginX] = beginY;
                }

                wavePaint.setColor(mWaveColor);
                final int wave2Shift = (int) (defaultWaveLength / 4);
                for (int beginX = 0; beginX < endX; beginX++) {
                    canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
                }

                // Use the bitamp to create the shader.
                mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
                this.mWavePaint.setShader(mWaveShader);
            }
        }
    }

    private void initAnimation() {
        // Wave waves infinitely.
        waveShiftAnim = ObjectAnimator.ofFloat(this, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.play(waveShiftAnim);
    }

    public void startAnimation() {
        if (mAnimatorSet != null && !isInEditMode()) {
            mAnimatorSet.start();
        }
    }

    public void endAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }

    public void cancelAnimation() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
    }

    public void pauseAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mAnimatorSet != null) {
                mAnimatorSet.pause();
            }
        }
    }

    public void resumeAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mAnimatorSet != null) {
                mAnimatorSet.resume();
            }
        }
    }

    /**
     * Sets the length of the animation. The default duration is 1000 milliseconds.
     *
     * @param duration The length of the animation, in milliseconds.
     */
    public void setAnimDuration(long duration) {
        waveShiftAnim.setDuration(duration);
    }

    /**
     * Transparent the given color by the factor
     * The more the factor closer to zero the more the color gets transparent
     *
     * @param color  The color to transparent
     * @param factor 1.0f to 0.0f
     * @return int - A transplanted color
     */
    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    /**
     * Paint.setTextSize(float textSize) default unit is px.
     *
     * @param spValue The real size of text
     * @return int - A transplanted sp
     */
    private int sp2px(float spValue) {
        final float fontScale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
