package workflow;

import java.util.Map;
import java.util.HashMap;

public class User {
    private final String userId;
    private final Map<String, String> userPrivateData;

    private User() {
        this.userId = Utilities.generateUniqueId();
        this.userPrivateData = new HashMap<>();
    }

    public static User of() {
        return new User();
    }

    public String getUserId() {
        return this.userId;
    }

    public void updatePrivateData(String key, String value) {
        this.userPrivateData.put(key, value);
    }

    public String getUserPrivateData(String key) {
        return this.userPrivateData.get(key);
    }

    public HashMap<String, String> getAllUserPrivateData() {
        return new HashMap<>(this.userPrivateData);
    }
}
