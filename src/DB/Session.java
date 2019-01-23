package DB;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.security.MessageDigest;


public abstract class Session {
    private static DataBase db;
    private static User loggedUser;

    private static MessageDigest hashFunc;

    public static void init() throws NoSuchAlgorithmException {
        hashFunc = MessageDigest.getInstance("SHA-256");
        try {
            db = new DataBase();
        } catch (SQLException e) {
            System.out.println("SQL ERROR: CANNOT CONNECT TO DB" + e.getMessage());
        }
        loggedUser = null;
    }

    public static DataBase getDb() {
        return db;
    }

    public static MessageDigest getHashFunc() {
        return hashFunc;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        Session.loggedUser = loggedUser;
    }
}
