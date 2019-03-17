package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagJson = new JSONObject();
        tagJson.put("name", tag.getName());
        return tagJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {
        JSONObject priorityJson = new JSONObject();
        priorityJson.put("important", priority.isImportant());
        priorityJson.put("urgent", priority.isUrgent());
        return priorityJson;
    }

    // EFFECTS: returns JSON representation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        if (dueDate != null) {
            JSONObject dueDateJson = new JSONObject();
            Calendar c = Calendar.getInstance();
            c.setTime(dueDate.getDate());
            dueDateJson.put("year", c.get(Calendar.YEAR));
            dueDateJson.put("month", c.get(Calendar.MONTH));
            dueDateJson.put("day", c.get(Calendar.DAY_OF_MONTH));
            dueDateJson.put("hour", c.get(Calendar.HOUR_OF_DAY));
            dueDateJson.put("minute", c.get(Calendar.MINUTE));
            return dueDateJson;
        }
        return null;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskJson = new JSONObject();
        JSONArray tags = new JSONArray();
        for (Tag t : task.getTags()) {
            JSONObject tagJson = tagToJson(t);
            tags.put(tagJson);
        }

        JSONObject dueDateJson = Jsonifier.dueDateToJson(task.getDueDate());

        taskJson.put("description", task.getDescription());
        taskJson.put("tags", tags);
        taskJson.put("due-date", (dueDateJson == null) ? JSONObject.NULL : dueDateJson);
        taskJson.put("priority", Jsonifier.priorityToJson(task.getPriority()));
        taskJson.put("status", task.getStatus());
        return taskJson;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray tasksJson = new JSONArray();
        if (tasks.size() != 0) {
            for (Task t : tasks) {
                JSONObject taskJson = taskToJson(t);
                tasksJson.put(taskJson);
            }
        }
        return tasksJson;
    }
}
