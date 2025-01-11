package civicconnect.apcoders.in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edFullName, edAuthorityLevel, edPassowrd, edEmail, edComfirm;
    TextView tv;
    Button btn;
    Spinner sp;
    RadioGroup radioGroup;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        edFullName = findViewById(R.id.editTextRegName);
        edAuthorityLevel = findViewById(R.id.editTextRegAuthorityLevel);
        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edComfirm = findViewById(R.id.editTextRegComfirmPassword);
        edPassowrd = findViewById(R.id.editTextRegPassword);
        tv = findViewById(R.id.textViewExistingUser);
        btn = findViewById(R.id.buttonRegister);
        radioGroup = findViewById(R.id.radioGroup);

        isLogin();
        setRadioButtons();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, Login_Activity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                String name = edFullName.getText().toString();
                String username = edUsername.getText().toString();
                String password = edPassowrd.getText().toString();
                String comfirm = edComfirm.getText().toString();
                String email = edEmail.getText().toString().trim();


                if (name.length() == 0 || username.length() == 0 || password.length() == 0 || comfirm.length() == 0 || email.length() == 0) {
                    Toasty.error(getApplicationContext(), "Fill all the above details", Toasty.LENGTH_SHORT).show();
                } else {
                    if (password.compareTo(comfirm) == 0) {
                        if (inValid(password)) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.VISIBLE);
                                        btn.setEnabled(true);
                                        Toasty.success(RegisterActivity.this, "Registration Done", Toasty.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    btn.setEnabled(true);
                                    Toasty.error(RegisterActivity.this, "Something Goes Wrong", Toasty.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btn.setEnabled(true);
                            Toasty.error(getApplicationContext(), "Password must contain at least 8 characters, having letter, digit and special character", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btn.setEnabled(true);
                        Toasty.error(getApplicationContext(), "Password and Comfirmed Password didn't match", Toasty.LENGTH_SHORT).show();
                    }


                }

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
                    edAuthorityLevel.setVisibility(View.GONE);
//                    Toasty.success(getApplicationContext(), "Normal User selected", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radioBtnAuthorityBtn) {
                    // Handle "Authorities" selection
                    edAuthorityLevel.setVisibility(View.VISIBLE);
//                    Toast.err(getApplicationContext(), "Authorities selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void isLogin() {
        try {
            if (firebaseAuth.getCurrentUser().getUid() != null) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        } catch (Exception exception) {

        }
    }
    public static boolean inValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        }
        for (int i = 0; i < passwordhere.length(); i++) {
            if (Character.isLetter(passwordhere.charAt(i))) {
                f1 = 1;
                break;
            }
        }
        for (int i = 0; i < passwordhere.length(); i++) {
            if (Character.isDigit(passwordhere.charAt(i))) {
                f2 = 1;
                break;
            }
        }
        for (int i = 0; i < passwordhere.length(); i++) {
            char c = passwordhere.charAt(i);
            if (c >= 33 && c <= 46 || c == 64) {
                f3 = 1;
                break;
            }
        }
        if (f1 == 1 && f2 == 1 && f3 == 1) {
            return true;


        }
        return false;
    }
}