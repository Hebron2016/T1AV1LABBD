package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Especialidade;

public class EspecialidadeDao implements ICrud<Especialidade> {

	private GenericDao gDao;

	public EspecialidadeDao(GenericDao gDao) {
			this.gDao = gDao;
		}

	@Override
	public void inserir(Especialidade e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO especialidade VALUES (?,?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getCodigo());
		ps.setString(2, e.getNome());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void atualizar(Especialidade e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE especialidade SET nome=? WHERE codigo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, e.getNome());
		ps.setInt(2, e.getCodigo());
		ps.execute();
		ps.close();
		c.close();

	}

	@Override
	public void excluir(Especialidade e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "DELETE especialidade WHERE codigo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getCodigo());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Especialidade consultar(Especialidade e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT codigo, nome FROM professor WHERE codigo = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getCodigo());
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			e.setCodigo(rs.getInt("codigo"));
			e.setNome(rs.getString("nome"));
		}
		rs.close();
		ps.close();
		c.close();
		return e;
	}

	@Override
	public List<Especialidade> listar() throws ClassNotFoundException, SQLException {
		List<Especialidade> especialidades = new ArrayList<>();
		Connection c = gDao.getConnection();
		String sql = "SELECT codigo, nome FROM especialidade";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Especialidade e = new Especialidade();
			e.setCodigo(rs.getInt("codigo"));
			e.setNome(rs.getString("nome"));

			especialidades.add(e);
		}
		rs.close();
		ps.close();
		c.close();
		return especialidades;
	}

}
