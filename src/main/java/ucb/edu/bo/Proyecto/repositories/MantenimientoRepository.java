package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.Mantenimiento;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Integer> {

    @Query("SELECT m FROM Mantenimiento m WHERE m.equipo.id = :id_equipo ORDER BY m.id ASC")
    List<Mantenimiento> getByIdEquipo(@Param("id_equipo")Integer id_equipo);
    @Query("SELECT m FROM Mantenimiento m WHERE m.solicitud_mantenimiento.id = :id_solicitud_mantenimiento ORDER BY m.id ASC")
    Mantenimiento getByIdSolicitudMantenimiento(@Param("id_solicitud_mantenimiento")Integer id_solicitud_mantenimiento);

    @Query("SELECT m FROM Mantenimiento m WHERE m.asignacion_mantenimiento.id = :id_asignacion_mantenimiento ORDER BY m.id ASC")
    Mantenimiento getByIdAsignacionMantenimiento(@Param("id_asignacion_mantenimiento")Integer id_asignacion_mantenimiento);
}
