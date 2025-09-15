import java.util.HashMap;
import java.util.Map;

public class AccountDatabase {
    private static Map<String, Account> accounts = new HashMap<>();

    public static Account createAccount(String id, String name, double balance) {
        Account acc = new Account(id, name, balance);
        accounts.put(id, acc);
        return acc;
    }

    public static Account getAccount(String id) {
        return accounts.get(id);
    }

    public static Account deposit(String id, double amount) {
        Account acc = accounts.get(id);
        if (acc != null) {
            acc.deposit(amount);
            return acc;
        }
        return null;
    }

    public static Account withdraw(String id, double amount) {
        Account acc = accounts.get(id);
        if (acc != null && acc.withdraw(amount)) {
            return acc;
        }
        return null;
    }
}
