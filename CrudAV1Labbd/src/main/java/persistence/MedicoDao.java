package persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Especialidade;
import model.Medico;

public class MedicoDao implements ICrud<Medico> {

	private GenericDao gDao;

	public MedicoDao(GenericDao gDao) {
			this.gDao = gDao;
		}

	@Override
	public void inserir(Medico m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "{CALL sp_cadMed (?,?,?,?,?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setString(1, m.getRg());
		cs.setString(2, m.getNome());
		cs.setString(3, m.getTelefone());
		cs.setInt(1, m.getEspecialidade().getCodigo());
		cs.execute();
		cs.close();
		c.close();
	}

	@Override
	public void atualizar(Medico e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE medico SET especialidadeCodigo=? WHERE pessoarg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setInt(1, e.getEspecialidade().getCodigo());
		ps.setString(2, e.getRg());
		ps.execute();
		ps.close();
		c.close();

	}

	@Override
	public void excluir(Medico m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "DELETE medico WHERE pessoarg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, m.getRg());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Medico consultar(Medico m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT m.rg AS rgMed");
		sql.append("SELECT e.codigo AS codEsp, e.nomeEsp AS espNome");
		sql.append("FROM medico m, especialidade e");
		sql.append("WHERE m.especialidadeCodigo = e.codigo");
		sql.append("AND m.pessoarg = ?");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setString(1, m.getRg());	
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			Especialidade e = new Especialidade();
			e.setCodigo(rs.getInt("codEsp"));
			e.setNome(rs.getString("nomeEsp"));
			
			m.setRg(rs.getString("rgMed"));
			m.setEspecialidade(e);
		}
		rs.close();
		ps.close();
		c.close();
		return m;
	}

	@Override
	public List<Medico> listar() throws ClassNotFoundException, SQLException {
		List<Medico> medicos = new ArrayList<>();
		Connection c = gDao.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT m.rg AS rgMed");
		sql.append("SELECT e.codigo AS codEsp, e.nomeEsp AS espNome");
		sql.append("FROM medico m, especialidade e");
		sql.append("WHERE m.especialidadeCodigo = e.codigo");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			Especialidade e = new Especialidade();
			e.setCodigo(rs.getInt("codEsp"));
			e.setNome(rs.getString("nomeEsp"));
			
			Medico m = new Medico("","","");
			m.setRg(rs.getString("rgMed"));
			m.setEspecialidade(e);
		}
		rs.close();
		ps.close();
		c.close();
		return medicos;
	}

}
