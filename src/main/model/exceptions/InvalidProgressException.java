package model.exceptions;

public class InvalidProgressException extends IllegalArgumentException {
    //default constructor
    public InvalidProgressException() {
        super();
    }

    //constructor with a String message
    public InvalidProgressException(String s) {
        super(s);
    }
}
