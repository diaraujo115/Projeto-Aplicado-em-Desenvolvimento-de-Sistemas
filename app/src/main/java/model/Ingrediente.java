package model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ingredientes")
public class Ingrediente {
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String nome;
    private String tipo;
    private String calorias;
    private String gorduraSaturada;
    private String gorduraTrans;
    private String sodio;
    private String colesterol;
    private String proteina;
    private String ferro;
    private String potassio;
    private String vitaminaD;
    private String fibraAlimentar;
    private String acucaresTotais;
    private String quantidade;

    public Ingrediente() {
    }

    public Ingrediente(Integer id, String nome, String tipo, String calorias, String gorduraSaturada, String gorduraTrans, String sodio, String colesterol, String proteina, String ferro, String potassio, String vitaminaD, String fibraAlimentar, String acucaresTotais, String quantidade) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.calorias = calorias;
        this.gorduraSaturada = gorduraSaturada;
        this.gorduraTrans = gorduraTrans;
        this.sodio = sodio;
        this.colesterol = colesterol;
        this.proteina = proteina;
        this.ferro = ferro;
        this.potassio = potassio;
        this.vitaminaD = vitaminaD;
        this.fibraAlimentar = fibraAlimentar;
        this.acucaresTotais = acucaresTotais;
        this.quantidade = quantidade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCalorias() {
        return calorias;
    }

    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    public String getGorduraSaturada() {
        return gorduraSaturada;
    }

    public void setGorduraSaturada(String gorduraSaturada) {
        this.gorduraSaturada = gorduraSaturada;
    }

    public String getGorduraTrans() {
        return gorduraTrans;
    }

    public void setGorduraTrans(String gorduraTrans) {
        this.gorduraTrans = gorduraTrans;
    }

    public String getSodio() {
        return sodio;
    }

    public void setSodio(String sodio) {
        this.sodio = sodio;
    }

    public String getColesterol() {
        return colesterol;
    }

    public void setColesterol(String colesterol) {
        this.colesterol = colesterol;
    }

    public String getProteina() {
        return proteina;
    }

    public void setProteina(String proteina) {
        this.proteina = proteina;
    }

    public String getFerro() {
        return ferro;
    }

    public void setFerro(String ferro) {
        this.ferro = ferro;
    }

    public String getPotassio() {
        return potassio;
    }

    public void setPotassio(String potassio) {
        this.potassio = potassio;
    }

    public String getVitaminaD() {
        return vitaminaD;
    }

    public void setVitaminaD(String vitaminaD) {
        this.vitaminaD = vitaminaD;
    }

    public String getFibraAlimentar() {
        return fibraAlimentar;
    }

    public void setFibraAlimentar(String fibraAlimentar) {
        this.fibraAlimentar = fibraAlimentar;
    }

    public String getAcucaresTotais() {
        return acucaresTotais;
    }

    public void setAcucaresTotais(String acucaresTotais) {
        this.acucaresTotais = acucaresTotais;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
