package br.com.mikael.mnagenda.ui.activity;

import static br.com.mikael.mnagenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.mikael.mnagenda.R;
import br.com.mikael.mnagenda.dao.AlunoDAO;
import br.com.mikael.mnagenda.model.Aluno;
import br.com.mikael.mnagenda.ui.adapter.ListaAlunosAdapter;

public class ListaAlunosActivity extends AppCompatActivity {
    public static final String TITULO_APPBAR = "Lista de alunos";
    private final AlunoDAO dao = new AlunoDAO();
    private ListaAlunosAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);
        setTitle(TITULO_APPBAR);
        configuraBotaoNovoAluno();
        configuraLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_lista_alunos_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.activity_lista_alunos_menu_remover) {
            confirmaRemocao(item);

        }
        return super.onContextItemSelected(item);
    }

    private void confirmaRemocao(@NonNull MenuItem item) {
        new AlertDialog
                .Builder(this)
                .setTitle("Removendo aluno")
                .setMessage("Tem certeza que deseja remover o aluno?")
                .setPositiveButton("SIM", (dialog, which) -> {
                    AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    assert menuInfo != null;
                    Aluno alunoEscolhido = adapter.getItem(menuInfo.position);
                    removeAluno(alunoEscolhido);
                })
                .setNegativeButton("NÃO", null)
                .show();
    }

    private void configuraBotaoNovoAluno() {
        FloatingActionButton botaoNovoAluno = findViewById(R.id.activity_main_novo_aluno);
        botaoNovoAluno.setOnClickListener(v -> abreFormNovoAluno());
    }

    private void abreFormNovoAluno() {
        startActivity(new Intent(this, FormularioAlunoActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaListaAlunos();
    }

    private void atualizaListaAlunos() {
        adapter.atualiza(dao.todos());
    }

    private void configuraLista() {
        ListView listaAlunos = findViewById(R.id.activity_main_lista_alunos);
        configuraAdapter(listaAlunos);
        configuraCliqueEmCadaItem(listaAlunos);
        registerForContextMenu(listaAlunos);
    }

    private void removeAluno(Aluno alunoEscolhido) {
        dao.remove(alunoEscolhido);
        adapter.remove(alunoEscolhido);
    }

    private void configuraCliqueEmCadaItem(ListView listaAlunos) {
        listaAlunos.setOnItemClickListener((adapterView, view, position, id) -> {
            Aluno alunoEscolhido = (Aluno) adapterView.getItemAtPosition(position);
            abreFormEditaAluno(alunoEscolhido);
        });
    }

    private void abreFormEditaAluno(Aluno alunoEscolhido) {
        Intent toFormulario = new Intent(ListaAlunosActivity.this, FormularioAlunoActivity.class);
        toFormulario.putExtra(CHAVE_ALUNO, alunoEscolhido);
        startActivity(toFormulario);
    }

    private void configuraAdapter(ListView listaAlunos) {
        adapter = new ListaAlunosAdapter(this);
        listaAlunos.setAdapter(adapter);
    }
}
