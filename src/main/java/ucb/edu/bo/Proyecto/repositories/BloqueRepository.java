package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.Bloque;

@Repository
public interface BloqueRepository extends JpaRepository<Bloque,Integer> {
}
