package model;

import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestProject {
    private Project p;
    private Project p2;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    @BeforeEach
    public void runBefore() {
        p = new Project("Project1");
        p2 = new Project("Project2");
        task1 = new Task("task1 ## today; cpsc210");
        task2 = new Task("task2 ## tomorrow; important; to do");
        task3 = new Task("task3 ## important; urgent; up next");
        task4 = new Task("task4 ## urgent; in progress");
    }

    @Test
    void testConstructorNothingThrown() {
        try {
            Project p1 = new Project("This is a sample project");
            assertEquals("This is a sample project", p1.getDescription());
        } catch (EmptyStringException e) {
            fail("EmptyStringException should not have been thrown");
        }
    }

    @Test
    void testConstructorExpectEmptyStringException() {
        try {
            Project p1 = new Project("");
            fail("EmptyStringException should have been thrown");
        } catch (EmptyStringException e) {
            // expected
        }

        try {
            Project p2 = new Project(null);
            fail("EmptyStringException should have been thrown");
        } catch (EmptyStringException e) {
            // expected
        }
    }

    @Test
    void testAddNothingThrown() {
        try {
            assertEquals(0, p.getNumberOfTasks());
            assertFalse(p.contains(task1));
            p.add(task1);
            p.add(task2);
            assertEquals(2, p.getNumberOfTasks());
            assertTrue(p.contains(task1));
            assertTrue(p.contains(task2));
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testAddItself() {
        assertEquals(0, p.getNumberOfTasks());
        p.add(p);
        assertEquals(0, p.getNumberOfTasks());
    }

    @Test
    void testAddDuplicate() {
        p.add(task1);
        assertEquals(1, p.getNumberOfTasks());

        p.add(task1);
        assertEquals(1, p.getNumberOfTasks());
    }

    @Test
    void testAddNullArgumentExceptionExpected() {
        try {
            p.add(null);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    void testRemoveNothingThrown() {
        try {
            p.add(task1);
            p.add(task2);

            p.remove(task1);
            assertEquals(1, p.getNumberOfTasks());
            assertFalse(p.contains(task1));
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testRemoveNotContained() {
        p.add(task1);
        assertEquals(1, p.getNumberOfTasks());
        p.remove(task2);
        assertEquals(1, p.getNumberOfTasks());
    }

    @Test
    void testRemoveNullArgumentExceptionExpected() {
        try {
            p.add(task1);
            p.add(task2);

            p.remove(null);
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

//    @Test
//    void testGetTasks() {
//        assertEquals(0, p.getNumberOfTasks());
//
//        List<Task> tasks = new ArrayList<Task>();
//        tasks.add(task1);
//        tasks.add(task2);
//        tasks = Collections.unmodifiableList(tasks);
//
//        p.add(task1);
//        p.add(task2);
//
//        assertEquals(tasks, p.getTasks());
//    }

    @Test
    void testGetProgress() {
        assertEquals(0, p.getProgress());

        p.add(task1);
        p.add(task2);
        p.add(task3);
        assertEquals(0, p.getProgress());

        task1.setProgress(100);
        assertEquals(33, p.getProgress());

        task2.setProgress(50);
        task3.setProgress(25);
        assertEquals(58, p.getProgress());

        Project project2 = new Project("Project2");
        project2.add(task4);
        project2.add(p);
        assertEquals(29, project2.getProgress());

    }

//    @Test
//    void testIsCompleted() {
//        assertFalse(p.isCompleted());
//        task2.setStatus(Status.DONE);
//        p.add(task1);
//        p.add(task2);
//        assertFalse(p.isCompleted());
//        task1.setStatus(Status.DONE);
//        assertTrue(p.isCompleted());
//    }

    @Test
    void testGetTasks() {
        try {
            p.getTasks();
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    void testContainsNothingThrown() {
        try {
            assertFalse(p.contains(task1));
            p.add(task1);
            p.add(task2);
            assertTrue(p.contains(task1));
        } catch (NullArgumentException e) {
            fail("NullArgumentException should not have been thrown");
        }
    }

    @Test
    void testContainsNullArgumentExceptionExpected() {
        try {
            assertFalse(p.contains(null));
            fail("NullArgumentException should have been thrown");
        } catch (NullArgumentException e) {
            // expected
        }
    }

    @Test
    void testEqualsItself() {
        assertTrue(p.equals(p));
    }


    @Test
    void testEqualsByDescription() {
        Project project2 = new Project(p.getDescription());
        assertTrue(p.equals(project2));
    }

    @Test
    void testEqualsDifferentObject() {
        assertFalse(p.equals(task1));
    }

    @Test
    void testHashCode() {
        p2 = new Project(p.getDescription());
        assertEquals(p.hashCode(), p2.hashCode());
    }

    @Test
    void testSetEstimatedTimeToComplete() {
        p.add(task1);
        p.add(task2);
        p.add(task3);
        assertEquals(0, p.getEstimatedTimeToComplete());

        task1.setEstimatedTimeToComplete(8);
        assertEquals(8, p.getEstimatedTimeToComplete());

        task2.setEstimatedTimeToComplete(2);
        task3.setEstimatedTimeToComplete(10);
        assertEquals(20, p.getEstimatedTimeToComplete());


        task4.setEstimatedTimeToComplete(4);
        p2.add(task4);
        p2.add(p);
        assertEquals(24, p2.getEstimatedTimeToComplete());
    }

    @Test
    void testIsCompleted() {
        assertFalse(p.isCompleted());
        p.add(task1);
        assertFalse(p.isCompleted());
        task1.setProgress(100);
        assertTrue(p.isCompleted());
    }

    @Test
    void testSetPriority() {
        assertEquals(new Priority(4), p.getPriority());
        p.setPriority(new Priority(1));
        assertEquals(new Priority(1), p.getPriority());
    }

    @Test
    void testIterator() {
        p2.setPriority(new Priority(1));

        List<Todo> todos = new ArrayList<>();
        todos.add(task3);
        todos.add(p2);
        todos.add(task2);
        todos.add(task4);
        todos.add(task1);

        p.add(task1);
        p.add(task2);
        p.add(task3);
        p.add(task4);
        p.add(p2);

        List<Todo> check = new ArrayList<>();
        for (Todo t : p) {
            check.add(t);
            System.out.println("Added: " + t.getDescription());
        }

        assertEquals(todos.size(), check.size());
        for (int i = 0; i < todos.size(); i++) {
            System.out.println(check.get(i).getDescription());
           assertTrue(todos.get(i).equals(check.get(i)));
        }
    }

    @Test
    void testIteratorNextExceptionExpected() {
        Iterator itr = p.iterator();
        try {
            itr.next();
            fail("NoSuchElementException expected");
        } catch (NoSuchElementException e) {
            // expected
        }
    }
}