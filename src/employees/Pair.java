package employees;

public class Pair {
    private int firstEmployee;
    private int secondEmployee;
    private long days;

    public int getFirstEmployee() {
        return firstEmployee;
    }

    public void setFirstEmployee(int firstEmployee) {
        this.firstEmployee = firstEmployee;
    }

    public int getSecondEmployee() {
        return secondEmployee;
    }

    public void setSecondEmployee(int secondEmployee) {
        this.secondEmployee = secondEmployee;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "firstEmployee=" + firstEmployee +
                ", secondEmployee=" + secondEmployee +
                ", days=" + days +
                '}';
    }
}
