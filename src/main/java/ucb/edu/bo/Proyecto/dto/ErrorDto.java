package ucb.edu.bo.Proyecto.dto;

public class ErrorDto {
    private String descripcion;


    public ErrorDto(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
