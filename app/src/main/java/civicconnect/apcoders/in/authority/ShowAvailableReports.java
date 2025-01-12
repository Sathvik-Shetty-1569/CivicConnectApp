package civicconnect.apcoders.in.authority;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;

import civicconnect.apcoders.in.Adapters.ShowProblemRecyclerAdapterAuthority;
import civicconnect.apcoders.in.Adapters.ShowProblemRecyclerViewAdapter;
import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.Utils.ProblemManagement;
import civicconnect.apcoders.in.models.ProblemModel;

public class ShowAvailableReports extends AppCompatActivity {

    RecyclerView AvailableReportsRecyclerView;
    ArrayList<ProblemModel> problemDataList = new ArrayList<>();
    TextView noDataFoundText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_available_reports);
        setBackBtn();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);
        noDataFoundText = findViewById(R.id.noDataFoundText);
        noDataFoundText.setVisibility(View.GONE);

        AvailableReportsRecyclerView = findViewById(R.id.AvailableReportsRecyclerView);
        AvailableReportsRecyclerView.setHasFixedSize(true);
        AvailableReportsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ProblemManagement.FetchProblemsByStatus("Reported", new ProblemManagement.GetAllProblems() {
            @Override
            public void GetFetchData(ArrayList<ProblemModel> ProblemDataList) {
                if (ProblemDataList.size() == 0) {
                    noDataFoundText.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    AvailableReportsRecyclerView.setVisibility(View.GONE);
                } else {
                    AvailableReportsRecyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    noDataFoundText.setVisibility(View.GONE);
                    problemDataList = ProblemDataList;
                    AvailableReportsRecyclerView.setAdapter(new ShowProblemRecyclerAdapterAuthority(ShowAvailableReports.this, problemDataList));
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setBackBtn() {
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(ShowAvailableReports.this, DashboardActivity.class));
            finish();
        });
    }
}