package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TestTask {
    private Task one;

    @BeforeEach
    void runBefore() {
        one = new Task("Some string");
    }

    @Test
    void testEquals() {
        Task a = new Task("This is a Task ## tag;");
        Task b = new Task("This is a Task ## tag;");
        assertEquals(a, b);
        assertEquals(a, a);
        assertFalse(a.equals(new Tag("tag")));
        /*
        return Objects.equals(description, task.description)
                // && Objects.equals(tags, task.tags)
                && Objects.equals(dueDate, task.dueDate)
                && Objects.equals(priority, task.priority)
                && status == task.status;
         */
        b = new Task ("aa # tag;");
        assertNotEquals(a,b);
        b = new Task ("This is a Task ## today; tag;");
        assertNotEquals(a, b);
        b = new Task ("This is a Task ## important; tag;");
        assertNotEquals(a, b);
        b = new Task ("This is a Task ## done; tag;");
        assertNotEquals(a, b);
    }

    @Test
    void testSetProgress() {
        Task t = new Task("This is a Task ## :p;");
        assertEquals(0, t.getProgress());
        try {
            t.setProgress(101);
            fail("Set progress to something invalid");
        } catch (Exception e) {

        }
        try {
            t.setProgress(-1);
            fail("Set progress to something invalid");
        } catch (Exception e) {

        }
        t.setProgress(100);
        assertEquals(100, t.getProgress());
    }

    @Test
    void testSetEtc() {
        Task t = new Task ("IU ## important; tag;");
        assertEquals(0, t.getEstimatedTimeToComplete());
        try {
            t.setEstimatedTimeToComplete(-1);
            fail("Set ETC to negative number");
        } catch (Exception e) {

        }
        t.setEstimatedTimeToComplete(10);
        assertEquals(10, t.getEstimatedTimeToComplete());
    }

    @Test
    void testContainsTag() {
        one.toString();
        one = new Task("Task ## tag;");
        one.toString();
        Task t = new Task("Task ## tag; t2; today;");
        assertTrue(t.containsTag("tag"));
        try {
            t.containsTag((String) null);
            fail("Null tag found?!");
        } catch (Exception e) {

        }
        try {
            t.containsTag("");
            fail("Empty tag found?!");
        } catch (Exception e) {

        }
    }

    @Test
    void testAddAndRemove() {
        one.addTag("Cat");
        assertTrue(one.containsTag("Cat"));
        assertFalse(one.containsTag("Dog"));
        one.removeTag("AA");
        assertEquals(1, one.getTags().size());
        one.removeTag("Cat");
        assertTrue(one.getTags().isEmpty());
    }

    @Test
    void testGetPriority() {
        assertEquals("DEFAULT", one.getPriority().toString());
    }

    @Test
    void testGetDescription() {
        assertEquals("Some string", one.getDescription());
    }

    @Test
    void testGetStatus() {
        assertEquals(Status.TODO, one.getStatus());
    }

    @Test
    void testGetDueDate() {
        try {
            String myDate = "2000/08/20 21:10:45";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sdf.parse(myDate);
            one.setDueDate(new DueDate(date));
            assertEquals("Sun Aug 20 2000 09:10 PM", one.getDueDate().toString());
        } catch (ParseException e) {

        }

    }

    @Test
    void testTags() {
        one.addTag("Cat");
        one.addTag("Dog");
        assertTrue(one.containsTag("Cat"));
    }

    @Test
    void testToString() {
        try {
            one.setDescription("Some string used for metadata");
            one.addTag("Cat");
            one.addTag("Kitty");
            String myDate = "2000/04/20 11:10:45";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = sdf.parse(myDate);
            one.setDueDate(new DueDate(date));
            one.setStatus(Status.IN_PROGRESS);
            one.setPriority(new Priority(2));
            /*
            assertEquals("Description: Some string used for metadata\n" +
                    "Due date: Thu Apr 20 2000 11:10 AM\n" +
                    "Status: IN PROGRESS\n" +
                    "Tags: #Cat #Kitty ", one.toString());
                    */
            assertTrue(one.toString().contains("Some string used for metadata"));
            assertTrue(one.toString().contains("Apr 20 2000 11:10 AM"));
            assertTrue(one.toString().contains("IN PROGRESS"));
            assertTrue(one.toString().contains("#Cat"));
            assertTrue(one.toString().contains("#Kitty"));
        } catch (ParseException e) {

        }
    }

    @Test
    void forceException() {
        try {
            one = new Task("");
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }
        try {
            one = new Task(null);
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }
        one = new Task("Kittens");
        try {
            one.addTag("");
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }
        try {
            one.addTag((Tag) null);
            fail("Did not successfully throw Empty String Exception");
        } catch (NullArgumentException e) {

        }
        try {
            one.removeTag("");
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }

        try {
            one.removeTag((Tag) null);
            fail("Did not successfully throw Empty String Exception");
        } catch (NullArgumentException e) {

        }

        try {
            one.setPriority(null);
            fail("Did not successfully throw Null Argument Exception");
        } catch (NullArgumentException e) {

        }

        try {
            one.setStatus(null);
            fail("Did not successfully throw Null Argument Exception");
        } catch (NullArgumentException e) {

        }

        try {
            one.setDescription("");
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }

        try {
            one.setDescription(null);
            fail("Did not successfully throw Empty String Exception");
        } catch (EmptyStringException e) {

        }
    }

    @Test
    void printEmptyDate() {
        Task t = new Task("Some task ## cpsc210; important; cat; todo");
        //System.out.println(t.toString());
        t.addTag("Tag");
        //System.out.println(t.toString());
        assertTrue(t.getTags().size() == 4);
        t.addTag("Tag");
        assertTrue(t.getTags().size() == 4);
    }

}