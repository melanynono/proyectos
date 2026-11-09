
package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Empleado crear(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    public List<Empleado> listar() {
        return empleadoRepository.findAll();
    }

    public Optional<Empleado> buscarPorId(int id) {
        return empleadoRepository.findById(id);
    }

    public Optional<Empleado> actualizar(int id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            return Optional.empty();
        }

        empleado.setId(id);
        return Optional.of(empleadoRepository.save(empleado));
    }

    public boolean eliminar(int id) {
        if (!empleadoRepository.existsById(id)) {
            return false;
        }

        empleadoRepository.deleteById(id);
        return true;
    }
}
