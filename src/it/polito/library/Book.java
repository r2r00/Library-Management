package it.polito.library;

public class Book {
	private String name;
	private int id;
	private boolean rented ;
	
	public Book(String name, int id) {
		this.id = id;
		this.name = name;
		rented = false;
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public boolean isRented() {
		return rented;
		
	}
	public void setRent (boolean rentStat) {
		this.rented = rentStat;
		
	}

}
