package com.example.karori.menuFragment;

public class AlimentoSpecifico {
    private String nome;
    private String id;
    private String calorie;
    private String quantità;
    String tipo;
    private String proteine;
    private String carboidrati;

    private String grassi;

    public AlimentoSpecifico(String nome, String id, String calorie, String quantità, String proteine, String tipo, String grassi, String carboidrati) {
        this.nome = nome;
        this.id = id;
        //prima l'aveva salvato come carboidrati il tipo
        this.tipo = tipo;
        this.calorie = calorie;
        this.quantità = quantità;
        this.proteine = proteine;
        this.grassi=grassi;
        this.carboidrati=carboidrati;
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
    public String getCarboidrati() {
        return carboidrati;
    }

    public void setCarboidrati(String carboidrati) {
        this.carboidrati = carboidrati;
    }

    public String getGrassi() {
        return grassi;
    }

    public void setGrassi(String grassi) {
        this.grassi = grassi;
    }
}
