package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class TestTask {
    // TODO: design tests for new behaviour added to Task class
    Task t;
    DueDate today;
    DueDate tomorrow;
    Calendar c;

    Tag tag;

    @BeforeEach
    void runBefore() {
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        today = new DueDate(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 1);
        tomorrow = new DueDate(c.getTime());

        t = new Task("This is a task");
        t.setStatus(Status.IN_PROGRESS);
        t.setPriority(new Priority(1));
        t.setDueDate(today);

        tag = new Tag("Tag1");
    }

    @Test
    void testConstructorWithTags() {
        Task task = new Task("Task with tags ## today; cpsc210");
        assertEquals("Task with tags ", task.getDescription());
        assertTrue(task.containsTag("cpsc210"));
        assertTrue(task.getDueDate().equals(today));
    }

    @Test
    void testConstructorExceptionExpected() {
        try {
            Task task = new Task("");
            fail("EmptyStringException expected");
        } catch (EmptyStringException e) {
            // expected
        }
        try {
            Task task = new Task(null);
            fail("EmptyStringException expected");
        } catch (EmptyStringException e) {
            // expected
        }
    }

    @Test
    void testEquals() {
        Task t2 = new Task("This is a task");
        t2.setStatus(Status.IN_PROGRESS);
        t2.setPriority(new Priority(1));
        t2.setDueDate(today);
        t2.addTag("tag");
        assertEquals(t2, t);
    }

    @Test
    void testEqualsItself() {
        assertTrue(t.equals(t));
    }

    @Test
    void testEqualsDifferentObject() {
        assertFalse(t.equals(tag));
    }

    @Test
    void testAddTag() {
        t.addTag(tag);
        assertTrue(t.containsTag(tag));
        assertTrue(tag.containsTask(t));
    }

    @Test
    void testRemoveTagByPointer() {
        t.addTag(tag);
        assertTrue(t.getTags().contains(tag));
        assertTrue(tag.containsTask(t));
        t.removeTag(tag);
        assertFalse(t.getTags().contains(tag));
        assertFalse(tag.containsTask(t));
    }

    @Test
    void testRemoveTagByName() {
        t.addTag(tag);
        t.removeTag(tag.getName());
        assertFalse(t.getTags().contains(tag));
    }

    @Test
    void testGetPriority() {
        assertEquals(new Priority(1), t.getPriority());
    }

    @Test
    void testSetPriorityExceptionExpected() {
        try {
            t.setPriority(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    void testGetDescription() {
        assertEquals("This is a task", t.getDescription());
    }

    @Test
    void testGetDueDate() {
        assertEquals(today, t.getDueDate());
    }

    @Test
    void testContainsTagPointer() {
        t.addTag(tag);
        assertTrue(t.containsTag(tag));
    }

    @Test
    void testContainsTagName() {
        t.addTag(tag);
        assertTrue(t.containsTag(tag.getName()));
    }

    @Test
    void testContainsTagExceptionExpected() {
        try {
            t.containsTag("");
            fail("EmptyStringException expected");
        } catch (EmptyStringException e) {
            // expected
        }
        try {
            String nstr = null;
            t.containsTag(nstr);
            fail("EmptyStringException expected");
        } catch (EmptyStringException e) {
            // expected
        }
        try {
            Tag ntag = null;
            t.containsTag(ntag);
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    void testSetStatus() {
        assertEquals(Status.IN_PROGRESS, t.getStatus());
        t.setStatus(Status.UP_NEXT);
        assertEquals(Status.UP_NEXT, t.getStatus());
    }

    @Test
    void testSetStatusExceptionExpected() {
        try {
            t.setStatus(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    void testSetDescriptionExceptionExpected() {
        try {
            t.setDescription("");
            fail("EmptyStringException Expected");
        } catch (EmptyStringException e) {
            // expected
        }
        try {
            t.setDescription(null);
            fail("EmptyStringException expected");
        } catch (EmptyStringException e) {
            // expected
        }
    }

    @Test
    void testToString() {
        String desc = "\n{" +
                "\n\tDescription: " + t.getDescription() +
                "\n\tDue date: " + t.getDueDate().toString() +
                "\n\tStatus: " + t.getStatus().toString() +
                "\n\tPriority: " + t.getPriority().toString() +
                "\n\tTags:  " +
                "\n}";
        assertEquals(desc, t.toString());
        t.addTag("tag1");
        t.addTag("tag2");
        desc =    "\n{" +
                  "\n\tDescription: " + t.getDescription() +
                  "\n\tDue date: " + t.getDueDate().toString() +
                  "\n\tStatus: " + t.getStatus().toString() +
                  "\n\tPriority: " + t.getPriority().toString() +
                  "\n\tTags: #tag1, #tag2" +
                  "\n}";
        assertEquals(desc, t.toString());
    }
}