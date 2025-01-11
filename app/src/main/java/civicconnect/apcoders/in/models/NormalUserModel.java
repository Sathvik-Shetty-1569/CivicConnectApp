package civicconnect.apcoders.in.models;

public class NormalUserModel {
    String Userid, UserFulName, Username, Email;

    public NormalUserModel() {

    }

    public NormalUserModel(String userid, String userFulName, String username, String email) {
        Userid = userid;
        UserFulName = userFulName;
        Username = username;
        Email = email;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserFulName() {
        return UserFulName;
    }

    public void setUserFulName(String userFulName) {
        UserFulName = userFulName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
