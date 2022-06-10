package com.example.myapplication.Inicio.models;

public class CentroModel {

    int idcentro;
    String  nome;
    String morada;

    public CentroModel(int idcentro, String nome, String morada) {
        this.idcentro = idcentro;
        this.nome = nome;
        this.morada = morada;
    }

    public int getidcentro() {
        return idcentro;
    }

    public void setidcentro(int idcentro) {
        this.idcentro = idcentro;
    }

    public String getnome() {
        return nome;
    }

    public void setnome(String nome) {
        this.nome = nome;
    }

    public String getmorada() {
        return morada;
    }

    public void setmorada(String nome) {
        this.morada = morada;
    }


}
