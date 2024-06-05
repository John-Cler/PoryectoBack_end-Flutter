package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.EquipoParteDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpEquipoParteService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/equipo_partes")
public class EquipoParteController {
    @Autowired
    private ImpEquipoParteService equipoParteService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipoParteDto>>> equipoPartes() {
        List<EquipoParteDto> equipoParteDtoList = equipoParteService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true, "", equipoParteDtoList));
    }

    @GetMapping("/por_equipo/{id}")
    public ResponseEntity<ApiResponse<List<EquipoParteDto>>> equipoPartes(@PathVariable("id") Integer id) {
        List<EquipoParteDto> equipoParteDtoList = equipoParteService.getByIdEquipo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "", equipoParteDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EquipoParteDto>> getEquipoParte(@PathVariable("id") Integer id) {
        EquipoParteDto equipoParteDto = equipoParteService.equipoParte(id);
        if (equipoParteDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", equipoParteDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) EquipoParteDto equipoParteDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (equipoParteDto.getId_equipo() == null) {
            errorResponse.addError("id_equipo", "Este campo es obligatorio");
        }
        if (equipoParteDto.getDescripcion() == null || equipoParteDto.getDescripcion().isEmpty()) {
            errorResponse.addError("descripcion", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        EquipoParteDto saveEquipoParteDto = new EquipoParteDto();
        saveEquipoParteDto.setId_equipo(equipoParteDto.getId_equipo());
        saveEquipoParteDto.setDescripcion(equipoParteDto.getDescripcion());
        saveEquipoParteDto = equipoParteService.guardar((saveEquipoParteDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveEquipoParteDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) EquipoParteDto equipoParteDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (equipoParteDto.getDescripcion() == null || equipoParteDto.getDescripcion().isEmpty()) {
            errorResponse.addError("descripcion", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        EquipoParteDto updateEquipoParteDto = equipoParteService.equipoParte(id);
        if (updateEquipoParteDto != null) {
            updateEquipoParteDto.setDescripcion(equipoParteDto.getDescripcion());
            updateEquipoParteDto = equipoParteService.guardar((updateEquipoParteDto));
            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateEquipoParteDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        EquipoParteDto equipoParteDto = equipoParteService.equipoParte(id);
        if (equipoParteDto != null) {
            equipoParteService.eliminar(equipoParteDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
