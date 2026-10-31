package es.progcipfpbatoi.todolistspring.exceptions;

/**
 * Thrown when trying to add a task whose code already exists.
 */
public class DuplicatedTaskException extends Exception{

	public DuplicatedTaskException() {
		super("Tasks with a repeated code aren't allowed");
	}
}
