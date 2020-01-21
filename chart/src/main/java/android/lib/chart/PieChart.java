package android.lib.chart;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;

public class PieChart extends View {

    private boolean isReadyToDraw=false;

    private int CHART_DEGREE=360;

    private List<HashMap<String,Integer>> data;

    private float mapValues[];

    private String mapValueName[];

    private int colors[];

    private float animationAngles[];

    private int labelColor;

    private String label;

    private int arcWidth=50;

    private int textSize=50;

    private float density;

    private Random randomColor;

    private int arcRadius;

    private Paint chartPaint;

    private Paint chartTextPaint;

    private RectF overallRectangle = new RectF();

    private Rect textRect = new Rect();

    private List<Float> chartDegreeData= new ArrayList<>();

    private String chartDivisionLabel;

    private int textColor= Color.BLACK;

    private PropertyValuesHolder animationValueHolder[];

    public PieChart(Context context) {
        super(context);

    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Log.i("PieChart","left "+left+" right "+right+" top "+top+" bt "+bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Log.i("PieChart","wi "+w+"ht "+h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        final int min = Math.min(width, height);

        Log.i("PieChart","wi "+ MeasureSpec.getSize( widthMeasureSpec )+" ht "+heightMeasureSpec+" su wi "+getSuggestedMinimumWidth()+" su ht "+getSuggestedMinimumHeight());
        Log.i("PieChart","================>width : "+width+" height : "+height+" padding "+getPaddingLeft());

        int arcDiameter = min - getPaddingLeft()-arcWidth*2;

        arcRadius = arcDiameter / 2;

        int top = height / 2 - (arcDiameter / 2);
        int left = width / 2 - (arcDiameter / 2);
        int right = (left + arcDiameter);
        int bottom = top + arcDiameter;

        overallRectangle.set(left, top, right, bottom);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void convertValuesToDegree() throws NullPointerException
    {
        float total=0;
        for (float value : mapValues)
        {

            total= total+value;

        }
        Log.i("PieChart","total "+total);

        float degree=0;
        for (float i : mapValues)
        {
            float t = total/i;
            float x = CHART_DEGREE/t;
            degree=degree+x;
            Log.i("PieChart","t: "+t+"perc "+degree);
            chartDegreeData.add(degree);

        }

        initAnimation();
    }

    private void initDrawingComponents()
    {
        chartTextPaint = new Paint();
        chartTextPaint.setColor(textColor);
        chartTextPaint.setAntiAlias(true);
        chartTextPaint.setStyle(Paint.Style.FILL);
        chartTextPaint.setTextSize(textSize);


        chartPaint = new Paint();
        chartPaint.setAntiAlias(true);
        chartPaint.setStyle(Paint.Style.STROKE);
        chartPaint.setStrokeWidth(arcWidth);
        chartPaint.setColor(textColor);



    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isReadyToDraw)
        {
            drawArc(canvas);

            writeChartNames(canvas);

            //canvas.drawCircle(overallRectangle.centerX(),overallRectangle.centerY(),arcRadius,chartTextPaint);
        }

    }

    private int angle,angle2;

    ValueAnimator animator;
    private void initAnimation()
    {
        Log.i("PieChart","animation started"+chartDegreeData.size());


        animator =  ValueAnimator.ofInt(0,250);





        for (int i =0;i<chartDegreeData.size();i++)
        {

            if (i==0)
            {
                animationValueHolder[i] = PropertyValuesHolder.ofFloat(String.valueOf(i), 0, chartDegreeData.get(i));

            }else
            {
                animationValueHolder[i] = PropertyValuesHolder.ofFloat(String.valueOf(i), chartDegreeData.get(i-1), chartDegreeData.get(i));
                Log.i("PieChart","set data "+animationValueHolder[i].getPropertyName());
            }
        }
        animator.setValues(animationValueHolder);
        if (!animator.isRunning())
        {

            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {



                    for (int i=0;i<animation.getValues().length;i++)
                    {

                        animationAngles[i]= (float) animation.getAnimatedValue(String.valueOf(i));
                    }

                    invalidate();

                }
            });
            animator.start();
        }

    }

