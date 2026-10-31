package es.progcipfpbatoi.todolistspring.exceptions;

/**
 * Thrown when something goes wrong while reading or writing the stored tasks.
 */
public class DatabaseErrorException extends RuntimeException {

    /**
     * @param message the text explaining the error
     */
    public DatabaseErrorException(String message) {
        super(message);
    }

    /**
     * @param message the text explaining the error
     * @param cause the original exception
     */
    public DatabaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}