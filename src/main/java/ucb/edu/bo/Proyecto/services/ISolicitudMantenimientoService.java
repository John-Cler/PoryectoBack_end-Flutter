package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.SolicitudMantenimientoDto;

import java.util.List;

public interface ISolicitudMantenimientoService {
    List<SolicitudMantenimientoDto> listado();
    List<SolicitudMantenimientoDto> getByIdEquipo(Integer id_equipo);
    List<SolicitudMantenimientoDto> getByIdUser(Integer id_user);
    SolicitudMantenimientoDto guardar(SolicitudMantenimientoDto solicitudMantenimientoDto);
    SolicitudMantenimientoDto actualizar(SolicitudMantenimientoDto solicitudMantenimientoDto);

    SolicitudMantenimientoDto solicitudMantenimiento(Integer id);
    Boolean eliminar(SolicitudMantenimientoDto solicitudMantenimientoDto);
}
