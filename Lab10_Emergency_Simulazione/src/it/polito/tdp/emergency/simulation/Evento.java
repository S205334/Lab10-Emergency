//////////////////////////////////////////////////////////////////-*-java-*-//
//             // Classroom code for "Tecniche di Programmazione"           //
//   #####     // (!) Giovanni Squillero <giovanni.squillero@polito.it>     //
//  ######     //                                                           //
//  ###   \    // Copying and distribution of this file, with or without    //
//   ##G  c\   // modification, are permitted in any medium without royalty //
//   #     _\  // provided this notice is preserved.                        //
//   |   _/    // This file is offered as-is, without any warranty.         //
//   |  _/     //                                                           //
//             // See: http://bit.ly/tecn-progr                             //
//////////////////////////////////////////////////////////////////////////////

package it.polito.tdp.emergency.simulation;

public class Evento implements Comparable<Evento> {

	public int getDato() {
		return dato;
	}

	@Override
	public String toString() {
		return "Evento [tempo=" + tempo + ", tipo=" + tipo + ", dato=" + dato + "]";
	}

	public enum TipoEvento {
		PAZIENTE_ARRIVA, PAZIENTE_GUARISCE, PAZIENTE_MUORE, DOCTOR_INIZIA_TURNO, DOCTOR_FINE_TURNO, ASSISTENTE_INIZIA_TURNO, ASSISTENTE_FINE_TURNO
	}

	protected long tempo;
	protected TipoEvento tipo;
	protected int dato;

	public long getTempo() {
		return tempo;
	}

	public TipoEvento getTipo() {
		return tipo;
	}

	public Evento(long time, TipoEvento type, int dato) {
		super();
		this.tempo = time;
		this.tipo = type;
		this.dato = dato;
	}

	@Override
	public int compareTo(Evento arg0) {
		return Long.compare(this.tempo, arg0.tempo);
	}

}
