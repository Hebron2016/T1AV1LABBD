package model;

import java.time.LocalDate;


public class Cliente extends Pessoa {
	public Cliente(String rg, String nome, String telefone) {
		super(rg, nome, telefone);
	}
	private LocalDate dataNasc;
	private String senha;

	public LocalDate getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(LocalDate dataNasc) {
		this.dataNasc = dataNasc;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
