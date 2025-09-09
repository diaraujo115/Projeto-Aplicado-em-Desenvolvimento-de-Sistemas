package dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.lifecycle.LiveData;
import java.util.List;

import model.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario); // Pode retornar long (o ID inserido) ou List<Long> se inserir vários

    @Update
    void update(Usuario usuario);

    @Delete
    void delete(Usuario usuario);

    @Query("DELETE FROM usuarios")
    void deleteAllUsuarios();

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    Usuario getUsuarioByEmail(String email);

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    Usuario getUsuarioById(int id);

    @Query("SELECT * FROM usuarios ORDER BY nome_completo ASC")
    LiveData<List<Usuario>> getAllUsuarios(); // Usando LiveData para observar mudanças
}
