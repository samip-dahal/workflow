package workflow;

import java.util.HashMap;

public class User {
    private final String userId;
    private final HashMap<String, String> userPrivateData;

    private User(String userId){
        this.userId = userId;
        this.userPrivateData = new HashMap<>();
    }

    public static User of(String userId){
        return new User(userId);
    }

    public String getUserId() {
        return this.userId;
    }

    public void updatePrivateData(String key, String value){
        this.userPrivateData.put(key, value);
    }

    public String getUserPrivateData(String key){
        return this.userPrivateData.get(key);
    }

    public HashMap<String, String> getAllUserPrivateData(){
        return new HashMap<>(this.userPrivateData);
    }
}
