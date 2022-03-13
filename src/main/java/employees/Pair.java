package employees;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pair {
    private String firstEmployee;
    private String secondEmployee;
    private int days;


    public String getFirstEmployee() {
        return firstEmployee;
    }

    public void setFirstEmployee(String firstEmployee) {
        this.firstEmployee = firstEmployee;
    }

    public String getSecondEmployee() {
        return secondEmployee;
    }

    public void setSecondEmployee(String secondEmployee) {
        this.secondEmployee = secondEmployee;
    }

    public int getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public static List<Pair> getPairs(List<Record> records) {
        List<Pair> pairs = new ArrayList<>();
        Map<String, List<Record>> map = putRecordsInMapByProjectId(records);
        for (Map.Entry<String, List<Record>> entry : map.entrySet()) {
            checkAllPossiblePairs(entry.getValue(), pairs);
        }
        return pairs;
    }

    private static void checkAllPossiblePairs(List<Record> records, List<Pair> pairs) {
        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                int time = calculateTimeWorked(records.get(i), records.get(j));
                if (time > 0) {
                    Pair pair = createPair(records.get(i), records.get(j), time);
                    checkForExistingPair(pairs, pair, time);
                }
            }
        }
    }

    private static Pair createPair(Record firstRecord, Record secondRecord, int time) {
        Pair pair = new Pair();
        pair.setSecondEmployee(firstRecord.getEmployeeId());
        pair.setFirstEmployee(secondRecord.getEmployeeId());
        pair.setDays(time);
        return pair;
    }

    private static int calculateTimeWorked(Record firstRecord, Record secondRecord) {
        long start = Math.max(firstRecord.getDateFrom().toEpochDay(), secondRecord.getDateFrom().toEpochDay());
        long end = Math.min(firstRecord.getDateTo().toEpochDay(), secondRecord.getDateTo().toEpochDay());

        return Math.toIntExact(ChronoUnit.DAYS.between(LocalDate.ofEpochDay(start), LocalDate.ofEpochDay(end)));
    }

    private static void checkForExistingPair(List<Pair> pairs, Pair pair, int time) {
        boolean isPairAlreadySaved = false;
        for (Pair p : pairs) {
            if ((p.getSecondEmployee().equals(pair.getSecondEmployee()) && p.getFirstEmployee().equals(pair.getFirstEmployee()))
                    || (p.getSecondEmployee().equals(pair.getFirstEmployee()) && p.getFirstEmployee().equals(pair.getSecondEmployee()))) {
                p.setDays(Math.toIntExact(p.getDays() + time));
                isPairAlreadySaved = true;
            }
        }
        if (!isPairAlreadySaved) {
            pairs.add(pair);
        }
    }

    private static Map<String, List<Record>> putRecordsInMapByProjectId(List<Record> records) {
        HashMap<String, List<Record>> map = new HashMap<>();
        if (records != null) {
            for (Record e : records) {

                if (!map.containsKey(e.getProjectId())) {
                    map.put(e.getProjectId(), new ArrayList<>());
                }
                List<Record> projectRecords = map.get(e.getProjectId());
                projectRecords.add(e);
                map.put(e.getProjectId(), projectRecords);
            }
        }
        return map;
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
