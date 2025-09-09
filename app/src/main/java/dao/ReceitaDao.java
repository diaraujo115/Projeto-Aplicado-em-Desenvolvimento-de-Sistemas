package dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Receita;

@Dao
public interface ReceitaDao {

    @Insert
    void insert(Receita receita); // Pode retornar long (o ID inserido) ou List<Long> se inserir vários

    @Update
    void update(Receita receita);

    @Delete
    void delete(Receita receita);

    @Query("DELETE FROM receitas")
    void deleteAllReceitas();

    @Query("SELECT * FROM receitas WHERE nome = :nome LIMIT 1")
    Receita getReceitaByNome(String nome);

    @Query("SELECT * FROM receitas WHERE id = :id LIMIT 1")
    Receita getReceitaById(int id);

    @Query("SELECT * FROM receitas ORDER BY nome ASC")
    LiveData<List<Receita>> getAllReceitas(); // Usando LiveData para observar mudanças
}
