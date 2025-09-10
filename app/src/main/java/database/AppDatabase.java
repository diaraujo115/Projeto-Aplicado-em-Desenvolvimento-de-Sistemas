package database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import dao.ComentariosDao;
import dao.IngredienteDao;
import dao.UsuarioDao;
import dao.ReceitaDao;
import dao.ClassificacaoDao;
import model.Comentario;
import model.Ingrediente;
import model.Usuario;
import model.Receita;
import model.Classificacao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Usuario.class, Receita.class, Classificacao.class, Comentario.class, Ingrediente.class}, version = 1, exportSchema = false)
// Incremente a 'version' quando você alterar o esquema do banco de dados (e forneça uma Migração)
// exportSchema = false é bom para projetos acadêmicos para evitar um warning de build. Para produção, considere exportar.
public abstract class AppDatabase extends RoomDatabase {

    public abstract UsuarioDao usuarioDao();
    public abstract ReceitaDao receitaDao(); // Adicione se existir
    public abstract ClassificacaoDao classificacaoDao(); // Adicione se existir
    public abstract ComentariosDao comentariosDao();
    public abstract IngredienteDao ingredienteDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "receita_despensa_db")
                            // .fallbackToDestructiveMigration() // Para projetos acadêmicos, se não quiser criar migrações
                            // .allowMainThreadQueries() // EVITE ISSO em produção! Apenas para testes rápidos e simples.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
