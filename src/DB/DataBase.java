package DB;


import UI.SceneWrapper;
import UI.WindowsTypes;
import com.sun.org.apache.bcel.internal.generic.Select;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.util.Pair;

import javax.jws.soap.SOAPBinding;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import com.google.common.hash.Hashing;

public class DataBase {
    private String url;

    public static final String DB_DEFAULT_URL = "jdbc:sqlite:C:/sqlite/db/budget.db";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws SQLException{
        this.url = url;
        try (Connection conn = DriverManager.getConnection(url)){
            System.out.println("Connection to database " + url + " was successful");
        }
    }

    public DataBase() throws SQLException{
        this(DB_DEFAULT_URL);
    }

    public DataBase(String url) throws SQLException{
        setUrl(url);
        createDataBase();
    }

    public void createDataBase() throws SQLException{
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            String sqlCreateCat = "CREATE TABLE IF NOT EXISTS Categories (\n" +
                    "    id integer NOT NULL CONSTRAINT Categories_pk PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name text NOT NULL UNIQUE\n" +
                    ");";
            String sqlCreateTrans = "CREATE TABLE IF NOT EXISTS Transactions (\n" +
                    "    id integer NOT NULL CONSTRAINT Transactions_pk PRIMARY KEY AUTOINCREMENT,\n" +
                    "    title text NOT NULL,\n" +
                    "    date date NOT NULL,\n" +
                    "    value decimal(10,2) NOT NULL,\n" +
                    "    categoryId integer NOT NULL,\n" +
                    "    userId integer NOT NULL \n" +
//                    "    CONSTRAINT Transactions_Categories FOREIGN KEY (categoryId)\n" +
//                    "    REFERENCES Categories (id)\n" +
                   // "    CONSTRAINT Transactions_Users FOREIGN KEY (userId)\n" +
                    //"    REFERENCES Users (id)\n" +
                    ");";
            String sqlCreateUsers = "CREATE TABLE IF NOT EXISTS Users(" +
                    "   id integer NOT NULL CONSTRAINT Users_pk PRIMARY KEY AUTOINCREMENT," +
                    "   login text NOT NULL UNIQUE," +
                    "   password varchar(255) NOT NULL" +
                    ");";
            stmt.execute(sqlCreateCat);
            stmt.execute(sqlCreateUsers);
            stmt.execute(sqlCreateTrans);
            addCategory("other");


            System.out.println("The database was initialized successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addTransaction(Transaction tr){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String askCatID = "SELECT id FROM Categories WHERE name = '" + tr.getCategory() + "';";

            ResultSet rs = stmt.executeQuery(askCatID);

            String catID = rs.getString("id");

            ArrayList data = tr.getTransactionInternalData();
            data.add(new Pair<>("categoryId", catID));

            // insert record
            String sql = SQLGenerator.Insert("Transactions", data);
            System.out.println(sql);
            stmt.executeQuery(sql);

            System.out.println("The database was initialized successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addUser(String login, String password) throws AlreadyExistsException{
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String uniqueQuery = "SELECT 'y' FROM Users where login = '" + login + "'";

           ResultSet res = stmt.executeQuery(uniqueQuery);
           if (res.next()) throw new AlreadyExistsException("User with this login already exists in DB");

           List data = new ArrayList<Pair<String, String>>();
           data.add(new Pair<>("login", login));

           SESSION.hashFunc.update(password.getBytes());
           String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8)
                   .toString();
           SESSION.hashFunc.reset();
           data.add(new Pair<>("password", hashedPassword));

           String sql = SQLGenerator.Insert("Users", data);
            stmt.execute(sql);
            System.out.println("Successfuly added user " + login);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public List<User> getUsers() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT id, login FROM Users";

            ResultSet res = stmt.executeQuery(sql);

            List userList = new LinkedList<User>();

            while(res.next()){
                userList.add(new User(
                        res.getString("login"),
                        res.getInt("id")
                ));
            }
            return userList;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new LinkedList<User>();
        }
    }

    public Boolean logIn(String login, String password){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String hashed = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            String sql = "SELECT login, id FROM Users WHERE login = '" + login +"' AND password = '" + hashed+"'";
            ResultSet res = stmt.executeQuery(sql);
            if(res.next()){
                SESSION.loggedUser = new User(
                        res.getString("login"), res.getInt("id")
                                );
                return true;
            }
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void addCategory(String categoryName){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            ArrayList data = new ArrayList<Pair<String, String>>();
            data.add(new Pair<>("name", categoryName));
            String sql = SQLGenerator.Insert("Categories", data);

            stmt.executeQuery(sql);

            System.out.println("Category was successfully added");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableList getCategoriesNames() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ArrayList<String> result = new ArrayList<>();
            String sql = "SELECT name FROM categories";
            ResultSet res = stmt.executeQuery(sql);

            while(res.next()){
                result.add(res.getString("name"));
            }
            return FXCollections.observableArrayList(result);

        }catch (SQLException e){
             System.out.println(e.getMessage());
             return FXCollections.observableArrayList();
        }

    }

    public TransactionList getTransactionsByPredicate(String predicate){
        String sql = "UNKNOWN";
        TransactionList result = new TransactionList();
        ResultSet res;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            sql = "SELECT t.title, t.date, t.value, t.userId, c.name" +
                    " FROM Transactions t JOIN Categories c ON t.categoryId = c.id WHERE " + predicate + " ORDER BY t.date ASC;";

             res = stmt.executeQuery(sql);

             while(res.next()){
                 result.add(new Transaction(
                         res.getString("title"),
                         res.getFloat("value"),
                         res.getString("date"),
                         res.getString("name"),
                         res.getInt("userId"))
                 );
             }
            System.out.println("Select with predicate query was executed successfully ");
        }catch (SQLException e) {
            System.out.println(e.getMessage() + " SQL: " + sql);
        }

        return result;
    }


    public TransactionList getTransactions() {
        return this.getTransactionsByPredicate("1==1");
    }
}
