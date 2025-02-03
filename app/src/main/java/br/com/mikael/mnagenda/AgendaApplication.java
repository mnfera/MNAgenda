package br.com.mikael.mnagenda;

import android.app.Application;

import br.com.mikael.mnagenda.dao.AlunoDAO;
import br.com.mikael.mnagenda.model.Aluno;

public class AgendaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        criaAlunosTeste();
    }

    private static void criaAlunosTeste() {
        AlunoDAO dao = new AlunoDAO();
        dao.salva(new Aluno("Aluno 01", "99999999999", "01@email.com"));
        dao.salva(new Aluno("Aluno 02", "88888888888", "02@email.com"));
    }
}
