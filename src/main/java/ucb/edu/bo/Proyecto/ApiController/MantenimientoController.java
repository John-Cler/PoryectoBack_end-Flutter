package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.AsignacionMantenimientoDto;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.dto.MantenimientoDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpAsignacionMantenimientoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpEquipoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpMantenimientoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpSolicitudMantenimientoService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/mantenimientos")
public class MantenimientoController {
    @Autowired
    private ImpMantenimientoService mantenimientoService;
    @Autowired
    private ImpAsignacionMantenimientoService asignacionMantenimientoService;
    @Autowired
    private ImpSolicitudMantenimientoService solicitudMantenimientoService;
    @Autowired
    private ImpEquipoService equipoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MantenimientoDto>>> mantenimientos() {
        List<MantenimientoDto> mantenimientoDtoList = mantenimientoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", mantenimientoDtoList));
    }

    @GetMapping("por_equipo/{id}")
    public ResponseEntity<ApiResponse<List<MantenimientoDto>>> mantenimientoByIdEquipo(@PathVariable("id") Integer id) {
        List<MantenimientoDto> mantenimientoDtoList = mantenimientoService.getByIdEquipo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", mantenimientoDtoList));
    }

    @GetMapping("por_codigo_equipo")
    public ResponseEntity<ApiResponse<List<MantenimientoDto>>> mantenimientoByCodigoEquipo(@RequestParam("codigo") String codigo) {
        EquipoDto equipoDto = equipoService.getByCodigo(codigo);
        if (equipoDto != null) {
            List<MantenimientoDto> mantenimientoDtoList = mantenimientoService.getByIdEquipo(equipoDto.getId());
            return ResponseEntity.ok(new ApiResponse<>(true, "", mantenimientoDtoList));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontrarón registros con ese código", null));

    }

    @GetMapping("por_solicitud_mantenimiento/{id}")
    public ResponseEntity<ApiResponse<MantenimientoDto>> mantenimientosBySolicitudMantenimiento(@PathVariable("id") Integer id) {
        MantenimientoDto mantenimientoDto = mantenimientoService.getByIdSolicitudMantenimiento(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", mantenimientoDto));
    }

    @GetMapping("por_asignacion_mantenimiento/{id}")
    public ResponseEntity<ApiResponse<MantenimientoDto>> mantenimientosByAsignacionMantenimiento(@PathVariable("id") Integer id) {
        MantenimientoDto mantenimientoDto = mantenimientoService.getByIdAsignacionMantenimiento(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", mantenimientoDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MantenimientoDto>> getMantenimiento(@PathVariable("id") Integer id) {
        MantenimientoDto mantenimientoDto = mantenimientoService.mantenimiento(id);
        if (mantenimientoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", mantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) MantenimientoDto mantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (mantenimientoDto.getId_asignacion_mantenimiento() == null) {
            errorResponse.addError("id_asignacion_mantenimiento", "Este campo es obligatorio");
        } else {
            // Verificar que exista la asignacion de mantenimiento
            AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(mantenimientoDto.getId_asignacion_mantenimiento());
            if (asignacionMantenimientoDto == null) {
                errorResponse.addError("id_asignacion_mantenimiento", "No se encontró el registro");
            } else {
                // verificar que la solicitud no tenga una asignacion previa
                MantenimientoDto mantenimientoDto1 = mantenimientoService.getByIdAsignacionMantenimiento(mantenimientoDto.getId_asignacion_mantenimiento());
                if (mantenimientoDto1 != null) {
                    return new ResponseEntity<String>("El registro de solicitud mantenimiento ya se encuentra con una asignación", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (mantenimientoDto.getDescripcion() == null || mantenimientoDto.getDescripcion().isEmpty()) {
            errorResponse.addError("descripcion", "Este campo es obligatorio");
        }
        if (mantenimientoDto.getObservaciones() == null || mantenimientoDto.getObservaciones().isEmpty()) {
            errorResponse.addError("observaciones", "Este campo es obligatorio");
        }
        if (mantenimientoDto.getEstado_equipo() == null || mantenimientoDto.getEstado_equipo().isEmpty()) {
            errorResponse.addError("estado_equipo", "Este campo es obligatorio");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        MantenimientoDto saveMantenimientoDto = new MantenimientoDto();
        saveMantenimientoDto.setId_asignacion_mantenimiento(mantenimientoDto.getId_asignacion_mantenimiento());
        saveMantenimientoDto.setDescripcion(mantenimientoDto.getDescripcion());
        saveMantenimientoDto.setObservaciones(mantenimientoDto.getObservaciones());
        saveMantenimientoDto.setEstado_equipo(mantenimientoDto.getEstado_equipo());
        saveMantenimientoDto = mantenimientoService.guardar((saveMantenimientoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveMantenimientoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) MantenimientoDto mantenimientoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (mantenimientoDto.getId_asignacion_mantenimiento() == null) {
            errorResponse.addError("id_asignacion_mantenimiento", "Este campo es obligatorio");
        } else {
            // Verificar que exista el equipo
            AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(mantenimientoDto.getId_asignacion_mantenimiento());
            if (asignacionMantenimientoDto == null) {
                errorResponse.addError("id_asignacion_mantenimiento", "No se encontró el registro");
            } else {
                // verificar que la solicitud no tenga una asignacion previa
                MantenimientoDto mantenimientoDto1 = mantenimientoService.getByIdAsignacionMantenimiento(asignacionMantenimientoDto.getId());
                MantenimientoDto mantenimientoDto2 = mantenimientoService.mantenimiento(id);
                if (mantenimientoDto1 != null && mantenimientoDto1.getId() != mantenimientoDto2.getId()) {
                    return new ResponseEntity<String>("El registro de solicitud mantenimiento ya se encuentra con una asignación", HttpStatus.BAD_REQUEST);
                }
            }
        }
        if (mantenimientoDto.getDescripcion() == null || mantenimientoDto.getDescripcion().isEmpty()) {
            errorResponse.addError("descripcion", "Este campo es obligatorio");
        }
        if (mantenimientoDto.getObservaciones() == null || mantenimientoDto.getObservaciones().isEmpty()) {
            errorResponse.addError("observaciones", "Este campo es obligatorio");
        }
        if (mantenimientoDto.getEstado_equipo() == null || mantenimientoDto.getEstado_equipo().isEmpty()) {
            errorResponse.addError("estado_equipo", "Este campo es obligatorio");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        MantenimientoDto updateMantenimientoDto = mantenimientoService.mantenimiento(id);
        if (updateMantenimientoDto != null) {
            updateMantenimientoDto.setId_asignacion_mantenimiento(mantenimientoDto.getId_asignacion_mantenimiento());
            updateMantenimientoDto.setDescripcion(mantenimientoDto.getDescripcion());
            updateMantenimientoDto.setObservaciones(mantenimientoDto.getObservaciones());
            updateMantenimientoDto.setEstado_equipo(mantenimientoDto.getEstado_equipo());
            updateMantenimientoDto = mantenimientoService.actualizar(updateMantenimientoDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateMantenimientoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        MantenimientoDto mantenimientoDto = mantenimientoService.mantenimiento(id);
        if (mantenimientoDto != null) {
            mantenimientoService.eliminar(mantenimientoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
