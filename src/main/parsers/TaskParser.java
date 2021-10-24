package parsers;

import model.*;
import model.exceptions.InvalidPriorityLevelException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

// Represents Task parser
public class TaskParser {
    private List<Task> tbr;
    private String description = "";
    private JSONObject priority = null;
    private JSONObject dueDate = null;
    private String status = "";
    private Object tags = null;
    private boolean isMissingField = false;

    //EFFECTS: checks to see if a JSONObject has a "description" tag
    private void checkHasDesc(JSONObject taskJson) {
        if (taskJson.has("description")) {
            description = taskJson.getString("description");
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: checks to see if a JSONObject has a "priority" tag
    private void checkHasPrior(JSONObject taskJson) {
        if (taskJson.has("priority")) {
            priority = (JSONObject) taskJson.get("priority");
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: checks to see if a JSONObject has a "due-date" tag
    private void checkHasDD(JSONObject taskJson) {
        if (taskJson.has("due-date")) {
            dueDate = (JSONObject) taskJson.get("due-date");
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: checks to see if a JSONObject has a "status" tag
    private void checkHasStatus(JSONObject taskJson) {
        if (taskJson.has("status")) {
            status = taskJson.getString("status");
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: checks to see if a JSONObject has a "tags" tag
    private void checkHasTags(JSONObject taskJson) {
        if (taskJson.has("tags")) {
            tags = taskJson.get("tags");
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: checks the entireJsonArray to ensure that all tags needed for Task parsing are present.
    private void checkHasStuff(JSONObject taskJson) {
        checkHasDesc(taskJson);
        checkHasPrior(taskJson);
        checkHasStatus(taskJson);
        checkHasTags(taskJson);
    }


    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        JSONArray ja = new JSONArray(input);
        tbr = new ArrayList<>();
        for (Object object : ja) {
            JSONObject taskJson = (JSONObject) object;
            isMissingField = false;
            checkHasStuff(taskJson);
            try {
                checkHasDD(taskJson);
            } catch (ClassCastException e) {
                dueDate = null;
            }
            try {
                tbr.add(dealWithInformation(description, dueDate, priority, status, tags));
            } catch (Exception e) {
                System.out.println("Task had trouble being parsed");
            }
        }
        return tbr;
    }

    //EFFECTS: updates the hour, minute of a calendar from the JSON Object
    private void checkHM(Calendar c) {
        if (dueDate.has("hour")) {
            c.set(Calendar.HOUR_OF_DAY, dueDate.getInt("hour"));
        } else {
            isMissingField = true;
            return;
        }
        if (dueDate.has("minute")) {
            c.set(Calendar.MINUTE, dueDate.getInt("minute"));
        } else {
            isMissingField = true;
        }
    }

    //EFFECTS: updates the year, month, date of a calendar from the JSON Object
    private void checkHasymd(Calendar c) {
        if (dueDate.has("year")) {
            c.set(Calendar.YEAR, dueDate.getInt("year"));
        } else {
            isMissingField = true;
        }
        if (dueDate.has("month")) {
            c.set(Calendar.MONTH, dueDate.getInt("month"));
        } else {
            isMissingField = true;
        }
        if (dueDate.has("day")) {
            c.set(Calendar.DATE, dueDate.getInt("day"));
        } else {
            isMissingField = true;
        }
        checkHM(c);
    }

    //EFFECTS: returns a new DueDate Object instantiated to a given time.
    private DueDate getDueDate() {
        Calendar c = Calendar.getInstance();
        checkHasymd(c);
        DueDate tbr = new DueDate();
        tbr.setDueDate(c.getTime());
        return tbr;
    }

    //EFFECTS: Consumes two boolean variables and assigns the corresponding Priority
    private Priority switchPriority(boolean important, boolean urgent) {
        if (important && urgent) {
            return new Priority(1);
        } else if (important) {
            return new Priority(2);
        } else if (urgent) {
            return new Priority(3);
        } else {
            return new Priority(4);
        }
    }

    //EFFECTS: Consumes a JSON Object and looks for both "important" and "urgent" tags. Returns the proper
    //         priority corresponding to important and urgent. Throws InvalidPriorityLevelException if a
    //         field is missing.
    private Priority checkPriority(Object priority) {
        JSONObject x = (JSONObject) priority;
        boolean important;
        boolean urgent;
        if (!x.has("important") || !x.has("urgent")) {
            throw new InvalidPriorityLevelException();
        }
        important = x.getBoolean("important");
        urgent = x.getBoolean("urgent");
        return switchPriority(important, urgent);
    }

    //EFFECTS: Consumes a String and returns the corresponding status and null otherwise;
    private Status checkStatus(String s) {
        if (s.equalsIgnoreCase("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else if (s.equalsIgnoreCase("UP_NEXT")) {
            return Status.UP_NEXT;
        } else if (s.equalsIgnoreCase("DONE")) {
            return Status.DONE;
        } else if (s.equalsIgnoreCase("TODO")) {
            return Status.TODO;
        } else {
            status = null;
            return null;
        }
    }

    //EFFECTS: Consumes a JSON object containing tags and a Task. Assigns the tags to the Task
    private void addTags(Task task, Object tags) {
        JSONArray t = (JSONArray) tags;
        Object current;
        JSONObject c;
        if (tags == null) {
            return;
        }
        for (int i = 0; i < t.length(); i++) {
            current = t.get(i);
            c = (JSONObject) current;
            if (c.has("name")) {
                task.addTag(new Tag((String) c.get("name")));
            } else {
                isMissingField = true;
            }
        }

    }

    //EFFECTS: Consumes a description, due date, priority, status, and tags and assigns the fields to a Task Object
    //         accordingly and returns the object. Throws a JSON Exception if any field is missing.
    private Task dealWithInformation(String description, JSONObject dueDate, Object priority, String status,
                                     Object tags) {
        Task tbr = new Task(description);
        if (dueDate != null) {
            tbr.setDueDate(getDueDate());
        } else {
            tbr.setDueDate(null);
        }
        tbr.setPriority(checkPriority(priority));
        tbr.setStatus(checkStatus(status));
        addTags(tbr, tags);
        if (isMissingField) {
            throw new JSONException("Task had trouble being parsed");
        }
        //System.out.println(tbr);
        return tbr;
    }


}
