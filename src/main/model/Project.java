package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo>, Observer {
    private List<Todo> tasks;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        if (description == null || description.length() == 0) {
            throw new EmptyStringException("Cannot construct a project with no description");
        }
        tasks = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (!contains(task) && !equals(task)) {
            tasks.add(task);
            task.addObserver(this);
            update(task,null);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (contains(task)) {
            tasks.remove(task);
            task.deleteObserver(this);
            update(task,null);
        }
    }

    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    @Override
    public int getEstimatedTimeToComplete() {
        return etcHours;
    }

    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
//     the percentage of completion (rounded down to the nearest integer).
//     the value returned is the average of the percentage of completion of
//     all the tasks and sub-projects in this project.
    public int getProgress() {
        int sum = 0;
        for (Todo t : tasks) {
            sum += t.getProgress();
        }
        return tasks.size() == 0 ? 0 : sum / tasks.size();
    }

    // EFFECTS: returns the number of tasks (and sub-projects) in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }

    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
//     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }

    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException("Illegal argument: task is null");
        }
        return tasks.contains(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new TodoIterator(); //stub
    }

    // EFFECTS: updates estimated completion time
    @Override
    public void update(Observable o, Object arg) {
        etcHours = 0;
        for (Todo t : tasks) {
            etcHours += t.getEstimatedTimeToComplete();
        }
        setChanged();
        notifyObservers();
    }

    // inner class
    private class TodoIterator implements Iterator<Todo> {
        private int index;
        private int priorityLevel;

        public TodoIterator() {
            index = 0;
            priorityLevel = 1;
        }

        @Override
        public boolean hasNext() {
            if (priorityLevel > 4 || tasks.size() == 0) {
                return false;
            }
            int currentIndex = index;
            int currentPriority = priorityLevel;
            Todo next = findNextValid();
            index = currentIndex;
            priorityLevel = currentPriority;

            return next != null;
        }

        @Override
        public Todo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return findNextValid();
        }

        // MODIFIES: this
        // EFFECTS: returns the next valid to do object, or null if none found
        private Todo findNextValid() {

            while (priorityLevel < 5) {
                Todo current = tasks.get(index);
                int currentp = priorityLevel;
                index++;

                if (index >= tasks.size()) {
                    index = 0;
                    priorityLevel++;
                }

                if (current.getPriority().equals(new Priority(currentp))) {
                    return current;
                }
            }
            return null;
        }
    }

}