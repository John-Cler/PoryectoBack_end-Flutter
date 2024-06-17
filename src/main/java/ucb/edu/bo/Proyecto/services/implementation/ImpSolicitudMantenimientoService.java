package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.SolicitudMantenimientoDto;
import ucb.edu.bo.Proyecto.entity.Equipo;
import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.EquipoRepository;
import ucb.edu.bo.Proyecto.repositories.SolicitudMantenimientoRepository;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.ISolicitudMantenimientoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpSolicitudMantenimientoService implements ISolicitudMantenimientoService {
    @Autowired
    private SolicitudMantenimientoRepository solicitudMantenimientoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private FechaActualService fechaActualService;
    @Autowired
    private ImpUserService userService;
    @Autowired
    private ImpEquipoService equipoService;

    @Override
    public List<SolicitudMantenimientoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<SolicitudMantenimiento> solicitudMantenimientos = solicitudMantenimientoRepository.findAll(sort);
        return solicitudMantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SolicitudMantenimientoDto> getByIdEquipo(Integer id_equipo) {
        List<SolicitudMantenimiento> solicitudMantenimientos = solicitudMantenimientoRepository.getByIdEquipo(id_equipo);
        return solicitudMantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<SolicitudMantenimientoDto> getByIdUser(Integer id_user) {
        List<SolicitudMantenimiento> solicitudMantenimientos = solicitudMantenimientoRepository.getByIdUser(id_user);
        return solicitudMantenimientos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SolicitudMantenimientoDto guardar(SolicitudMantenimientoDto solicitudMantenimientoDto) {
        solicitudMantenimientoDto.setEstado("Pendiente");
        solicitudMantenimientoDto.setFecha_registro(fechaActualService.getFechaAtual());
        SolicitudMantenimiento solicitudMantenimiento = converToEntity(solicitudMantenimientoDto);
        SolicitudMantenimiento saveSolicitudMantenimiento = solicitudMantenimientoRepository.save(solicitudMantenimiento);

        //actualizar estado equipo
        Equipo equipo = saveSolicitudMantenimiento.getEquipo();
        equipo.setEstado("Mantenimiento Pendiente");
        equipo = equipoRepository.save(equipo);

        saveSolicitudMantenimiento.setEquipo(equipo);
        saveSolicitudMantenimiento.setCodigo("SM."+saveSolicitudMantenimiento.getId());
        saveSolicitudMantenimiento = solicitudMantenimientoRepository.save(saveSolicitudMantenimiento);


        return convertToDto(saveSolicitudMantenimiento);
    }

    @Override
    @Modifying
    @Transactional
    public SolicitudMantenimientoDto actualizar(SolicitudMantenimientoDto solicitudMantenimientoDto) {
        SolicitudMantenimiento solicitudMantenimiento = converToEntity(solicitudMantenimientoDto);
        SolicitudMantenimiento solicitudMantenimientoModificado = solicitudMantenimientoRepository.save(solicitudMantenimiento);
        return convertToDto(solicitudMantenimientoModificado);
    }

    @Override
    public SolicitudMantenimientoDto solicitudMantenimiento(Integer id) {
        Optional<SolicitudMantenimiento> optionalSolicitudMantenimiento = solicitudMantenimientoRepository.findById(id);
        if (optionalSolicitudMantenimiento.isPresent()) {
            SolicitudMantenimientoDto solicitudMantenimientoDto = convertToDto(optionalSolicitudMantenimiento.get());
            return solicitudMantenimientoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public Boolean eliminar(SolicitudMantenimientoDto solicitudMantenimientoDto) {
        try {
            SolicitudMantenimiento solicitudMantenimiento = converToEntity(solicitudMantenimientoDto);

            //actualizar estado equipo
            Equipo equipo = solicitudMantenimiento.getEquipo();
            equipo.setEstado("Activo");
            equipoRepository.save(equipo);

            solicitudMantenimientoRepository.delete(solicitudMantenimiento);
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

    private SolicitudMantenimientoDto convertToDto(SolicitudMantenimiento solicitudMantenimiento) {
        SolicitudMantenimientoDto solicitudMantenimientoDto = modelMapper.map(solicitudMantenimiento, SolicitudMantenimientoDto.class);
        solicitudMantenimientoDto.setId_user(solicitudMantenimiento.getUser().getId());
        solicitudMantenimientoDto.setId_equipo(solicitudMantenimiento.getEquipo().getId());
        solicitudMantenimientoDto.setEquipoDto(equipoService.equipo(solicitudMantenimiento.getEquipo().getId()));
        solicitudMantenimientoDto.setUserDto(userService.user(solicitudMantenimiento.getUser().getId()));
        return solicitudMantenimientoDto;
    }

    private SolicitudMantenimiento converToEntity(SolicitudMantenimientoDto solicitudMantenimientoDto) {
        SolicitudMantenimiento solicitudMantenimiento = modelMapper.map(solicitudMantenimientoDto, SolicitudMantenimiento.class);
        Optional<User> optionalUser = userRepository.findById(solicitudMantenimientoDto.getId_user());
        if(optionalUser.isPresent()){
            solicitudMantenimiento.setUser(optionalUser.get());
        }
        Optional<Equipo> optionalEquipo = equipoRepository.findById(solicitudMantenimientoDto.getId_equipo());
        if(optionalEquipo.isPresent()){
            solicitudMantenimiento.setEquipo(optionalEquipo.get());
        }
        return solicitudMantenimiento;
    }
}

