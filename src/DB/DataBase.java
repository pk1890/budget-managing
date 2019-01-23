package DB;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public String getCategoryId(String name){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String askCatID = "SELECT id FROM Categories WHERE name = '" + name + "';";

            ResultSet rs = stmt.executeQuery(askCatID);

            String catID = rs.getString("id");
            return catID;
        } catch (SQLException e) {
            return "1";
        }
    }

    public void addTransaction(Transaction tr){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            String catID = getCategoryId(tr.getCategory());

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

           Session.getHashFunc().update(password.getBytes());
           String hashedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8)
                   .toString();
           Session.getHashFunc().reset();
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

            List<User> userList = new LinkedList<User>();

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
                Session.setLoggedUser(new User(
                        res.getString("login"), res.getInt("id")
                                ));
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

    public List<String> getCategoriesNames() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            ArrayList<String> result = new ArrayList<>();
            String sql = "SELECT name FROM categories";
            ResultSet res = stmt.executeQuery(sql);

            while(res.next()){
                result.add(res.getString("name"));
            }
            return result;

        }catch (SQLException e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

    }


    public ObservableList getCategoriesNamesObservableList() {
       return FXCollections.observableArrayList(getCategoriesNames());

    }

    public SortedTransactionList getTransactionsByPredicate(String predicate){
        String sql = "UNKNOWN";
        SortedTransactionList result = new SortedTransactionList();
        ResultSet res;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            sql = "SELECT t.id, t.title, t.date, t.value, t.userId, c.name" +
                    " FROM Transactions t JOIN Categories c ON t.categoryId = c.id WHERE " + predicate + " ORDER BY t.date ASC;";

             res = stmt.executeQuery(sql);

             while(res.next()){
                 result.add(new Transaction(
                         res.getInt("id"),
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


    public void deleteTransaction(int transactionId){
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            String sql = "DELETE FROM Transactions WHERE id = " + new Integer(transactionId).toString();

            stmt.execute(sql);
            System.out.println("Succesfuly deleted transaction no " + transactionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SortedTransactionList getTransactions() {
        return this.getTransactionsByPredicate("1==1");
    }

    public SortedTransactionList getCurrentUserTransactionsByPredicate(String predicate) {
        if(Session.getLoggedUser() != null)
            return this.getTransactionsByPredicate("userId = " + Session.getLoggedUser().id + " AND " + predicate);

        return new SortedTransactionList();
    }

    public SortedTransactionList getCurrentUserTransactions() {
        return this.getCurrentUserTransactionsByPredicate("1==1");
    }
}
