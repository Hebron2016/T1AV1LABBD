package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Consulta {

private LocalDate data;
private Timestamp hora;
private Cliente cliente;
private Medico medico;
private PlanoDeSaude PlanoDeSaude;
private String tipoConsulta;
private double vlrTotal;
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public Timestamp getHora() {
		return hora;
	}
	public void setHora(Timestamp hora) {
		this.hora = hora;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	public PlanoDeSaude getPlanoDeSaude() {
		return PlanoDeSaude;
	}
	public void setPlanoDeSaude(PlanoDeSaude planoDeSaude) {
		PlanoDeSaude = planoDeSaude;
	}
	public String getTipoConsulta() {
		return tipoConsulta;
	}
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	public double getVlrTotal() {
		return vlrTotal;
	}
	public void setVlrTotal(double vlrTotal) {
		this.vlrTotal = vlrTotal;
	}
}
