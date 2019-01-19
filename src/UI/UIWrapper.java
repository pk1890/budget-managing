package UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

public final class UIWrapper {
    private UIWrapper(){
    }

    public static ObservableList<PieChart.Data> getBalancePieChartData(double income, double expense){
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        data.add(new PieChart.Data("Przychody", income));
        data.add(new PieChart.Data("Wydatki", expense));
        return data;
    }
}
