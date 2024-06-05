package ucb.edu.bo.Proyecto.services.implementation;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ucb.edu.bo.Proyecto.dto.BloqueDto;
import ucb.edu.bo.Proyecto.entity.Bloque;
import ucb.edu.bo.Proyecto.repositories.BloqueRepository;
import ucb.edu.bo.Proyecto.services.IBloqueService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImpBloqueService implements IBloqueService {
    @Autowired
    private BloqueRepository bloqueRepository;

    @Override
    public List<BloqueDto> listado() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nombre");
        List<Bloque> bloques = bloqueRepository.findAll(sort);
        return bloques.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public BloqueDto guardar(BloqueDto bloqueDto) {
        Bloque bloque = converToEntity(bloqueDto);
        return convertToDto(bloqueRepository.save(bloque));
    }

    @Override
    public BloqueDto bloque(Integer id) {
        Optional<Bloque> optionalBloque = bloqueRepository.findById(id);
        if (optionalBloque.isPresent()) {
            BloqueDto bloqueDto = convertToDto(optionalBloque.get());
            return bloqueDto;
        }
        //throw new RegisterNotFoundException("Registro no encontrado");
        return null;
    }

    @Override
    @Transactional
    public Boolean eliminar(BloqueDto bloqueDto) {
        try {
            Bloque bloque = converToEntity(bloqueDto);
            bloqueRepository.delete(bloque);
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

    private BloqueDto convertToDto(Bloque bloque) {
        BloqueDto bloqueDto = modelMapper.map(bloque, BloqueDto.class);
        return bloqueDto;
    }

    private Bloque converToEntity(BloqueDto bloqueDto) {
        Bloque bloque = modelMapper.map(bloqueDto, Bloque.class);
        return bloque;
    }
}
