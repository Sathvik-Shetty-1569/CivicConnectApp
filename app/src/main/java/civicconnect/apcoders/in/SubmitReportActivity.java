package civicconnect.apcoders.in;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

import civicconnect.apcoders.in.Utils.ProblemManagement;
import civicconnect.apcoders.in.Utils.UserLocation;
import es.dmoral.toasty.Toasty;

public class SubmitReportActivity extends AppCompatActivity {
    TextInputEditText product_name_edittext, product_description_edittext, product_quantity_edittext, product_price_edittext, product_address_edittext;
    String product_name, product_description, product_address;
    Button submitReportBtn;
    LinearLayout product_images_layout;
    ImageView product_image_add_image;
    FirebaseAuth firebaseAuth;
    TextView location_cooridnates;
    double latitudeValue = 0.0;
    double longitudeValue = 0.0;
    boolean isLocationPermissionGranted = false;
    ImageView backBtn;
    Uri ImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_submit_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SubmitReport();
        backBtn = findViewById(R.id.backBtn);
        product_name_edittext = findViewById(R.id.product_name);
        product_description_edittext = findViewById(R.id.product_description);
        product_address_edittext = findViewById(R.id.product_address);
        product_images_layout = findViewById(R.id.product_images_layout);
        submitReportBtn = findViewById(R.id.submitReportBtn);
        location_cooridnates = findViewById(R.id.location_cooridnates);
        product_image_add_image = findViewById(R.id.product_image_add_image);
        firebaseAuth = FirebaseAuth.getInstance();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        product_images_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallary = new Intent(Intent.ACTION_PICK);
                openGallary.setType("image/*");
                openGallary.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                ImagePickerLauncher.launch(Intent.createChooser(openGallary, "Select Product Images"));
            }
        });
        UserLocation.GetCurrentLocation(this, new UserLocation.LocationCallback() {
                    @Override
                    public void onLocationReceived(double latitude, double longitude) {
                        latitudeValue = latitude;
                        longitudeValue = longitude;
                        location_cooridnates.setText("Latitude: " + latitude + ", Longitude: " + longitude);
                    }

                    @Override
                    public void onLocationError(String errorMessage) {
                        location_cooridnates.setText(errorMessage);
                        GetUserLocation();
                    }

                }
        );
    }


    private void SubmitReport() {
        submitReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReportBtn.setEnabled(false);

                product_name = Objects.requireNonNull(product_name_edittext.getText()).toString();
                product_description = Objects.requireNonNull(product_description_edittext.getText()).toString();
                product_address = Objects.requireNonNull(product_address_edittext.getText()).toString();

                if (TextUtils.isEmpty(product_name) || TextUtils.isEmpty(product_description) || TextUtils.isEmpty(product_address)) {
                    Toasty.error(SubmitReportActivity.this, "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
//                    GetUserLocation();
                    submitReportBtn.setEnabled(true);
                } else {
                    if (ImageUri != null) {
                        ProblemManagement.SubmitReport(SubmitReportActivity.this,
                                Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), product_name, new GeoPoint(latitudeValue, longitudeValue), product_address,
                                product_description, ImageUri, new ProblemManagement.UploadReportCallback() {
                                    @Override
                                    public void onCallback(String message) {
                                        if (message.equals("OK")) {
                                            product_name_edittext.setText(null);
                                            product_description_edittext.setText(null);
                                            product_quantity_edittext.setText(null);
                                            product_address_edittext.setText(null);
                                            product_price_edittext.setText(null);
                                            product_image_add_image.setImageDrawable(getDrawable(R.drawable.upload_product));
                                        } else {
                                            Log.d("TAG", "onCallback: " + message);
                                            Toasty.error(SubmitReportActivity.this, "Report Not Submitted", Toasty.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    } else {
                        Toasty.error(SubmitReportActivity.this, "Please Upload Photo", Toasty.LENGTH_LONG).show();
                    }
                }

            }
        });
    }


    ActivityResultLauncher<Intent> ImagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    Intent data = activityResult.getData();
                    if (data != null) {
                        ImageUri = data.getData();
                        product_image_add_image.setImageURI(data.getData());
                    }
                }
            });

    private void GetUserLocation() {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // If permission is denied but can ask again (not "Don't ask again")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show a rationale dialog explaining why the permission is needed
                showLocationPermissionRationale();
            } else {
                // Request the permission for the first time or if the user has not chosen "Don't ask again"
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        } else {
            // Permission already granted, proceed with location-based functionality
            isLocationPermissionGranted = true;
        }
    }


    // Handles the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location-based functionality
                isLocationPermissionGranted = true;
            } else {
                // Permission denied, show message or take necessary action
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // User has denied with "Don't ask again"
                    showPermissionDeniedDialog();
                } else {
                    Toasty.error(this, "Location permission required for nearby products.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Shows a rationale dialog for requesting location permission
    private void showLocationPermissionRationale() {
        new AlertDialog.Builder(this)
                .setTitle("Location Permission Needed")
                .setCancelable(false)
                .setMessage("This app requires location access to suggest nearby farm equipment. Please enable it for better functionality.")
                .setPositiveButton("Allow", (dialog, which) -> ActivityCompat.requestPermissions(SubmitReportActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1))
                .setNegativeButton("Deny", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Shows a dialog when permission is denied permanently (user selected "Don't ask again")
    private void showPermissionDeniedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Enable Location Permission")
                .setCancelable(false)
                .setMessage("Location access is required to suggest nearby farm equipment. Please enable it in settings.")
                .setPositiveButton("Go to Settings", (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                })
                .create()
                .show();
    }

}