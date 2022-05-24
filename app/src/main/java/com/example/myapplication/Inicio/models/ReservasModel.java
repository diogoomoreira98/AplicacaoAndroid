package com.example.myapplication.Inicio.models;

public class ReservasModel {

    int idreserva;
    String  titulo;
    String data;
    String sala;
    String horaInicio;
    String horaFim;
    String centro;
    String idSala;

    public ReservasModel(int idreserva, String titulo, String data, String sala, String horaInicio, String horaFim, String centro) {
        this.idreserva = idreserva;
        this.titulo = titulo;
        this.data = data;
        this.sala = sala;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.centro = centro;
    }


    public int getidreserva() {
        return idreserva;
    }

    public void setidreserva(int idreserva) {
        this.idreserva = idreserva;
    }

    public String getIdSala() {
        return idSala;
    }

    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }



}
