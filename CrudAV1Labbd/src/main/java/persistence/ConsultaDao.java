package persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Especialidade;
import model.Consulta;

public class ConsultaDao implements ICrud<Consulta> {

	private GenericDao gDao;

	public ConsultaDao(GenericDao gDao) {
			this.gDao = gDao;
		}

	@Override
	public void inserir(Consulta m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "{CALL sp_cadConsulta (?,?,?,?,?,?,?,?)}";
		CallableStatement cs = c.prepareCall(sql);
		cs.setObject(1, m.getData());
		cs.setObject(2, m.getHora());
		cs.setString(3, m.getCliente().getRg());
		cs.setString(4, m.getMedico().getEspecialidade().getNome());
		cs.setString(5, m.getMedico().getRg());	
		cs.setInt(6, m.getPlanoDeSaude().getCodigoAut());		
		cs.setString(7,m.getTipoConsulta());
		cs.setDouble(8, m.getVlrTotal());
		cs.execute();
		cs.close();
		c.close();
	}

	@Override
	public void atualizar(Consulta e) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE Consulta SET data=? WHERE clientePessoarg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setObject(1, e.getData());
		ps.setString(2, e.getCliente().getRg());
		ps.execute();
		ps.close();
		c.close();

	}

	@Override
	public void excluir(Consulta m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "DELETE Consulta WHERE data = ? AND hora = ? AND clientePessoarg = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setObject(1, m.getData());
		ps.setObject(2, m.getHora());
		ps.setString(3, m.getCliente().getRg());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public Consulta consultar(Consulta m) throws ClassNotFoundException, SQLException {
		Connection c = gDao.getConnection();
		String sql = "SELECT * FROM Consulta WHERE data = ? AND hora = ? AND clientePessoarg = ? ";		
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ps.setObject(1, m.getData());
		ps.setObject(2, m.getHora());
		ps.setString(3, m.getCliente().getRg());
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
	public List<Consulta> listar() throws ClassNotFoundException, SQLException {
		List<Consulta> Consultas = new ArrayList<>();
		Connection c = gDao.getConnection();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT m.rg AS rgMed");
		sql.append("SELECT e.codigo AS codEsp, e.nomeEsp AS espNome");
		sql.append("FROM Consulta m, especialidade e");
		sql.append("WHERE m.especialidadeCodigo = e.codigo");
		PreparedStatement ps = c.prepareStatement(sql.toString());
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			Especialidade e = new Especialidade();
			e.setCodigo(rs.getInt("codEsp"));
			e.setNome(rs.getString("nomeEsp"));
			
			Consulta m = new Consulta("","","");
			c.setRg(rs.getString("rgMed"));
			m.setEspecialidade(e);
		}
		rs.close();
		ps.close();
		c.close();
		return Consultas;
	}

}
