package civicconnect.apcoders.in.Utils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        collectionReference.whereEqualTo("Userid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        NormalUserModel normalUserModel = task.getResult().toObjects(NormalUserModel.class).get(i);
                        GetNormalUserData.onCallback(normalUserModel);
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                NormalUserModel normalUserModel = new NormalUserModel(null, null, null, null);
                GetNormalUserData.onCallback(null);
            }
        });
    }


    public static void FetchAuthorityData(GetAuthorityData GetAuthorityData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firebaseFirestore.collection("Authorities");

        collectionReference.whereEqualTo("Userid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 0; i < task.getResult().size(); i++) {
                        AuthorityModel authorityModel = task.getResult().toObjects(AuthorityModel.class).get(i);
                        GetAuthorityData.onCallback(authorityModel);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GetAuthorityData.onCallback(null);
            }
        });
    }
}