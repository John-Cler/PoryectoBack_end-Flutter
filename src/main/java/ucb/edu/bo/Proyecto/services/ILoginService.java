package ucb.edu.bo.Proyecto.services;

import ucb.edu.bo.Proyecto.dto.LoginDto;
import ucb.edu.bo.Proyecto.dto.UserDto;

public interface ILoginService {
    UserDto login(LoginDto loginDto);
}
