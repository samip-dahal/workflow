package workflow;

import java.util.UUID;

public class User {
    private final String userId;

    private User() {
        this.userId = UUID.randomUUID().toString();
    }

    public static User of() {
        return new User();
    }

    public String getUserId() {
        return this.userId;
    }
}
