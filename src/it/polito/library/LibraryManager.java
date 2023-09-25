package it.polito.library;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;





public class LibraryManager {
	    
    // R1: Readers and Books 
    
    /**
	 * adds a book to the library archive
	 * The method can be invoked multiple times.
	 * If a book with the same title is already present,
	 * it increases the number of copies available for the book
	 * 
	 * @param title the title of the added book
	 * @return the ID of the book added 
	 */
	private TreeMap<Integer , Book> books = new TreeMap<>();
	private TreeMap<String, Integer> copies = new TreeMap<>();
	private TreeMap<Integer, Reader> readers = new TreeMap<>();
	private ArrayList<Rental> rentals= new ArrayList<>();

	
	
	private int nextBook = 0;
	private final int OFFSET = 1000;
	private int nextReader = 0;
	
    public String addBook(String title) {
    	books.put(nextBook+OFFSET, new Book(title, nextBook));
    	Book tmp = new Book(title, nextBook);
    	nextBook++;
    	if (copies.containsKey(title)) {
    		Integer temp1 ;
    		temp1 =copies.get(title);
    		temp1++;
    	
    		copies.put(title, temp1 );
    	}
    	else {
    		copies.put(title, 1 );
    	}
        return Integer.toString(books.get(nextBook+OFFSET-1).getId()+OFFSET);
    }
    
    /**
	 * Returns the book titles available in the library
	 * sorted alphabetically, each one linked to the
	 * number of copies available for that title.
	 * 
	 * @return a map of the titles liked to the number of available copies
	 */
    public SortedMap<String, Integer> getTitles() { 
    	SortedMap<String, Integer> sortedCopies = new TreeMap<String, Integer>();
    	sortedCopies.putAll(copies);
//    	Stream<String, Integer> strss = new 
        return copies;
    }
    /**
	 * Returns the books available in the library
	 * 
	 * @return a set of the titles liked to the number of available copies
	 */
    public Set<String> getBooks() { 
    	
        return books.keySet().stream().map(x->Integer.toString(x)).collect(Collectors.toSet());
    }
    
    /**
	 * Adds a new reader
	 * 
	 * @param name first name of the reader
	 * @param surname last name of the reader
	 */
    public void addReader(String name, String surname) {
    	Reader tmp = new Reader(name, surname, nextReader+OFFSET);
    	readers.put(nextReader+OFFSET, tmp);
    	nextReader++;
    }
    
    
    /**
	 * Returns the reader name associated to a unique reader ID
	 * 
	 * @param readerID the unique reader ID
	 * @return the reader name
	 * @throws LibException if the readerID is not present in the archive
	 */
    public String getReaderName(String readerID) throws LibException {
    	if (!readers.containsKey(Integer.parseInt(readerID))) throw new LibException("Reader not entered");
    	Reader tmp = readers.get(Integer.parseInt(readerID));
    	
        return tmp.getFirstName()+" "+tmp.getLastName();
    }    
    
    
    // R2: Rentals Management
    
    
    /**
	 * Retrieves the bookID of a copy of a book if available
	 * 
	 * @param bookTitle the title of the book
	 * @return the unique book ID of a copy of the book or the message "Not available"
	 * @throws LibException  an exception if the book is not present in the archive
	 */
    public String getAvailableBook(String bookTitle) throws LibException {
    	if (!copies.containsKey(bookTitle)) throw new LibException("Book not in the library");
    	Book tmp= books.values().stream().sorted(Comparator.comparing(Book::getId)).filter(x-> !x.isRented()).filter(x -> bookTitle.equals(x.getName())).findFirst().orElse(null); 
    	if (tmp == null) {
    		return "Not available";
    	}
        return Integer.toString(tmp.getId()+OFFSET);
    }   

    /**
	 * Starts a rental of a specific book copy for a specific reader
	 * 
	 * @param bookID the unique book ID of the book copy
	 * @param readerID the unique reader ID of the reader
	 * @param startingDate the starting date of the rental
	 * @throws LibException  an exception if the book copy or the reader are not present in the archive,
	 * if the reader is already renting a book, or if the book copy is already rented
	 */
	public void startRental(String bookID, String readerID, String startingDate) throws LibException {
		boolean found = false;
		if (!books.containsKey(Integer.parseInt(bookID))) throw new LibException("Book not in the library");
		if (!readers.containsKey(Integer.parseInt(readerID))) throw new LibException("Reader not registered");
		if (books.get(Integer.parseInt(bookID)).isRented()) throw new LibException("Book is rented");
		if (readers.get(Integer.parseInt(readerID)).hasRented()) throw new LibException("The reader is already renting a book.");
		for (Rental r: rentals) {
			if (r.getBook().getId() == Integer.parseInt(bookID) && r.getReader().getId() ==Integer.parseInt(readerID) && !r.getEndDate().equals("ONGOING")) {
				found = true;
				r.setStartDate(startingDate);
			}
		}
		if (found == false)
			rentals.add(new Rental(books.get(Integer.parseInt(bookID)), readers.get(Integer.parseInt(readerID)), startingDate));
			books.get(Integer.parseInt(bookID)).setRent(true);
			readers.get(Integer.parseInt(readerID)).setRent(true);
		
		

    }
    
