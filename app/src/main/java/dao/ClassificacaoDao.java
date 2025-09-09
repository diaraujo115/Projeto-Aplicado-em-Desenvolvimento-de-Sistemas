package dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Classificacao;
import model.Comentario;
import model.Receita;

@Dao
public interface ClassificacaoDao {

    @Insert
    void insert(Classificacao classificacao); // Pode retornar long (o ID inserido) ou List<Long> se inserir vários

    @Update
    void update(Classificacao classificacao);

    @Delete
    void delete(Classificacao classificacao);

    @Query("DELETE FROM classificacoes")
    void deleteAllClassificacao();

    @Query("SELECT * FROM classificacoes WHERE receita_id = :receita_id LIMIT 1")
    Classificacao getClassificacaoByReceitaId(int receita_id);

    @Query("SELECT * FROM classificacoes WHERE usuario_id = :usuarioId and receita_id = :receitaId LIMIT 1")
    Classificacao getClassificacaoById(int usuarioId, int receitaId);

    @Query("SELECT * FROM classificacoes ORDER BY receita_id ASC")
    LiveData<List<Classificacao>> getAllClassificacoes(); // Usando LiveData para observar mudanças
}
