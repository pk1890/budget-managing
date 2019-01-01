package DB;


import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

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
                    "    title integer NOT NULL,\n" +
                    "    date date NOT NULL,\n" +
                    "    value decimal(10,2) NOT NULL,\n" +
                    "    categoryId integer NOT NULL,\n" +
                    "    CONSTRAINT Transactions_Categories FOREIGN KEY (categoryId)\n" +
                    "    REFERENCES Categories (id)\n" +
                    ");";

            stmt.execute(sqlCreateCat);
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

    public TransactionList getTransactionsByPredicate(String predicate){
        String sql = "UNKNOWN";
        TransactionList result = new TransactionList();
        ResultSet res;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            sql = "SELECT t.title, t.date, t.value, c.name" +
                    " FROM Transactions t JOIN Categories c ON t.categoryId = c.id WHERE " + predicate + ";";

             res = stmt.executeQuery(sql);

             while(res.next()){
                 result.add(new Transaction(
                         res.getString("title"),
                         res.getFloat("value"),
                         res.getString("date"),
                         res.getString("name"))
                 );
             }
            System.out.println("Select with predicate query was executed successfully ");
        }catch (SQLException e) {
            System.out.println(e.getMessage() + " SQL: " + sql);
        }

        return result;
    }


}