	/**
	 * Ends a rental of a specific book copy for a specific reader
	 * 
	 * @param bookID the unique book ID of the book copy
	 * @param readerID the unique reader ID of the reader
	 * @param endingDate the ending date of the rental
	 * @throws LibException  an exception if the book copy or the reader are not present in the archive,
	 * if the reader is not renting a book, or if the book copy is not rented
	 */
    public void endRental(String bookID, String readerID, String endingDate) throws LibException {
    	boolean found = false;
    	
    	for (Rental r : rentals) {
    		if (r.getBook().equals(books.get(Integer.parseInt(bookID))) && r.getReader().equals(readers.get(Integer.parseInt(readerID)))) {
    			found = true;
    			r.endRental(endingDate);
    			books.get(Integer.parseInt(bookID)).setRent(false);
    			readers.get(Integer.parseInt(readerID)).setRent(false);
    		}
    	}
    	if (found == false) throw new LibException("Could not end the rent");
    	
    }
    
    
   /**
	* Retrieves the list of readers that rented a specific book.
	* It takes a unique book ID as input, and returns the readers' reader IDs and the starting and ending dates of each rental
	* 
	* @param bookID the unique book ID of the book copy
	* @return the map linking reader IDs with rentals starting and ending dates
	* @throws LibException  an exception if the book copy or the reader are not present in the archive,
	* if the reader is not renting a book, or if the book copy is not rented
	*/
    public SortedMap<String, String> getRentals(String bookID) throws LibException {
    	if (!books.containsKey(Integer.parseInt(bookID)))
    		throw new LibException("Book not available.");
    	TreeMap<String, String> tmp = new TreeMap<>();
    	for (Rental r : rentals)
    		if (bookID.equals(Integer.toString(r.getBook().getId() + OFFSET)))
    			tmp.put(Integer.toString(r.getReader().getId()), r.getStartDate() + " " + r.getEndDate());
    	if (tmp.isEmpty())
    		throw new LibException("Book not in rent.");
    	return tmp; 
    }
    
    
    // R3: Book Donations
    
    /**
	* Collects books donated to the library.
	* 
	* @param donatedTitles It takes in input book titles in the format "First title,Second title"
	*/
    public void receiveDonation(String donatedTitles) {
    	for (String bookToAdd : donatedTitles.split(",")) {
    		this.addBook(bookToAdd);
    	}
    }
    
    // R4: Archive Management

    /**
	* Retrieves all the active rentals.
	* 
	* @return the map linking reader IDs with their active rentals

	*/
    public Map<String, String> getOngoingRentals() {
    	TreeMap <String, String> tmp = new TreeMap<>();
    	for (Rental r : rentals) {
    		if (r.getEndDate().equals("ONGOING")) {
    			tmp.put(Integer.toString(r.getReader().getId()), Integer.toString(r.getBook().getId()+OFFSET));
    		}
    	}
//        return rentals.stream().map(x->
//        	Integer.toString(x.getReader().getId()).
//        	
//       );
    	return tmp;
    }
    
    /**
	* Removes from the archives all book copies, independently of the title, that were never rented.
	* 
	*/
    public void removeBooks() {
    	List<String> tmp = rentals.stream().map(x -> x.getBook().getName()).toList();
    	books.entrySet().removeIf(entry -> !tmp.contains(entry.getValue().getName()));
    }
    	
    // R5: Stats
    
    /**
	* Finds the reader with the highest number of rentals
	* and returns their unique ID.
	* 
	* @return the uniqueID of the reader with the highest number of rentals
	*/
    public String findBookWorm() {
    	return rentals.stream()
                .collect(Collectors.groupingBy(r -> Integer.toString(r.getReader().getId()), TreeMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
    
    /**
	* Returns the total number of rentals by title. 
	* 
	* @return the map linking a title with the number of rentals
	*/
    public Map<String, Integer> rentalCounts() {
        TreeMap<String, Integer> tmp = new TreeMap<>();
        for (Rental r : rentals)
            tmp.put(r.getBook().getName(), tmp.getOrDefault(r.getBook().getName(), 0) + 1);
        return tmp;
    }
}
