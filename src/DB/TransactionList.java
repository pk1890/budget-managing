package DB;

import com.sun.org.glassfish.external.statistics.Statistic;

import java.util.ArrayList;
import java.util.Iterator;

public class TransactionList{
    public ArrayList<Transaction> list;


    public TransactionList(ArrayList<Transaction> list){
        this.list = list;
    }

    public TransactionList(){
        list = new ArrayList<Transaction>();
    }

    public void add(Transaction tr){
        list.add(tr);
    }

    public float sum(){
        float sum = 0;

        for (Transaction tr:list) {
            sum += tr.getValue();
        }
        return sum;
    }

    public float average(){
        return list.size() != 0 ? sum()/list.size() : 0;
    }



}
