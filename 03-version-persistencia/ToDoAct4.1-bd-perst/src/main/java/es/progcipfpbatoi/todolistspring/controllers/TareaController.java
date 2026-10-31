package es.progcipfpbatoi.todolistspring.controllers;

import es.progcipfpbatoi.todolistspring.exceptions.DatabaseErrorException;
import es.progcipfpbatoi.todolistspring.exceptions.DuplicatedTaskException;
import es.progcipfpbatoi.todolistspring.exceptions.NotFoundException;
import es.progcipfpbatoi.todolistspring.model.entities.Prioritat;
import es.progcipfpbatoi.todolistspring.model.entities.Tarea;
import es.progcipfpbatoi.todolistspring.model.repositories.ITareaRepository;
import es.progcipfpbatoi.todolistspring.model.repositories.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Web controller for the task pages: menu, list, search, form, details and delete.
 * Routes, template names and request parameter names are kept as they are
 * because the HTML templates depend on them.
 */
@Controller
public class TareaController {


	@Autowired
	private ITareaRepository tareaRepository;

	/**
	 * Shows the main menu.
	 *
	 * @return the menu template
	 */
	@GetMapping("/")
	public String menu() {
		return "menu";
	}

	/**
	 * Shows the full task list.
	 *
	 * @param model the data passed to the template
	 * @return the list template
	 */
	@GetMapping("/tasques")
	public String llistat(Model model) {
		ArrayList<Tarea> tasks = tareaRepository.findAll();
		model.addAttribute("tareas", tasks);
		return "listado";
	}

	/**
	 * Shows the empty search page.
	 *
	 * @param model the data passed to the template
	 * @return the search template
	 */
	@GetMapping("/cercar")
	public String mostrarCercador(Model model) {
		model.addAttribute("tareas", null);
		return "buscador";
	}

	/**
	 * Searches tasks using the filters filled in by the user (all optional).
	 *
	 * @param usuario the user to filter by
	 * @param fechaVencimiento the due date to filter by
	 * @param realizada whether to show only done tasks
	 * @param model the data passed to the template
	 * @return the search template with the results
	 */
	@PostMapping("/cercar")
	public String cercar(@RequestParam(required = false) String usuario,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaVencimiento,
			@RequestParam(required = false) Boolean realizada, Model model) {

		ArrayList<Tarea> tasks = tareaRepository.findByFiltros(usuario, fechaVencimiento, realizada);
		model.addAttribute("tareas", tasks);
		model.addAttribute("usuario", usuario);
		model.addAttribute("fechaVencimiento", fechaVencimiento);
		model.addAttribute("realizada", realizada);
		return "buscador";
	}

	/**
	 * Shows the form to add a new task.
	 *
	 * @param model the data passed to the template
	 * @return the form template
	 */
	@GetMapping("/tarea-form")
	public String tareaFormView(Model model) {
		model.addAttribute("prioritats", Prioritat.values());
		model.addAttribute("categories",
				List.of("Deporte", "Escola", "Jardinería", "Tareas del Hogar", "Ocio"));
		return "tarea_form_view";
	}

	/**
	 * Creates a task from the form data and saves it.
	 *
	 * @param user the user the task belongs to
	 * @param description what the task is about
	 * @param endingDate the due date
	 * @param endingTime the due time
	 * @param priority the task's priority
	 * @param done whether it's already done
	 * @param categoria the chosen category (optional)
	 * @param model the data passed to the template
	 * @return a redirect to the search page, or the form again if the task is duplicated
	 */
	@PostMapping("/tarea-add")
	public String postAddAction(@RequestParam String user, @RequestParam String description,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endingDate,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endingTime, @RequestParam Prioritat priority,
			@RequestParam(defaultValue = "false") boolean done, @RequestParam(required = false) String categoria,
			Model model) {

		try {
			Tarea task = new Tarea();
			task.setNombre(user);
			task.setDescripcion(description);
			task.setVencimientoEn(LocalDateTime.of(endingDate, endingTime));
			task.setPrioridad(priority);
			task.setRealizada(done);
			task.setCategoria(categoria);

			tareaRepository.add(task);
			return "redirect:/cercar";

		} catch (DuplicatedTaskException e) {
			model.addAttribute("errorMsg", e.getMessage());
			model.addAttribute("prioritats", Prioritat.values());
			model.addAttribute("categories", List.of("Deporte", "Escola", "Jardineria", "Tareas del Hogar", "Otros"));
			return "tarea_form_view";
		} catch (Exception e) {
			throw new DatabaseErrorException("Error saving the task: " + e.getMessage(), e);
		}
	}

	/**
	 * Shows the details of one task.
	 *
	 * @param code the task's code
	 * @param model the data passed to the template
	 * @return the details template
	 * @throws NotFoundException if there's no task with that code
	 */
	@GetMapping("/tarea")
	public String getTarea(@RequestParam int code, Model model) throws NotFoundException {
		Tarea task = tareaRepository.get(code);
		model.addAttribute("tarea", task);
		return "tarea_view";
	}

	/**
	 * Deletes a task and shows a confirmation message.
	 *
	 * @param code the task's code
	 * @param model the data passed to the template
	 * @return the delete confirmation template
	 * @throws NotFoundException if there's no task with that code
	 */
	@GetMapping("/esborrar")
	public String esborrar(@RequestParam int code, Model model) throws NotFoundException {
		Tarea task = tareaRepository.delete(code);
		model.addAttribute("tarea", task);
		model.addAttribute("missatge", "Task " + code + " deleted successfully");
		return "esborrar";
	}

	/**
	 * Shows the error page when a task isn't found.
	 *
	 * @param ex the exception that was thrown
	 * @param model the data passed to the template
	 * @return the error template
	 */
	@ExceptionHandler(NotFoundException.class)
	public String handleNotFound(NotFoundException ex, Model model) {
		model.addAttribute("errorMsg", ex.getMessage());
		return "error";
	}

	/**
	 * Shows the error page when there's a database error.
	 *
	 * @param ex the exception that was thrown
	 * @param model the data passed to the template
	 * @return the error template
	 */
	@ExceptionHandler(DatabaseErrorException.class)
	public String handleDbError(DatabaseErrorException ex, Model model) {
		model.addAttribute("errorMsg", ex.getMessage());
		return "error";
	}

}
