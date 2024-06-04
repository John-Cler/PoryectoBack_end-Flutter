package ucb.edu.bo.Proyecto.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.AsignacionEquipo;

import java.util.List;

@Repository
public interface AsignacionEquipoRepository extends JpaRepository<AsignacionEquipo, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM AsignacionEquipo ae WHERE ae.id = :id")
    void eliminaAsigacionById(@Param("id") Integer id);
    @Query("SELECT ae FROM AsignacionEquipo ae WHERE ae.equipo.id = :id_equipo")
    AsignacionEquipo getByIdEquipo(@Param("id_equipo") Integer id_equipo);

    @Query("SELECT ae FROM AsignacionEquipo ae WHERE ae.administrativo.id = :id_administrativo ORDER BY id ASC")
    List<AsignacionEquipo> getByIdAdministrativo(@Param("id_administrativo") Integer id_administrativo);
}
