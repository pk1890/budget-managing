package DB;

public enum ComparisonMethod {
    DATE("Date"), VALUE("Value"), CATEGORY("Category");

    String text;

    ComparisonMethod(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
