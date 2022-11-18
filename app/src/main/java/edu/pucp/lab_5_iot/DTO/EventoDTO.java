package edu.pucp.lab_5_iot.DTO;

import java.time.LocalDateTime;

public class EventoDTO {
    String titulo;
    String descripcion;
    LocalDateTime fechaYHora;
    String imgID;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(LocalDateTime fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgUrl) {
        this.imgID = imgUrl;
    }
}
