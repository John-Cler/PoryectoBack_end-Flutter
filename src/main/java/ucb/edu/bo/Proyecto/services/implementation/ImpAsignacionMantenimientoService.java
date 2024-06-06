package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.AsignacionMantenimientoDto;
import ucb.edu.bo.Proyecto.entity.AsignacionMantenimiento;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.AsignacionMantenimientoRepository;
import ucb.edu.bo.Proyecto.repositories.EquipoRepository;
import ucb.edu.bo.Proyecto.repositories.SolicitudMantenimientoRepository;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.IAsignacionMantenimientoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpAsignacionMantenimientoService implements IAsignacionMantenimientoService {
    @Autowired
    private AsignacionMantenimientoRepository asignacionMantenimientoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SolicitudMantenimientoRepository solicitudMantenimientoRepository;
    @Autowired
    private FechaActualService fechaActualService;
    @Autowired
    private ImpUserService userService;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private ImpSolicitudMantenimientoService solicitudMantenimientoService;

    @Override
    public List<AsignacionMantenimientoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<AsignacionMantenimiento> asignacionMantenimientos = asignacionMantenimientoRepository.findAll(sort);
        return asignacionMantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AsignacionMantenimientoDto getByIdSolicitudMantenimiento(Integer id_solicitud_mantenimiento) {
        AsignacionMantenimiento asignacionMantenimiento = asignacionMantenimientoRepository.getByIdSolicitudMantenimiento(id_solicitud_mantenimiento);
        if (asignacionMantenimiento != null) {
            AsignacionMantenimientoDto asignacionMantenimientoDto = convertToDto(asignacionMantenimiento);
            return asignacionMantenimientoDto;
        }
        return null;
    }

    @Override
    public List<AsignacionMantenimientoDto> getByIdUser(Integer id_user) {
        List<AsignacionMantenimiento> asignacionMantenimientos = asignacionMantenimientoRepository.getByIdUser(id_user);
        return asignacionMantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AsignacionMantenimientoDto guardar(AsignacionMantenimientoDto asignacionMantenimientoDto) {
        asignacionMantenimientoDto.setEstado("Pendiente");
        asignacionMantenimientoDto.setFecha_asignacion(fechaActualService.getFechaAtual());
        AsignacionMantenimiento asignacionMantenimiento = converToEntity(asignacionMantenimientoDto);
        AsignacionMantenimiento saveAsignacionMantenimiento = asignacionMantenimientoRepository.save(asignacionMantenimiento);

        //actualizar estado equipo
        Equipo equipo = saveAsignacionMantenimiento.getSolicitud_mantenimiento().getEquipo();
        equipo.setEstado("En Mantenimiento");
        equipo = equipoRepository.save(equipo);
        saveAsignacionMantenimiento.getSolicitud_mantenimiento().setEquipo(equipo);

        saveAsignacionMantenimiento.setCodigo("AM." + saveAsignacionMantenimiento.getId());
        saveAsignacionMantenimiento = asignacionMantenimientoRepository.save(saveAsignacionMantenimiento);

        // actualizar el estado de la solicitud
        SolicitudMantenimiento solicitudMantenimiento = saveAsignacionMantenimiento.getSolicitud_mantenimiento();
        solicitudMantenimiento.setEstado("Asignado");
        solicitudMantenimientoRepository.save(solicitudMantenimiento);

        return convertToDto(saveAsignacionMantenimiento);
    }

    @Override
    @Modifying
    @Transactional
    public AsignacionMantenimientoDto actualizar(AsignacionMantenimientoDto asignacionMantenimientoDto) {
        AsignacionMantenimiento asignacionMantenimiento = converToEntity(asignacionMantenimientoDto);
        AsignacionMantenimiento asignacionMantenimientoModificado = asignacionMantenimientoRepository.save(asignacionMantenimiento);
        return convertToDto(asignacionMantenimientoModificado);
    }

    @Override
    public AsignacionMantenimientoDto asignacionMantenimiento(Integer id) {
        Optional<AsignacionMantenimiento> optionalAsignacionMantenimiento = asignacionMantenimientoRepository.findById(id);
        if (optionalAsignacionMantenimiento.isPresent()) {
            AsignacionMantenimientoDto asignacionMantenimientoDto = convertToDto(optionalAsignacionMantenimiento.get());
            return asignacionMantenimientoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public Boolean eliminar(AsignacionMantenimientoDto asignacionMantenimientoDto) {
        try {
            AsignacionMantenimiento asignacionMantenimiento = converToEntity(asignacionMantenimientoDto);
            // actualizar el estado de la solicitud
            SolicitudMantenimiento solicitudMantenimiento = asignacionMantenimiento.getSolicitud_mantenimiento();
            solicitudMantenimiento.setEstado("Pendiente");

            //actualizar estado equipo
            Equipo equipo = asignacionMantenimiento.getSolicitud_mantenimiento().getEquipo();
            equipo.setEstado("Mantenimiento Pendiente");
            equipoRepository.save(equipo);
            solicitudMantenimientoRepository.save(solicitudMantenimiento);

            asignacionMantenimiento.getSolicitud_mantenimiento().setAsignacionMantenimiento(null);
            asignacionMantenimientoRepository.delete(asignacionMantenimiento);
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

    private AsignacionMantenimientoDto convertToDto(AsignacionMantenimiento asignacionMantenimiento) {
        AsignacionMantenimientoDto asignacionMantenimientoDto = modelMapper.map(asignacionMantenimiento, AsignacionMantenimientoDto.class);
        asignacionMantenimientoDto.setId_user(asignacionMantenimiento.getUser().getId());
        asignacionMantenimientoDto.setId_solicitud_mantenimiento(asignacionMantenimiento.getSolicitud_mantenimiento().getId());
        asignacionMantenimientoDto.setSolicitudMantenimientoDto(solicitudMantenimientoService.solicitudMantenimiento(asignacionMantenimiento.getSolicitud_mantenimiento().getId()));
        asignacionMantenimientoDto.setUserDto(userService.user(asignacionMantenimiento.getUser().getId()));
        return asignacionMantenimientoDto;
    }

    private AsignacionMantenimiento converToEntity(AsignacionMantenimientoDto asignacionMantenimientoDto) {
        AsignacionMantenimiento asignacionMantenimiento = modelMapper.map(asignacionMantenimientoDto, AsignacionMantenimiento.class);
        Optional<User> optionalUser = userRepository.findById(asignacionMantenimientoDto.getId_user());
        if (optionalUser.isPresent()) {
            asignacionMantenimiento.setUser(optionalUser.get());
        }
        Optional<SolicitudMantenimiento> optionalSolicitudMantenimiento = solicitudMantenimientoRepository.findById(asignacionMantenimientoDto.getId_solicitud_mantenimiento());
        if (optionalSolicitudMantenimiento.isPresent()) {
            asignacionMantenimiento.setSolicitud_mantenimiento(optionalSolicitudMantenimiento.get());
        }
        return asignacionMantenimiento;
    }
}
