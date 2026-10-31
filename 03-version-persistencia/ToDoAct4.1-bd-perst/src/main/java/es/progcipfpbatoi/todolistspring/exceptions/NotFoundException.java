package es.progcipfpbatoi.todolistspring.exceptions;

/**
 * Thrown when the requested task doesn't exist.
 */
public class NotFoundException extends Exception {

    /**
     * @param message the text explaining which task wasn't found
     */
    public NotFoundException(String message) {
        super(message);
    }
}
