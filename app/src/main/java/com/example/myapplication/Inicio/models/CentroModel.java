package com.example.myapplication.Inicio.models;

public class CentroModel {

    int idcentro;
    String  nome;


    public CentroModel(int idcentro, String nome) {
        this.idcentro = idcentro;
        this.nome = nome;
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

    @Override
    public String toString() {
        return this.nome;
    }

}
