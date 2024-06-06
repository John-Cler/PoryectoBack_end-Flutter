package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.AdministrativoDto;
import ucb.edu.bo.Proyecto.dto.AsignacionEquipoDto;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpAdministrativoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpAsignacionEquipoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpEquipoService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/asignacion_equipos")
public class AsignacionEquipoController {
    @Autowired
    private ImpAsignacionEquipoService asignacionEquipoService;
    @Autowired
    private ImpAdministrativoService administrativoService;
    @Autowired
    private ImpEquipoService equipoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AsignacionEquipoDto>>> asignacionEquipos() {
        List<AsignacionEquipoDto> asignacionEquipoDtoList = asignacionEquipoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionEquipoDtoList));
    }

    @GetMapping("/por_equipo/{id}")
    public ResponseEntity<ApiResponse<AsignacionEquipoDto>> getByIdEquipo(@PathVariable("id") Integer id) {
        AsignacionEquipoDto asignacionEquipoDto = asignacionEquipoService.getByIdEquipo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionEquipoDto));
    }

    @GetMapping("/por_administrativo/{id}")
    public ResponseEntity<ApiResponse<List<AsignacionEquipoDto>>> getByAdministrativo(@PathVariable("id") Integer id) {
        List<AsignacionEquipoDto> asignacionEquipoDtoList = asignacionEquipoService.getByIdAdministrativo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", asignacionEquipoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AsignacionEquipoDto>> getAsignacionEquipo(@PathVariable("id") Integer id) {
        AsignacionEquipoDto asignacionEquipoDto = asignacionEquipoService.asignacionEquipo(id);
        if (asignacionEquipoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", asignacionEquipoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) AsignacionEquipoDto asignacionEquipoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (asignacionEquipoDto.getId_equipo() == null) {
            errorResponse.addError("id_equipo", "Este campo es obligatorio");
        }else{
            // Verificar que el equipo y administrativo existen
            EquipoDto equipoDto = equipoService.equipo(asignacionEquipoDto.getId_equipo());
            if (equipoDto == null) {
                errorResponse.addError("id_equipo", "No se encontró el registro");
            }
            // Verificar que el equipo que se quiere asignar no se encuentre asignado a otro Administrativo
            AsignacionEquipoDto asignacionEquipoDto1 = asignacionEquipoService.getByIdEquipo(asignacionEquipoDto.getId_equipo());
            if (asignacionEquipoDto1 != null) {
                errorResponse.addError("id_equipo", "Este registro ya se encuentra asignado");
            }
        }

        if (asignacionEquipoDto.getId_administrativo() == null) {
            errorResponse.addError("id_administrativo", "Este campo es obligatorio");
        }else{
            AdministrativoDto administrativoDto = administrativoService.administrativo(asignacionEquipoDto.getId_administrativo());
            if (administrativoDto == null) {
                errorResponse.addError("id_administrativo", "No se encontró el registro");
            }
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        AsignacionEquipoDto saveAsignacionEquipoDto = new AsignacionEquipoDto();
        saveAsignacionEquipoDto.setId_equipo(asignacionEquipoDto.getId_equipo());
        saveAsignacionEquipoDto.setId_administrativo(asignacionEquipoDto.getId_administrativo());
        saveAsignacionEquipoDto = asignacionEquipoService.guardar(saveAsignacionEquipoDto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveAsignacionEquipoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) AsignacionEquipoDto asignacionEquipoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (asignacionEquipoDto.getId_equipo() == null) {
            errorResponse.addError("id_equipo", "Este campo es obligatorio");
        }else{
            // Verificar que el equipo y administrativo existen
            EquipoDto equipoDto = equipoService.equipo(asignacionEquipoDto.getId_equipo());
            if (equipoDto == null) {
                errorResponse.addError("id_equipo", "No se encontró el registro");
            }
            // Verificar que el equipo que se quiere asignar no se encuentre asignado a otro Administrativo
            AsignacionEquipoDto asignacionEquipoDto1 = asignacionEquipoService.getByIdEquipo(asignacionEquipoDto.getId_equipo());
            AsignacionEquipoDto asignacionEquipoDto2 = asignacionEquipoService.asignacionEquipo(id);
            if (asignacionEquipoDto1 != null) {
                if( asignacionEquipoDto1.getId() != asignacionEquipoDto2.getId()){
                    errorResponse.addError("id_equipo", "Este registro ya se encuentra asignado");
                }
            }
        }

        if (asignacionEquipoDto.getId_administrativo() == null) {
            errorResponse.addError("id_administrativo", "Este campo es obligatorio");
        }else{
            AdministrativoDto administrativoDto = administrativoService.administrativo(asignacionEquipoDto.getId_administrativo());
            if (administrativoDto == null) {
                errorResponse.addError("id_administrativo", "No se encontró el registro");
            }
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        AsignacionEquipoDto updateAsignacionEquipoDto = asignacionEquipoService.asignacionEquipo(id);
        if (updateAsignacionEquipoDto != null) {;
            updateAsignacionEquipoDto.setId_equipo(asignacionEquipoDto.getId_equipo());
            updateAsignacionEquipoDto.setId_administrativo(asignacionEquipoDto.getId_administrativo());
            AsignacionEquipoDto asignacionEquipoDtoModificado = asignacionEquipoService.actualizar(updateAsignacionEquipoDto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", asignacionEquipoDtoModificado));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        AsignacionEquipoDto asignacionEquipoDto = asignacionEquipoService.asignacionEquipo(id);
        if (asignacionEquipoDto != null) {
            asignacionEquipoService.eliminar(asignacionEquipoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}