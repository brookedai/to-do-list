package parsers;

import model.DueDate;
import model.Priority;
import model.Status;
import model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents Task parser
public class TaskParser {

    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        List<Task> finalList = new ArrayList<Task>();

        String[] tasks = getTasks(input);
        for (String stringTask : tasks) {
            String[][] components = taskComponents(stringTask);
            Task task = new Task("none");
            try {
                initializeTask(task, components);
                finalList.add(task);
            } catch (Exception e) {
                // go to next task
            }
        }
        return finalList;   // stub
    }

    // EFFECTS: takes a JSONArray of tasks represented as a string input and creates a
    //          string array with each individual task
    private String[] getTasks(String input) {
        String bracketsRemoved = "";
        if (input.length() > 2) {
            bracketsRemoved = input.substring(2, input.length() - 2);
            String[] tasks = bracketsRemoved.split("},\\{(?!\\\"name\\\")");
            return tasks;
        } else {
            return new String[0];
        }
    }

    // EFFECTS: takes a JSONObject task represented as a string input and creates a
    //          string array of all the different components of the task in the form
    //          [description, tags, due-date, priority, status]
    private String[][] taskComponents(String input) {
        String[][] components = new String[5][2];
        String unfinished = input;
        String[] componentValue = unfinished.split(",(?=\\\"(due-date|description|priority|status|tags)\\\":)");

        for (int i = 0; i < components.length; i++) {
            String[] value = componentValue[i].split(":", 2);
            components[i][0] = cleanUp(value[0], 1);
            components[i][1] = value[1];
        }
        String[][] componentsFormatted = format(components, "description", "tags", "due-date", "priority", "status");
        return componentsFormatted;
    }

    // REQUIRES: unformatted is String[5][2]
    // EFFECTS: formats the unformatted String array into an array in the form
    //          [s1, s2, s3, s4, s5]
    private String[][] format(String[][] unformatted, String s1, String s2, String s3, String s4, String s5) {
        String[][] formatted = new String[5][2];
        for (int i = 0; i < unformatted.length; i++) {
            if (unformatted[i][0].equals(s1)) {
                formatted[0] = unformatted[i];
            } else if (unformatted[i][0].equals(s2)) {
                formatted[1] = unformatted[i];
            } else if (unformatted[i][0].equals(s3)) {
                formatted[2] = unformatted[i];
            } else if (unformatted[i][0].equals(s4)) {
                formatted[3] = unformatted[i];
            } else if (unformatted[i][0].equals(s5)) {
                formatted[4] = unformatted[i];
            }
        }
        return formatted;
    }

    // REQUIRES: components.length == 5
    //           components is ordered as follows:
    //           {description, tags, due-date, priority, status}
    // MODIFIES: task
    // EFFECTS: sets all fields of task to values given in components array
    private void initializeTask(Task task, String[][] components) {
        setDescription(task, components[0]);
        setTags(task, components[1]);
        setDueDate(task, components[2]);
        setPriority(task, components[3]);
        setStatus(task, components[4]);
    }

    // MODIFIES: task
    // EFFECTS: adds description to task
    private void setDescription(Task task, String[] component) {
        if (component[0].equals("description")) {
            task.setDescription(cleanUp(component[1], 1));
        } else {
            throw new RuntimeException("Description field incorrectly represented as " + component[0]);
        }
    }

    // MODIFIES: task
    // EFFECTS: adds tags in component to task
    private void setTags(Task task, String[] component) {
        if (component[0].equals("tags")) {
            String[] tags = getTagNames(component[1]);
            for (String t : tags) {
                task.addTag(t);
            }
        } else {
            throw new RuntimeException("Tags field incorrectly represented as " + component[0]);
        }
    }

    // EFFECTS: takes a JSONArray of JSONObject tags and creates a string array
    //          with the names of all the tags
    private String[] getTagNames(String input) {
        String cleanedInput = cleanUp(input, 1);
        String[] tags = cleanedInput.split(",");
        String[] finalTags = new String[tags.length];
        for (int i = 0; i < tags.length; i++) {
            String[] separated = cleanUp(tags[i], 1).split(":");
            if (separated.length == 1) {
                return new String[0];
            } else if (separated[1].charAt(0) != '"' || separated[1].charAt(separated[1].length() - 1) != '"') {
                throw new RuntimeException("Given tag is not a string: " + separated[1]);
            }
            finalTags[i] = cleanUp(separated[1], 1);
        }
        return finalTags;
    }


    // MODIFIES: task
    // EFFECTS: sets t DueDate to duedate in component
    private void setDueDate(Task task, String[] component) {
        if (component[0].equals("due-date")) {
            DueDate d = null;
            String[] dateComponents = cleanUp(component[1], 1).split(",");
            if (dateComponents.length == 5) {
                String[][] componentValue = format(dateValues(dateComponents),"year", "month", "day","hour","minute");

                Calendar c = Calendar.getInstance();
                c.set(Integer.parseInt(componentValue[0][1]), Integer.parseInt(componentValue[1][1]),
                        Integer.parseInt(componentValue[2][1]), Integer.parseInt(componentValue[3][1]),
                        Integer.parseInt(componentValue[4][1]));
                d = new DueDate();
                d.setDueDate(c.getTime());
            } else if (!component[1].equals("null")) {
                throw new RuntimeException(component[1] + " is not a valid DueDate");
            }
            task.setDueDate(d);
        } else {
            throw new RuntimeException("DueDate field incorrectly represented as " + component[0]);
        }
    }

    // EFFECTS: takes a JSONObject DueDate and creates a list of values in the form
    //          [year, month, day, hour, minute]
    private String[][] dateValues(String[] input) {
        String[][] componentValue = new String[5][2];
        for (int i = 0; i < componentValue.length; i++) {
            String[] value = input[i].split(":");
            if (value[1].charAt(0) == '"' || value[1].charAt(value[1].length() - 1) == '"') {
                throw new RuntimeException("DueDate field incorrectly represented as " + value[1]);
            }
            value[0] = cleanUp(value[0],1);
            componentValue[i] = value;
        }
        return componentValue;
    }

    // MODIFIES: task
    // EFFECTS: sets t priority to priority in component
    private void setPriority(Task task, String[] component) {
        if (component[0].equals("priority")) {
            Priority p = new Priority();
            String[] importantUrgent = cleanUp(component[1], 1).split(",");
            setPriority(p, importantUrgent);
            task.setPriority(p);
        } else {
            throw new RuntimeException("Priority field incorrectly represented as " + component[0]);
        }
    }


    // MODIFIES: priority
    // EFFECTS: sets priority based on component
    private void setPriority(Priority priority, String[] components) {
        String[] important = components[0].split(":");
        String[] urgent = components[1].split(":");
        if (!important[1].equals("true") && !important[1].equals("false")) {
            throw new RuntimeException("Priority field type not allowed: " + important[1]);
        }
        if (!urgent[1].equals("true") && !urgent[1].equals("false")) {
            throw new RuntimeException("Priority field type not allowed: " + urgent[1]);
        }

        boolean isImportant = Boolean.parseBoolean(important[1]);
        boolean isUrgent = Boolean.parseBoolean(urgent[1]);

        priority.setImportant(isImportant);
        priority.setUrgent(isUrgent);
    }

    // MODIFIES: task
    // EFFECTS: sets task status to status in component
    private void setStatus(Task task, String[] component) {
        if (component[0].equals("status")) {
            if (isStatus(component[1])) {
                setStatus(task, component[1]);
            } else {
                throw new RuntimeException("Status field type not allowed: " + component[1]);
            }
        } else {
            throw new RuntimeException("Status field incorrectly represented as " + component[0]);
        }
    }

    // MODIFIES: task
    // EFFECTS: sets task status to status in input
    private void setStatus(Task task, String input) {
        switch (cleanUp(input, 1)) {
            case "UP_NEXT":
                task.setStatus(Status.UP_NEXT);
                break;
            case "IN_PROGRESS":
                task.setStatus(Status.IN_PROGRESS);
                break;
            case "DONE":
                task.setStatus(Status.DONE);
                break;
            default:
                task.setStatus(Status.TODO);
        }
    }

    // EFFECTS: returns true if given string is a status with "" surrounding it
    private boolean isStatus(String input) {
        return (input.equals("\"TODO\"")
                || input.equals("\"UP_NEXT\"")
                || input.equals("\"IN_PROGRESS\"")
                || input.equals("\"DONE\""));
    }


    // EFFECTS: removes first and last character from input numTimes times
    private String cleanUp(String input, int numTimes) {
        String cleaned = input;
        if (input.length() == 0) {
            return input;
        }
        for (int i = 0; i < numTimes; i++) {
            cleaned = cleaned.substring(1, cleaned.length() - 1);
        }
        return cleaned;
    }

}
