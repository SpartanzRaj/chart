package raj.apps.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.lib.chart.PieChart;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float d[]={10.0f,30f,55.0f,5.0f};
        String n[]={"One","Two","Three","Four"};
        int colors[]={Color.GREEN,Color.RED,Color.MAGENTA,Color.BLUE};
        PieChart pieChart = findViewById(R.id.chart);
        pieChart.setMapValues(d);
        pieChart.setMapValueName(n);
        pieChart.show();


    }
}
