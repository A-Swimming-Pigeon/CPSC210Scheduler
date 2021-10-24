package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class    TestProject {
    private Project one;

    @BeforeEach
    void runBefore() {
        one = new Project("Kittens are amazing.");
    }

    @Test
    void forceDefaultConstructorsForExceptions() {
        try {
            throw new InvalidProgressException();
        } catch (InvalidProgressException e) {

        }
        try {
            throw new NegativeInputException();
        } catch (NegativeInputException e) {

        }
    }

    @Test
    void testEmptyProjectProgress() {
        assertEquals(0, one.getEstimatedTimeToComplete());
        assertEquals(0, one.getProgress());
    }

    @Test
    void testAddMyself() {
        assertEquals(0, one.getNumberOfTasks());
        one.add(one);
        assertEquals(0, one.getNumberOfTasks());
    }

    @Test
    void testCompleted() {
        assertFalse(one.isCompleted());
        Task t = new Task("Copy a cat ## aa;");
        t.setProgress(100);
        one.add(t);
        assertEquals(100, t.getProgress());
        assertTrue(one.isCompleted());
        t.setProgress(50);
        assertFalse(one.isCompleted());

    }

    @Test
    void testEquals() {
        Project x = new Project("Project X");
        Project y = new Project("Project X");
        assertEquals(x, x);
        assertEquals(x, y);
        y = new Project("Project Y");
        assertNotEquals(x, y);
        assertNotEquals(x, new Tag("Tag"));
        System.out.println(x.hashCode());
    }

    @Test
    void testConstructor() {
        assertEquals("Kittens are amazing.", one.getDescription());
    }

    @Test
    void testAddTask() {
        one.add(new Task("Task_one"));
        assertEquals(1, one.getNumberOfTasks());
    }

    @Test
    void testRemoveTask() {
        one.add(new Task("Task_one"));
        one.remove(new Task("Kitty"));
        assertEquals(1, one.getNumberOfTasks());
        one.remove(new Task("Task_one"));
        assertEquals(0, one.getNumberOfTasks());
    }

    @Test
    void testGetProgress() {
        Task t1 = new Task("Task 1 ## tag;");
        Task t2 = new Task("Task 2 ## tag;");
        Task t3 = new Task("Task 3 ## tag;");
        Project p = new Project("This is a Project");
        assertEquals(0, p.getProgress());
        assertEquals(0, p.getNumberOfTasks());
        p.add(t2);
        p.add(t3);
        assertEquals(2, p.getNumberOfTasks());
        assertEquals(0, p.getProgress());
        t1.setProgress(100);
        p.add(t1);
        assertEquals(3, p.getNumberOfTasks());
        assertEquals(33, p.getProgress());
        p = new Project("Meowmeowmeow");
        t2.setProgress(50);
        t3.setProgress(25);
        p.add(t1);
        p.add(t2);
        p.add(t3);
        assertEquals(58, p.getProgress());
        Project p2 = new Project("mrow?");
        Task t4 = new Task("Task 4 ## today;");
        p2.add(t4);
        p2.add(p);
        assertEquals(29, p2.getProgress());

    }

    @Test
    void testGetEtc() {
        Task t1 = new Task("Task 1 ## tag;");
        Task t2 = new Task("Task 2 ## tag;");
        Task t3 = new Task("Task 3 ## tag;");
        Project p = new Project("This is a Project");
        p.add(t1);
        p.add(t2);
        p.add(t3);
        assertEquals(0, p.getEstimatedTimeToComplete());
        t1.setEstimatedTimeToComplete(8);
        assertEquals(8, p.getEstimatedTimeToComplete());
        t2.setEstimatedTimeToComplete(2);
        t3.setEstimatedTimeToComplete(10);
        assertEquals(20, p.getEstimatedTimeToComplete());
        Project p2 = new Project("https://xkcd.com/1597/");
        Task t4 = new Task("This is a task ## tag;");
        t4.setEstimatedTimeToComplete(4);
        p2.add(t4);
        p2.add(p);
        assertEquals(24, p2.getEstimatedTimeToComplete());

    }

    @Test
    void testContains() {
        one.add(new Task("Zero"));
        one.add(new Task("One"));
        one.add(new Task("Two"));
        one.add(new Task("Three"));
        assertTrue(one.contains(new Task("Zero")));
        assertFalse(one.contains(new Task("Meow")));
    }

    @Test
    void testExceptions() {
        try {
            one.getTasks();
            fail("Deprecated method used");
        } catch (Exception e) {

        }
        Project two = new Project("Cat");
        try {
            two = new Project("");
            fail("Did not throw empty string exception");
        } catch (EmptyStringException e ) {

        }
        assertEquals("Cat", two.getDescription());
        try {
            two = new Project(null);
            fail("Did not throw empty string exception");
        } catch (EmptyStringException e ) {

        }
        assertEquals("Cat", two.getDescription());
        try {
            two.add(null);
            fail("Did not throw null argument exception");
        } catch (NullArgumentException e ) {

        }
        //assertEquals(0, two.getTasks().size());
        try {
            two.remove(null);
            fail("Did not throw null argument exception");
        } catch (NullArgumentException e ) {

        }
        //assertEquals(0, two.getTasks().size());
        try {
            two.contains(null);
            fail("Did not throw null argument exception");
        } catch (NullArgumentException e ) {

        }
        //assertEquals(0, two.getTasks().size());
        try {
            two.add(new Task("SOME TASK"));

        } catch (NullArgumentException e ) {
            fail("Threw null argument exception");
        }
        //assertEquals(1, two.getTasks().size());
        try {
            two.add(new Task("SOME TASK"));

        } catch (NullArgumentException e ) {
            fail("Threw null argument exception");
        }
        //assertEquals(1, two.getTasks().size());
    }


    @Test
    void testIterator() {
        Project project = new Project("Some description");
        Task t = new Task("IU ## important; urgent;");
        Task u = new Task("U ## tag; urgent;");
        Task v = new Task("D ## tag;");
        Task w = new Task("I ## tag; important;");
        Task x = new Task("IU2 ## tag; important; urgent;");
        Project p2 = new Project("P2");
        p2.setPriority(new Priority(2));
        p2.add(new Task("This is a task ## important;"));
        project.add(t);
        project.add(u);
        project.add(v);
        project.add(w);
        project.add(x);
        project.add(p2);
        Iterator<Todo> itr = project.iterator();
        assertTrue(itr.hasNext());
        assertEquals(t.getDescription(), itr.next().getDescription());
        assertTrue(itr.hasNext());
        assertEquals(x.getDescription(), itr.next().getDescription());
        assertTrue(itr.hasNext());
        assertEquals(w.getDescription(), itr.next().getDescription());
        assertTrue(itr.hasNext());
        assertEquals(p2.getDescription(), itr.next().getDescription());
        assertTrue(itr.hasNext());
        assertEquals(u.getDescription(), itr.next().getDescription());
        assertTrue(itr.hasNext());
        assertEquals(v.getDescription(), itr.next().getDescription());
        assertFalse(itr.hasNext());
        try {
            itr.next();
            fail("Tried to advance to nonexistent element");
        } catch (NoSuchElementException e) {

        }

    }


}