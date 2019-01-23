package UI;

import com.sun.istack.internal.NotNull;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.Map;

abstract class UIWrapper {
    private UIWrapper(){
    }

    static ObservableList<PieChart.Data> getBalancePieChartData(double income, double expense){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Przychody", income));
        data.add(new PieChart.Data("Wydatki", expense));
        return data;
    }
    
    static ObservableList<PieChart.Data> getCategoriesPieChartData(@NotNull Map<String, Double> values){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        for (Map.Entry<String, Double> entry: values.entrySet()
             ) {
            data.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        return  data;
    }
}
