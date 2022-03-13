package employees;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Record> records = null;
        try {
            records = Parser.parseFile("test.csv");
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Pair> pairs = Pair.getPairs(records);

        if (!records.isEmpty()) {
            if (!pairs.isEmpty()) {
                pairs.sort((p1, p2) -> (p2.getDays() - p1.getDays()));
                System.out.println(pairs.get(0));
            } else {
                System.out.println("Not found employees have worked together on common project at the same period of time");
            }
        } else {
            System.out.println("File is empty");
        }

    }
}
