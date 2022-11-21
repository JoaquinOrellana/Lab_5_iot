package edu.pucp.lab_5_iot.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class EventoDTO {
    String titulo;
    String descripcion;
    LocalDateTime fechaYHoraInicio;;
    LocalDateTime fechaYHoraFin;
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

    public LocalDateTime getFechaYHoraInicio() {return fechaYHoraInicio;}

    public void setFechaYHoraInicio(LocalDateTime fechaYHoraInicio) {this.fechaYHoraInicio = fechaYHoraInicio;}

    public LocalDateTime getFechaYHoraFin() {return fechaYHoraFin;}

    public void setFechaYHoraFin(LocalDateTime fechaYHoraFin) {this.fechaYHoraFin = fechaYHoraFin;}

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgUrl) {
        this.imgID = imgUrl;
    }
}
