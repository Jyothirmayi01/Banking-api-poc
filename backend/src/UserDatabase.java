import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private static Map<String, String> users = new HashMap<>();

    // Register a new user
    public static boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false; // already exists
        }
        users.put(username, password);
        return true;
    }

    // Login
    public static boolean login(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
