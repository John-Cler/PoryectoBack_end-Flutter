package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.AsignacionMantenimiento;

import java.util.List;

@Repository
public interface AsignacionMantenimientoRepository extends JpaRepository<AsignacionMantenimiento, Integer> {
    @Query("SELECT am FROM AsignacionMantenimiento am WHERE am.solicitud_mantenimiento.id = :id_solicitud_mantenimiento ORDER BY am.id ASC")
    AsignacionMantenimiento getByIdSolicitudMantenimiento(@Param("id_solicitud_mantenimiento")Integer id_solicitud_mantenimiento);

    @Query("SELECT am FROM AsignacionMantenimiento am WHERE am.user.id = :id_user ORDER BY am.id ASC")
    List<AsignacionMantenimiento> getByIdUser(@Param("id_user")Integer id_user);
}
