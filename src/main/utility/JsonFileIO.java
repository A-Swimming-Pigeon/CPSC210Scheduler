package utility;

import model.Task;
import org.json.JSONArray;
import parsers.TaskParser;
import persistence.Jsonifier;

import java.io.*;
import java.util.List;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");

    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        File jsonDataFile = new File("./resources/json/tasks.json");
        FileReader fr = null;
        TaskParser parser = new TaskParser();
        String string = "";
        fr = getFileReader(jsonDataFile, fr);
        int i;
        try {
            while ((i = fr.read()) != -1) {
                string += (char) i;
                //System.out.print((char) i);
            }
        } catch (IOException e) {
            System.out.println("Something went wrong when converting to string");
        }
        //System.out.println(string);

        List<Task> temp;
        temp = parser.parse(string);
        return temp; // stub
    }

    private static FileReader getFileReader(File jsonDataFile, FileReader fr) {
        try {
            fr = new FileReader(jsonDataFile);
        } catch (FileNotFoundException e) {
            System.out.println("tasks.json file was not found.");
        }
        return fr;
    }

    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        JSONArray fileContent = Jsonifier.taskListToJson(tasks);
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(jsonDataFile);
            fileWriter.write(fileContent.toString());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("File had trouble being written to");
        }


    }
}
