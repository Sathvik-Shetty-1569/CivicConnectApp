package civicconnect.apcoders.in;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    EditText edUsername, edPassowrd, edEmail, edComfirm;
    TextView tv;
    Button btn;
    Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edComfirm = findViewById(R.id.editTextRegComfirmPassword);
        edPassowrd = findViewById(R.id.editTextRegPassword);
        tv = findViewById(R.id.textViewExistingUser);
        btn = findViewById(R.id.buttonRegister);


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, Login_Activity.class));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edUsername.getText().toString();
                String password = edPassowrd.getText().toString();
                String comfirm = edComfirm.getText().toString();
                String email = edEmail.getText().toString().trim();
                Database db = new Database(getApplicationContext(), "healthcare", null, 1);


                if (username.length() == 0 || password.length() == 0 || comfirm.length() == 0 || email.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Fill all the above details", Toast.LENGTH_SHORT).show();
                } else {
                    if (password.compareTo(comfirm) == 0) {
                        if (inValid(password)) {

                        }
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