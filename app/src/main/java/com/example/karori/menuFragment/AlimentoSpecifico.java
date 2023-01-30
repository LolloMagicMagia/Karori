package com.example.karori.menuFragment;

public class AlimentoSpecifico {
    private String nome;
    private String id;
    private String calorie;
    private String quantità;
    private String proteine;
    private String tipo;

    public AlimentoSpecifico(String nome, String id, String calorie, String quantità, String proteine, String tipo) {
        this.nome = nome;
        this.id = id;
        this.tipo = tipo;
        this.calorie = calorie;
        this.quantità = quantità;
        this.proteine = proteine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getQuantità() {
        return quantità;
    }

    public void setQuantità(String quantità) {
        this.quantità = quantità;
    }

    public String getProteine() {
        return proteine;
    }

    public void setProteine(String proteine) {
        this.proteine = proteine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
