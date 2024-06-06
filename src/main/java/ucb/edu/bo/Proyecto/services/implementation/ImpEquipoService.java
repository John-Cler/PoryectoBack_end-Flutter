package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.entity.Bloque;
import ucb.edu.bo.Proyecto.entity.Departamento;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.repositories.BloqueRepository;
import ucb.edu.bo.Proyecto.repositories.DepartamentoRepository;
import ucb.edu.bo.Proyecto.repositories.EquipoRepository;
import ucb.edu.bo.Proyecto.services.IEquipoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpEquipoService implements IEquipoService {
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private BloqueRepository bloqueRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ImpBloqueService bloqueService;
    @Autowired
    private ImpDepartamentoService departamentoService;

    @Override
    public List<EquipoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Equipo> equipos = equipoRepository.findAll(sort);
        return equipos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipoDto guardar(EquipoDto equipoDto) {
        Equipo equipo = converToEntity(equipoDto);
        return convertToDto(equipoRepository.save(equipo));
    }

    @Override
    @Transactional
    @Modifying
    public EquipoDto actualizar(EquipoDto equipoDto) {
        Equipo equipo = converToEntity(equipoDto);
        return convertToDto(equipoRepository.save(equipo));
    }

    @Override
    public EquipoDto getByCodigo(String codigo) {
        Equipo equipo = equipoRepository.findByCodigo(codigo);
        if (equipo != null) {
            EquipoDto equipoDto = convertToDto(equipo);
            return equipoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    public EquipoDto equipo(Integer id) {
        Optional<Equipo> optionalEquipo = equipoRepository.findById(id);
        if (optionalEquipo.isPresent()) {
            EquipoDto equipoDto = convertToDto(optionalEquipo.get());
            return equipoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional
    public Boolean eliminar(EquipoDto equipoDto) {
        try {
            Equipo equipo = converToEntity(equipoDto);
            equipoRepository.delete(equipo);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /* ********** *
     *    MAPEO
     *********** */
    @Autowired
    private ModelMapper modelMapper;

    private EquipoDto convertToDto(Equipo equipo) {
        EquipoDto equipoDto = modelMapper.map(equipo, EquipoDto.class);
        if (equipo.getBloque() != null) {
            equipoDto.setId_bloque(equipo.getBloque().getId());
            BloqueDto bloqueDto = bloqueService.bloque(equipo.getBloque().getId());
            equipoDto.setBloqueDto(bloqueDto);
        }
        if (equipo.getDepartamento() != null) {
            equipoDto.setId_departamento(equipo.getDepartamento().getId());
            DepartamentoDto departamentoDto = departamentoService.departamento(equipo.getDepartamento().getId());
            equipoDto.setDepartamentoDto(departamentoDto);
        }
        return equipoDto;
    }

    private Equipo converToEntity(EquipoDto equipoDto) {
        Equipo equipo = modelMapper.map(equipoDto, Equipo.class);
        if (equipoDto.getId() != null) {
            Optional<Equipo> optionalEquipo = equipoRepository.findById(equipoDto.getId());
            if (optionalEquipo.isPresent()) {
                equipo.setEquipo_partes(optionalEquipo.get().getEquipo_partes());
            }
        }
        Optional<Bloque> optionalBloque = bloqueRepository.findById(equipoDto.getId_bloque());
        if (optionalBloque.isPresent()) {
            equipo.setBloque(optionalBloque.get());
        }
        Optional<Departamento> optionalDepartamento = departamentoRepository.findById(equipoDto.getId_departamento());
        if (optionalDepartamento.isPresent()) {
            equipo.setDepartamento(optionalDepartamento.get());
        }
        return equipo;
    }
}
