package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.MantenimientoDto;

import java.util.List;

public interface IMantenimientoService {
    List<MantenimientoDto> listado();

    List<MantenimientoDto> getByIdEquipo(Integer id_equipo);

    MantenimientoDto getByIdSolicitudMantenimiento(Integer id_equipo);

    MantenimientoDto getByIdAsignacionMantenimiento(Integer id_user);

    MantenimientoDto guardar(MantenimientoDto mantenimientoDto);

    MantenimientoDto actualizar(MantenimientoDto mantenimientoDto);

    MantenimientoDto mantenimiento(Integer id);

    Boolean eliminar(MantenimientoDto mantenimientoDto);
}
