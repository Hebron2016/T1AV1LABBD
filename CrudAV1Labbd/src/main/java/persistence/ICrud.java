package persistence;

import java.sql.SQLException;
import java.util.List;

public interface ICrud<T> {
	
	public void inserir(T t)throws ClassNotFoundException, SQLException;
	public void atualizar(T t)throws ClassNotFoundException, SQLException;
	public void excluir(T t)throws ClassNotFoundException, SQLException;
 	public T consultar(T t)throws ClassNotFoundException, SQLException;
	public List<T> listar()throws ClassNotFoundException, SQLException;

}
