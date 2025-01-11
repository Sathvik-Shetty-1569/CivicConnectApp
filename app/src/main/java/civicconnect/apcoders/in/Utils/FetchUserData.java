package civicconnect.apcoders.in.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import civicconnect.apcoders.in.models.AuthorityModel;
import civicconnect.apcoders.in.models.NormalUserModel;

public class FetchUserData {

    public interface GetNormalUserData {
        public void onCallback(NormalUserModel normalUserModel);
    }

    public interface GetAuthorityData {
        public void onCallback(AuthorityModel authorityModel);
    }

    public FetchUserData() {

    }

    public static void FetchNormalUserData(GetNormalUserData GetNormalUserData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        Log.d("TAG", "FetchNormalUserData: " + uid);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        collectionReference.whereEqualTo("userid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    NormalUserModel normalUserModel = null;
                    for (int i = 0; i < task.getResult().size(); i++) {
                        normalUserModel = task.getResult().toObjects(NormalUserModel.class).get(i);
                    }
                    GetNormalUserData.onCallback(normalUserModel);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.getMessage());
                NormalUserModel normalUserModel = new NormalUserModel(null, null, null, null);
                GetNormalUserData.onCallback(null);
            }
        });
    }


    public static void FetchAuthorityData(GetAuthorityData GetAuthorityData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        Log.d("TAG", "FetchNormalUserData: " + uid);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        CollectionReference collectionReference = firebaseFirestore.collection("Authorities");

        collectionReference.whereEqualTo("userid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    AuthorityModel authorityModel = null;
                    for (int i = 0; i < task.getResult().size(); i++) {
                        authorityModel = task.getResult().toObjects(AuthorityModel.class).get(i);
                    }
                    GetAuthorityData.onCallback(authorityModel);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.getMessage());
                GetAuthorityData.onCallback(null);
            }
        });
    }
}