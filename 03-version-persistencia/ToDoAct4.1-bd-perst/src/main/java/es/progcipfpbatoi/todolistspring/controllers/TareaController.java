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

@Controller
public class TareaController {
	

	@Autowired
	private ITareaRepository tareaRepository;

	@GetMapping("/")
	public String menu() {
		return "menu";
	}

	@GetMapping("/tasques")
	public String llistat(Model model) {
		ArrayList<Tarea> tareas = tareaRepository.findAll();
		model.addAttribute("tareas", tareas);
		return "listado";
	}

	@GetMapping("/cercar")
	public String mostrarCercador(Model model) {
		model.addAttribute("tareas", null);
		return "buscador";
	}

	@PostMapping("/cercar")
	public String cercar(@RequestParam(required = false) String usuario,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaVencimiento,
			@RequestParam(required = false) Boolean realizada, Model model) {

		ArrayList<Tarea> tareas = tareaRepository.findByFiltros(usuario, fechaVencimiento, realizada);
		model.addAttribute("tareas", tareas);
		model.addAttribute("usuario", usuario);
		model.addAttribute("fechaVencimiento", fechaVencimiento);
		model.addAttribute("realizada", realizada);
		return "buscador";
	}

	@GetMapping("/tarea-form")
	public String tareaFormView(Model model) {
	    model.addAttribute("prioritats", Prioritat.values());
	    model.addAttribute("categories",
	        List.of("Deporte", "Escola", "Jardinería", "Tareas del Hogar", "Ocio"));
	    return "tarea_form_view";
	}

	@PostMapping("/tarea-add")
	public String postAddAction(@RequestParam String user, @RequestParam String description,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endingDate,
			@RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endingTime, @RequestParam Prioritat priority,
			@RequestParam(defaultValue = "false") boolean done, @RequestParam(required = false) String categoria,
			Model model) {

		try {
			Tarea tarea = new Tarea();
			tarea.setNombre(user);
			tarea.setDescripcion(description);
			tarea.setVencimientoEn(LocalDateTime.of(endingDate, endingTime));
			tarea.setPrioridad(priority);
			tarea.setRealizada(done);
			tarea.setCategoria(categoria);

			tareaRepository.add(tarea);
			return "redirect:/cercar";

		} catch (DuplicatedTaskException e) {
			model.addAttribute("errorMsg", e.getMessage());
			model.addAttribute("prioritats", Prioritat.values());
			model.addAttribute("categories", List.of("Deporte", "Escola", "Jardineria", "Tareas del Hogar", "Otros"));
			return "tarea_form_view";
		} catch (Exception e) {
			throw new DatabaseErrorException("Error al guardar la tasca: " + e.getMessage(), e);
		}
	}

	@GetMapping("/tarea")
	public String getTarea(@RequestParam int code, Model model) throws NotFoundException {
		Tarea tarea = tareaRepository.get(code);
		model.addAttribute("tarea", tarea);
		return "tarea_view";
	}

	@GetMapping("/esborrar")
	public String esborrar(@RequestParam int code, Model model) throws NotFoundException {
		Tarea tarea = tareaRepository.delete(code);
		model.addAttribute("tarea", tarea);
		model.addAttribute("missatge", "Tasca " + code + " eliminada amb èxit");
		return "esborrar";
	}

	@ExceptionHandler(NotFoundException.class)
	public String handleNotFound(NotFoundException ex, Model model) {
		model.addAttribute("errorMsg", ex.getMessage());
		return "error";
	}

	@ExceptionHandler(DatabaseErrorException.class)
	public String handleDbError(DatabaseErrorException ex, Model model) {
		model.addAttribute("errorMsg", ex.getMessage());
		return "error";
	}

}
