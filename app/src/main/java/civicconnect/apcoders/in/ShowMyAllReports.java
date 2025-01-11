package civicconnect.apcoders.in;

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

import civicconnect.apcoders.in.Adapters.ShowProblemRecyclerViewAdapter;
import civicconnect.apcoders.in.Utils.ProblemManagement;
import civicconnect.apcoders.in.models.ProblemModel;

public class ShowMyAllReports extends AppCompatActivity {

    RecyclerView MyReportsReportsRecyclerView;
    ArrayList<ProblemModel> problemDataList = new ArrayList<>();
    TextView noDataFoundText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_my_all_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setBackBtn();

        noDataFoundText = findViewById(R.id.noDataFoundText);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        MyReportsReportsRecyclerView = findViewById(R.id.MyReportsReportsRecyclerView);
        MyReportsReportsRecyclerView.setHasFixedSize(true);
        MyReportsReportsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ProblemManagement.FetchCurrentUserProblems(new ProblemManagement.GetAllProblems() {
            @Override
            public void GetFetchData(ArrayList<ProblemModel> ProblemDataList) {
                if (ProblemDataList.size() == 0) {
                    MyReportsReportsRecyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    noDataFoundText.setVisibility(View.VISIBLE);
                } else {
                    MyReportsReportsRecyclerView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    problemDataList = ProblemDataList;
                    noDataFoundText.setVisibility(View.GONE);
                    MyReportsReportsRecyclerView.setAdapter(new ShowProblemRecyclerViewAdapter(ShowMyAllReports.this, problemDataList));
                }
            }
        });
    }

    private void setBackBtn() {
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(ShowMyAllReports.this, MainActivity.class));
            finish();
        });
    }
}