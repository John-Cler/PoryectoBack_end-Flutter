package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.AsignacionMantenimientoDto;
import ucb.edu.bo.Proyecto.dto.SolicitudMantenimientoDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpAsignacionMantenimientoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpSolicitudMantenimientoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/asignacion_mantenimientos")
public class AsignacionMantenimientoController {
    @Autowired
    private ImpAsignacionMantenimientoService asignacionMantenimientoService;
    @Autowired
    private ImpUserService userService;
    @Autowired
    private ImpSolicitudMantenimientoService solicitudMantenimientoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AsignacionMantenimientoDto>>> asignacionMantenimientos() {
        List<AsignacionMantenimientoDto> asignacionMantenimientoDtoList = asignacionMantenimientoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionMantenimientoDtoList));
    }

    @GetMapping("por_solicitud_mantenimiento/{id}")
    public ResponseEntity<ApiResponse<AsignacionMantenimientoDto>> asignacionMantenimientosBySolicitudMantenimiento(@PathVariable("id") Integer id) {
        AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.getByIdSolicitudMantenimiento(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionMantenimientoDto));
    }

    @GetMapping("por_user/{id}")
    public ResponseEntity<ApiResponse<List<AsignacionMantenimientoDto>>> asignacionMantenimientosByUser(@PathVariable("id") Integer id) {
        List<AsignacionMantenimientoDto> asignacionMantenimientoDtoList = asignacionMantenimientoService.getByIdUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionMantenimientoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AsignacionMantenimientoDto>> getAsignacionMantenimiento(@PathVariable("id") Integer id) {
        AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(id);
        if (asignacionMantenimientoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", asignacionMantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) AsignacionMantenimientoDto asignacionMantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (asignacionMantenimientoDto.getId_user() == null) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        } else {
            // Verificar que exista el usuario
            UserDto userDto = userService.user(asignacionMantenimientoDto.getId_user());
            if (userDto == null) {
                errorResponse.addError("id_user", "No se encontró el registro");
            }else{
                // verificar que sea un Técnico
                if(!userDto.getTipo().equals("Técnico")){
                    errorResponse.addError("id_user", "El usuario debe ser un técnico");
                }
            }
        }
        if (asignacionMantenimientoDto.getId_solicitud_mantenimiento() == null) {
            errorResponse.addError("id_solicitud_mantenimiento", "Este campo es obligatorio");
        } else {
            // Verificar que exista el equipo
            SolicitudMantenimientoDto solicitudMantenimientoDto = solicitudMantenimientoService.solicitudMantenimiento(asignacionMantenimientoDto.getId_solicitud_mantenimiento());
            if (solicitudMantenimientoDto == null) {
                errorResponse.addError("id_solicitud_mantenimiento", "No se encontró el registro");
            } else {
                // verificar que la solicitud no tenga una asignacion previa
                AsignacionMantenimientoDto asignacionMantenimientoDto1 = asignacionMantenimientoService.getByIdSolicitudMantenimiento(solicitudMantenimientoDto.getId());
                if (asignacionMantenimientoDto1 != null) {
                    return new ResponseEntity<String>("El registro de solicitud mantenimiento ya se encuentra con una asignación", HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        AsignacionMantenimientoDto saveAsignacionMantenimientoDto = new AsignacionMantenimientoDto();
        saveAsignacionMantenimientoDto.setId_user(asignacionMantenimientoDto.getId_user());
        saveAsignacionMantenimientoDto.setId_solicitud_mantenimiento(asignacionMantenimientoDto.getId_solicitud_mantenimiento());
        saveAsignacionMantenimientoDto = asignacionMantenimientoService.guardar((saveAsignacionMantenimientoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveAsignacionMantenimientoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) AsignacionMantenimientoDto asignacionMantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (asignacionMantenimientoDto.getId_user() == null) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        } else {
            // Verificar que exista el usuario
            UserDto userDto = userService.user(asignacionMantenimientoDto.getId_user());
            if (userDto == null) {
                errorResponse.addError("id_user", "No se encontró el registro");
            }else{
                // verificar que sea un Técnico
                if(!userDto.getTipo().equals("Técnico")){
                    errorResponse.addError("id_user", "El usuario debe ser un técnico");
                }
            }
        }
        if (asignacionMantenimientoDto.getId_solicitud_mantenimiento() == null) {
            errorResponse.addError("id_solicitud_mantenimiento", "Este campo es obligatorio");
        } else {
            // Verificar que exista el equipo
            SolicitudMantenimientoDto solicitudMantenimientoDto = solicitudMantenimientoService.solicitudMantenimiento(asignacionMantenimientoDto.getId_solicitud_mantenimiento());
            if (solicitudMantenimientoDto == null) {
                errorResponse.addError("id_solicitud_mantenimiento", "No se encontró el registro");
            } else {
                // verificar que la solicitud no tenga una asignacion previa
                AsignacionMantenimientoDto asignacionMantenimientoDto1 = asignacionMantenimientoService.getByIdSolicitudMantenimiento(solicitudMantenimientoDto.getId());
                AsignacionMantenimientoDto asignacionMantenimientoDto2 = asignacionMantenimientoService.asignacionMantenimiento(id);
                if (asignacionMantenimientoDto1 != null && asignacionMantenimientoDto1.getId() != asignacionMantenimientoDto2.getId()) {
                    return new ResponseEntity<String>("El registro de solicitud mantenimiento ya se encuentra con una asignación", HttpStatus.BAD_REQUEST);
                }
            }
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        AsignacionMantenimientoDto updateAsignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(id);
        if (updateAsignacionMantenimientoDto != null) {
            updateAsignacionMantenimientoDto.setId_user(asignacionMantenimientoDto.getId_user());
            updateAsignacionMantenimientoDto.setId_solicitud_mantenimiento(asignacionMantenimientoDto.getId_solicitud_mantenimiento());
            updateAsignacionMantenimientoDto = asignacionMantenimientoService.actualizar(updateAsignacionMantenimientoDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateAsignacionMantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(id);
        if (asignacionMantenimientoDto != null) {
            asignacionMantenimientoService.eliminar(asignacionMantenimientoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
