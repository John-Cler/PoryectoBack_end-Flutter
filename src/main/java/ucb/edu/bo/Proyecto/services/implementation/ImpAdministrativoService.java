package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.AdministrativoDto;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.entity.Administrativo;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.AdministrativoRepository;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.IAdministrativo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpAdministrativoService implements IAdministrativo {


    Logger logger = LoggerFactory.getLogger(ImpAdministrativoService.class);
    @Autowired
    private AdministrativoRepository administrativoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ImpUserService userService;
    @Autowired
    private ImpDepartamentoService departamentoService;
    @Autowired
    private ImpBloqueService bloqueService;

    @Override
    public List<AdministrativoDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Administrativo> administrativos = administrativoRepository.findAll(sort);
        return administrativos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AdministrativoDto guardar(AdministrativoDto administrativoDto) {
        Optional<User> optionalUser = userRepository.findById(administrativoDto.getId_user());
        if (optionalUser.isPresent()) {
            Optional<Administrativo> optionalAdministrativo = null;
            if (administrativoDto.getId() != null) {
                optionalAdministrativo = administrativoRepository.findById(administrativoDto.getId());
            }
            logger.info("Registrando...");
            User oldUser = null;
            Administrativo oldAdministrativo = null;
            if (optionalAdministrativo != null && optionalAdministrativo.isPresent()) {
                oldAdministrativo = optionalAdministrativo.get();
                oldUser = oldAdministrativo.getUser();
            }
            // si es para actualización encontrara el registro y
            // se debe desvincular la relacion con el usuario
            // esto no afecta la agregación de nuevos registros
            if (oldUser != null && oldUser.getId() != 0) {
                // oldUser.setAdministrativo(null); // elimina el adminitrativo
                oldUser.setTipo(null);
                userRepository.save(oldUser);
            }

            Administrativo administrativo = converToEntity(administrativoDto);
            // realizar la relacion entre usuario administrativo
            Administrativo saveAdministrativo = administrativoRepository.save(administrativo);
            User user = optionalUser.get();
            user.setAdministrativo(saveAdministrativo);
            user.setTipo("Administrativo");
            userRepository.save(user);
            return convertToDto(saveAdministrativo);
        }
        return null;
    }

    @Override
    public AdministrativoDto administrativo(Integer id) {
        Optional<Administrativo> optionalAdministrativo = administrativoRepository.findById(id);
        if (optionalAdministrativo.isPresent()) {
            AdministrativoDto administrativoDto = convertToDto(optionalAdministrativo.get());
            return administrativoDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional()
    public Boolean eliminar(AdministrativoDto administrativoDto) {
        try {
            Administrativo administrativo = converToEntity(administrativoDto);
            User user = administrativo.getUser();
            user.setTipo(null);
            user.setAdministrativo(null);
            userRepository.save(user);
            administrativoRepository.deleteById(administrativo.getId());
            Optional<Administrativo> optionalAdministrativo = administrativoRepository.findById(administrativo.getId());
            return !optionalAdministrativo.isPresent();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* ********** *
     *    MAPEO
     *********** */
    @Autowired
    private ModelMapper modelMapper;

    private AdministrativoDto convertToDto(Administrativo administrativo) {
        AdministrativoDto administrativoDto = modelMapper.map(administrativo, AdministrativoDto.class);

        DepartamentoDto departamentoDto = departamentoService.departamento(administrativo.getDepartamento().getId());
        BloqueDto bloqueDto = bloqueService.bloque(administrativo.getBloque().getId());

        administrativoDto.setId_user(administrativo.getUser().getId());
        administrativoDto.setDepartamentoDto(departamentoDto);
        administrativoDto.setId_departamento(departamentoDto.getId());
        administrativoDto.setBloqueDto(bloqueDto);
        administrativoDto.setId_bloque(bloqueDto.getId());
        return administrativoDto;
    }

    private Administrativo converToEntity(AdministrativoDto administrativoDto) {
        Administrativo administrativo = modelMapper.map(administrativoDto, Administrativo.class);
        UserDto userDto = userService.user(administrativoDto.getId_user());
        User user = modelMapper.map(userDto, User.class);
        user.setAdministrativo(administrativo);
        user.setTipo("Administrativo");
        administrativo.setUser(user);
        return administrativo;
    }
}
