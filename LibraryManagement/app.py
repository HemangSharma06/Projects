from flask import Flask, render_template, request, redirect, session, flash, url_for
from werkzeug.datastructures import ImmutableMultiDict
import mysql.connector
from datetime import datetime, timedelta

app = Flask(__name__)
app.secret_key = "test@test123@000"

# Database Connection
def get_db_connection():
    try:
        return mysql.connector.connect(
            host="localhost",
            user="root",
            password="Hemang@2006",
            database="librarymanagement"
        )
    except mysql.connector.Error as err:
        print(f"Database Connection Error: {err}")
        return None

# Home Page
@app.route('/')
def home():
    return render_template('login.html')

# User Signup
@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if request.method == 'POST':
        name = request.form['name']
        email = request.form['email']
        contact = request.form['contact']
        password = request.form['password']
        con = get_db_connection()
        if con:
            cur = con.cursor()
            try:
                cur.execute("INSERT INTO users (User_Name, User_Mail, User_Password, Contact) VALUES (%s, %s, %s, %s)",
                            (name, email, password, contact))
                cur.execute("Insert INTO members (Name,ContactNo) values (%s,%s);",(name,contact))
                con.commit()
                flash("Signup successful! Please log in.", "success")
                return redirect('/login')
            except mysql.connector.Error as err:
                flash(f"Error: {err}", "danger")
            finally:
                con.close()
        else:
            flash("Database connection failed!", "danger")
    return render_template('sign_up.html')

# User Login
@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        con = get_db_connection()
        if con:
            cur = con.cursor()
            cur.execute("SELECT * FROM users WHERE User_Mail = %s AND User_Password = %s", (email, password))
            user = cur.fetchone()
            con.close()

            if user:
                session['user_id'] = user[0]
                session['user_name'] = user[1]
                flash("Login successful!", "success")
                return redirect('/dashboard')
            else:
                flash("Invalid Credentials!", "danger")
        else:
            flash("Database connection failed!", "danger")
    return render_template('login.html')

# Staff Login
@app.route('/staff_login', methods=['GET', 'POST'])
def staff_login():
    if request.method == 'POST':
        email = request.form['email']
        password = request.form['password']

        con = get_db_connection()
        if con:
            cur = con.cursor(dictionary=True)
            cur.execute("SELECT * FROM staff WHERE E_Mail = %s AND Password = %s", (email, password))
            staff = cur.fetchone()
            con.close()

            if staff:
                session['staff_id'] = staff['Staff_ID']
                session['staff_name'] = staff['Name']
                flash("Staff login successful!", "success")
                return redirect('/admin')
            else:
                flash("Invalid Credentials!", "danger")
        else:
            flash("Database connection failed!", "danger")
    
    return render_template('staff_login.html')

# User Dashboard
@app.route('/dashboard')
def dashboard():
    if 'user_id' not in session:
        return redirect('/login')

    con = get_db_connection()
    if con:
        cur = con.cursor(dictionary=True)

        # Fetch available books
        cur.execute("SELECT * FROM books WHERE Available > 0")
        available_books = cur.fetchall()
        cur.execute("""
            SELECT transactions.Transaction_ID, books.Book_ID, books.Book_Name, books.Author, transactions.Return_Date 
            FROM transactions
            JOIN books ON transactions.Book_ID = books.Book_ID
            WHERE transactions.Member_ID = %s
        """, (session['user_id'],))

        issued_books = cur.fetchall()
        con.close()
        return render_template('dashboard.html', user_name=session['user_name'], available_books=available_books, issued_books=issued_books)
    else:
        flash("Database connection failed!", "danger")
        return redirect('/')

# Admin Panel
@app.route('/admin')
def admin():
    if 'staff_id' not in session:
        return redirect('/staff_login')

    con = get_db_connection()
    if con:
        cur = con.cursor(dictionary=True)
        cur.execute("Select * from books;")
        books = cur.fetchall()
        cur.execute("Select * from transactions;")
        transactions = cur.fetchall()
        cur.execute("Select * from Users;")
        users = cur.fetchall()
        con.close()
        return render_template('admin.html', books=books, transactions = transactions, users = users)
    else:
        flash("Database connection failed!", "danger")
        return redirect('/')

# Book Issue
@app.route('/issue', methods=['POST'])
def issue_book():
    if 'user_id' not in session:
        return redirect('/login')
    
    con = get_db_connection()
    cur = con.cursor()
    cur.execute("SELECT Member_ID FROM members WHERE Name = %s", (session['user_name'],))
    member = cur.fetchone()
    if not member:
        flash("Error: User is not registered as a library member!", "danger")
        return redirect('/dashboard')
    member_id = member[0]
    book_id = request.form.get('book_id') 
    cur.execute("SELECT COUNT(*) FROM transactions WHERE Member_ID = %s;",(member_id,))
    issued_count = cur.fetchone()[0]
    if issued_count >= 5:
        flash("You cannot issue more than 5 books at a time.", "danger")
        return redirect(url_for('dashboard'))
    if not book_id:
        return "Error: Book ID is missing", 400

    try:
        # Insert issue record
        cur.execute("INSERT INTO transactions (Member_ID, Book_ID, Issue_Date, Return_Date) VALUES (%s, %s, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY))", 
                    (member_id, book_id))

        # Decrease book availability
        cur.execute("UPDATE books SET Available = Available - 1 WHERE Book_ID = %s AND Available > 0", (book_id,))
        flash("Book issued successfully!", "success")
        if cur.rowcount == 0:
            return "Error: Book not available or invalid book ID", 400

        con.commit()
    except Exception as e:
        con.rollback()
        return f"Database error: {str(e)}", 500
    finally:
        cur.close()
        con.close()

    return redirect('/dashboard')


