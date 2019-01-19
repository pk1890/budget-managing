package DB;

import Util.Interval;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;
import java.util.List;

public class SortedTransactionList implements Plottable {
    public List<Transaction> list;


    public SortedTransactionList(List<Transaction> list){
        this.list = list;
    }

    public SortedTransactionList(){
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

    public SortedTransactionList getTransactionsToDate(Date date){
        int index = 0;
        while(index < this.list.size() && this.list.get(index).getDate().compareTo(date) <= 0 ){
            index++;
        }
        SortedTransactionList resultList = new SortedTransactionList(this.list.subList(0, index));
        System.out.println(index + " " + date.getYear() + " " +  date.getMonth() + " " + date.getDay());
        return resultList;
    }

    public SortedTransactionList getTransactionsFromDate(Date date){
        int index = 0;
        while(index < this.list.size() && this.list.get(index).getDate().compareTo(date) < 0 ){
            index++;
        }
        SortedTransactionList resultList = new SortedTransactionList(this.list.subList(index, this.list.size()));

        return resultList;
    }

    public SortedTransactionList getTransactionsBetweenDates(Date start, Date end){
        return this.getTransactionsFromDate(start).getTransactionsToDate(end);
    }

    @Override
    public XYChart.Series getPlotData(Interval interval) {
        XYChart.Series result = new XYChart.Series();
        java.util.Date date =  new java.util.Date();
        System.out.println(date);

        switch (interval){
            default:
                for(int i = 1; i <= Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH); i++){
                  //  result.getData().add(new XYChart.Data(i, this.getTransactionsToDate(new Date(
                    //        date
                   // )).sum()
                  //  ));
                }

        }
        return result;
    }


}
