package es.progcipfpbatoi.todolistspring.model.entities;

/**
 * Task priority: high (ALTA), medium (MEDIA) or low (BAJA).
 * The values are kept as they are because they're stored in the database by name.
 */
public enum Prioritat {
	ALTA,
	MEDIA,
	BAJA;

}
