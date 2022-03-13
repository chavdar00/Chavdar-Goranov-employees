package employees;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public final class Parser {
    private static final String CSV_SPLITTER = ", ";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Parser() {

    }

    public static List<Record> parseFile(String fileName) throws Exception {
        String extension = getFileExtension(fileName);
        if (extension.equals("csv")) {
            return readCSV(fileName);
        } else if (extension.equals("json")) {
            return readJSON(fileName);
        } else {
            throw new Exception("Unsupported file type " + extension);
        }
    }

    private static List<Record> readJSON(String fileName) throws IOException {
        File file = new File(fileName);
        String jsonFromFile = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        List<RecordFromJson> records = objectMapper.readValue(jsonFromFile, new TypeReference<List<RecordFromJson>>() {
        });
        return mapRecordsFromJsonsRecords(records);
    }

    private static List<Record> mapRecordsFromJsonsRecords(List<RecordFromJson> jsonRecords) {
        List<Record> records = new ArrayList<>();
        for (RecordFromJson r : jsonRecords) {
            Record record = new Record();
            record.setEmployeeId(r.getEmployeeId());
            record.setProjectId(r.getProjectId());
            record.setDateFrom(LocalDate.parse(r.getDateFrom(), DATE_FORMAT));
            record.setDateTo(r.getDateTo().equalsIgnoreCase("NULL") ?
                    LocalDate.now() :
                    LocalDate.parse(r.getDateTo(), DATE_FORMAT));
            records.add(record);
        }
        return records;
    }

    private static List<Record> readCSV(String fileName) {
        String line = "";
        List<Record> records = new ArrayList<>();

        try( BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] record = line.split(CSV_SPLITTER);
                Record e = new Record();
                e.setEmployeeId(record[0]);
                e.setProjectId(record[1]);
                e.setDateFrom(LocalDate.parse(record[2], DATE_FORMAT));
                e.setDateTo(record[3].equalsIgnoreCase("NULL") ?
                        LocalDate.now() :
                        LocalDate.parse(record[3], DATE_FORMAT));
                records.add(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static String getFileExtension(String fileName) {
        String extension = "";
        int index = fileName.indexOf('.');
        if (index > 0) {
            extension = fileName.substring(index + 1);
        }
        return extension;
    }
}
