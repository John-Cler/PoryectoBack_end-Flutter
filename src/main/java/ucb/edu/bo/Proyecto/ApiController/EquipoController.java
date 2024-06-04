package ucb.edu.bo.Proyecto.ApiController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.dto.responses.ApiResponse;
import ucb.edu.bo.Proyecto.dto.responses.ValidationErrorsResponse;
import ucb.edu.bo.Proyecto.services.implementation.ImpBloqueService;
import ucb.edu.bo.Proyecto.services.implementation.ImpDepartamentoService;
import ucb.edu.bo.Proyecto.services.implementation.ImpEquipoService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/equipos")
public class EquipoController {
    @Autowired
    private ImpEquipoService equipoService;
    @Autowired
    private ImpBloqueService bloqueService;
    @Autowired
    private ImpDepartamentoService departamentoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EquipoDto>>> equipos() {
        List<EquipoDto> equipoDtoList = equipoService.listado();
        return ResponseEntity.ok(new ApiResponse<>(true,"",equipoDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EquipoDto>> getEquipo(@PathVariable("id") Integer id) {
        EquipoDto equipoDto = equipoService.equipo(id);
        if (equipoDto != null) {

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro obtenido", equipoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se encontró el registro", null));
    }

    @PostMapping(value = "/guardar")
    public ResponseEntity<?> guardar(@RequestBody(required = true) EquipoDto equipoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (equipoDto.getTipo() == null || equipoDto.getTipo().isEmpty()) {
            errorResponse.addError("tipo", "Este campo es obligatorio");
        }
        if (equipoDto.getId_departamento() == null) {
            errorResponse.addError("id_departamento", "Este campo es obligatorio");
        }
        if (equipoDto.getId_bloque() == null) {
            errorResponse.addError("id_bloque", "Este campo es obligatorio");
        }
        if (equipoDto.getNro_activo() == null || equipoDto.getNro_activo().isEmpty()) {
            errorResponse.addError("nro_activo", "Este campo es obligatorio");
        }

        // verificar existencia de bloques, departamentos
        BloqueDto bloqueDto = bloqueService.bloque(equipoDto.getId_bloque());
        if (bloqueDto == null) {
            errorResponse.addError("id_bloque", "No se encontró el registro");
        }
        DepartamentoDto departamentoDto = departamentoService.departamento(equipoDto.getId_departamento());
        if (departamentoDto == null) {
            errorResponse.addError("id_departamento", "No se encontró el registro");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        EquipoDto saveEquipoDto = new EquipoDto();
        saveEquipoDto.setTipo(equipoDto.getTipo());
        saveEquipoDto.setId_departamento(equipoDto.getId_departamento());
        saveEquipoDto.setId_bloque(equipoDto.getId_bloque());
        saveEquipoDto.setDescripcion(equipoDto.getDescripcion());
        saveEquipoDto.setNro_activo(equipoDto.getNro_activo());
        saveEquipoDto.setNro_serie(equipoDto.getNro_serie());
        saveEquipoDto.setMarca(equipoDto.getMarca());
        saveEquipoDto.setModelo(equipoDto.getModelo());
        saveEquipoDto = equipoService.guardar((saveEquipoDto));
        return ResponseEntity.ok(new ApiResponse<>(true, "Registro correcto", saveEquipoDto));
    }

    @PostMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable("id") Integer id, @RequestBody(required = true) EquipoDto equipoDto) {
        ValidationErrorsResponse errorResponse = new ValidationErrorsResponse();
        // Validar los datos
        if (equipoDto.getTipo() == null || equipoDto.getTipo().isEmpty()) {
            errorResponse.addError("tipo", "Este campo es obligatorio");
        }
        if (equipoDto.getId_departamento() == null) {
            errorResponse.addError("id_departamento", "Este campo es obligatorio");
        }
        if (equipoDto.getId_bloque() == null) {
            errorResponse.addError("id_bloque", "Este campo es obligatorio");
        }
        if (equipoDto.getNro_activo() == null || equipoDto.getNro_activo().isEmpty()) {
            errorResponse.addError("nro_activo", "Este campo es obligatorio");
        }

        // verificar existencia de bloques, departamentos
        BloqueDto bloqueDto = bloqueService.bloque(equipoDto.getId_bloque());
        if (bloqueDto == null) {
            errorResponse.addError("id_bloque", "No se encontró el registro");
        }
        DepartamentoDto departamentoDto = departamentoService.departamento(equipoDto.getId_departamento());
        if (departamentoDto == null) {
            errorResponse.addError("id_departamento", "No se encontró el registro");
        }

        if (!errorResponse.getErrors().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        EquipoDto updateEquipoDto = equipoService.equipo(id);
        if (updateEquipoDto != null) {
            updateEquipoDto.setTipo(equipoDto.getTipo());
            updateEquipoDto.setId_departamento(equipoDto.getId_departamento());
            updateEquipoDto.setId_bloque(equipoDto.getId_bloque());
            updateEquipoDto.setDescripcion(equipoDto.getDescripcion());
            updateEquipoDto.setNro_activo(equipoDto.getNro_activo());
            updateEquipoDto.setNro_serie(equipoDto.getNro_serie());
            updateEquipoDto.setMarca(equipoDto.getMarca());
            updateEquipoDto.setModelo(equipoDto.getModelo());
            updateEquipoDto = equipoService.guardar((updateEquipoDto));
            equipoService.guardar(updateEquipoDto);

            return ResponseEntity.ok(new ApiResponse<>(true, "Registro actualizado", updateEquipoDto));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "No se econtró el registro", null));
    }

    @PostMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) {
        EquipoDto equipoDto = equipoService.equipo(id);
        if (equipoDto != null) {
            equipoService.eliminar(equipoDto);
            return ResponseEntity.ok("Registro eliminado");
        }
        return new ResponseEntity<String>("No se encontró el registro", HttpStatus.BAD_REQUEST);
    }
}
