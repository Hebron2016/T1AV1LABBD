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

import model.Cliente;
import persistence.GenericDao;
import persistence.ClienteDao;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	public ClienteServlet() {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("professoor.jsp");
		rd.forward(request,response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("botao");
		String codigo = request.getParameter("codigo");
		String nome = request.getParameter("nome");
		String telefone = request.getParameter("telefone");
		String dataNasc = request.getParameter("dataNasc");
		String senha = request.getParameter("senha");
		
		String saida="";
		String erro="";
		Cliente c = new Cliente("","","");
		List<Cliente> clientes = new ArrayList<>();
		
		if (!cmd.contains("Listar")) {
			c.setRg(codigo);
		}
		if(cmd.contains("Cadastrar")|| cmd.contains("Alterar")) {
			c.setNome(nome);
			c.setTelefone((telefone));
			c.setDataNasc(LocalDate.parse(dataNasc));
			c.setSenha(senha);
		}
		try {
			if(cmd.contains("Cadastrar")) {
				cadastrarCliente(c);
				saida ="Cliente cadastrado com sucesso";
				c = null;
			}
			if(cmd.contains("Alterar")) {
				alterarCliente(c);
				saida ="Cliente alterado com sucesso";
				c = null;
			}

			if(cmd.contains("Buscar")) {
				c = buscarCliente(c);
			}
			if(cmd.contains("Listar")) {
				clientes = listarClientes();
			}
		}catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		}finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("professor", c);
			request.setAttribute("professores", clientes);
			
			RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
			rd.forward(request,response);
		}
	}

	private void cadastrarCliente(Cliente c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		cDao.inserir(c);
		listarClientes();
	}

	private void alterarCliente(Cliente c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		cDao.atualizar(c);
		listarClientes();	
	}

	private Cliente buscarCliente(Cliente c)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao pDao = new ClienteDao(gDao);
		c = pDao.consultar(c);
		return c;
	}

	private List<Cliente> listarClientes()throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao cDao = new ClienteDao(gDao);
		List<Cliente> clientes = cDao.listar();
		return clientes; 
	}

}
