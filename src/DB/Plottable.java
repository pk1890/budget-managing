package DB;

import Util.Interval;
import javafx.scene.chart.XYChart;

public interface Plottable {
    XYChart.Series getPlotData(Interval interval);
}
