package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.AdministrativoDto;

import java.util.List;

public interface IAdministrativo {
    List<AdministrativoDto> listado();
    AdministrativoDto guardar(AdministrativoDto administrativoDto);

    AdministrativoDto administrativo(Integer id);
    Boolean eliminar(AdministrativoDto administrativoDto);
}