# Book Search
@app.route('/search', methods=['GET', 'POST'])
def search():
    if request.method == 'POST':
        search_query = request.form.get('query')

        con = get_db_connection()
        if con:
            cur = con.cursor(dictionary=True)
            cur.execute("SELECT * FROM books WHERE Book_Name LIKE %s OR Author LIKE %s",
                        ('%' + search_query + '%', '%' + search_query + '%'))
            books = cur.fetchall()
            con.close()
            return render_template('search_results.html', books=books, query=search_query)
        else:
            flash("Database connection failed!", "danger")

    return render_template('search.html')

# Book Return
@app.route('/return', methods=['POST'])
def return_book():
    if 'user_id' not in session:
        return redirect('/login')

    book_id = request.form['book_id']
    user_id = session['user_id']
    t_id = request.form['transaction_id']

    con = get_db_connection()
    if con:
        cur = con.cursor()
        try:
            cur.execute("SELECT Transaction_ID FROM transactions WHERE Book_ID = %s AND Transaction_id = %s;", (book_id, t_id))
            transaction = cur.fetchone()

            if transaction:
                transaction_id = transaction[0]

                # Delete the transaction
                cur.execute("DELETE FROM transactions WHERE Transaction_ID = %s;", (transaction_id,))

                # Update book availability
                cur.execute("UPDATE books SET Available = Available + 1 WHERE Book_ID = %s;", (book_id,))

                con.commit()
                flash("Book returned successfully!", "success")
            else:
                flash("Error: No active transaction found for this book.", "danger")

        except mysql.connector.Error as err:
            flash(f"Error returning book: {err}", "danger")
            con.rollback()
        finally:
            con.close()
    else:
        flash("Database connection failed!", "danger")

    return redirect('/dashboard')

# Update Books
@app.route('/update_book/<int:book_id>', methods=['GET', 'POST'])
def update_book(book_id):
    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)
    
    # Fetch the book details based on the book_id
    cursor.execute("SELECT * FROM books WHERE Book_ID = %s", (book_id,))
    books = cursor.fetchall()

    if not books:
        return "Book not found", 404
    if request.method == 'POST':
        render_template('update_book.html', books = books)
        book_name = request.form['book_name']
        author = request.form['author']
        available = request.form['available']

        cursor.execute(
            "UPDATE books SET Book_Name = %s, Author = %s, Available = %s WHERE Book_ID = %s;",
            (book_name, author, available, book_id)
        )
        conn.commit()
        return redirect('/admin')
    return redirect('/admin')

# Delete Books
@app.route('/delete_book', methods=['GET', 'POST'])
def delete_book():
    if 'staff_id' not in session:
        return redirect('/staff_login')

    con = get_db_connection()
    if not con:
        flash("Database connection failed!", "danger")
        return redirect('/admin')

    cur = con.cursor(dictionary=True)

    if request.method == 'POST':
        book_id = request.form['book_id']
        
        # Delete book from the database
        cur.execute("DELETE FROM books WHERE Book_ID=%s;", (book_id,))
        con.commit()
        con.close()

        flash("Book deleted successfully!", "success")
        return redirect('/admin')

    elif request.method == 'GET':
        book_id = request.args.get('book_id')
        cur.execute("SELECT * FROM books WHERE Book_ID=%s", (book_id,))
        book = cur.fetchone()
        con.close()

        if not book:
            flash("Book not found!", "danger")
            return redirect('/admin')

        return render_template('delete_book.html', book=book)

# Delete User
@app.route('/delete_user', methods=['POST'])
def delete_user():
    if 'staff_id' not in session:
        return redirect('/staff_login')

    user_id = request.form['user_id']

    con = get_db_connection()
    if not con:
        flash("Database connection failed!", "danger")
        return redirect('/admin')

    cur = con.cursor()

    try:
        # Delete user from the users table
        cur.execute("DELETE FROM users WHERE User_ID=%s", (user_id,))
        con.commit()

        flash("User deleted successfully!", "success")
    except mysql.connector.Error as err:
        flash(f"Error deleting user: {err}", "danger")
        con.rollback()
    finally:
        con.close()
    return redirect('/admin')

# Add Books
@app.route('/add_book', methods=['GET', 'POST'])
def add_book():
    if request.method == 'POST':
        title = request.form['title']
        author = request.form['author']
        quantity = int(request.form['quantity'])

        # Connect to the database
        conn = get_db_connection()
        cursor = conn.cursor()

        # Insert book data into the 'books' table
        cursor.execute('''
            INSERT INTO books (Book_Name, Author, Available) 
            VALUES (%s, %s, %s)
        ''', (title, author, quantity))

        # Commit the transaction and close the connection
        conn.commit()
        cursor.close()
        conn.close()

        return redirect(url_for('admin'))
    
    return render_template('add_book.html')

# Logout
@app.route('/logout')
def logout():
    session.clear()
    flash("Logged out successfully!", "info")
    return redirect('/')

if __name__ == '__main__':
    app.run(debug=True, port=100, host='0.0.0.0')