    private void drawArc(Canvas canvas)
    {

        for (int i=0;i<chartDegreeData.size();i++)
        {
            chartPaint.setColor(colors[i]);
            if (i==0)
            {

              //  canvas.drawArc(overallRectangle,0, angle, false, chartPaint);
                canvas.drawArc(overallRectangle,0, animationAngles[0], false, chartPaint);
            }else
            {

                //float sweepAngle =chartDegreeData.get(i)-chartDegreeData.get(i-1);
                float sweepAngle =animationAngles[i]-animationAngles[i-1];
                //canvas.drawArc(overallRectangle,angle,angle2 , false,chartPaint);
                canvas.drawArc(overallRectangle,animationAngles[i-1],sweepAngle , false,chartPaint);
                Log.i("PieChart","Angle : "+sweepAngle);
            }

        }
    }

    private int getRandomColor()
    {
        int color = Color.argb(255, randomColor.nextInt(256), randomColor.nextInt(256), randomColor.nextInt(256));
        return color;
    }

    private void writeChartNames(Canvas canvas)
    {
            writeNamesInCenter(canvas);
    }



    private void writeNamesInCenter(Canvas canvas)
    {
        TextPaint paint = new TextPaint();
        paint.setColor(getRandomColor());
        paint.setTextSize(textSize);

        StaticLayout.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && label != null) {

            builder = StaticLayout.Builder.obtain(label, 0,label.length(), paint, (int)(arcRadius*density-arcWidth));
            builder.setAlignment(Layout.Alignment.ALIGN_CENTER);
            StaticLayout myStaticLayout = builder.build();
            canvas.translate(overallRectangle.centerX()-(arcRadius-arcWidth/density),overallRectangle.centerY()-(textSize/2));
            canvas.save();
            myStaticLayout.draw(canvas);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int)event.getX();
        int y = (int)event.getY();

        double angle = Math.toDegrees(Math.atan2(y-overallRectangle.centerY() , x - overallRectangle.centerX()));
        double temp =  angle <= 0? ((CHART_DEGREE+angle)): angle;

        for (int i=0;i<chartDegreeData.size();i++)
        {
            if (chartDegreeData.get(i)>temp)
            {
                if (mapValueName != null && !mapValueName[i].isEmpty())
                {
                    label=mapValueName[i]+" : "+mapValues[i];
                }else
                {
                    label= String.valueOf(mapValues[i]);
                }

                Log.i("PieChart","position "+(i));
                invalidate();
                break;

            }
        }
        Log.i("PieChart","temp : "+temp+" angle "+angle);

      return super.onTouchEvent(event);
    }

    public void show()
    {

        isReadyToDraw=true;

        density = getResources().getDisplayMetrics().density;
        arcWidth = (int) (arcWidth * density);

//        mapValues=new float[]{100,150,80,35,99,23};
//
//        mapValueName=new String[]{"A","B","C","D","E","F"};



        randomColor = new Random();

        initDrawingComponents();

        //try {


            convertValuesToDegree();


        //}
//        catch (NullPointerException e)
//        {
//            Log.i("PieChart","error "+e);
//            throw new NullPointerException("Chart data cannot be empty. use setData() to initialize values");
//        }

        loadDefaultColors();

    }

    private void loadDefaultColors()
    {
        colors = new int[mapValues.length];
        int i=0;
        while (i<mapValues.length)
        {
            colors[i]= getRandomColor();
            i++;
        }

        labelColor = getRandomColor();
    }


    public boolean isReadyToDraw() {
        return isReadyToDraw;
    }


    public void setReadyToDraw(boolean readyToDraw) {
        isReadyToDraw = readyToDraw;
    }

    public List<HashMap<String, Integer>> getData() {
        return data;
    }

    public void setData(List<HashMap<String, Integer>> data) {
        this.data = data;
    }

    public float[] getMapValues() {
        return mapValues;
    }

    public void setMapValues(float[] mapValues) {
        this.mapValues = mapValues;

        animationAngles = new float[mapValues.length];

        animationValueHolder = new PropertyValuesHolder[mapValues.length];
    }

    public String[] getMapValueName() {
        return mapValueName;
    }

    public void setMapValueName(String[] mapValueName) {
        this.mapValueName = mapValueName;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getArcRadius() {
        return arcRadius;
    }

    public void setArcRadius(int arcRadius) {
        this.arcRadius = arcRadius;
    }
}
