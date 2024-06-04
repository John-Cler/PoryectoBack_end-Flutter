package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.entity.Departamento;
import ucb.edu.bo.Proyecto.repositories.DepartamentoRepository;
import ucb.edu.bo.Proyecto.services.IDepartamentoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpDepartamentoService implements IDepartamentoService {
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Override
    public List<DepartamentoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nombre");
        List<Departamento> departamentos = departamentoRepository.findAll(sort);
        return departamentos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public DepartamentoDto guardar(DepartamentoDto departamentoDto) {
        Departamento departamento = converToEntity(departamentoDto);
        return convertToDto(departamentoRepository.save(departamento));
    }

    @Override
    public DepartamentoDto departamento(Integer id) {
        Optional<Departamento> optionalDepartamento = departamentoRepository.findById(id);
        if (optionalDepartamento.isPresent()) {
            DepartamentoDto departamentoDto = convertToDto(optionalDepartamento.get());
            return departamentoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional
    public Boolean eliminar(DepartamentoDto departamentoDto) {
        try {
            Departamento departamento = converToEntity(departamentoDto);
            departamentoRepository.delete(departamento);
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

    private DepartamentoDto convertToDto(Departamento departamento) {
        DepartamentoDto departamentoDto = modelMapper.map(departamento, DepartamentoDto.class);
        return departamentoDto;
    }

    private Departamento converToEntity(DepartamentoDto departamentoDto) {
        Departamento departamento = modelMapper.map(departamentoDto, Departamento.class);
        return departamento;
    }
}
