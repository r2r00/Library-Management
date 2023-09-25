# Library Management Program

This program supports book rentals and archiving in a public library. It includes classes located in the `it.polito.library` package, with the main class being `LibraryManager`. The `TestExample` class in the `example` package demonstrates usage examples for the main methods and the requested checks. Exception handling is performed using the `LibException` type.



## R1: Readers and Books

The library maintains a collection of books in its archives, each with a unique title. Multiple copies of the same book can be archived, each with a unique book ID. Each book copy is associated with its rental history and current rental status.

- The `addBook()` method allows the addition of new books to the library's collection. It can be called multiple times, and if a duplicate book (with the same title) is added, it updates the number of available copies. It returns the unique book ID as a String.
- The `getTitles()` method returns a SortedMap with book titles sorted alphabetically, each linked to the number of available copies.
- The `getBooks()` method returns a Set of unique book IDs available in the library.
- The `addReader()` method adds a new reader by providing their name and surname. Reader IDs are assigned incrementally starting from 1000.
- The `getReaderName()` method retrieves a reader's name given their reader ID. It returns the name and surname in the format "Name Surname". If the ID doesn't exist, it raises a `LibException`.

## R2: Rentals Management

Readers can rent one book at a time.

- The `getAvailableBook()` method retrieves the book ID of an available book copy by providing the book title. It returns the book ID of the first available copy, sorted by book ID. If all copies of the title are rented, it returns "Not available". If the title is not in the library archive, it raises a `LibException`.
- The `startRental()` method initiates a rental of a book for a reader, requiring the unique book ID, unique reader ID, and starting date. If the same reader rents the same unique book copy twice, the second rental overwrites the first one. If either the book or the reader is not registered or involved in a rental, it raises a `LibException`.
- The `endRental()` method ends a rental of a specific book for a specific reader, updating the book's rental history and the reader's rental status. It takes the unique book ID, unique reader ID, and ending date as inputs. If either the book or the reader is not registered, or the book is not currently rented, it raises a `LibException`.
- The `getRentals()` method retrieves the list of readers who rented a specific book. It returns a SortedMap linking reader IDs to the starting and ending dates of each rental. If a rental is ongoing, it reports only the starting date and "ONGOING" for the ending date.

## R3: Book Donations

- The `receiveDonation()` method collects donated books, taking a list of book titles as input (in the format "First title, Second title"). It adds the received titles to the library archive.

## R4: Archive Management

- The `getOngoingRentals()` method retrieves all active rentals as a Map linking reader IDs of renting readers and the book IDs of the books they are currently renting.
- The `removeBooks()` method renews the book collection by removing all book copies, regardless of the title, that were never rented.

## R5: Stats

- The `findBookWorm()` method finds the reader with the highest total number of rentals and returns their unique reader ID as a String.
- The `rentalCounts()` method returns the number of total rentals (both ended and ongoing) for each title in the library as a map with titles as keys and the number of rentals as values.

