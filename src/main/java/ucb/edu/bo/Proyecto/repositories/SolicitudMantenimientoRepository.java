package ucb.edu.bo.Proyecto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ucb.edu.bo.Proyecto.entity.SolicitudMantenimiento;

import java.util.List;

@Repository
public interface SolicitudMantenimientoRepository extends JpaRepository<SolicitudMantenimiento, Integer> {

    @Query("SELECT sm FROM SolicitudMantenimiento sm WHERE sm.equipo.id = :id_equipo ORDER BY sm.id ASC")
    List<SolicitudMantenimiento> getByIdEquipo(@Param("id_equipo")Integer id_equipo);

    @Query("SELECT sm FROM SolicitudMantenimiento sm WHERE sm.user.id = :id_user ORDER BY sm.id ASC")
    List<SolicitudMantenimiento> getByIdUser(@Param("id_user")Integer id_user);
}
