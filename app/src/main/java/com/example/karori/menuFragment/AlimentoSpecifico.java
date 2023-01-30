package com.example.karori.menuFragment;

public class AlimentoSpecifico {
    private String nome;
    private String calorie;
    private String quantità;
    private String proteine;

    public AlimentoSpecifico(String nome, String calorie, String quantità, String proteine) {
        this.nome = nome;
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


}
