package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

import model.Especialidade;
import persistence.GenericDao;
import persistence.EspecialidadeDao;

@WebServlet("/especialidade")
public class EspecialidadeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public EspecialidadeServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("especialidade.jsp");
		rd.forward(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		String codigo = request.getParameter("codigo");
		String nome = request.getParameter("nome");
		
		String saida="";
		String erro="";
		Especialidade e = new Especialidade();
		List<Especialidade> especialidades = new ArrayList<>();
		
		if (!cmd.contains("Listar")) {
			e.setCodigo(Integer.parseInt(codigo));
		}
		if(cmd.contains("Cadastrar")|| cmd.contains("Alterar")) {
			e.setNome(nome);
		}
		try {
			if(cmd.contains("Cadastrar")) {
				cadastrarEspecialidade(e);
				saida ="Professor cadastrado com sucesso";
				e = null;
			}
			if(cmd.contains("Alterar")) {
				alterarEspecialidade(e);
				saida ="Professor alterado com sucesso";
				e = null;
			}
			if(cmd.contains("Excluir")) {
				excluirEspecialidade(e);
				saida ="Professor excluir com sucesso";
				e = null;
			}
			if(cmd.contains("Buscar")) {
				e = buscarProfessor(e);
			}
			if(cmd.contains("Listar")) {
				especialidades = listarEspecialidades();
			}
		}catch(SQLException | ClassNotFoundException er) {
			erro = er.getMessage();
		}finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("especialidade", e);
			request.setAttribute("especialidades", especialidades);
			
			RequestDispatcher rd = request.getRequestDispatcher("especialidade.jsp");
			rd.forward(request,response);
		}
	}

	private void cadastrarEspecialidade(Especialidade e)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		EspecialidadeDao eDao = new EspecialidadeDao(gDao);
		eDao.inserir(e);
		listarEspecialidades();
	}

	private void alterarEspecialidade(Especialidade e)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		EspecialidadeDao eDao = new EspecialidadeDao(gDao);
		eDao.atualizar(e);
		listarEspecialidades();	
	}

	private void excluirEspecialidade(Especialidade e)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		EspecialidadeDao eDao = new EspecialidadeDao(gDao);
		eDao.excluir(e);
		listarEspecialidades();	
	}

	private Especialidade buscarProfessor(Especialidade e)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		EspecialidadeDao eDao = new EspecialidadeDao(gDao);
		e = eDao.consultar(e);
		return e;
	}

	private List<Especialidade> listarEspecialidades()throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		EspecialidadeDao eDao = new EspecialidadeDao(gDao);
		List<Especialidade> especialidades = eDao.listar();
		return especialidades; 
	}

}
