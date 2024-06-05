package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.UserTipoDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    @Autowired
    private ImpUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDto>>> users() {
        List<UserDto> userDtoList = userService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", userDtoList));
    }

    @GetMapping("/sin_tipo")
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsersSinTipo() {
        List<UserDto> userDtoList = userService.listadoSinTipo();
        return ResponseEntity.ok(new ApiResponse<>(true, "", userDtoList));
    }

    @GetMapping("/para_administrativos")
    public ResponseEntity<ApiResponse<List<UserDto>>> getUsersParaAdministrativos() {
        List<UserDto> userDtoList = userService.listadoParaAdministrativos();
        return ResponseEntity.ok(new ApiResponse<>(true, "", userDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable("id") Integer id) {
        UserDto userDto = userService.user(id);
        if (userDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", userDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) UserDto userDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            errorResponse.addError("email", "Este campo es obligatorio");
        }
        if (userDto.getNombres() == null || userDto.getNombres().isEmpty()) {
            errorResponse.addError("nombres", "Este campo es obligatorio");
        }
        if (userDto.getApellidos() == null || userDto.getApellidos().isEmpty()) {
            errorResponse.addError("apellidos", "Este campo es obligatorio");
        }
        //validar existencia email
        UserDto userDto1 = userService.findByEmail(userDto.getEmail());
        if (userDto1 != null) {
            errorResponse.addError("email", "Este correo ya fue registrado");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        UserDto saveUserDto = new UserDto();
        saveUserDto.setEmail(userDto.getEmail());
        saveUserDto.setNombres(userDto.getNombres());
        saveUserDto.setApellidos(userDto.getApellidos());
        saveUserDto = userService.guardar((saveUserDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveUserDto));
    }

    @PostMapping(value = "/asignar_tipo/{id}")
    public ResponseEntity<?> asignar_tipo(@PathVariable("id") Integer id, @RequestBody(required = true) UserTipoDto userTipoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();

        // validar que el usuario no sea un Administrativo
        UserDto verificaUser = userService.user(id);
        if (verificaUser == null) {
            return new ResponseEntity<String>("No se encontró ningun registro", HttpStatus.BAD_REQUEST);
        }

        if (verificaUser.getTipo() != null && verificaUser.getTipo().equals("Administrativo")) {
            return new ResponseEntity<String>("No es posible asignar un nuevo tipo de usuario a un Administrativo", HttpStatus.BAD_REQUEST);
        }

        // Validar los datos
        if (userTipoDto.getTipo() == null || userTipoDto.getTipo().isEmpty()) {
            errorResponse.addError("tipo", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        UserDto userDto = userService.asignarTipo(id, userTipoDto.getTipo());
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", userDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) UserDto userDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            errorResponse.addError("email", "Este campo es obligatorio");
        }
        if (userDto.getNombres() == null || userDto.getNombres().isEmpty()) {
            errorResponse.addError("nombres", "Este campo es obligatorio");
        }
        if (userDto.getApellidos() == null || userDto.getApellidos().isEmpty()) {
            errorResponse.addError("apellidos", "Este campo es obligatorio");
        }
        //validar existencia email
        UserDto userDto1 = userService.findByEmail(userDto.getEmail());
        if (userDto1 != null && userDto1.getId() != id) {
            errorResponse.addError("email", "Este correo ya fue registrado");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        UserDto updateUserDto = userService.user(id);
        if (updateUserDto != null) {
            updateUserDto.setEmail(userDto.getEmail());
            updateUserDto.setNombres(userDto.getNombres());
            updateUserDto.setApellidos(userDto.getApellidos());
            userService.guardar(updateUserDto);

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateUserDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        UserDto userDto = userService.user(id);
        if (userDto != null) {
            userService.eliminar(userDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
