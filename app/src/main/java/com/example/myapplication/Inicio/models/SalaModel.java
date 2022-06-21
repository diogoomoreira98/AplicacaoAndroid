package com.example.myapplication.Inicio.models;

public class SalaModel {

        int idsala;
        String nome;
        String duracaolimpeza;
        String nmaxutentes;
        String alocacaomaxima;

        public SalaModel (int idsala, String nome, String duracaolimpeza, String nmaxutentes, String alocacaomaxima) {

            this.idsala = idsala;
            this.nome = nome;
            this.duracaolimpeza = duracaolimpeza;
            this.nmaxutentes = nmaxutentes;
            this.alocacaomaxima = alocacaomaxima;
        }

        public int getIdsala() { return idsala; }
        public void setidsala(int idcentro) { this.idsala = idsala; }

        public String getNome() {
        return nome;
    }
        public void setNome(String nome) {
        this.nome = nome;
    }

        public String getDuracaolimpeza() {
        return duracaolimpeza;
    }
        public void setDuracaolimpeza(String duracaolimpeza) { this.duracaolimpeza = duracaolimpeza; }

        public String getNmaxutentes() {
        return nmaxutentes;
    }
        public void setNmaxutentes(String nmaxutentes) { this.nmaxutentes = nmaxutentes; }

        public String getAlocacaomaxima() { return alocacaomaxima; }
        public void setAlocacaomaxima(String alocacaomaxima) { this.alocacaomaxima = alocacaomaxima; }

}
