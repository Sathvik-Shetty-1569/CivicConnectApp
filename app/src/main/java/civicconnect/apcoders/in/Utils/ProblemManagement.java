package civicconnect.apcoders.in.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

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

    public static void FetchOthersProblems(GetAllProblems GetAllProblems) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        collectionName.whereNotEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public static void UpVoteProblem(String problemId, UserUpvoteCallback userUpvoteCallback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference collection = firebaseFirestore.collection(COLLECTION_NAME);

        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                task.getResult().getDocuments().forEach(document -> {
                    String documentId = document.getId();
                    ArrayList<String> upvoteUserIds = (ArrayList<String>) document.get("upvoteUserIds");
                    if (upvoteUserIds != null) {
                        if (upvoteUserIds.contains(userId)) {
                            userUpvoteCallback.onCheckCompleted(true, documentId);
                            Log.d(TAG, "Already upvoted for problemId: " + problemId);
                        }
                    }
                    // Increment upvotes and add userId to the array
                    collection.document(documentId).update(
                            "upvotes", FieldValue.increment(1),
                            "upvoteUserIds", FieldValue.arrayUnion(userId)
                    ).addOnSuccessListener(aVoid -> {
                        userUpvoteCallback.onCheckCompleted(true, documentId);
                        Log.d(TAG, "Upvote successful and userId added for problemId: " + problemId);
                    }).addOnFailureListener(e -> {
                        userUpvoteCallback.onCheckCompleted(false, documentId);
                        Log.e(TAG, "Failed to update upvote for problemId: " + problemId, e);
                    });
                });
            } else {
                userUpvoteCallback.onCheckCompleted(false, null);
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

    public static void RemoveUpVoteProblem(String problemId, UserUpvoteCallback userUpvoteCallback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        CollectionReference collection = firebaseFirestore.collection(COLLECTION_NAME);

        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                task.getResult().getDocuments().forEach(document -> {
                    String documentId = document.getId();
                    ArrayList<String> upvoteUserIds = (ArrayList<String>) document.get("upvoteUserIds");
                    if (upvoteUserIds != null && upvoteUserIds.contains(userId)) {
                        // Decrement upvotes and remove userId from the array
                        collection.document(documentId).update(
                                "upvotes", FieldValue.increment(-1),
                                "upvoteUserIds", FieldValue.arrayRemove(userId)
                        ).addOnSuccessListener(aVoid -> {
                            userUpvoteCallback.onCheckCompleted(true, documentId);
                            Log.d(TAG, "Upvote removed and userId removed for problemId: " + problemId);
                        }).addOnFailureListener(e -> {
                            userUpvoteCallback.onCheckCompleted(false, documentId);
                            Log.e(TAG, "Failed to remove upvote for problemId: " + problemId, e);
                        });
                    } else {
                        userUpvoteCallback.onCheckCompleted(false, documentId);
                        Log.d(TAG, "User had not upvoted problemId: " + problemId);
                    }
                });
            } else {
                userUpvoteCallback.onCheckCompleted(false, null);
                Log.e(TAG, "Error finding problem to remove upvote: " + task.getException());
            }
        }).addOnFailureListener(e -> Log.e(TAG, "RemoveUpVoteProblem failed: ", e));
    }

    public static void FetchProblemsByStatus(String Status, GetAllProblems GetAllProblems) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection(COLLECTION_NAME);

        collectionName.whereEqualTo("status", Status).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "onComplete: ");
                    ArrayList<ProblemModel> ProblemDataList = new ArrayList<>();
                    for (int i = 0; i < task.getResult().size(); i++) {
                        ProblemModel problemModel = task.getResult().getDocuments().get(i).toObject(ProblemModel.class);
                        ProblemDataList.add(problemModel);
                    }
                    Log.d("TAG", "onComplete: " + ProblemDataList.size());
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

    private static Bitmap resizeAndCompressImage(Context context, Uri imageUri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
        Bitmap original = BitmapFactory.decodeStream(inputStream);
        inputStream.close();

        // Resize the image
        int maxWidth = 800; // Adjust as needed
        int maxHeight = 800; // Adjust as needed
        float aspectRatio = original.getWidth() / (float) original.getHeight();
        int width = maxWidth;
        int height = Math.round(maxWidth / aspectRatio);

        if (aspectRatio > 1) {
            width = Math.round(maxHeight * aspectRatio);
            height = maxHeight;
        }

        Bitmap resized = Bitmap.createScaledBitmap(original, width, height, true);

        // Compress the image
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 80, outputStream); // Adjust quality (80) as needed
        return BitmapFactory.decodeByteArray(outputStream.toByteArray(), 0, outputStream.size());
    }

    public static void SubmitReport(Context context, String UserId, String ProblemName, GeoPoint ProblemLocation,
                                    String ProblemAddress, String ProblemDescription, Uri ImageUri,
                                    UploadReportCallback uploadReportCallback) {

        String problemId = "PROB_" + UUID.randomUUID().toString();
        Log.d("TAG", "SubmitReport: ");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collectionName = firebaseFirestore.collection("Problems");

        // Convert the image to Base64
        try {
//            InputStream inputStream = context.getContentResolver().openInputStream(ImageUri);
//            if (inputStream == null) {
//                Log.d(TAG, "SubmitReport: Failed to read image data.");
//                if (uploadReportCallback != null) {
//                    uploadReportCallback.onCallback("Failed to read image data.");
//                }
//                return;
//            }
//
//            // Read the image data into a byte array
//            byte[] imageBytes = readBytes(inputStream);

            Bitmap resizedImage = resizeAndCompressImage(context, ImageUri);

            // Convert the resized image to Base64
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedImage.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            String base64Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            // Create the ProblemModel with the Base64 image string
            ProblemModel problemModel = new ProblemModel(
                    ProblemName,
                    ProblemDescription,
                    "Reported",
                    problemId
                    , // Use the Base64 string here
                    UserId,
                    base64Image,
                    ProblemLocation
            );
            Log.d(TAG, "SubmitReport: ");
            // Store the problem data in Firestore
            collectionName.add(problemModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        if (uploadReportCallback != null) {
                            uploadReportCallback.onCallback("Report submitted successfully!");
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (uploadReportCallback != null) {
                        uploadReportCallback.onCallback("Failed to submit report: " + e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            if (uploadReportCallback != null) {
                uploadReportCallback.onCallback("Error processing image: " + e.getMessage());
            }
        }
    }

    // Helper method to read bytes from an InputStream
    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    public static void displayImageFromBase64(String base64Image, ImageView imageView) {
        try {
            // Decode Base64 string to byte array

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);

            // Convert byte array to Bitmap
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            // Set the Bitmap to the ImageView
            imageView.setImageBitmap(decodedBitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "displayImageFromBase64: " + e.getMessage());
            // You can set a placeholder or show an error message here if decoding fails
        }
    }

    public static void updateProblemStatus(String problemId, String newStatus, ProblemStatusUpdateCallback callback) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference collection = firebaseFirestore.collection(COLLECTION_NAME);

        // Step 1: Find the document where problemId matches
        collection.whereEqualTo("problemId", problemId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                // Get the document ID
                String documentId = task.getResult().getDocuments().get(0).getId();

                // Step 2: Update the document's status
                collection.document(documentId).update("status", newStatus)
                        .addOnSuccessListener(aVoid -> {
                            callback.onStatusUpdated(true, "Problem status updated successfully.");
                            Log.d(TAG, "Problem status updated for problemId: " + problemId);
                        })
                        .addOnFailureListener(e -> {
                            callback.onStatusUpdated(false, "Failed to update problem status.");
                            Log.e(TAG, "Error updating problem status for problemId: " + problemId, e);
                        });
            } else {
                callback.onStatusUpdated(false, "Problem not found.");
                Log.e(TAG, "Problem not found for problemId: " + problemId);
            }
        }).addOnFailureListener(e -> {
            callback.onStatusUpdated(false, "Error fetching problem.");
            Log.e(TAG, "Error fetching problem for status update: ", e);
        });
    }

    // Callback interface for status updates
    public interface ProblemStatusUpdateCallback {
        void onStatusUpdated(boolean success, String message);
    }

}
