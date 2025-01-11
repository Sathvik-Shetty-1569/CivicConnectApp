package civicconnect.apcoders.in.authority;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

import civicconnect.apcoders.in.Adapters.ShowProblemRecyclerViewAdapter;
import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.models.ProblemModel;

public class ShowCompleteReports extends AppCompatActivity {
    RecyclerView CompleteReportsRecyclerView;
    ArrayList<ProblemModel> ProblemDataList = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_complete_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setBackBtn();

        CompleteReportsRecyclerView = findViewById(R.id.CompleteReportsRecyclerView);
        CompleteReportsRecyclerView.setHasFixedSize(true);
        CompleteReportsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ProblemDataList.add(new ProblemModel("Broken Street Lights", "Report broken street lights causing inconvenience and safety concerns in your area. Quick action is needed.", 99, "Pending", "1", "Atul Dubal", "", new GeoPoint(17.12, 13.33)));
        CompleteReportsRecyclerView.setAdapter(new ShowProblemRecyclerViewAdapter(this, ProblemDataList));

    }
    private void setBackBtn() {
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(ShowCompleteReports.this, DashboardActivity.class));
            finish();
        });
    }
}