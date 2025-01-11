package civicconnect.apcoders.in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {


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

        @SuppressLint("InflateParams") View view = LayoutInflater.from(this).inflate(R.layout.drawer_header, null, false);
        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "User");
        Log.d("onCreate: ", username);

        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
        Log.d("onCreate: ", usernameTextView.getText().toString());
        usernameTextView.setText("123");
        usernameTextView.setText(username);

        SharedPreferences sp = getSharedPreferences("Emailprefs", MODE_PRIVATE);
        String email = sp.getString("email", "email");
        TextView emailTextView = headerView.findViewById(R.id.menu_email);
        emailTextView.setText(email);
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

//                if (itemId == R.id.navigation_home) {
//                    Toast.makeText(HomeActivity.this, "Home Clicked", Toast.LENGTH_SHORT).show();
//                    // No need to start HomeActivity again as it's already the current one
//                } else if (itemId == R.id.navigation_profile) {
//                    Toast.makeText(HomeActivity.this, "Manage Profile", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(HomeActivity.this, Profile_Activity.class));
//                } else if (itemId == R.id.navigation_about) {
//                    Toast.makeText(HomeActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(HomeActivity.this, AboutActivity.class));
//                } else if (itemId == R.id.navigation_share) {
//                    Toast.makeText(HomeActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.setType("text/plain");
//                    String Body = "Download this App";
//                    String Sub = "https://play.google.com";
//                    intent.putExtra(Intent.EXTRA_TEXT, Body);
//                    intent.putExtra(Intent.EXTRA_TEXT, Sub);
//                    startActivity(Intent.createChooser(intent, "Share using"));
//
//
//                } else if (itemId == R.id.navigation_logout) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean("isLoggedIn", false);
//                    editor.apply();
//
//                    Toast.makeText(HomeActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(HomeActivity.this, Login_Activity.class));
//                    finish(); // Close HomeActivity
//
//                }
//                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

//        Button incident = findViewById(R.id.buttonReportIncident);
//        incident.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, IncidentActivity.class));
//            }
//        });
//
//        Button portal = findViewById(R.id.buttonPortal);
//        portal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, PortalActivity.class));
//            }
//
//        });
//
//        Button buttonTrackingSystem = findViewById(R.id.buttonTrackingSystem);
//        buttonTrackingSystem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
//            }
//        });
//
//        Button PanicPortal = findViewById(R.id.buttonPanic);
//        PanicPortal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(HomeActivity.this, PanicActivity.class));
//            }
//        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}