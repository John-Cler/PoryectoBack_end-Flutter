package ucb.edu.bo.Proyecto.ApiController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpBloqueService;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/bloques")
public class BloqueController {
    @Autowired
    private ImpBloqueService bloqueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BloqueDto>>> bloques() {
        List<BloqueDto> bloqueDtoList = bloqueService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true,"",bloqueDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BloqueDto>> getBloque(@PathVariable("id") Integer id) {
        BloqueDto bloqueDto = bloqueService.bloque(id);
        if (bloqueDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", bloqueDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) BloqueDto bloqueDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (bloqueDto.getNombre() == null || bloqueDto.getNombre().isEmpty()) {
            errorResponse.addError("nombre", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        BloqueDto saveBloqueDto = new BloqueDto();
        saveBloqueDto.setNombre(bloqueDto.getNombre());
        saveBloqueDto = bloqueService.guardar((saveBloqueDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveBloqueDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) BloqueDto bloqueDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (bloqueDto.getNombre() == null || bloqueDto.getNombre().isEmpty()) {
            errorResponse.addError("nombre", "Este campo es obligatorio");
        }
        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        BloqueDto updateBloqueDto = bloqueService.bloque(id);
        if (updateBloqueDto != null) {
            updateBloqueDto.setNombre(bloqueDto.getNombre());
            bloqueService.guardar(updateBloqueDto);

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateBloqueDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        BloqueDto bloqueDto = bloqueService.bloque(id);
        if (bloqueDto != null) {
            bloqueService.eliminar(bloqueDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
