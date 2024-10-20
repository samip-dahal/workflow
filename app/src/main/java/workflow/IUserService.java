package workflow;

import java.util.HashMap;

public interface IUserService {
    void updateUserPrivateData(String userId, HashMap<String, String> updatedPrivateData) throws InvalidUserException;
}
