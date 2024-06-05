package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ucb.edu.bo.Proyecto.dto.LoginDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpLoginService;

@RestController
@RequestMapping(path = "/api/v1/login")
public class LoginController {

    @Autowired
    ImpLoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody(required = true) LoginDto loginDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (loginDto.getEmail() == null || loginDto.getEmail().isEmpty()) {
            errorResponse.addError("email", "Este campo es obligatorio");
        }
        if (loginDto.getNombres() == null || loginDto.getNombres().isEmpty()) {
            errorResponse.addError("nombres", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        LoginDto saveLoginDto = new LoginDto();
        saveLoginDto.setEmail(loginDto.getEmail());
        saveLoginDto.setNombres(loginDto.getNombres());
        saveLoginDto.setApellidos(loginDto.getApellidos());
        UserDto userDto = loginService.login(saveLoginDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login correcto", userDto));
    }
}
