package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import model.exceptions.NullArgumentException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        if (tag == null) {
            throw new NullArgumentException("Cannot convert a null tag to JSONObject");
        }
        JSONObject tbr = new JSONObject();
        tbr.put("name", tag.getName());
        return tbr;
    }

    //EFFECTS: Enters "important" and "urgent" tags into a JSON Object based off of a given Priority
    private static JSONObject check(Priority priority, JSONObject a) {
        if (priority.isImportant() && priority.isUrgent()) {
            a.put("important", true);
            a.put("urgent", true);
        } else if (priority.isImportant()) {
            a.put("important", true);
            a.put("urgent", false);
        } else if (priority.isUrgent()) {
            a.put("important", false);
            a.put("urgent", true);
        } else {
            a.put("important", false);
            a.put("urgent", false);
        }
        return a;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        if (priority == null) {
            throw new NullArgumentException("Cannot convert a null priority to JSONObject");
        }
        JSONObject a = new JSONObject();
        a = check(priority, a);
        return a;
    }

    //EFFECTS: adds date tags into a JSON Array
    private static JSONArray addCategories(DueDate dueDate, JSONArray names) {
        names.put("year");
        names.put("month");
        names.put("day");
        names.put("hour");
        names.put("minute");
        return names;
    }

    // EFFECTS: returns JSON representation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        if (dueDate == null) {
            return null;
        }
        JSONArray names = new JSONArray();
        names = addCategories(dueDate, names);
        JSONArray tbr = new JSONArray();
        Calendar c = Calendar.getInstance();
        c.setTime(dueDate.getDate());
        tbr.put(c.get(Calendar.YEAR));
        tbr.put(c.get(Calendar.MONTH));
        tbr.put(c.get(Calendar.DATE));
        tbr.put(c.get(Calendar.HOUR_OF_DAY));
        tbr.put(c.get(Calendar.MINUTE));
        return tbr.toJSONObject(names);
    }

    //EFFECTS: Adds fields to a JSON Array of names to be used in combination
    private static JSONArray addNames(JSONArray names) {
        names.put("description");
        names.put("tags");
        names.put("due-date");
        names.put("priority");
        names.put("status");
        return names;
    }


    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONArray temp = new JSONArray();
        JSONArray names = new JSONArray();
        names = addNames(names);
        temp.put(task.getDescription());
        ArrayList<Tag> tags = new ArrayList<>(task.getTags());
        JSONArray toAddTags = new JSONArray();
        for (Tag t: tags) {
            toAddTags.put(tagToJson(t));
        }
        temp.put(toAddTags);
        if (task.getDueDate() == null) {
            temp.put(JSONObject.wrap(null));
        } else {
            temp.put(dueDateToJson(task.getDueDate()));
        }
        temp.put(priorityToJson(task.getPriority()));
        temp.put(task.getStatus());
        return temp.toJSONObject(names);
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray tbr = new JSONArray();
        for (int i = 0; i < tasks.size(); i++) {
            tbr.put(taskToJson(tasks.get(i)));
        }
        return tbr;
    }
}
