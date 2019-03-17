package utility;

import model.Task;
import org.json.JSONArray;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.*;
import java.util.List;
import java.util.Scanner;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");

    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() throws IOException {
        Scanner scanner = new Scanner(jsonDataFile);
        String str = "";
        while (scanner.hasNextLine()) {
            String tempStr = scanner.nextLine();
            str += tempStr.trim();
        }

        TaskParser parser = new TaskParser();
        List<Task> tasks = parser.parse(str);
        return tasks;
    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) throws IOException {
        FileWriter writer = new FileWriter(jsonDataFile);

        Jsonifier jsonifier = new Jsonifier();
        JSONArray jsonArray = jsonifier.taskListToJson(tasks);
        writer.write(jsonArray.toString());
        writer.close();
    }

}
