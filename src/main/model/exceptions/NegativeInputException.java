package model.exceptions;

public class NegativeInputException extends IllegalArgumentException {
    //default constructor
    public NegativeInputException() {
        super();
    }

    //constructor with a String message
    public NegativeInputException(String s) {
        super(s);
    }
}
