package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.AsignacionMantenimientoDto;
import ucb.edu.bo.Proyecto.dto.MantenimientoDto;
import ucb.edu.bo.Proyecto.entity.AsignacionMantenimiento;
import ucb.edu.bo.Proyecto.entity.Mantenimiento;
import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;
import ucb.edu.bo.Proyecto.repositories.AsignacionMantenimientoRepository;
import ucb.edu.bo.Proyecto.repositories.MantenimientoRepository;
import ucb.edu.bo.Proyecto.repositories.SolicitudMantenimientoRepository;
import ucb.edu.bo.Proyecto.services.IMantenimientoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpMantenimientoService implements IMantenimientoService {
    @Autowired
    private MantenimientoRepository mantenimientoRepository;
    @Autowired
    private AsignacionMantenimientoRepository asignacionMantenimientoReopository;
    @Autowired
    private SolicitudMantenimientoRepository solicitudMantenimientoRepository;
    @Autowired
    private FechaActualService fechaActualService;
    @Autowired
    private ImpAsignacionMantenimientoService asignacionMantenimientoService;
    @Autowired
    private ImpSolicitudMantenimientoService solicitudMantenimientoService;

    @Override
    public List<MantenimientoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findAll(sort);
        return mantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MantenimientoDto> getByIdEquipo(Integer id_equipo) {
        List<Mantenimiento> mantenimientos = mantenimientoRepository.getByIdEquipo(id_equipo);
        return mantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MantenimientoDto getByIdSolicitudMantenimiento(Integer id_solicitud_mantenimiento) {
        Mantenimiento mantenimiento = mantenimientoRepository.getByIdSolicitudMantenimiento(id_solicitud_mantenimiento);
        if(mantenimiento != null){
            MantenimientoDto mantenimientoDto = convertToDto(mantenimiento);
            return mantenimientoDto;
        }
        return null;
    }

    @Override
    public MantenimientoDto getByIdAsignacionMantenimiento(Integer id_asignacion_mantenimiento) {
        Mantenimiento mantenimiento = mantenimientoRepository.getByIdAsignacionMantenimiento(id_asignacion_mantenimiento);
        if(mantenimiento != null){
            MantenimientoDto mantenimientoDto = convertToDto(mantenimiento);
            return mantenimientoDto;
        }
        return null;
    }

    @Override
    public MantenimientoDto guardar(MantenimientoDto mantenimientoDto) {
        mantenimientoDto.setFecha_registro(fechaActualService.getFechaAtual());
        Mantenimiento mantenimiento = converToEntity(mantenimientoDto);
        Mantenimiento saveMantenimiento = mantenimientoRepository.save(mantenimiento);

        // actualizar el estado de la solicitud
        AsignacionMantenimiento asignacionMantenimiento = saveMantenimiento.getAsignacion_mantenimiento();
        asignacionMantenimiento.setEstado("Finalizado");
        asignacionMantenimientoReopository.save(asignacionMantenimiento);

        return convertToDto(saveMantenimiento);
    }

    @Override
    @Modifying
    @Transactional
    public MantenimientoDto actualizar(MantenimientoDto mantenimientoDto) {
        Mantenimiento mantenimiento = converToEntity(mantenimientoDto);
        Mantenimiento mantenimientoModificado = mantenimientoRepository.save(mantenimiento);
        return convertToDto(mantenimientoModificado);
    }

    @Override
    public MantenimientoDto mantenimiento(Integer id) {
        Optional<Mantenimiento> optionalMantenimiento = mantenimientoRepository.findById(id);
        if (optionalMantenimiento.isPresent()) {
            MantenimientoDto mantenimientoDto = convertToDto(optionalMantenimiento.get());
            return mantenimientoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public Boolean eliminar(MantenimientoDto mantenimientoDto) {
        try {
            Mantenimiento mantenimiento = converToEntity(mantenimientoDto);
            // actualizar el estado de la solicitud
            AsignacionMantenimiento asignacionMantenimiento = mantenimiento.getAsignacion_mantenimiento();
            asignacionMantenimiento.setEstado("Pendiente");
            asignacionMantenimientoReopository.save(asignacionMantenimiento);
            mantenimientoRepository.delete(mantenimiento);
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

    private MantenimientoDto convertToDto(Mantenimiento mantenimiento) {
        MantenimientoDto mantenimientoDto = modelMapper.map(mantenimiento, MantenimientoDto.class);
        mantenimientoDto.setId_asignacion_mantenimiento(mantenimiento.getAsignacion_mantenimiento().getId());
        AsignacionMantenimientoDto asignacionMantenimientoDto = asignacionMantenimientoService.asignacionMantenimiento(mantenimiento.getAsignacion_mantenimiento().getId());
        mantenimientoDto.setId_solicitud_mantenimiento(asignacionMantenimientoDto.getId());
        mantenimientoDto.setSolicitudMantenimientoDto(asignacionMantenimientoDto.getSolicitudMantenimientoDto());
        mantenimientoDto.setAsignacionMantenimientoDto(asignacionMantenimientoService.asignacionMantenimiento(mantenimiento.getAsignacion_mantenimiento().getId()));
        mantenimientoDto.setId_equipo(asignacionMantenimientoDto.getSolicitudMantenimientoDto().getEquipoDto().getId());
        mantenimientoDto.setEquipoDto(asignacionMantenimientoDto.getSolicitudMantenimientoDto().getEquipoDto());
        return mantenimientoDto;
    }

    private Mantenimiento converToEntity(MantenimientoDto mantenimientoDto) {
        Mantenimiento mantenimiento = modelMapper.map(mantenimientoDto, Mantenimiento.class);
        Optional<AsignacionMantenimiento> optionalAsignacionMantenimiento = asignacionMantenimientoReopository.findById(mantenimientoDto.getId_asignacion_mantenimiento());
        if (optionalAsignacionMantenimiento.isPresent()) {
            mantenimiento.setAsignacion_mantenimiento(optionalAsignacionMantenimiento.get());
            AsignacionMantenimiento asignacionMantenimiento = optionalAsignacionMantenimiento.get();
            Optional<SolicitudMantenimiento> optionalSolicitudMantenimiento = solicitudMantenimientoRepository.findById(asignacionMantenimiento.getSolicitud_mantenimiento().getId());
            if (optionalSolicitudMantenimiento.isPresent()) {
                mantenimiento.setSolicitud_mantenimiento(optionalSolicitudMantenimiento.get());
                mantenimiento.setEquipo(optionalSolicitudMantenimiento.get().getEquipo());
            }
        }
        return mantenimiento;
    }
}
