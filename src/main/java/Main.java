import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String fileCsvName = "data.csv";
        String fileJson = "data.json";
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

        List<Employee> employeeList = parseCSV(columnMapping, fileCsvName);
        List<String> stringListJson = listToJson(employeeList);
        writeString(stringListJson,fileJson);

    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileCsvName){
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping(columnMapping);
        CsvToBean<Employee> csv = null;
        try {
         csv = new CsvToBeanBuilder<Employee>(new CSVReader(Files.newBufferedReader(Path.of(fileCsvName))))
                .withMappingStrategy(strategy)
                .build();
        }catch (IOException e){
            e.printStackTrace();
        }
        assert csv != null;
        return csv.parse();
    }

    private static List<String> listToJson(List<Employee> list){
        List<String> listJson = new ArrayList<>();
        for (Employee employee : list) {
            String gson = new GsonBuilder().create().toJson(employee, Employee.class);
            listJson.add(gson);
        }
        return listJson;
    }

    private static void writeString(List<String> stringListJson, String fileJson){
        try(FileWriter fileWriter = new FileWriter(fileJson))
        {
            fileWriter.write(String.valueOf(stringListJson));
            fileWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
