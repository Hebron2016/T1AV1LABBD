package persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.Especialidade;

public class ClienteDao implements ICrudCliente<Cliente> {

	private GenericDao gDao;

	public ClienteDao(GenericDao gDao) {
			this.gDao = gDao;
		}

	@Override
	public void inserir(Cliente l) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "{CALL sp_cadCliente (?,?,?,?,?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setString(1, l.getRg());
		cs.setString(2, l.getNome());
		cs.setString(3, l.getTelefone());
		cs.setObject(4, l.getDataNasc());
		cs.setString(5, l.getSenha());
		cs.execute();
		cs.close();
		c.close();
	}

	@Override
	public void atualizar(Cliente l) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE cliente c, pessoa p SET p.telefone = ?, c.senha = ?   WHERE pessoaRg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, l.getNome());
		ps.setString(2, l.getSenha());
		ps.setString(3, l.getRg());
		ps.execute();
		ps.close();
		c.close();

	}

	@Override
	public Cliente consultar(Cliente l) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT pessoaRg, dataNasc, senha FROM Cliente WHERE pessoaRg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, l.getRg());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			l.setRg(rs.getString("pessoaRg"));
			l.setDataNasc(rs.getDate("dataNasc").toLocalDate());
			l.setSenha(rs.getString("senha"));
		}
		rs.close();
		ps.close();
		c.close();
		return l;
	}

	@Override
	public List<Cliente> listar() throws ClassNotFoundException, SQLException {
		List<Cliente> clientes = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT pessoaRg, dataNasc, senha FROM cliente";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Cliente l = new Cliente("","","");
			l.setRg(rs.getString("pessoaRg"));
			l.setDataNasc(rs.getDate("dataNasc").toLocalDate());
			l.setSenha(rs.getString("senha"));
			clientes.add(l);
		}
		rs.close();
		ps.close();
		c.close();
		return clientes;
	}

}
