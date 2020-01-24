### PieChart

An android Library to load data in piechart
Dependency
   ` implementation  'android.piechart:piechart:1.0.3'`
   
   ## Samples
   <img src="https://user-images.githubusercontent.com/19813264/73049990-592e8100-3ea4-11ea-97bd-5a21a680af36.png" alt="drawing" width="200"/>
   
## Sample Code
Add piechart to XML files
```xml
<android.lib.chart.PieChart
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:id="@+id/chart"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

Add Java/kotlin code
```java
float d[]={10.0f,30f,55.0f,5.0f};
        String n[]={"One","Two","Three","Four"};
        int colors[]={Color.GREEN,Color.RED,Color.MAGENTA,Color.BLUE};
        PieChart pieChart = findViewById(R.id.chart);
        pieChart.setMapValues(d);
        pieChart.setMapValueName(n);
        pieChart.show();
```




