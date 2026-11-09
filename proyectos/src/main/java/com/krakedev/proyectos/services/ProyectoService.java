
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }


    public Proyecto crear(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }


    public List<Proyecto> listar() {
        return proyectoRepository.findAll();
    }


    public Optional<Proyecto> buscarPorId(int id) {
        return proyectoRepository.findById(id);
    }
    
    public Optional<Proyecto> actualizar(int id, Proyecto proyecto) {
        if (!proyectoRepository.existsById(id)) {
            return Optional.empty();
        }

        proyecto.setId(id);
        return Optional.of(proyectoRepository.save(proyecto));
    }

    public boolean eliminar(int id) {
        if (!proyectoRepository.existsById(id)) {
            return false;
        }

        proyectoRepository.deleteById(id);
        return true;
    }
}
