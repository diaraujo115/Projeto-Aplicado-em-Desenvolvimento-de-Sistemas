package model;

public class Classificacao {
    private Integer id;
    private Float nota;
    private Receita receita;
    private Usuario usuario;

    public Classificacao(Integer id, Float nota, Receita receita, Usuario usuario) {
        this.id = id;
        this.nota = nota;
        this.receita = receita;
        this.usuario = usuario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
