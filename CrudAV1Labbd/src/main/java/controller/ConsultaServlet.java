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
import java.sql.Timestamp;
import java.time.LocalDate;

import model.Cliente;
import model.Consulta;
import model.Medico;
import model.PlanoDeSaude;
import persistence.GenericDao;
import persistence.MedicoDao;
import persistence.ClienteDao;
import persistence.ConsultaDao;

@WebServlet("/medico")
public class ConsultaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public ConsultaServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("consulta.jsp");
		rd.forward(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		String data = request.getParameter("data");
		String hora = request.getParameter("hora");
		String rgcliente = request.getParameter("rgCliente");
		String rgmedico = request.getParameter("rgMedico");
		String pds = request.getParameter("pds");
		String tc = request.getParameter("tc");
		String vlrTotal = request.getParameter("vlrTotal");
		
		
		String saida="";
		String erro="";
		Consulta c = new Consulta();
		List<Medico> medicos = new ArrayList<>();
		
		if (!cmd.contains("Listar")) {
			LocalDate date = LocalDate.parse(data);
			Timestamp time = Timestamp.valueOf(hora);
			Cliente cliente = new Cliente("", "", "");
			c.setData(date);
			c.setHora(time);
			cliente.setRg(rgcliente);
			c.setCliente(cliente);
		}
		if(cmd.contains("Cadastrar")|| cmd.contains("Alterar")) {
			LocalDate date = LocalDate.parse(data);
			Timestamp time = Timestamp.valueOf(hora);
			Cliente cliente = new Cliente("", "", "");
			Medico med = new Medico("", "", "");
			PlanoDeSaude plano = new PlanoDeSaude();
			c.setData(date);
			c.setHora(time);
			cliente.setRg(rgcliente);
			c.setCliente(cliente);
			med.setRg(rgmedico);
			c.setMedico(med);
			plano.setCodigoAut(Integer.valueOf(pds));
			c.setTipoConsulta(tc);
			c.setVlrTotal(Double.parseDouble(vlrTotal));
			
		}
		try {
			if(cmd.contains("Cadastrar")) {
				cadastrarConsulta(c);
				saida ="Consulta cadastrado com sucesso";
				c = null;
			}
			if(cmd.contains("Alterar")) {
				alterarConsulta(c);
				saida ="Consulta alterado com sucesso";
				c = null;
			}
			if(cmd.contains("Excluir")) {
				excluirConsulta(c);
				saida ="Consulta excluir com sucesso";
				c = null;
			}
			if(cmd.contains("Buscar")) {
				c = buscarConsulta(c);
			}
			if(cmd.contains("Listar")) {
				consultas = listarConsulta();
			}
		}catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		}finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("Consulta", c);
			request.setAttribute("Consultas", consultas);
			
			RequestDispatcher rd = request.getRequestDispatcher("Consulta.jsp");
			rd.forward(request,response);
		}
	}

	private void cadastrarMedico(Consulta c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ConsultaDao cDao = new ConsultaDao(gDao);
		cDao.inserir(c);
		listarConsulta();
	}

	private void alterarConsulta(Consulta c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ConsultaDao cDao = new ConsultaDao(gDao);
		cDao.atualizar(c);
		listarConsulta();
	}

	private void excluirConsulta(Consulta c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ConsultaDao	cDao = new ConsultaDao(gDao);
		cDao.excluir(c);
		listarConsulta();
	}

	private Consulta buscarConsulta(Consulta c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ConsultaDao cDao = new ConsultaDao(gDao);
		c = cDao.consultar(c);
		return c;
	}

	private List<Consulta> listarConsulta()throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ConsultaDao cDao = new ConsultaDao(gDao);
		List<Consulta> medicos = cDao.listar();
		return consulta; 
	}

}
