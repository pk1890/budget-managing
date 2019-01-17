package DB;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.security.MessageDigest;


public abstract class SESSION {
    public static DataBase db;
    public static User loggedUser;

    public static MessageDigest hashFunc;

    public static void init() throws NoSuchAlgorithmException {
        hashFunc = MessageDigest.getInstance("SHA-256");
        try {
            db = new DataBase();
        } catch (SQLException e) {
            System.out.println("SQL ERROR: CANNOT CONNECT TO DB" + e.getMessage());
        }
        loggedUser = null;
    }
}
