package civicconnect.apcoders.in;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import civicconnect.apcoders.in.Utils.FetchUserData;
import civicconnect.apcoders.in.models.AuthorityModel;
import civicconnect.apcoders.in.models.NormalUserModel;

public class ProfileActivity extends AppCompatActivity {
    Spinner spn;
    Button btn;
    EditText add;
    EditText cont_no;
    ImageView back;
    Boolean saved;
    TextView fullNameTextView, emailTextView;
    String initialAddress, initialContactNo, initialRegion;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        emailTextView = findViewById(R.id.textview_email);
        fullNameTextView = findViewById(R.id.textview_fullname_tag);
//        btn = findViewById(R.id.button_Apply_changes);
//        add = findViewById(R.id.editTextAddress);
//        cont_no = findViewById(R.id.editTextContact);
//        back = findViewById(R.id.btnback);
//        spn = findViewById(R.id.spinner_profile_region);


        sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("UserType", "Normal User");
        if (userType.equals("Normal User")) {
            FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
                @Override
                public void onCallback(NormalUserModel normalUserModel) {
                    fullNameTextView.setText(normalUserModel.getUserFulName());
                    emailTextView.setText(normalUserModel.getEmail());
                }
            });
        } else if (userType.equals("Authorities")) {
            FetchUserData.FetchAuthorityData(new FetchUserData.GetAuthorityData() {
                @Override
                public void onCallback(AuthorityModel authorityModel) {
                    fullNameTextView.setText(authorityModel.getUserFulName());
                    emailTextView.setText(authorityModel.getEmail());
                }
            });
        }


//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinnerlist, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spn.setAdapter(adapter);


//        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
////                checkForChanges();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int spinnerPosition = adapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }


    // Checks if current values differ from the initial values
    private void checkForChanges() {
        String currentAddress = add.getText().toString();
        String currentContactNo = cont_no.getText().toString();
        String currentRegion = spn.getSelectedItem().toString();

        saved = currentAddress.equals(initialAddress) && currentContactNo.equals(initialContactNo) && currentRegion.equals(initialRegion);
    }

    // TextWatcher to detect changes in EditTexts
    private class ChangeWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkForChanges();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}