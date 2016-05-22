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

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import it.polito.tdp.emergency.simulation.Paziente.StatoPaziente;


public class Core {

	int pazientiSalvati = 0;
	int pazientiPersi = 0;
	
	StringBuilder result = new StringBuilder();
	Queue<Evento> listaEventi = new PriorityQueue<Evento>();
	Queue<Dottore> docDisponibili = new PriorityQueue<Dottore>();
	Queue<Assistente> assDisponibili = new PriorityQueue<Assistente>();
	Queue<Paziente> pazientiInAttesa = new PriorityQueue<Paziente>();
	Map<Integer, Paziente> pazienti = new HashMap<Integer, Paziente>();
	Map<Integer, Dottore> dottori = new HashMap<Integer, Dottore>();
	Map<Integer, Assistente> assistenti = new HashMap<>();
	Map<Paziente, Dottore> docOccupati = new HashMap<>();
	Map<Paziente, Assistente> assOccupati = new HashMap<>();
	
	
	public String getResult() {
		result.append("\nPersi: " +pazientiPersi);
		result.append("\nSalvati: " +pazientiSalvati);
		return result.toString();
	}
	public int getDottori() {
		return dottori.size();
	}

	public void aggiungiEvento(Evento e) {
		listaEventi.add(e);
	}

	public void aggiungiAssistente (Assistente a) {
		assistenti.put(a.getId(), a);
	}
	
	public void aggiungiPaziente(Paziente p) {
		pazienti.put(p.getId(), p);
	}
	
	public boolean aggiungiDottore(Dottore d) {
		if(dottori.put(d.getId(), d) == null)
			return true;
		return false;
	}

