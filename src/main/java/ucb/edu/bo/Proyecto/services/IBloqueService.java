package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.BloqueDto;

import java.util.List;

public interface IBloqueService {
    List<BloqueDto> listado();
    BloqueDto guardar(BloqueDto bloqueDto);

    BloqueDto bloque(Integer id);
    Boolean eliminar(BloqueDto bloqueDto);

}
