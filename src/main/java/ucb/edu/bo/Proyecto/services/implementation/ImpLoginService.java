package ucb.edu.bo.Proyecto.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.LoginDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.services.ILoginService;

@Service
public class ImpLoginService implements ILoginService {
    @Autowired
    ImpUserService userService;
    @Override
    public UserDto login(LoginDto loginDto) {
        UserDto userDto = userService.findByEmail(loginDto.getEmail());
        if (userDto == null) {
            UserDto nuevoUserDto = new UserDto();
            nuevoUserDto.setEmail(loginDto.getEmail());
            nuevoUserDto.setNombres(loginDto.getNombres());
            nuevoUserDto.setApellidos(loginDto.getApellidos());
            userDto = userService.guardar(nuevoUserDto);
        }
        return userDto;
    }
}
