package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.EquipoDto;

import java.util.List;

public interface IEquipoService {
    List<EquipoDto> listado();
    EquipoDto guardar(EquipoDto equipoDto);

    EquipoDto equipo(Integer id);
    Boolean eliminar(EquipoDto equipoDto);
}
