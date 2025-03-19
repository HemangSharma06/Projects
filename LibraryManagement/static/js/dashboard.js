// Function to fetch and display user-issued books
function loadUserBooks() {
    fetch('/user_books')
        .then(response => response.json())
        .then(data => {
            let booksList = document.getElementById('issuedBooksList');
            booksList.innerHTML = '';

            if (data.length === 0) {
                booksList.innerHTML = '<p>No books issued yet.</p>';
                return;
            }

            data.forEach(book => {
                let listItem = document.createElement('li');
                listItem.innerHTML = `
                    <span>${book.title} by ${book.author}</span>
                    <button onclick="returnBook(${book.book_id})">Return</button>
                `;
                booksList.appendChild(listItem);
            });
        })
        .catch(error => console.error('Error fetching books:', error));
}

// Function to return a book
function returnBook(bookId) {
    let confirmation = confirm("Are you sure you want to return this book?");
    if (confirmation) {
        fetch(`/return_book/${bookId}`, {
            method: 'POST'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('Book returned successfully!');
                loadUserBooks();
            } else {
                alert(`Error: ${data.message}`);
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

// Function to handle book search
document.addEventListener('DOMContentLoaded', function () {
    let searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', function (event) {
            event.preventDefault();
            let query = document.getElementById('searchInput').value;

            fetch(`/search_books?q=${query}`)
                .then(response => response.json())
                .then(data => {
                    let searchResults = document.getElementById('searchResults');
                    searchResults.innerHTML = '';

                    if (data.length === 0) {
                        searchResults.innerHTML = '<p>No books found.</p>';
                        return;
                    }

                    data.forEach(book => {
                        let bookItem = document.createElement('div');
                        bookItem.classList.add('book-item');
                        bookItem.innerHTML = `
                            <h4>${book.title}</h4>
                            <p>by ${book.author}</p>
                            <p>Available: ${book.available ? 'Yes' : 'No'}</p>
                        `;
                        searchResults.appendChild(bookItem);
                    });
                })
                .catch(error => console.error('Error searching books:', error));
        });
    }

    // Load issued books when the dashboard loads
    loadUserBooks();
});
