package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.EquipoParte;

import java.util.List;

@Repository
public interface EquipoParteRepository extends JpaRepository<EquipoParte,Integer> {
    @Query("SELECT ep FROM EquipoParte ep WHERE ep.equipo.id = :id_equipo ORDER BY id ASC")
    List<EquipoParte> getByIdEquipo(@Param("id_equipo") Integer id_equipo);
}
