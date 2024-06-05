package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.EquipoParteDto;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.EquipoParte;
import ucb.edu.bo.Proyecto.repositories.EquipoParteRepository;
import ucb.edu.bo.Proyecto.repositories.EquipoRepository;
import ucb.edu.bo.Proyecto.services.IEquipoParteService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpEquipoParteService implements IEquipoParteService {
    @Autowired
    private EquipoParteRepository equipoParteRepository;
    @Autowired
    EquipoRepository equipoRepository;

    @Override
    public List<EquipoParteDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<EquipoParte> equipoPartes = equipoParteRepository.findAll(sort);
        return equipoPartes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipoParteDto> getByIdEquipo(Integer id_equipo) {
        List<EquipoParte> equipoPartes = equipoParteRepository.getByIdEquipo(id_equipo);
        return equipoPartes.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public EquipoParteDto guardar(EquipoParteDto equipoParteDto) {
        EquipoParte equipoParte = converToEntity(equipoParteDto);
        return convertToDto(equipoParteRepository.save(equipoParte));
    }

    @Override
    public EquipoParteDto equipoParte(Integer id) {
        Optional<EquipoParte> optionalEquipoParte = equipoParteRepository.findById(id);
        if (optionalEquipoParte.isPresent()) {
            EquipoParteDto equipoParteDto = convertToDto(optionalEquipoParte.get());
            return equipoParteDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional
    public Boolean eliminar(EquipoParteDto equipoParteDto) {
        try {
            EquipoParte equipoParte = converToEntity(equipoParteDto);
            equipoParteRepository.delete(equipoParte);
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

    private EquipoParteDto convertToDto(EquipoParte equipoParte) {
        EquipoParteDto equipoParteDto = modelMapper.map(equipoParte, EquipoParteDto.class);
        equipoParteDto.setId_equipo(equipoParte.getEquipo().getId());
        return equipoParteDto;
    }

    private EquipoParte converToEntity(EquipoParteDto equipoParteDto) {
        EquipoParte equipoParte = modelMapper.map(equipoParteDto, EquipoParte.class);
        Optional<Equipo> optionalEquipo = equipoRepository.findById(equipoParteDto.getId_equipo());
        if (optionalEquipo.isPresent()) {
            equipoParte.setEquipo(optionalEquipo.get());
        }
        return equipoParte;
    }
}
