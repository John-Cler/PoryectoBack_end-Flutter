package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.AsignacionEquipoDto;

import java.util.List;

public interface IAsignacionEquipoService {
    List<AsignacionEquipoDto> listado();
    AsignacionEquipoDto guardar(AsignacionEquipoDto asignacionEquipoDto);
    AsignacionEquipoDto actualizar(AsignacionEquipoDto asignacionEquipoDto);

    AsignacionEquipoDto asignacionEquipo(Integer id);

    AsignacionEquipoDto getByIdEquipo(Integer id);
    List<AsignacionEquipoDto> getByIdAdministrativo(Integer id);

    Boolean eliminar(AsignacionEquipoDto asignacionEquipoDto);
}
