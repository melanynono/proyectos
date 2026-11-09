package com.krakedev.proyectoscontrollers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final ProyectoRepository proyectoRepository;
    private final EmpleadoRepository empleadoRepository;

    public TareaController(
            TareaService tareaService,
            ProyectoRepository proyectoRepository,
            EmpleadoRepository empleadoRepository) {

        this.tareaService = tareaService;
        this.proyectoRepository = proyectoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Tarea tarea) {
        try {
            if (tarea.getProyecto() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("mensaje", "Debe indicar un proyecto"));
            }

            int proyectoId = tarea.getProyecto().getId();

            Proyecto proyecto = proyectoRepository.findById(proyectoId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("El proyecto no existe"));

            tarea.setProyecto(proyecto);

            List<Empleado> empleadosAsociados = new ArrayList<>();

            if (tarea.getEmpleados() != null) {
                for (Empleado empleado : tarea.getEmpleados()) {

                    Empleado existente = empleadoRepository
                            .findById(empleado.getId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "El empleado con ID " + empleado.getId()
                                            + " no existe"));

                    empleadosAsociados.add(existente);
                }
            }

            tarea.setEmpleados(empleadosAsociados);

            Tarea nueva = tareaService.crear(tarea);

            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", String.valueOf(e.getMessage())));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", String.valueOf(e.getMessage())));
        }
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            List<Tarea> tareas = tareaService.listar();
            return ResponseEntity.ok(tareas);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", String.valueOf(e.getMessage())));
        }
    }
}
