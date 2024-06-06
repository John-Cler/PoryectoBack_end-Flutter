package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.dto.SolicitudMantenimientoDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpEquipoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpSolicitudMantenimientoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/solicitud_mantenimientos")
public class SolicitudMantenimientoController {
    @Autowired
    private ImpSolicitudMantenimientoService solicitudMantenimientoService;
    @Autowired
    private ImpUserService userService;
    @Autowired
    private ImpEquipoService equipoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SolicitudMantenimientoDto>>> solicitudMantenimientos() {
        List<SolicitudMantenimientoDto> solicitudMantenimientoDtoList = solicitudMantenimientoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", solicitudMantenimientoDtoList));
    }

    @GetMapping("por_equipo/{id}")
    public ResponseEntity<ApiResponse<List<SolicitudMantenimientoDto>>> solicitudMantenimientosByEquipo(@PathVariable("id") Integer id) {
        List<SolicitudMantenimientoDto> solicitudMantenimientoDtoList = solicitudMantenimientoService.getByIdEquipo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", solicitudMantenimientoDtoList));
    }

    @GetMapping("por_codigo_equipo")
    public ResponseEntity<?> solicitudMantenimientosByCodigoEquipo(@RequestParam("codigo") String codigo) {
        EquipoDto equipoDto = equipoService.getByCodigo(codigo);
        if (equipoDto != null) {
            List<SolicitudMantenimientoDto> solicitudMantenimientoDtoList = solicitudMantenimientoService.getByIdEquipo(equipoDto.getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "", solicitudMantenimientoDtoList));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontrarón registros con ese código", null));
    }

    @GetMapping("por_user/{id}")
    public ResponseEntity<ApiResponse<List<SolicitudMantenimientoDto>>> solicitudMantenimientosByUser(@PathVariable("id") Integer id) {
        List<SolicitudMantenimientoDto> solicitudMantenimientoDtoList = solicitudMantenimientoService.getByIdUser(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", solicitudMantenimientoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SolicitudMantenimientoDto>> getSolicitudMantenimiento(@PathVariable("id") Integer id) {
        SolicitudMantenimientoDto solicitudMantenimientoDto = solicitudMantenimientoService.solicitudMantenimiento(id);
        if (solicitudMantenimientoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", solicitudMantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) SolicitudMantenimientoDto solicitudMantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (solicitudMantenimientoDto.getId_user() == null) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        } else {
            // Verificar que exista el usuario
            UserDto userDto = userService.user(solicitudMantenimientoDto.getId_user());
            if (userDto == null) {
                errorResponse.addError("id_user", "No se encontró el registro");
            }
        }
        if (solicitudMantenimientoDto.getCodigo() == null) {
            errorResponse.addError("codigo", "Este campo es obligatorio");
        } else {
            // Verificar que exista el equipo
            EquipoDto equipoDto = equipoService.getByCodigo(solicitudMantenimientoDto.getCodigo());
            if (equipoDto == null) {
                errorResponse.addError("codigo", "No se encontró el registro");
            }
        }
        if (solicitudMantenimientoDto.getMotivo() == null || solicitudMantenimientoDto.getMotivo().isEmpty()) {
            errorResponse.addError("motivo", "Este campo es obligatorio");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        SolicitudMantenimientoDto saveSolicitudMantenimientoDto = new SolicitudMantenimientoDto();
        saveSolicitudMantenimientoDto.setId_user(solicitudMantenimientoDto.getId_user());
        saveSolicitudMantenimientoDto.setId_equipo(equipoService.getByCodigo(solicitudMantenimientoDto.getCodigo()).getId());
        saveSolicitudMantenimientoDto.setMotivo(solicitudMantenimientoDto.getMotivo());
        saveSolicitudMantenimientoDto = solicitudMantenimientoService.guardar((saveSolicitudMantenimientoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveSolicitudMantenimientoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) SolicitudMantenimientoDto solicitudMantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (solicitudMantenimientoDto.getId_user() == null) {
            errorResponse.addError("id_user", "Este campo es obligatorio");
        } else {
            // Verificar que exista el usuario
            UserDto userDto = userService.user(solicitudMantenimientoDto.getId_user());
            if (userDto == null) {
                errorResponse.addError("id_user", "No se encontró el registro");
            }
        }
        if (solicitudMantenimientoDto.getCodigo() == null) {
            errorResponse.addError("codigo", "Este campo es obligatorio");
        } else {
            // Verificar que exista el equipo
            EquipoDto equipoDto = equipoService.getByCodigo(solicitudMantenimientoDto.getCodigo());
            if (equipoDto == null) {
                errorResponse.addError("codigo", "No se encontró el registro");
            }
        }
        if (solicitudMantenimientoDto.getMotivo() == null || solicitudMantenimientoDto.getMotivo().isEmpty()) {
            errorResponse.addError("motivo", "Este campo es obligatorio");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        SolicitudMantenimientoDto updateSolicitudMantenimientoDto = solicitudMantenimientoService.solicitudMantenimiento(id);
        if (updateSolicitudMantenimientoDto != null) {
            updateSolicitudMantenimientoDto.setId_user(solicitudMantenimientoDto.getId_user());
            updateSolicitudMantenimientoDto.setId_equipo(equipoService.getByCodigo(solicitudMantenimientoDto.getCodigo()).getId());
            updateSolicitudMantenimientoDto.setMotivo(solicitudMantenimientoDto.getMotivo());
            updateSolicitudMantenimientoDto = solicitudMantenimientoService.actualizar(updateSolicitudMantenimientoDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateSolicitudMantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        SolicitudMantenimientoDto solicitudMantenimientoDto = solicitudMantenimientoService.solicitudMantenimiento(id);
        if (solicitudMantenimientoDto != null) {
            solicitudMantenimientoService.eliminar(solicitudMantenimientoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}

