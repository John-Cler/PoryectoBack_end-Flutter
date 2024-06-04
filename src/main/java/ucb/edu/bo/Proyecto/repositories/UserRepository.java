package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(@Param(("email")) String email);

    @Query("SELECT u FROM User u LEFT JOIN u.administrativo a WHERE u.tipo = 'Administrativo' OR u.tipo IS NULL OR u.tipo = '' ORDER BY u.id DESC")
    List<User> listadoRegistroAdministrativo();

    @Query("SELECT u FROM User u WHERE u.tipo IS NULL or u.tipo='' ORDER BY id desc")
    List<User> listadoSinTipos();
}
