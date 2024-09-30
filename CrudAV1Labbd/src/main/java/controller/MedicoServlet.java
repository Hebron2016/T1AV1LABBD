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
import java.time.LocalDate;

import model.Medico;
import persistence.GenericDao;
import persistence.MedicoDao;
import persistence.ClienteDao;

@WebServlet("/medico")
public class MedicoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public MedicoServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("medico.jsp");
		rd.forward(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		String codigo = request.getParameter("codigo");
		String nome = request.getParameter("nome");
		String telefone = request.getParameter("telefone");

		
		String saida="";
		String erro="";
		Medico m = new Medico("","","");
		List<Medico> medicos = new ArrayList<>();
		
		if (!cmd.contains("Listar")) {
			m.setRg(codigo);
		}
		if(cmd.contains("Cadastrar")|| cmd.contains("Alterar")) {
			m.setNome(nome);
			m.setTelefone((telefone));
		}
		try {
			if(cmd.contains("Cadastrar")) {
				cadastrarMedico(m);
				saida ="Medico cadastrado com sucesso";
				m = null;
			}
			if(cmd.contains("Alterar")) {
				alterarMedico(m);
				saida ="Medico alterado com sucesso";
				m = null;
			}
			if(cmd.contains("Excluir")) {
				excluirMedico(m);
				saida ="Medico excluir com sucesso";
				m = null;
			}
			if(cmd.contains("Buscar")) {
				m = buscarMedico(m);
			}
			if(cmd.contains("Listar")) {
				medicos = listarMedico();
			}
		}catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		}finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("Medico", m);
			request.setAttribute("Medicos", medicos);
			
			RequestDispatcher rd = request.getRequestDispatcher("Medico.jsp");
			rd.forward(request,response);
		}
	}

	private void cadastrarMedico(Medico m)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		MedicoDao mDao = new MedicoDao(gDao);
		mDao.inserir(m);
		listarMedico();
	}

	private void alterarMedico(Medico m)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		MedicoDao mDao = new MedicoDao(gDao);
		mDao.atualizar(m);
		listarMedico();
	}

	private void excluirMedico(Medico m)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		MedicoDao mDao = new MedicoDao(gDao);
		mDao.excluir(m);
		listarMedico();
	}

	private Medico buscarMedico(Medico m)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		MedicoDao mDao = new MedicoDao(gDao);
		m = mDao.consultar(m);
		return m;
	}

	private List<Medico> listarMedico()throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		MedicoDao mDao = new MedicoDao(gDao);
		List<Medico> medicos = mDao.listar();
		return medicos; 
	}

}
