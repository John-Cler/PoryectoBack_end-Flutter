package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.IUserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpUserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "apellidos");
        List<User> users = userRepository.findAll(sort);
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    /* *********** *
     * Retorna una lista de usuarios que no tienen asignado un tipo
     * ********** */
    public List<UserDto> listadoSinTipo() {
        List<User> users = userRepository.listadoSinTipos();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    /* *********** *
     * Retorna una lista de usuarios que no tienen asignado un tipo
     * o que son Administrativo
     * ********** */
    public List<UserDto> listadoParaAdministrativos() {
        List<User> users = userRepository.listadoRegistroAdministrativo();
        return users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto guardar(UserDto userDto) {
        User user = converToEntity(userDto);
        return convertToDto(userRepository.save(user));
    }

    @Override
    public UserDto asignarTipo(Integer id,String tipo){
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setTipo(tipo);
            return convertToDto(userRepository.save(user));

        }
        return null;
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return convertToDto(user);
        }
        return null;
    }

    @Override
    public UserDto user(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            UserDto userDto = convertToDto(optionalUser.get());
            return userDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional
    public Boolean eliminar(UserDto userDto) {
        try {
            User user = converToEntity(userDto);
            userRepository.delete(user);
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

    private UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    private User converToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }
}
