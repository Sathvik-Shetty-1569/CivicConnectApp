package civicconnect.apcoders.in.models;

public class AuthorityModel {
    String Userid, UserFulName, Username, Email, AuthorityLevel;

public AuthorityModel(){

}
    public AuthorityModel(String userid, String userFulName, String username, String email, String authorityLevel) {
        Userid = userid;
        UserFulName = userFulName;
        Username = username;
        Email = email;
        AuthorityLevel = authorityLevel;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getAuthorityLevel() {
        return AuthorityLevel;
    }
    public void setAuthorityLevel(String authorityLevel) {
        AuthorityLevel = authorityLevel;
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
