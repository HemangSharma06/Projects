// Function to switch between sections in the admin panel
function showSection(sectionId) {
    let sections = document.querySelectorAll('.admin-section');
    sections.forEach(section => {
        section.classList.remove('active');
    });

    document.getElementById(sectionId).classList.add('active');
}

// Function to confirm before deleting a record
function confirmDelete(recordType, recordId) {
    let confirmation = confirm(`Are you sure you want to delete this ${recordType}?`);
    if (confirmation) {
        // Assuming you have an API route to handle deletions
        fetch(`/delete_${recordType}/${recordId}`, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert(`${recordType} deleted successfully!`);
                location.reload(); // Refresh page after deletion
            } else {
                alert(`Error: ${data.message}`);
            }
        })
        .catch(error => console.error('Error:', error));
    }
}

// Function to handle form submission for adding books
document.addEventListener('DOMContentLoaded', function () {
    let bookForm = document.getElementById('addBookForm');
    if (bookForm) {
        bookForm.addEventListener('submit', function (event) {
            event.preventDefault();

            let formData = new FormData(bookForm);
            fetch('/add_book', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Book added successfully!');
                    location.reload();
                } else {
                    alert(`Error: ${data.message}`);
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }

    // Form handling for adding users
    let userForm = document.getElementById('addUserForm');
    if (userForm) {
        userForm.addEventListener('submit', function (event) {
            event.preventDefault();

            let formData = new FormData(userForm);
            fetch('/add_user', {
                method: 'POST',
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('User added successfully!');
                    location.reload();
                } else {
                    alert(`Error: ${data.message}`);
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }
});
