package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.EquipoParteDto;

import java.util.List;

public interface IEquipoParteService {
    List<EquipoParteDto> listado();

    List<EquipoParteDto> getByIdEquipo(Integer id_equipo);

    EquipoParteDto guardar(EquipoParteDto equipoParteDto);

    EquipoParteDto equipoParte(Integer id);

    Boolean eliminar(EquipoParteDto equipoParteDto);
}
