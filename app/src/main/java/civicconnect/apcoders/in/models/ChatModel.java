package civicconnect.apcoders.in.models;

import android.widget.ImageView;

public class ChatModel {
    String SenderName, Message;
    ImageView userProfile;

    public ChatModel() {

    }

    public ChatModel(String senderName, String message) {
        SenderName = senderName;
        Message = message;

    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ImageView getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(ImageView userProfile) {
        this.userProfile = userProfile;
    }
}
