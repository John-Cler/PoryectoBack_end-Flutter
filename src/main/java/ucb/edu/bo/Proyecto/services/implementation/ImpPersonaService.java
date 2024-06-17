package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.PersonaDto;
import ucb.edu.bo.Proyecto.dto.UserDto;
import ucb.edu.bo.Proyecto.dto.DepartamentoDto;
import ucb.edu.bo.Proyecto.entity.Persona;
import ucb.edu.bo.Proyecto.entity.User;
import ucb.edu.bo.Proyecto.repositories.PersonaRepository;
import ucb.edu.bo.Proyecto.repositories.UserRepository;
import ucb.edu.bo.Proyecto.services.IPersonaService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpPersonaService implements IPersonaService {
    Logger logger = LoggerFactory.getLogger(ImpPersonaService.class);
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ImpUserService userService;
    @Autowired
    private ImpDepartamentoService departamentoService;
    @Autowired
    private ImpBloqueService bloqueService;

    @Override
    public List<PersonaDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<Persona> personas = personaRepository.findAll(sort);
        return personas.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PersonaDto guardar(PersonaDto personaDto) {
        Persona persona = converToEntity(personaDto);
        Optional<User> optionalUser = userRepository.findById(personaDto.getUserDto().getId());
        if (optionalUser.isPresent()) {
            // realizar la relacion entre persona y user
            Persona savePersona = personaRepository.save(persona);
            User user = savePersona.getUser();
            user.setPersona(savePersona);
            userRepository.save(user);
            return convertToDto(savePersona);
        }
        return null;
    }

    @Override
    @Modifying
    @Transactional
    public PersonaDto actualizar(PersonaDto personaDto, Integer oldId) {

        // Desvincular el antiguo usuario
        Optional<Persona> optionalPersona = personaRepository.findById(oldId);
        if (optionalPersona.isEmpty()) {
            return null;
        }

        Persona oldPersona = optionalPersona.get();
        User oldUser = oldPersona.getUser();
        if (oldUser != null) {
            oldUser.setPersona(null);
            userRepository.save(oldUser);
        }

        //actualizar el registro
        Optional<Persona> optionalPersona1 = personaRepository.findById(personaDto.getId());
        if (optionalPersona1.isPresent()) {
            Persona persona = optionalPersona1.get();
            persona.setCodigo(personaDto.getCodigo());
            persona.setNombre(personaDto.getNombre());
            persona.setPaterno(personaDto.getPaterno());
            persona.setMaterno(personaDto.getMaterno());
            persona.setCorreo(personaDto.getCorreo());
            persona.setTelefono(personaDto.getTelefono());
            persona.setFecha_nacimiento(personaDto.getFecha_nacimiento());
            Persona savePersona = personaRepository.save(persona);
            Optional<User> optionalUser = userRepository.findById(personaDto.getId_user());
            if (optionalUser.isPresent()) {
                // realizar la relacion entre persona y user
                User userPersona = optionalUser.get();
                userRepository.asignaPersona(userPersona.getId(), savePersona);
                return convertToDto(savePersona, userPersona.getId());
            }
        }

        return null;
    }


    @Override
    public PersonaDto getByCodigo(String codigo) {
        Persona persona = personaRepository.findByCodigo(codigo);
        if (persona != null) {
            PersonaDto personaDto = convertToDto(persona);
            return personaDto;
        }
        return null;
    }

    @Override
    public PersonaDto persona(Integer id) {
        Optional<Persona> optionalPersona = personaRepository.findById(id);
        if (optionalPersona.isPresent()) {
            PersonaDto personaDto = convertToDto(optionalPersona.get());
            return personaDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional()
    public Boolean eliminar(PersonaDto personaDto) {
        try {
            Optional<Persona> optionalPersona = personaRepository.findById(personaDto.getId());
            if (optionalPersona.isPresent()) {
                Persona persona = optionalPersona.get();
                User user = persona.getUser();
                if (user != null) {
                    user.setPersona(null);
                    userRepository.save(user);
                }
                persona.setUser(null);
                personaRepository.delete(persona);
                Optional<Persona> optionalPersona1 = personaRepository.findById(persona.getId());
                return !optionalPersona1.isPresent();
            }
            return false;
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

    private PersonaDto convertToDto(Persona persona) {
        PersonaDto personaDto = modelMapper.map(persona, PersonaDto.class);
        if (persona.getUser() != null) {
            personaDto.setId_user(persona.getUser().getId());
            UserDto userDto = userService.user(persona.getUser().getId());
            personaDto.setUserDto(userDto);
        }
        return personaDto;
    }

    private PersonaDto convertToDto(Persona persona, Integer newIdUser) {
        PersonaDto personaDto = modelMapper.map(persona, PersonaDto.class);
        personaDto.setId_user(newIdUser);
        UserDto userDto = userService.user(newIdUser);
        personaDto.setUserDto(userDto);
        return personaDto;
    }

    private Persona converToEntity(PersonaDto personaDto) {
        Persona persona = modelMapper.map(personaDto, Persona.class);
        Optional<User> optionalUser = userRepository.findById(personaDto.getId_user());
        if (optionalUser.isPresent()) {
            persona.setUser(optionalUser.get());
        }
        return persona;
    }
}

