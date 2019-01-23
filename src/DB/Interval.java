package DB;

public enum Interval {
    MONTH("Month"), YEAR("Year");

    String text;

    Interval(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
