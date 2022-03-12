package employees;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Employee> employees = readCSV();
        //projectId and list of employees
        Map<Integer, List<Employee>> map = putEmployeesInMapByProjectId(employees);
        List<Pair> pairs = getPairs(map);

        pairs.sort((p1,p2)-> (int) (p2.getDays()-p1.getDays()));
        System.out.println(pairs.get(0));
    }

    private static List<Pair> getPairs(Map<Integer, List<Employee>> map) {
        List<Pair> pairs = new ArrayList<>();

        for (Map.Entry<Integer, List<Employee>> entry : map.entrySet()) {
            List<Employee> e = entry.getValue();
            for (int i = 0; i < e.size(); i++) {
                for (int j = i + 1; j < e.size(); j++) {

                    long start = Math.max(e.get(i).getDateFrom().toEpochDay(), e.get(j).getDateFrom().toEpochDay());
                    long end = Math.min(e.get(i).getDateTo().toEpochDay(), e.get(j).getDateTo().toEpochDay());

                    long time = ChronoUnit.DAYS.between(LocalDate.ofEpochDay(start), LocalDate.ofEpochDay(end));
                    if (time > 0) {
                        Pair pair = new Pair();
                        pair.setSecondEmployee(e.get(i).getEmployeeId());
                        pair.setFirstEmployee(e.get(j).getEmployeeId());
                        pair.setDays(time);
                        boolean isPairAlreadySaved = false;
                        for (Pair p :pairs){
                            if((p.getSecondEmployee() == pair.getSecondEmployee() && p.getFirstEmployee()==pair.getFirstEmployee())
                                    || (p.getSecondEmployee()==pair.getFirstEmployee() && p.getFirstEmployee()== pair.getSecondEmployee())){
                                p.setDays(p.getDays()+time);
                                isPairAlreadySaved=true;
                            }
                        }
                        if(!isPairAlreadySaved){
                            pairs.add(pair);
                        }
                    }
                }
            }
        }
        return pairs;
    }

    private static Map<Integer, List<Employee>> putEmployeesInMapByProjectId(List<Employee> employees) {
        HashMap<Integer, List<Employee>> map = new HashMap<>();
        for (Employee e : employees) {

            if (!map.containsKey(e.getProjectId())) {
                map.put(e.getProjectId(), new ArrayList<>());
            }
            List<Employee> projectEmployees = map.get(e.getProjectId());
            projectEmployees.add(e);
            map.put(e.getProjectId(), projectEmployees);
        }
        return map;
    }

    public static List<Employee> readCSV() {
        String line = "";
        String splitBy = ", ";
        List<Employee> employees = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("test.csv"));
            while ((line = bufferedReader.readLine()) != null) {
                String[] employee = line.split(splitBy);
                Employee e = new Employee();
                e.setEmployeeId(Integer.parseInt(employee[0]));
                e.setProjectId(Integer.parseInt(employee[1]));
                e.setDateFrom(LocalDate.parse(employee[2], formatter));
                e.setDateTo(employee[3].equalsIgnoreCase("NULL") ? LocalDate.now() : LocalDate.parse(employee[3], formatter));
                employees.add(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }
}
