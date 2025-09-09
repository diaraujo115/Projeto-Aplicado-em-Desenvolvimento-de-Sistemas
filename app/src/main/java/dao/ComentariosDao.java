package dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Comentario;
import model.Receita;

@Dao
public interface ComentariosDao {

    @Insert
    void insert(Comentario comentario); // Pode retornar long (o ID inserido) ou List<Long> se inserir vários

    @Update
    void update(Comentario comentario);

    @Delete
    void delete(Comentario comentario);

    @Query("DELETE FROM comentarios")
    void deleteAllComentarios();

    @Query("SELECT * FROM comentarios WHERE receita_id = :receita_id LIMIT 1")
    Receita getComentarioByReceitaId(int receita_id);

    @Query("SELECT * FROM comentarios WHERE usuario_id = :usuarioId and receita_id = :receitaId LIMIT 1")
    Comentario getComentarioById(int usuarioId, int receitaId);

    @Query("SELECT * FROM comentarios ORDER BY data_publicacao ASC")
    LiveData<List<Comentario>> getAllComentarios(); // Usando LiveData para observar mudanças
}
