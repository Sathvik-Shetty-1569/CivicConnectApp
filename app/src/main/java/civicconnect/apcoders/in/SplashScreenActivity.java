package civicconnect.apcoders.in;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.firebase.auth.FirebaseAuth;

import civicconnect.apcoders.in.authority.DashboardActivity;
import es.dmoral.toasty.Toasty;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                boolean isLogging = sharedPreferences.getBoolean("isLoggedIn", false);
                String UserType = sharedPreferences.getString("UserType", "Normal User");
                Toasty.success(SplashScreenActivity.this, UserType, Toasty.LENGTH_LONG).show();
                if (firebaseAuth.getCurrentUser() != null) {
                    if (UserType.equals("Normal User")) {
                        Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                        i.putExtra("UserType", UserType);
                        startActivity(i);
                        finish();
                    } else if (UserType.equals("Authorities")) {
                        Log.d("TAG", "run: Authorities");
                        Intent i = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        i.putExtra("UserType", UserType);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, Login_Activity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}