package ucb.edu.bo.Proyecto.dto;

import ucb.edu.bo.Proyecto.entity.AsignacionEquipo;
import ucb.edu.bo.Proyecto.entity.Bloque;
import ucb.edu.bo.Proyecto.entity.Departamento;
import ucb.edu.bo.Proyecto.entity.EquipoParte;
//import jakarta.persistence.*;

import java.util.List;

public class EquipoDto {
    private Integer id;
    private String codigo;
    private String tipo;
    private Integer id_departamento;
    private DepartamentoDto departamentoDto;
    private Integer id_bloque;
    private BloqueDto bloqueDto;
    private String descripcion;
    private String nro_activo;
    private String nro_serie;
    private String marca;
    private String modelo;
    private String estado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getId_departamento() {
        return id_departamento;
    }

    public void setId_departamento(Integer id_departamento) {
        this.id_departamento = id_departamento;
    }

    public DepartamentoDto getDepartamentoDto() {
        return departamentoDto;
    }

    public void setDepartamentoDto(DepartamentoDto departamentoDto) {
        this.departamentoDto = departamentoDto;
    }

    public Integer getId_bloque() {
        return id_bloque;
    }

    public void setId_bloque(Integer id_bloque) {
        this.id_bloque = id_bloque;
    }

    public BloqueDto getBloqueDto() {
        return bloqueDto;
    }

    public void setBloqueDto(BloqueDto bloqueDto) {
        this.bloqueDto = bloqueDto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNro_activo() {
        return nro_activo;
    }

    public void setNro_activo(String nro_activo) {
        this.nro_activo = nro_activo;
    }

    public String getNro_serie() {
        return nro_serie;
    }

    public void setNro_serie(String nro_serie) {
        this.nro_serie = nro_serie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
