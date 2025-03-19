from flask import Flask, render_template, request, redirect, session, flash, url_for
import mysql.connector
from datetime import datetime, timedelta

app = Flask(__name__)
app.secret_key = "test@test123@000"

redirect('admin.html')
if __name__ == '__main__':
    app.run(debug=True, port=10000)