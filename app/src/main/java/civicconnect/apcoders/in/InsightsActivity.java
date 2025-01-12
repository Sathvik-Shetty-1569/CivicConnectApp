package civicconnect.apcoders.in;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class InsightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insights);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setInsightsData();
    }

    private void setInsightsData() {
        PieChart pieChart = findViewById(R.id.pieChart);

        // Data points for the chart
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40, "Solved"));
        entries.add(new PieEntry(40, "Pending"));
        entries.add(new PieEntry(20, "UnSolved"));


        PieDataSet dataSet = new PieDataSet(entries, "Reports Insights");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setText("Reports Insights");
        pieChart.animateY(1500);

        LineChart lineChart = findViewById(R.id.lineChart);

//         Data points for the chart
        ArrayList<Entry> chartentries = new ArrayList<>();
        chartentries.add(new Entry(0, 1));
        chartentries.add(new Entry(1, 3));
        chartentries.add(new Entry(2, 2));
        chartentries.add(new Entry(3, 5));

        LineDataSet chartdataSet = new LineDataSet(chartentries, "Sample Data");
        chartdataSet.setColor(Color.BLUE);
        chartdataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(chartdataSet);
        lineChart.setData(lineData);

        // Customize chart appearance
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        lineChart.getDescription().setText("Line Chart Example");
        lineChart.animateX(1500);
    }

}