	public void passo() {
		Evento e = listaEventi.remove();
		
		switch (e.getTipo()) {
		
		case PAZIENTE_ARRIVA:
			result.append(e.getTempo()+" : Arriva il paziente " + pazienti.get(e.getDato()).getNome()+"\n");
			pazientiInAttesa.add(pazienti.get(e.getDato()));
		
				switch (pazienti.get(e.getDato()).getStato()) {
					case WHITE:
						break;
					case YELLOW:
						this.aggiungiEvento(new Evento(e.getTempo() + 6 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
						break;
					case RED:
						this.aggiungiEvento(new Evento(e.getTempo() + 1 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
						break;
					case GREEN:
						this.aggiungiEvento(new Evento(e.getTempo() + 12 * 60, Evento.TipoEvento.PAZIENTE_MUORE, e.getDato()));
						break;
					default:
						System.err.println("Panik!");
				}
			break;
		
		case PAZIENTE_GUARISCE:
			if (pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.BLACK) {
								
				if(pazienti.get(e.getDato()).getStato() != Paziente.StatoPaziente.CURA_SOSPESA) {
					pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.SALVO);
					if(docOccupati.containsKey(pazienti.get(e.getDato()))) {
						Dottore d = docOccupati.remove(pazienti.get(e.getDato()));
						docDisponibili.add(d);
						result.append(e.getTempo()+" : Il paziente "+pazienti.get(e.getDato()).getNome()+" è stato salvato da " +d.getNome()+"\n");
					} else {
						Assistente a = assOccupati.remove(pazienti.get(e.getDato()));
						assDisponibili.add(a);
						result.append(e.getTempo()+" : Il paziente "+pazienti.get(e.getDato()).getNome()+" è stato salvato da " +a.getNome()+"\n");
					
					}
					++pazientiSalvati;
				}
			}
			break;
		
		case PAZIENTE_MUORE:
			if (pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.SALVO) {
				// System.out.println("Paziente già salvato: " + e);
			} else {
				
				result.append(e.getTempo()+" : Il paziente "+pazienti.get(e.getDato()).getNome()+" è morto\n");
				
				if(pazienti.get(e.getDato()).getStato() == Paziente.StatoPaziente.IN_CURA) {
					if(docOccupati.containsKey(pazienti.get(e.getDato()))) {
						Dottore d = docOccupati.remove(pazienti.get(e.getDato()));
						docDisponibili.add(d);
						result.append(e.getTempo()+" : Il medico "+d.getNome()+" torna libero\n");
					} else {
						Assistente a = assOccupati.remove(pazienti.get(e.getDato()));
						assDisponibili.add(a);
						result.append(e.getTempo()+" : L'assistente "+a.getNome()+" torna libero\n");
					}
				}
				
				++pazientiPersi;
				pazienti.get(e.getDato()).setStato(Paziente.StatoPaziente.BLACK);
				
				
			}
			break;
			
		case DOCTOR_INIZIA_TURNO:
			docDisponibili.add(dottori.get(e.getDato()));
			this.aggiungiEvento(new Evento(e.getTempo() + 60 * 8, Evento.TipoEvento.DOCTOR_FINE_TURNO, e.getDato()));
			
			result.append(e.getTempo()+" : Il medico "+dottori.get(e.getDato()).getNome()+" inizia il turno\n");
			
			break;
		
		case DOCTOR_FINE_TURNO:
			if(docOccupati.containsValue(dottori.get(e.dato))) {
				for(Paziente k : docOccupati.keySet())
					if(docOccupati.get(k).equals(dottori.get(e.dato))){
						if(!docDisponibili.isEmpty()) {
							Dottore d =  docDisponibili.poll();
							docOccupati.replace(k, d);	
							result.append(e.getTempo()+" : Il medico "+d.getNome()+" ha sostituito "+dottori.get(e.getDato()).getNome()+"\n");
						}
						else {
							pazienti.get(k.getId()).setStato(Paziente.StatoPaziente.CURA_SOSPESA);
							result.append(e.getTempo()+" : Al paziente "+pazienti.get(k.getId()).getNome()+" è stata sospesa la cura\n");
						}
					}
			} else {
			
				docDisponibili.remove(dottori.get(dottori.get(e.getDato())));
			}
			
				if(!pazientiInAttesa.isEmpty())
					this.aggiungiEvento(new Evento(e.getTempo() + 60 * 16, Evento.TipoEvento.DOCTOR_INIZIA_TURNO, e.getDato()));
				
				result.append(e.getTempo()+" : Il medico "+dottori.get(e.getDato()).getNome()+" finisce il turno\n");

			break;
			
		case ASSISTENTE_INIZIA_TURNO:
			assDisponibili.add(assistenti.get(e.getDato()));
			this.aggiungiEvento(new Evento(e.getTempo() + 60 * 8, Evento.TipoEvento.ASSISTENTE_FINE_TURNO, e.getDato()));
			
			result.append(e.getTempo()+" : L'assistente "+assistenti.get(e.getDato()).getNome()+" inizia il turno\n");
			
			break;
			
		case ASSISTENTE_FINE_TURNO:
			if(assOccupati.containsValue(assistenti.get(e.dato))) {
				for(Paziente k : assOccupati.keySet())
					if(assOccupati.get(k).equals(assistenti.get(e.dato))){
						if(!assDisponibili.isEmpty()) {
							Assistente a = assDisponibili.poll();
							assOccupati.replace(k, a);	
							result.append(e.getTempo()+" : L'assistente "+a.getNome()+" ha sostituito "+assistenti.get(e.getDato()).getNome()+"\n");
						}
						else {
							pazienti.get(k.getId()).setStato(Paziente.StatoPaziente.CURA_SOSPESA);
							result.append(e.getTempo()+" : Al paziente "+pazienti.get(k.getId()).getNome()+" è stata sospesa la cura\n");
						}
					}
			} else {
			
				assDisponibili.remove(assistenti.get(assistenti.get(e.getDato())));
			}
			
				if(!pazientiInAttesa.isEmpty())
					this.aggiungiEvento(new Evento(e.getTempo() + 60 * 16, Evento.TipoEvento.ASSISTENTE_INIZIA_TURNO, e.getDato()));
				
				result.append(e.getTempo()+" : L'assistente "+assistenti.get(e.getDato()).getNome()+" finisce il turno\n");

			break;
			
		default:
			System.err.println("Panik!");
		}

		while (cura(e.getTempo()));
	}

	protected boolean cura(long adesso) {
		
		if (pazientiInAttesa.isEmpty()) {
			return false;
		}
		
		if (docDisponibili.isEmpty()&&assDisponibili.isEmpty())
			return false;
		

		Paziente p = pazientiInAttesa.remove();
		
		if (p.getStato() != Paziente.StatoPaziente.BLACK) {
			
			pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
			
			if(p.getStato() != StatoPaziente.RED && !assDisponibili.isEmpty()) {
				Assistente a = assDisponibili.poll();
				assOccupati.put(p, a);
				result.append(adesso+" : L'assistente " + a.getNome() +" inizia a curare il paziente "+p.getNome()+"\n");
			} else {
				pazienti.get(p.getId()).setStato(Paziente.StatoPaziente.IN_CURA);
				Dottore d = docDisponibili.poll();
				docOccupati.put(p, d);
				result.append(adesso+" : Il medico " + d.getNome() +" inizia a curare il paziente "+p.getNome()+"\n");
			}
			
			aggiungiEvento(new Evento(adesso + 30, Evento.TipoEvento.PAZIENTE_GUARISCE, p.getId()));
		}

		return true;
	}

	public void simula() {
		
		while (!listaEventi.isEmpty()) {
			passo();
		}
	}

}
