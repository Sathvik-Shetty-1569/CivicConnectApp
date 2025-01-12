package civicconnect.apcoders.in;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;

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


        BarChart barChart = findViewById(R.id.lineChart); // Ensure this is a BarChart in your XML layout

        PieDataSet dataSet = new PieDataSet(entries, "Reports Insights");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setText("Reports Insights");
        pieChart.animateY(1500);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 3));
        barEntries.add(new BarEntry(2, 2));

// Create a dataset for the bar chart
        BarDataSet barDataSet = new BarDataSet(barEntries, "reports As Pee date");
        barDataSet.setColor(Color.BLUE); // Set the color for the bars
        barDataSet.setValueTextColor(Color.BLACK); // Set the color for the values displayed on the bars

// Create BarData object and set it to the chart
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

// Customize the X-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Position the X-axis at the bottom
        xAxis.setDrawGridLines(false); // Disable grid lines for a cleaner look
        xAxis.setGranularity(1f); // Ensure consistent intervals between the bars

// Optional: Add labels to the X-axis
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int intValue = (int) value;
                if (intValue == 1) {
                    return " " + new Date(2025, 0, 11).toLocaleString().substring(0, 6); // Custom labels for X-axis
                } else {
                    return " " + new Date(2025, 0, 12).toLocaleString().substring(0, 6);
                }
            }
        });

// Additional BarChart Customizations
        barChart.setTouchEnabled(true); // Enable touch gestures
        barChart.setPinchZoom(true); // Allow zooming in/out
        barChart.setFitBars(true); // Make bars fit within the chart view
        barChart.getDescription().setEnabled(false); // Disable the description label
        barChart.animateY(1000); // Add animation for better UX

// Refresh the chart
        barChart.invalidate();
    }

}