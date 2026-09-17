package es.progcipfpbatoi.todolistspring.exceptions;

public class DatabaseErrorException extends RuntimeException {
    public DatabaseErrorException(String mensaje) {
        super(mensaje);
    }
    public DatabaseErrorException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}