package DB;

import Util.Interval;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
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

    public double sum(){
        double sum = 0;

        for (Transaction tr:list) {
            sum += tr.getValue();
        }
        sum = Math.round(sum*100.0)/100.0;
        return sum;
    }

    public double average(){
        return list.size() != 0 ? sum()/list.size() : 0;
    }

    public SortedTransactionList getTransactionsToDate(Date date){
        int index = 0;
        //date.setYear(date.getYear()-1900);
        while(index < this.list.size() && this.list.get(index).getDate().compareTo(date) <= 0 ){
            index++;
        }
        SortedTransactionList resultList = new SortedTransactionList(this.list.subList(0, index));
        System.out.println("indeks: " + index + " rok: " + date.getYear() + " mies: " +  date.getMonth() + " dzien: " + date.getDay() + "data text: "+ date.toString());
//        date = this.list.get(index).getDate();
        System.out.println("DATA W INDEKSIE: " + index + " rok: " + date.getYear() + " mies: " +  date.getMonth() + " dzien: " + date.getDay() + "data text: "+ date.toString());
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
        LocalDate now = LocalDate.now();

        switch (interval){
            default:
                for(int i = 1; i <= now.getDayOfMonth(); i++){

                    Date date = new Date(
                            now.getYear(),
                            now.getMonthValue(),
                            i
                    );

//                    System.out.println("Data: " + date.toString() + ",    Saldo: " + this.getTransactionsFromDate(date).sum());


                    result.getData().add(new XYChart.Data(i, getTransactionsToDate(date).sum()));
                }

        }
        return result;
    }




}
