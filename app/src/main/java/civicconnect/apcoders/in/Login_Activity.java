package civicconnect.apcoders.in;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    EditText edEmail, edPassowrd, EdAuthorityLevel;
    TextView tv;
    Button btn;
    FirebaseAuth firebaseAuth;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EdAuthorityLevel = findViewById(R.id.editTextRegAuthorityLevel);
        firebaseAuth = FirebaseAuth.getInstance();
        edEmail = findViewById(R.id.editTextLoginUsername);
        edPassowrd = findViewById(R.id.editTextLoginPassword);
        tv = findViewById(R.id.textViewNewUser);
        btn = findViewById(R.id.buttonLogin);
        radioGroup = findViewById(R.id.radioGroup);

        setRadioButtons();
        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        try {
            if (firebaseAuth.getCurrentUser().getUid() != null) {
                // If already logged in, skip login and go to HomeActivity
                navigateToHome();

            }
        } catch (Exception exception) {

        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassowrd.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(Login_Activity.this, "Fill All The Filed", Toast.LENGTH_LONG).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login_Activity.this, "Something goes Wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Activity.this, RegisterActivity.class));
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setRadioButtons() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtnNormalBtn) {
                    // Handle "Normal User" selection
                    EdAuthorityLevel.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Normal User selected", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radioBtnAuthorityBtn) {
                    // Handle "Authorities" selection
                    EdAuthorityLevel.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Authorities selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //    private void checkProfileCompletion() {
//        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//
//        String add = sharedPreferences.getString("address", null);
//        String contact = sharedPreferences.getString("contactNo", null);
//        String region = sharedPreferences.getString("region", null);
//
//        if (add == null || contact == null || region == null ||
//                add.isEmpty() || contact.isEmpty() || region.isEmpty()) {
//
//            Intent intent = new Intent(Login_Activity.this, Profile_Activity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(Login_Activity.this, HomeActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
    private void navigateToHome() {
        Intent intent = new Intent(Login_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}