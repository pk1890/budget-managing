package UI;

public enum WindowsTypes {
    MAIN("MainController.java"),
    HISTORY("HistoryController.java");

    String controllerPath;
    WindowsTypes(String path){
        this.controllerPath = path;
    }
}
