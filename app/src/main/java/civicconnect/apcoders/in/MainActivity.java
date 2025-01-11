package civicconnect.apcoders.in;

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

import civicconnect.apcoders.in.Utils.FetchUserData;
import civicconnect.apcoders.in.authority.AuthorityScoreChecker;
import civicconnect.apcoders.in.models.AuthorityModel;
import civicconnect.apcoders.in.models.NormalUserModel;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(normalUserModel.getEmail());

                    } else {
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login_Activity.class));
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
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(authorityModel.getEmail());
                    } else {
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login_Activity.class));
                        finish();
                    }
                }
            });
        } else {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, Login_Activity.class));
            finish();
        }


        EdgeToEdge.enable(this);


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
                    Toasty.success(MainActivity.this, "Home Clicked", Toasty.LENGTH_SHORT).show();
                    // No need to start MainActivity again as it's already the current one
                } else if (itemId == R.id.navigation_profile) {
                    Toasty.success(MainActivity.this, "Manage Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if (itemId == R.id.navigation_about) {
                    Toasty.success(MainActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
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
//                    Toasty.success(MainActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();


                } else if (itemId == R.id.navigation_logout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                    firebaseAuth.signOut();
                    Toasty.success(MainActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Login_Activity.class));
                    finish(); // Close MainActivity

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

//        Button incident = findViewById(R.id.buttonReportIncident);
//        incident.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, IncidentActivity.class));
//            }
//        });
//
        CardView SubmitReportCardView = findViewById(R.id.SubmitReportCardView);
        SubmitReportCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SubmitReportActivity.class));
            }

        });

        CardView MyReportsCardView = findViewById(R.id.MyReportsCardView);
        MyReportsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowMyAllReports.class));
            }
        });

        CardView OthersReportCardView = findViewById(R.id.OthersReportCardView);
        OthersReportCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OthersActivityUpVotesReport.class));
            }
        });

        CardView CommunityCardView = findViewById(R.id.CommunityCardView);
        CommunityCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CommunityActivity.class));
            }
        });
        CardView authorityScorecardView = findViewById(R.id.AuthorityScorecardView);
        authorityScorecardView.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AuthorityScoreChecker.class));
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void isLogin() {
        try {
            if (firebaseAuth.getCurrentUser().getUid() != null) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        } catch (Exception exception) {

        }
    }
}