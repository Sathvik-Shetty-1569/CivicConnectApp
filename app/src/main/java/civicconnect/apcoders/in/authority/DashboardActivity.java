package civicconnect.apcoders.in.authority;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import civicconnect.apcoders.in.AboutActivity;
import civicconnect.apcoders.in.CommunityActivity;
import civicconnect.apcoders.in.InsightsActivity;
import civicconnect.apcoders.in.Login_Activity;
import civicconnect.apcoders.in.ProfileActivity;
import civicconnect.apcoders.in.R;
import civicconnect.apcoders.in.Utils.FetchUserData;
import civicconnect.apcoders.in.models.AuthorityModel;
import civicconnect.apcoders.in.models.NormalUserModel;
import es.dmoral.toasty.Toasty;

public class DashboardActivity extends AppCompatActivity {

    CardView avilableReportsCardView,ProceedingCardView, CommunityCardView, InsightsCardView, pendingReportsCardView, completeReportsCardView, authorityScorecardView;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        avilableReportsCardView = findViewById(R.id.AvilableReportsCardView);
        pendingReportsCardView = findViewById(R.id.pendingReportsCardView);
        completeReportsCardView = findViewById(R.id.CompleteReportsCardView);
        authorityScorecardView = findViewById(R.id.AuthorityScorecardView);
        ProceedingCardView = findViewById(R.id.ProceedingCardView);

        DrawerLayout drawerLayout;
        NavigationView navigationView;
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        ImageButton buttondrawer = findViewById(R.id.buttonDrawer);
        View headerView = navigationView.getHeaderView(0);

        View view = LayoutInflater.from(this).inflate(R.layout.drawer_header, null, false);

        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("UserType", "Normal User");
        if (userType.equals("Normal User")) {
            FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
                @Override
                public void onCallback(NormalUserModel normalUserModel) {
                    if (normalUserModel != null) {
                        Log.d("TAG", "onCallback: " + normalUserModel.getUserFulName() + normalUserModel.getEmail());
                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
                        usernameTextView.setText(normalUserModel.getUserFulName());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserFullName", normalUserModel.getUserFulName());
                        editor.apply();
                        editor.commit();
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(normalUserModel.getEmail());

                    } else {
                        firebaseAuth.signOut();
                        startActivity(new Intent(DashboardActivity.this, Login_Activity.class));
                        finish();
                    }
                }
            });
        } else if (userType.equals("Authorities")) {
            FetchUserData.FetchAuthorityData(new FetchUserData.GetAuthorityData() {
                @Override
                public void onCallback(AuthorityModel authorityModel) {
                    if (authorityModel != null) {
                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
                        usernameTextView.setText(authorityModel.getUserFulName());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserFullName", authorityModel.getUserFulName());
                        editor.apply();
                        editor.commit();
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(authorityModel.getEmail());
                    } else {
                        firebaseAuth.signOut();
                        startActivity(new Intent(DashboardActivity.this, Login_Activity.class));
                        finish();
                    }
                }
            });
        } else {
            firebaseAuth.signOut();
            startActivity(new Intent(DashboardActivity.this, Login_Activity.class));
            finish();
        }

        buttondrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navigation_home) {
                    Toasty.success(DashboardActivity.this, "Home Clicked", Toasty.LENGTH_SHORT).show();
                    // No need to start DashboardActivity again as it's already the current one
                } else if (itemId == R.id.navigation_profile) {
                    Toasty.success(DashboardActivity.this, "Manage Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                } else if (itemId == R.id.navigation_about) {
                    Toasty.success(DashboardActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DashboardActivity.this, AboutActivity.class));
                } else if (itemId == R.id.navigation_share) {
                    String shareMessage = "🌟 Join the Movement with CivicConnect 🌟\n\n" +
                            "Are you ready to make a difference? 🤝 Whether you need help or want to lend a hand, our app connects people and resources like never before!\n\n" +
                            "✔️ Discover Local Help\n" +
                            "✔️ Contribute to Your Community\n" +
                            "✔️ Make Every Action Count\n\n" +
                            "📲 Download now and become a part of something bigger!\n" +
                            "👉 https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n" +
                            "Let's work together to create a better tomorrow. 💙\n\n" +
                            "#CrowdsourceAid #CommunitySupport #HelpConnect";

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share App via"));
//                    Toasty.success(DashboardActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();


                } else if (itemId == R.id.navigation_logout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                    firebaseAuth.signOut();
                    Toasty.success(DashboardActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DashboardActivity.this, Login_Activity.class));
                    finish(); // Close DashboardActivity

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        InsightsCardView = findViewById(R.id.InsightsCardView);
        InsightsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, InsightsActivity.class));
            }
        });

        avilableReportsCardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ShowAvailableReports.class));
        });
        CommunityCardView = findViewById(R.id.CommunityCardView);
        CommunityCardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, CommunityActivity.class));
        });
        pendingReportsCardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ShowPendingReports.class));
        });

        completeReportsCardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ShowCompleteReports.class));
        });
        ProceedingCardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, ShowProcessingReports.class));
        });

        authorityScorecardView.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, AuthorityScoreChecker.class));
        });

    }
}