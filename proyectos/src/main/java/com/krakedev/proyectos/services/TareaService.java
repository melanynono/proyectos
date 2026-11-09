
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }


    public Tarea crear(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public List<Tarea> listar() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> buscarPorId(int id) {
        return tareaRepository.findById(id);
    }

    public Optional<Tarea> actualizar(int id, Tarea tarea) {
        if (!tareaRepository.existsById(id)) {
            return Optional.empty();
        }

        tarea.setId(id);
        return Optional.of(tareaRepository.save(tarea));
    }

    public boolean eliminar(int id) {
        if (!tareaRepository.existsById(id)) {
            return false;
        }

        tareaRepository.deleteById(id);
        return true;
    }
}
