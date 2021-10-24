package model;

import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private String description;
    private List<Todo> tasks;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //          the constructed project shall have no tasks.
    //          throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        this.description = description;
        tasks = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //          throws NullArgumentException when task is null
    public void add(Todo task) {
        if (!contains(task) && !task.equals(this)) {
            tasks.add(task);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //          throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (contains(task)) {
            tasks.remove(task);
        }
    }

    @Override
    //EFFECTS: returns the sum of estimated time to complete for all tasks
    public int getEstimatedTimeToComplete() {
        int totalHours = 0;
        for (Todo t : tasks) {
            totalHours += t.getEstimatedTimeToComplete();
        }
        return totalHours;
    }

    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    //EFFECTS: returns the sum of all task progresses for all tasks under this Project
    private int taskProgress() {
        int tbr = 0;
        for (Todo t : tasks) {
            tbr += t.getProgress();
        }
        return tbr;
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
    //     the percentage of completed tasks (rounded down to the closest integer).
    //     returns 100 if this project has no tasks!
    public int getProgress() {
        float numerator = taskProgress();
        float denominator = getNumberOfTasks();
        if (numerator == 0 && denominator == 0) {
            return 0;
        } else {
            return (int) Math.floor(numerator / denominator);
        }
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
    //returns an Iterator that returns tasks in order of priority and order added
    public Iterator<Todo> iterator() {
        return new SortedTasksIterator();
    }

    //Represents an Iterator that returns tasks in order of priority and order added
    private class SortedTasksIterator implements Iterator<Todo> {
        private int index;
        private int priorityCheck;
        private int returnedCount;

        //EFFECTS: initializes iterator
        public SortedTasksIterator() {
            index = 0;
            priorityCheck = 0;
            returnedCount = 0;
        }


        //REQUIRES: 0 <= priorityCheck <= 4, 0 <= index < tasks.size()
        //EFFECTS: consumes an index and a priority level corresponding to the Priority class
        //         and compares the priority of tasks.get(index) to that priority level.
        //         returns the object if tasks.get(index).getPriority is equal to the priority
        //         we're looking for, null otherwise
        private Todo check(int index, int priorityCheck) {
            if (priorityCheck == 0) {
                if (tasks.get(index).getPriority().equals(new Priority(1))) {
                    return tasks.get(index);
                }
            } else if (priorityCheck == 1) {
                if (tasks.get(index).getPriority().equals(new Priority(2))) {
                    return tasks.get(index);
                }
            } else if (priorityCheck == 2) {
                if (tasks.get(index).getPriority().equals(new Priority(3))) {
                    return tasks.get(index);
                }
            } else {
                if (tasks.get(index).getPriority().equals(new Priority(4))) {
                    return tasks.get(index);
                }
            }
            return null;
        }


        @Override
        //EFFECTS: returns true if iterator has not returned all items within tasks
        public boolean hasNext() {
            return returnedCount < tasks.size();
        }

        @Override
        //EFFECTS: returns the next object in the iterator and throws a NoSuchElement
        //         Exception if there is no next element
        public Todo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Todo tbr = check(index, priorityCheck);
            index += 1;
            if (index == tasks.size()) {
                index = 0;
                priorityCheck += 1;
            }
            if (tbr != null) {
                returnedCount += 1;
                return tbr;
            } else {
                return next();
            }

        }
    }
}