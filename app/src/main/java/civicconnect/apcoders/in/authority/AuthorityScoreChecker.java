package civicconnect.apcoders.in.authority;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.Utils.ProblemManagement;
import civicconnect.apcoders.in.models.ProblemModel;

public class AuthorityScoreChecker extends AppCompatActivity {

    private TextView txtProgress;
    private ProgressBar progressBar;
    private int pStatus = 0;
    private Handler handler = new Handler();
    int SolveCount = 3;
    int PendingCount = 2;
    int ReportedCount = 5;
    TextView ResolvedCountTextView, PendingCountTextView, ReportedCountTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authority_score_checker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ResolvedCountTextView = findViewById(R.id.ResolvedCount);
        PendingCountTextView = findViewById(R.id.PendingCount);
        ReportedCountTextView = findViewById(R.id.ReportedCount);

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setBackBtn();
        ProblemManagement.FetchAllProblems(new ProblemManagement.GetAllProblems() {
            @Override
            public void GetFetchData(ArrayList<ProblemModel> ProblemDataList) {
                for (ProblemModel problem : ProblemDataList) {
                    if (problem.getStatus().equals("Reported")) {
                        ReportedCount++;
                    } else if (problem.getStatus().equals("Resolved")) {
                        SolveCount++;
                    } else if (problem.getStatus().equals("Pending")) {
                        PendingCount++;
                    }
                }
            }
        });
//        Log.d("TAG", "onCreate: "+ calculateSolvePercentage(10,100,10));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pStatus <= calculateSolvePercentage(ReportedCount, PendingCount, SolveCount)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ResolvedCountTextView.setText("Resolved : " + SolveCount);
                            PendingCountTextView.setText("Pending : " + PendingCount);
                            ReportedCountTextView.setText("UnResolve : " + ReportedCount);
                            progressBar.setProgress(pStatus);
                            txtProgress.setText(pStatus + " %");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
        }).start();

    }

    public double calculateSolvePercentage(int reported, int pending, int solved) {
        int totalCount = reported + pending + solved;

        if (totalCount == 0) {
            // If all counts are zero, return 0%
            return 0;
        }

        // Adjusted formula: Ensure percentage reflects solved proportion
        double percentage = ((double) solved / totalCount) * 100;

        // Ensure the percentage does not go below 0
        return Math.max(0, percentage);
    }


    private void setBackBtn() {
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(AuthorityScoreChecker.this, DashboardActivity.class));
            finish();
        });
    }

}