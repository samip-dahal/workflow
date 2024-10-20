package workflow;

import java.util.UUID;

public class Utilities {
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}
// remove this