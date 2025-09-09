package model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "comentarios",
        foreignKeys = {
                // Chave estrangeira para a tabela 'receitas'
                @ForeignKey(entity = Receita.class,
                        parentColumns = "id",         // Coluna 'id' na tabela 'receitas'
                        childColumns = "receita_id",  // Coluna 'receita_id' nesta tabela 'comentarios'
                        onDelete = ForeignKey.CASCADE,  // Se uma receita for deletada, seus comentários também serão.
                        // Outras opções: SET_NULL, RESTRICT, NO_ACTION, SET_DEFAULT
                        onUpdate = ForeignKey.CASCADE), // Se o id da receita for atualizado (raro para PK auto-gerada), atualize aqui também.

                // Chave estrangeira para a tabela 'usuarios' (assumindo que você tem uma entidade Usuario)
                @ForeignKey(entity = Usuario.class,      // Sua classe de entidade Usuario
                        parentColumns = "id",         // Coluna 'id' na tabela 'usuarios'
                        childColumns = "usuario_id",  // Coluna 'usuario_id' nesta tabela 'comentarios'
                        onDelete = ForeignKey.SET_NULL, // Se um usuário for deletado, o comentário permanece, mas sem autor (usuario_id fica NULL).
                        // Se você quiser que o comentário seja deletado, use CASCADE.
                        onUpdate = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = {"receita_id"}),
                @Index(value = {"usuario_id"})
        }
)
public class Comentario {

    @PrimaryKey(autoGenerate = true)
    private Integer id; // Chave primária única para cada comentário
    @ColumnInfo(name = "texto_comentario")
    private String texto;

    @ColumnInfo(name = "data_publicacao")
    private LocalDateTime dataPublicacao;

    @ColumnInfo(name = "receita_id") // Esta coluna armazenará o ID da receita à qual o comentário pertence
    private Integer receitaId;

    @ColumnInfo(name = "usuario_id") // Esta coluna armazenará o ID do usuário que fez o comentário
    private Integer usuarioId;     // Pode ser nulo se onDelete = ForeignKey.SET_NULL para o usuário

    public Comentario() {
    }

    public Comentario(Integer id, String texto, LocalDateTime dataPublicacao, Integer receitaId, Integer usuarioId) {
        this.id = id;
        this.texto = texto;
        this.dataPublicacao = dataPublicacao;
        this.receitaId = receitaId;
        this.usuarioId = usuarioId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public Integer getReceitaId() {
        return receitaId;
    }

    public void setReceitaId(Integer receitaId) {
        this.receitaId = receitaId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }
}
