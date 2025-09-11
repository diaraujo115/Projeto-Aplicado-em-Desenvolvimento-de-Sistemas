package repository;


import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import dao.UsuarioDao;
import database.AppDatabase;
import model.Usuario;

public class IngredienteRepository {
    private UsuarioDao mUsuarioDao;
    private LiveData<List<Usuario>> mAllUsuarios;

    public IngredienteRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUsuarioDao = db.usuarioDao();
        mAllUsuarios = mUsuarioDao.getAllUsuarios(); // Exemplo de carregar dados observáveis
    }

    // Wrapper para buscar todos os usuários (observável)
    public LiveData<List<Usuario>> getAllUsuarios() {
        return mAllUsuarios;
    }

    // Wrapper para inserir. Deve ser chamado em uma thread de background.
    public void insert(Usuario usuario) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mUsuarioDao.insert(usuario);
        });
    }

    // Wrapper para buscar por email. Deve ser chamado em background ou usar LiveData/Flow.
    // Este exemplo é síncrono e SÓ DEVE ser chamado de uma background thread.
    public Usuario getUsuarioByEmail(String email) {
        // ATENÇÃO: Esta é uma chamada síncrona. Não chame da UI Thread!
        // Para chamadas da UI, use LiveData ou passe um Callback, ou use Kotlin Coroutines.
        return mUsuarioDao.getUsuarioByEmail(email);
    }

    // Adicione outros métodos conforme necessário (update, delete, etc.)
    // sempre executando operações de escrita ou leitura longa em background.
}