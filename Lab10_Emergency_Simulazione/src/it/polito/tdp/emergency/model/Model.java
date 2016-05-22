package it.polito.tdp.emergency.model;

import it.polito.tdp.emergency.db.FieldHospitalDAO;
import it.polito.tdp.emergency.simulation.Assistente;
import it.polito.tdp.emergency.simulation.Core;
import it.polito.tdp.emergency.simulation.Dottore;
import it.polito.tdp.emergency.simulation.Evento;

public class Model {
	
	Core simulatore;
	FieldHospitalDAO dao;

	public Model() {
		simulatore = new Core();
		dao = new FieldHospitalDAO();
	}
	
	public boolean addMedico(String nome, int time) {
		if(simulatore.aggiungiDottore(new Dottore(simulatore.getDottori(), nome))) {
			simulatore.aggiungiEvento(new Evento(time, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, simulatore.getDottori()-1));
			return true;
		}
		
		return false;
	}

	public String stub() {
		dao.getAllConnection(simulatore);
		
		simulatore.aggiungiAssistente(new Assistente(1, "Fulvio Rossi"));
		simulatore.aggiungiEvento(new Evento(0, Evento.TipoEvento.ASSISTENTE_INIZIA_TURNO, 1));
		simulatore.aggiungiAssistente(new Assistente(2, "Mario Bianchi"));
		simulatore.aggiungiEvento(new Evento(320, Evento.TipoEvento.ASSISTENTE_INIZIA_TURNO, 2));
		simulatore.aggiungiAssistente(new Assistente(3, "Pietro Sasso"));
		simulatore.aggiungiEvento(new Evento(640, Evento.TipoEvento.ASSISTENTE_INIZIA_TURNO, 3));

		simulatore.simula();
		return simulatore.getResult();
	}

}
