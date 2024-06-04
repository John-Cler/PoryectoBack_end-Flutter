package ucb.edu.bo.Proyecto.services.implementation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.time.format.DateTimeFormatter;

@Service
public class FechaActualService {
    public Date getFechaAtual() {
        LocalDate currentDate = LocalDate.now();
        Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }
}
