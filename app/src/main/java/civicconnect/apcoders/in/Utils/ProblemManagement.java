package civicconnect.apcoders.in.Utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import civicconnect.apcoders.in.models.ProblemModel;

public class ProblemManagement {
    private static final String TAG = "ProblemManagement";
    private static final String COLLECTION_NAME = "Problems";

    public interface GetAllProblems {
        public void GetFetchData(ArrayList<ProblemModel> ProblemDataList);
    }

    public interface FetchProblemDataCallback {
        void onDataFetched(boolean success, ProblemModel problemModel);
    }

    public interface UserUpvoteCallback {
        void onCheckCompleted(boolean hasUpvoted, String documentId);
    }

    public interface UploadReportCallback {
        public void onCallback(String message);
    }

    public ProblemManagement() {

    }

    public static void FetchAllProblems(GetAllProblems GetAllProblems) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        collectionName.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<ProblemModel> ProblemDataList = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        ProblemModel problemModel = task.getResult().getDocuments().get(i).toObject(ProblemModel.class);
                        ProblemDataList.add(problemModel);
                    }
                    GetAllProblems.GetFetchData(ProblemDataList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GetAllProblems.GetFetchData(null);
            }
        });
    }

    public static void FetchCurrentUserProblems(GetAllProblems GetAllProblems) {
        String COLLECTION_NAME = "Problems";
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        collectionName.whereEqualTo("userId", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<ProblemModel> ProblemDataList = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        ProblemModel problemModel = task.getResult().getDocuments().get(i).toObject(ProblemModel.class);
                        ProblemDataList.add(problemModel);
                    }
                    GetAllProblems.GetFetchData(ProblemDataList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GetAllProblems.GetFetchData(null);
            }
        });
    }

    public static void UpVoteProblem(String problemId) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference collection = firebaseFirestore.collection(COLLECTION_NAME);

        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                task.getResult().getDocuments().forEach(document -> {
                    String documentId = document.getId();
                    ArrayList<String> upvoteUserIds = (ArrayList<String>) document.get("upvoteUserIds");
                    if (upvoteUserIds.contains(userId)) {
                        Log.d(TAG, "Already upvoted for problemId: " + problemId);
                        return;
                    }
                    // Increment upvotes and add userId to the array
                    collection.document(documentId).update(
                            "upvotes", FieldValue.increment(1),
                            "upvoteUserIds", FieldValue.arrayUnion(userId)
                    ).addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Upvote successful and userId added for problemId: " + problemId);
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to update upvote for problemId: " + problemId, e);
                    });
                });
            } else {
                Log.e(TAG, "Error finding problem to upvote: " + task.getException());
            }
        }).addOnFailureListener(e -> Log.e(TAG, "UpVoteProblem failed: ", e));
    }

    public static void HasUserUpvoted(String problemId, UserUpvoteCallback callback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference collection = firebaseFirestore.collection("Problems");

        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                if (!task.getResult().isEmpty()) {
                    // Assuming one problem ID maps to one document
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    ArrayList<String> upvoteUserIds = (ArrayList<String>) task.getResult().getDocuments().get(0).get("upvoteUserIds");

                    if (upvoteUserIds != null && upvoteUserIds.contains(userId)) {
                        callback.onCheckCompleted(true, documentId); // User has upvoted
                    } else {
                        callback.onCheckCompleted(false, documentId); // User has not upvoted
                    }
                } else {
                    callback.onCheckCompleted(false, null); // Problem not found
                }
            } else {
                callback.onCheckCompleted(false, null); // Error or no data found
            }
        }).addOnFailureListener(e -> {
            callback.onCheckCompleted(false, null); // Failure to fetch data
        });
    }

    public static void FetchProblemDataById(String problemId, FetchProblemDataCallback callback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collection = firebaseFirestore.collection("Problems");

        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                if (!task.getResult().isEmpty()) {
                    // Assuming one problemId maps to one document
                    String documentId = task.getResult().getDocuments().get(0).getId();
                    ProblemModel problemModel = task.getResult().getDocuments().get(0).toObject(ProblemModel.class);
                    callback.onDataFetched(true, problemModel);
                } else {
                    callback.onDataFetched(false, null); // Problem not found
                }
            } else {
                callback.onDataFetched(false, null); // Error or no data found
            }
        }).addOnFailureListener(e -> {
            callback.onDataFetched(false, null); // Failure to fetch data
        });
    }

    public static void FetchProblemsByStatus(String Status, GetAllProblems GetAllProblems) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        collectionName.whereEqualTo("status", Status).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<ProblemModel> ProblemDataList = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        ProblemModel problemModel = task.getResult().getDocuments().get(i).toObject(ProblemModel.class);
                        ProblemDataList.add(problemModel);
                    }
                    GetAllProblems.GetFetchData(ProblemDataList);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                GetAllProblems.GetFetchData(null);
            }
        });
    }
//
//    public  static void SubmitReport(Context context, String UserId, String ProblemName, GeoPoint ProblemLocation, String ProblemAddress, String ProblemDescription, Uri ImageUri, UploadReportCallback uploadReportCallback){
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);
//
//        ProblemModel problemModel = new ProblemModel(ProblemName,ProblemDescription,"Pending","",UserId,"",ProblemLocation);
//
//        collectionName.add(problemModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }

    public static void SubmitReport(Context context, String UserId, String ProblemName, GeoPoint ProblemLocation,
                                    String ProblemAddress, String ProblemDescription, Uri ImageUri,
                                    UploadReportCallback uploadReportCallback) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        // Create a unique name for the image based on timestamp or any unique identifier
        String imageName = "problem_images/" + System.currentTimeMillis() + "_" + UserId + ".jpg";

        // Reference to the image in Firebase Storage
        StorageReference imageRef = storageReference.child(imageName);

        // Start image upload
        imageRef.putFile(ImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();

                        // Create the ProblemModel with the image URL
                        ProblemModel problemModel = new ProblemModel(
                                ProblemName,
                                ProblemDescription,
                                "Pending",
                                imageUrl, // Include the image URL here
                                UserId,
                                "",
                                ProblemLocation
                        );

                        // Store the problem data in Firestore
                        collectionName.add(problemModel)
                                .addOnSuccessListener(documentReference -> {
                                    if (uploadReportCallback != null) {
                                        uploadReportCallback.onCallback("Report submitted successfully!");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    if (uploadReportCallback != null) {
                                        uploadReportCallback.onCallback(e.getMessage());
                                    }
                                });

                    }).addOnFailureListener(e -> {
                        if (uploadReportCallback != null) {
                            uploadReportCallback.onCallback("Failed to get image URL: " + e.getMessage());
                        }
                    });

                }).addOnFailureListener(e -> {
                    if (uploadReportCallback != null) {
                        uploadReportCallback.onCallback("Image upload failed: " + e.getMessage());
                    }
                });
    }

}
