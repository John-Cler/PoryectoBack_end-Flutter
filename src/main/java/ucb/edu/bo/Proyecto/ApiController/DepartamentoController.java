package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpDepartamentoService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/departamentos")
public class DepartamentoController {
    @Autowired
    private ImpDepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartamentoDto>>> departamentos() {
        List<DepartamentoDto> departamentoDtoList = departamentoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true,"",departamentoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartamentoDto>> getDepartamento(@PathVariable("id") Integer id) {
        DepartamentoDto departamentoDto = departamentoService.departamento(id);
        if (departamentoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", departamentoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) DepartamentoDto departamentoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (departamentoDto.getNombre() == null || departamentoDto.getNombre().isEmpty()) {
            errorResponse.addError("nombre", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        DepartamentoDto saveDepartamentoDto = new DepartamentoDto();
        saveDepartamentoDto.setNombre(departamentoDto.getNombre());
        saveDepartamentoDto = departamentoService.guardar((saveDepartamentoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveDepartamentoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) DepartamentoDto departamentoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (departamentoDto.getNombre() == null || departamentoDto.getNombre().isEmpty()) {
            errorResponse.addError("nombre", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        DepartamentoDto updateDepartamentoDto = departamentoService.departamento(id);
        if (updateDepartamentoDto != null) {
            updateDepartamentoDto.setNombre(departamentoDto.getNombre());
            departamentoService.guardar(updateDepartamentoDto);

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateDepartamentoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        DepartamentoDto departamentoDto = departamentoService.departamento(id);
        if (departamentoDto != null) {
            departamentoService.eliminar(departamentoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
