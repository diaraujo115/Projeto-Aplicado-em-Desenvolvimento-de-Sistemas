package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

// Se estiver usando ViewBinding:
// import com.example.receitadedespensa.databinding.ActivityCadastroBinding;

public class CadastroActivity extends AppCompatActivity {

    // Se estiver usando ViewBinding:
    // private ActivityCadastroBinding binding;

    // Se NÃO estiver usando ViewBinding (usando findViewById):
    private TextInputLayout textFieldNomeLayout;
    private TextInputLayout textFieldEmailLayout;
    private TextInputLayout textFieldSenhaLayout;
    private TextInputLayout textFieldConfirmarSenhaLayout;
    private TextInputEditText editTextNome;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextSenha;
    private TextInputEditText editTextConfirmarSenha;
    private Button buttonCadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se estiver usando ViewBinding:
        // binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());
        //
        // binding.buttonCadastrar.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         String nome = binding.textFieldNome.getEditText().getText().toString();
        //         String email = binding.textFieldEmail.getEditText().getText().toString();
        //         String senha = binding.textFieldSenha.getEditText().getText().toString();
        //         String confirmarSenha = binding.textFieldConfirmarSenha.getEditText().getText().toString();
        //
        //         if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty() && !confirmarSenha.isEmpty()) {
        //             if (senha.equals(confirmarSenha)) {
        //                 // Simulação de cadastro bem-sucedido
        //                 Toast.makeText(CadastroActivity.this, "Cadastro de " + nome + " realizado com sucesso!", Toast.LENGTH_SHORT).show();
        //                 // Aqui você pode adicionar a lógica para salvar os dados do usuário
        //                 // (ex: em um banco de dados local ou em um servidor)
        //                 // e navegar para outra tela (ex: tela de login ou tela principal).
        //                 // Exemplo:
        //                 // Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        //                 // startActivity(intent);
        //                 // finish(); // Fecha a activity de cadastro para não voltar com o botão "back"
        //             } else {
        //                 Toast.makeText(CadastroActivity.this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
        //             }
        //         } else {
        //             Toast.makeText(CadastroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
        //         }
        //     }
        // });


        // Se NÃO estiver usando ViewBinding (usando findViewById):
        setContentView(R.layout.activity_cadastro); // Certifique-se que o nome do layout está correto

        textFieldNomeLayout = findViewById(R.id.textFieldNome);
        textFieldEmailLayout = findViewById(R.id.textFieldEmail);
        textFieldSenhaLayout = findViewById(R.id.textFieldSenha);
        textFieldConfirmarSenhaLayout = findViewById(R.id.textFieldConfirmarSenha);

        // É importante pegar o EditText de dentro do TextInputLayout
        editTextNome = (TextInputEditText) textFieldNomeLayout.getEditText();
        editTextEmail = (TextInputEditText) textFieldEmailLayout.getEditText();
        editTextSenha = (TextInputEditText) textFieldSenhaLayout.getEditText();
        editTextConfirmarSenha = (TextInputEditText) textFieldConfirmarSenhaLayout.getEditText();

        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // É uma boa prática verificar se os EditTexts não são nulos antes de pegar o texto
                if (editTextNome == null || editTextEmail == null || editTextSenha == null || editTextConfirmarSenha == null) {
                    Toast.makeText(CadastroActivity.this, "Erro ao carregar campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nome = editTextNome.getText().toString();
                String email = editTextEmail.getText().toString();
                String senha = editTextSenha.getText().toString();
                String confirmarSenha = editTextConfirmarSenha.getText().toString();

                if (!nome.isEmpty() && !email.isEmpty() && !senha.isEmpty() && !confirmarSenha.isEmpty()) {
                    if (senha.equals(confirmarSenha)) {
                        // Simulação de cadastro bem-sucedido
                        Toast.makeText(CadastroActivity.this, "Cadastro de " + nome + " realizado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Aqui você pode adicionar a lógica para salvar os dados do usuário
                        // (ex: em um banco de dados local ou em um servidor)
                        // e navegar para outra tela (ex: tela de login ou tela principal).
                        // Exemplo:
                        // Intent intent = new Intent(CadastroActivity.this, LoginActivity.class); // Crie LoginActivity.java
                        // startActivity(intent);
                        // finish(); // Fecha a activity de cadastro para não voltar com o botão "back"
                    } else {
                        Toast.makeText(CadastroActivity.this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
