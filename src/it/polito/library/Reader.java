package it.polito.library;

public class Reader {
	private String firstName;
	private String lastName;
	private int id;
	private boolean rented;
	
	Reader(String firstname, String lastName, int id ){
		this.firstName =firstname;
		this.lastName = lastName;
		this.id = id;
		rented = false;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public int getId() {
		return id;
	}
	public boolean hasRented() {
		return rented;
	}
	public void setRent (boolean rent) {
		this.rented = rent;
		
	}

}
