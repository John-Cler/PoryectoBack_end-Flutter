package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.AdministrativoDto;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpAdministrativoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpBloqueService;
import ucb.edu.bo.Proyecto.services.implementation.ImpDepartamentoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/administrativos")
public class AdministrativoController {
    @Autowired
    private ImpAdministrativoService administrativoService;
    @Autowired
    private ImpBloqueService bloqueService;
    @Autowired
    private ImpDepartamentoService departamentoService;
    @Autowired
    private ImpUserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AdministrativoDto>>> administrativos() {
        List<AdministrativoDto> administrativoDtoList = administrativoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", administrativoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdministrativoDto>> getAdministrativo(@PathVariable("id") Integer id) {
        AdministrativoDto administrativoDto = administrativoService.administrativo(id);
        if (administrativoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", administrativoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) AdministrativoDto administrativoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (administrativoDto.getId_user() == null || administrativoDto.getId_user() <= 0) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        }

        // validar usuario
        // validar que el usuario no sea un Administrativo
        UserDto verificaUser = userService.user(administrativoDto.getId_user());
        if (verificaUser == null) {
            return new ResponseEntity<String>("No se encontró ningun usuario", HttpStatus.BAD_REQUEST);
        }

        if (verificaUser.getTipo() != null && !verificaUser.getTipo().equals("Administrativo")) {
            return new ResponseEntity<String>("No es posible asignar el tipo Administrativo a usuarios de otro tipo", HttpStatus.BAD_REQUEST);
        }

        // continuar con las otras validaciones
        if (administrativoDto.getId_departamento() == null || administrativoDto.getId_departamento() <= 0) {
            errorResponse.addError("id_departamento", "Este campo es obligatorio");
        }
        if (administrativoDto.getId_bloque() == null || administrativoDto.getId_bloque() <= 0) {
            errorResponse.addError("id_bloque", "Este campo es obligatorio");
        }

        // validar existencia de usuario, departamento y bloque
        UserDto userDto = userService.user(administrativoDto.getId_user());
        if (userDto == null) {
            errorResponse.addError("id_user", "No se encontró el registro");
        }
        BloqueDto bloqueDto = bloqueService.bloque(administrativoDto.getId_bloque());
        if (bloqueDto == null) {
            errorResponse.addError("id_bloque", "No se encontró el registro");
        }
        DepartamentoDto departamentoDto = departamentoService.departamento(administrativoDto.getId_departamento());
        if (departamentoDto == null) {
            errorResponse.addError("id_departamento", "No se encontró el registro");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        AdministrativoDto saveAdministrativoDto = new AdministrativoDto();
        saveAdministrativoDto.setId_user(administrativoDto.getId_user());
        saveAdministrativoDto.setId_departamento(administrativoDto.getId_departamento());
        saveAdministrativoDto.setId_bloque(administrativoDto.getId_bloque());
        saveAdministrativoDto.setBloqueDto(bloqueDto);
        saveAdministrativoDto.setDepartamentoDto(departamentoDto);
        saveAdministrativoDto = administrativoService.guardar((saveAdministrativoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveAdministrativoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) AdministrativoDto administrativoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (administrativoDto.getId_user() == null || administrativoDto.getId_user() <= 0) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        }

        // validar usuario
        // validar que el usuario no sea un Administrativo
        UserDto verificaUser = userService.user(administrativoDto.getId_user());
        if (verificaUser == null) {
            return new ResponseEntity<String>("No se encontró ningun usuario", HttpStatus.BAD_REQUEST);
        }

        if (verificaUser.getTipo() != null && !verificaUser.getTipo().equals("Administrativo")) {
            return new ResponseEntity<String>("No es posible asignar el tipo Administrativo a usuarios de otro tipo", HttpStatus.BAD_REQUEST);
        }

        // continuar con las otras validaciones
        if (administrativoDto.getId_departamento() == null || administrativoDto.getId_departamento() <= 0) {
            errorResponse.addError("id_departamento", "Este campo es obligatorio");
        }
        if (administrativoDto.getId_bloque() == null || administrativoDto.getId_bloque() <= 0) {
            errorResponse.addError("id_bloque", "Este campo es obligatorio");
        }

        // validar existencia de usuario, departamento y bloque
        UserDto userDto = userService.user(administrativoDto.getId_user());
        if (userDto == null) {
            errorResponse.addError("id_user", "No se encontró el registro");
        }
        BloqueDto bloqueDto = bloqueService.bloque(administrativoDto.getId_bloque());
        if (bloqueDto == null) {
            errorResponse.addError("id_bloque", "No se encontró el registro");
        }
        DepartamentoDto departamentoDto = departamentoService.departamento(administrativoDto.getId_departamento());
        if (departamentoDto == null) {
            errorResponse.addError("id_departamento", "No se encontró el registro");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        AdministrativoDto updateAdministrativoDto = administrativoService.administrativo(id);
        if (updateAdministrativoDto != null) {
            updateAdministrativoDto.setId_user(administrativoDto.getId_user());
            updateAdministrativoDto.setId_departamento(administrativoDto.getId_departamento());
            updateAdministrativoDto.setId_bloque(administrativoDto.getId_bloque());
            updateAdministrativoDto.setBloqueDto(bloqueDto);
            updateAdministrativoDto.setDepartamentoDto(departamentoDto);
            updateAdministrativoDto = administrativoService.guardar((updateAdministrativoDto));
            administrativoService.guardar(updateAdministrativoDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateAdministrativoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        AdministrativoDto administrativoDto = administrativoService.administrativo(id);
        if (administrativoDto != null) {
            if(administrativoService.eliminar(administrativoDto)){
                return ResponseEntity.ok("Registro eliminado");
            }else{
                return ResponseEntity.internalServerError().body("No se pudo eliminar el registro");
            }
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
