package DB;

import com.sun.istack.internal.NotNull;
import javafx.util.Pair;

import java.util.ArrayList;
import java.sql.Date;

public class Transaction {
    private  int id;
    private String title;
    private float value;
    private Date date;
    private String category;
    private int userId;

    public Transaction(int id, String title, float value, int year, int month, int day, @ NotNull String category, int userId){
        this.id = id;
        this.title = title;
        this.value = value;
        this.date = new Date(year-1900, month, day);
        this.category = category;
        this.userId = userId;
    }

    public Transaction(int id, String title, float value, @NotNull String date, @NotNull String category, int userId){
        this.id = id;
        this.title = title;
        this.value = value;
        this.category = category;
        this.userId = userId;

        String [] details = date.split("-");
        this.date = new Date(
                Integer.parseInt(details[0]),
                Integer.parseInt(details[1]),
                Integer.parseInt(details[2])
                );
    }



    public ArrayList<Pair<String, String>> getTransactionInternalData(){
        ArrayList <Pair<String, String>> res = new ArrayList<>();
        //res.add(new Pair<>("id", new Integer(id).toString()));
        res.add(new Pair<>("title", title));
        res.add(new Pair<>("date", date.toString()));
        res.add(new Pair<>("value", Float.toString(value)));
        res.add(new Pair<>("userId", Integer.toString(userId)));
        return res;
    }

    public int getId(){return  id;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getUserId(){return userId;}
}
