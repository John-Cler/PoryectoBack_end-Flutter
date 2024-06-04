package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.DepartamentoDto;

import java.util.List;

public interface IDepartamentoService {
    List<DepartamentoDto> listado();
    DepartamentoDto guardar(DepartamentoDto departamentoDto);

    DepartamentoDto departamento(Integer id);
    Boolean eliminar(DepartamentoDto departamentoDto);
}
