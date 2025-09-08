package model;

public class Comentario {
    private Integer id;
    private String text;
    private String like;
    private String nolike;

    private Usuario usuario;
    private Receita receita;

    public Comentario(Integer id, String text, String like, String nolike, Usuario usuario, Receita receita) {
        this.id = id;
        this.text = text;
        this.like = like;
        this.nolike = nolike;
        this.usuario = usuario;
        this.receita = receita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getNolike() {
        return nolike;
    }

    public void setNolike(String nolike) {
        this.nolike = nolike;
    }

    public Usuario getUser() {
        return usuario;
    }

    public void setUser(Usuario usuario) {
        this.usuario = usuario;
    }

    public Receita getReceita() {
        return receita;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    @Override
    public String toString() {
        return "Comentario{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", like='" + like + '\'' +
                ", nolike='" + nolike + '\'' +
                ", user=" + usuario +
                ", receita=" + receita +
                '}';
    }
}
