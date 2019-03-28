package model;

import model.exceptions.InvalidProgressException;
import model.exceptions.NegativeInputException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class testExceptions {
    InvalidProgressException ipe;
    NegativeInputException nie;

    @Test
    void testIPEConstructorWithMsg() {
        ipe = new InvalidProgressException("msg");
        assertEquals("msg", ipe.getMessage());
    }

    @Test
    void testNIEConstrutorWithMsg() {
        nie = new NegativeInputException("msg");
        assertEquals("msg", nie.getMessage());
    }

}
