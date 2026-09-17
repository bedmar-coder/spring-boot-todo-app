package es.progcipfpbatoi.todolistspring.exceptions;

public class DuplicatedTaskException extends Exception{

	public DuplicatedTaskException() {
		super("No se admiten tareas con codigo repetido");
	}
}
