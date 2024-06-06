package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.AsignacionEquipoDto;
import ucb.edu.bo.Proyecto.dto.EquipoDto;
import ucb.edu.bo.Proyecto.entity.Administrativo;
import ucb.edu.bo.Proyecto.entity.AsignacionEquipo;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.AdministrativoRepository;
import ucb.edu.bo.Proyecto.repositories.AsignacionEquipoRepository;
import ucb.edu.bo.Proyecto.repositories.EquipoRepository;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.IAsignacionEquipoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpAsignacionEquipoService implements IAsignacionEquipoService {

    private static final Logger logger = LoggerFactory.getLogger(ImpAsignacionEquipoService.class);
    @Autowired
    private AsignacionEquipoRepository asignacionEquipoRepository;
    @Autowired
    private AdministrativoRepository administrativoRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImpEquipoService equipoService;

    @Override
    public List<AsignacionEquipoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<AsignacionEquipo> asignacionEquipos = asignacionEquipoRepository.findAll(sort);
        return asignacionEquipos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AsignacionEquipoDto guardar(AsignacionEquipoDto asignacionEquipoDto) {
        AsignacionEquipo asignacionEquipo = converToEntity(asignacionEquipoDto);
        return convertToDto(asignacionEquipoRepository.save(asignacionEquipo));
    }


    @Override
    @Transactional
    @Modifying
    public AsignacionEquipoDto actualizar(AsignacionEquipoDto asignacionEquipoDto) {
        AsignacionEquipo asignacionEquipo = converToEntity(asignacionEquipoDto);
        logger.info("ID EQUIPO: " + asignacionEquipo.getEquipo().getId());
        AsignacionEquipo updateAsignacionEquipo = asignacionEquipoRepository.save(asignacionEquipo);
        AsignacionEquipoDto asignacionEquipoDto1 = convertToDto(updateAsignacionEquipo);

        logger.info("ID EQUIPO DTO: " + asignacionEquipoDto1.getEquipoDto().getId());
        return asignacionEquipoDto1;
    }

    @Override
    public AsignacionEquipoDto asignacionEquipo(Integer id) {
        Optional<AsignacionEquipo> optionalAsignacionEquipo = asignacionEquipoRepository.findById(id);
        if (optionalAsignacionEquipo.isPresent()) {
            AsignacionEquipoDto asignacionEquipoDto = convertToDto(optionalAsignacionEquipo.get());
            return asignacionEquipoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    public AsignacionEquipoDto getByIdEquipo(Integer id_equipo) {
        AsignacionEquipo asignacionEquipo = asignacionEquipoRepository.getByIdEquipo(id_equipo);
        if (asignacionEquipo != null) {
            return convertToDto(asignacionEquipo);
        }
        return null;
    }

    @Override
    public List<AsignacionEquipoDto> getByIdAdministrativo(Integer id_administrativo) {
        List<AsignacionEquipo> asignacionEquipos = asignacionEquipoRepository.getByIdAdministrativo(id_administrativo);
        return asignacionEquipos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Boolean eliminar(AsignacionEquipoDto asignacionEquipoDto) {
        try {
            AsignacionEquipo asignacionEquipo = converToEntity(asignacionEquipoDto);
            logger.info("AsignacionEquipo convertido: " + asignacionEquipo);

            // Eliminar referencias en User
            User user = asignacionEquipo.getUser();
            if (user != null) {
                user.getAsignacion_equipos().remove(asignacionEquipo);
                userRepository.save(user);
                logger.info("AsignacionEquipo eliminado de User: " + user.getId());
            }

            // Eliminar referencias en Administrativo
            Administrativo administrativo = asignacionEquipo.getAdministrativo();
            if (administrativo != null) {
                administrativo.getAsignacion_equipos().remove(asignacionEquipo);
                administrativoRepository.save(administrativo);
                logger.info("AsignacionEquipo eliminado de Administrativo: " + administrativo.getId());
            }

            // Eliminar AsignacionEquipo
            asignacionEquipoRepository.eliminaAsigacionById(asignacionEquipo.getId());
            logger.info("AsignacionEquipo eliminado: " + asignacionEquipo.getId());

            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar AsignacionEquipo: " + e.getMessage(), e);
            return false;
        }
    }

    /* ********** *
     *    MAPEO
     *********** */
    @Autowired
    private ModelMapper modelMapper;

    private AsignacionEquipoDto convertToDto(AsignacionEquipo asignacionEquipo) {
        AsignacionEquipoDto asignacionEquipoDto = modelMapper.map(asignacionEquipo, AsignacionEquipoDto.class);
        EquipoDto equipoDto = equipoService.equipo(asignacionEquipo.getEquipo().getId());

        logger.info("XXX ID EQUIPO: " + asignacionEquipo.getEquipo().getId());
        asignacionEquipoDto.setEquipoDto(equipoDto);
        asignacionEquipoDto.setId_equipo(asignacionEquipo.getEquipo().getId());
        asignacionEquipoDto.setId_administrativo(asignacionEquipo.getAdministrativo().getId());
        asignacionEquipoDto.setId_user(asignacionEquipo.getUser().getId());
        return asignacionEquipoDto;
    }

    private AsignacionEquipo converToEntity(AsignacionEquipoDto asignacionEquipoDto) {
        AsignacionEquipo asignacionEquipo = modelMapper.map(asignacionEquipoDto, AsignacionEquipo.class);
        asignacionEquipo.setAdministrativo(null);
        asignacionEquipo.setEquipo(null);
        asignacionEquipo.setUser(null);

        Optional<Administrativo> optionalAdministrativo = administrativoRepository.findById(asignacionEquipoDto.getId_administrativo());
        if (optionalAdministrativo.isPresent()) {
            asignacionEquipo.setAdministrativo(optionalAdministrativo.get());
            asignacionEquipo.setUser(optionalAdministrativo.get().getUser());
        }
        Optional<Equipo> optionalEquipo = equipoRepository.findById(asignacionEquipoDto.getId_equipo());
        if (optionalEquipo.isPresent()) {
            asignacionEquipo.setEquipo(optionalEquipo.get());
        }

        return asignacionEquipo;
    }
}
