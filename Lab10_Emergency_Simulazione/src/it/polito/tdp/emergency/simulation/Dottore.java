package it.polito.tdp.emergency.simulation;

public class Dottore implements Comparable<Dottore>{
	
	private int id;
	private String nome;

	public Dottore(int id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return nome;
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
		Dottore other = (Dottore) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(Dottore o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	
}
