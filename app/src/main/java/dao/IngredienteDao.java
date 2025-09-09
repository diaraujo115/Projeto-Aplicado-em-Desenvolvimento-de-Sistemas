package dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import model.Ingrediente;
import model.Usuario;

@Dao
public interface IngredienteDao {

    @Insert
    void insert(Ingrediente ingrediente); // Pode retornar long (o ID inserido) ou List<Long> se inserir vários

    @Update
    void update(Ingrediente ingrediente);

    @Delete
    void delete(Ingrediente ingrediente);

    @Query("DELETE FROM ingredientes")
    void deleteAllIngredientes();

    @Query("SELECT * FROM ingredientes WHERE tipo = :tipo LIMIT 1")
    Ingrediente getIngredienteByTipo(String tipo);

    @Query("SELECT * FROM ingredientes WHERE id = :id LIMIT 1")
    Ingrediente getIngredienteById(int id);

    @Query("SELECT * FROM ingredientes ORDER BY nome ASC")
    LiveData<List<Ingrediente>> getAllIngredientes(); // Usando LiveData para observar mudanças
}
