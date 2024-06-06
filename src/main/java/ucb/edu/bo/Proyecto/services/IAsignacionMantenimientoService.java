package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.AsignacionMantenimientoDto;

import java.util.List;

public interface IAsignacionMantenimientoService {
    List<AsignacionMantenimientoDto> listado();

    AsignacionMantenimientoDto getByIdSolicitudMantenimiento(Integer id_equipo);

    List<AsignacionMantenimientoDto> getByIdUser(Integer id_user);

    AsignacionMantenimientoDto guardar(AsignacionMantenimientoDto asignacionMantenimientoDto);

    AsignacionMantenimientoDto actualizar(AsignacionMantenimientoDto asignacionMantenimientoDto);

    AsignacionMantenimientoDto asignacionMantenimiento(Integer id);

    Boolean eliminar(AsignacionMantenimientoDto asignacionMantenimientoDto);
}