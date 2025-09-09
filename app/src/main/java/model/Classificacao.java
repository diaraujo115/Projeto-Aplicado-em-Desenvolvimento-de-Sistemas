package model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "classificacoes",
        primaryKeys = {"usuario_id", "receita_id"},
        foreignKeys = {
                @ForeignKey(entity = Usuario.class,
                        parentColumns = "id",
                        childColumns = "usuario_id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Receita.class,
                        parentColumns = "id",
                        childColumns = "receita_id",
                        onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index("usuario_id"), @Index("receita_id")})
public class Classificacao {
    @ColumnInfo(name = "usuario_id")
    public int usuarioId;

    @ColumnInfo(name = "receita_id")
    public int receitaId;

    public Float nota;

    public Classificacao() {
    }

    public Classificacao(int usuarioId, int receitaId, Float nota) {
        this.usuarioId = usuarioId;
        this.receitaId = receitaId;
        this.nota = nota;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(int receitaId) {
        this.receitaId = receitaId;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }
}
