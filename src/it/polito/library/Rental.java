package it.polito.library;

public class Rental {
	private Book book;
	private Reader reader;
	private String startDate;
	private String endDate;
	
	Rental (Book book, Reader reader, String startDate){
		this.book = book;
		this.reader = reader;
		this.startDate = startDate;
		endDate = "ONGOING";
		
	}
	public Book getBook() {
		return book;
	}
	public Reader getReader() {
		return reader;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void endRental (String endDate) {
		this.endDate = endDate;
		
	}
	public String getStartDate() {
		return startDate;
	}
	public String getEndDate() {
		return endDate;
	}

}
