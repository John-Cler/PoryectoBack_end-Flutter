package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.UserDto;

import java.util.List;

public interface IUserService {
    List<UserDto> listado();
    List<UserDto> listadoSinTipo();
    List<UserDto> listadoParaAdministrativos();
    UserDto guardar(UserDto userDto);
    UserDto asignarTipo(Integer id,String tipo);

    UserDto findByEmail(String email);
    UserDto user(Integer id);
    Boolean eliminar(UserDto userDto);
}
