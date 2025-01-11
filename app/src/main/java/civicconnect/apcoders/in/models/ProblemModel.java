package civicconnect.apcoders.in.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class ProblemModel {
    GeoPoint geoPointLocation;
    String ProblemName, ProblemDescription;
    int upvotes;
    ArrayList<String> UpVotesUserId;
    String problemId;
    String userId;
    String PhotoUrl;

    public ProblemModel(GeoPoint geoPointLocation, String problemName, String problemDescription, int upvotes, String problemId, String userId, String photoUrl) {
        this.geoPointLocation = geoPointLocation;
        ProblemName = problemName;
        ProblemDescription = problemDescription;
        this.upvotes = upvotes;
        this.problemId = problemId;
        this.userId = userId;
        PhotoUrl = photoUrl;
    }

    public ArrayList<String> getUpVotesUserId() {
        return UpVotesUserId;
    }

    public void setUpVotesUserId(ArrayList<String> upVotesUserId) {
        UpVotesUserId = upVotesUserId;
    }

    public GeoPoint getGeoPointLocation() {
        return geoPointLocation;
    }

    public void setGeoPointLocation(GeoPoint geoPointLocation) {
        this.geoPointLocation = geoPointLocation;
    }

    public String getProblemName() {
        return ProblemName;
    }

    public void setProblemName(String problemName) {
        ProblemName = problemName;
    }

    public String getProblemDescription() {
        return ProblemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        ProblemDescription = problemDescription;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }
}
