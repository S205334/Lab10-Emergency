package it.polito.tdp.emergency.simulation;

public class Assistente implements Comparable<Assistente> {

	private int id;
	private String nome;
	
	public Assistente(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}	

	@Override
	public String toString() {
		return "Assistente [id=" + id + ", nome=" + nome + "]";
	}

	@Override
	public int compareTo(Assistente o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Assistente other = (Assistente) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	

}
