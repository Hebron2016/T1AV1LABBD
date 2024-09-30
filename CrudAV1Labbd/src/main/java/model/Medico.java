package model;

public class Medico extends Pessoa {

	public Medico(String rg, String nome, String telefone) {
		super(rg, nome, telefone);
	}
	
	private Especialidade especialidade;
	
	public Especialidade getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	
}
