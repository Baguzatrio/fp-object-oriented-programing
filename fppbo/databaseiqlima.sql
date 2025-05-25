CREATE DATABASE db_fppbo;
USE db_fppbo;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE customer (
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    telp_customer VARCHAR(100),
    alamat_customer VARCHAR(200)
);

CREATE TABLE distribusi (
    no_nota VARCHAR(20) PRIMARY KEY,
    id_customer INT NOT NULL,
    nama_customer VARCHAR(50) NOT NULL,
    alamat TEXT NOT NULL,
    tanggal DATE NOT NULL,
    total INT NOT NULL,
    dibayar INT NOT NULL,
    kembalian INT NOT NULL,
    status ENUM('Lunas', 'Belum Lunas') NOT NULL,
    FOREIGN KEY (id_customer) REFERENCES customer(id_customer)
);

CREATE TABLE detail_distribusi (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    no_nota VARCHAR(20) NOT NULL,
    nama_produk VARCHAR(50) NOT NULL,
    harga INT NOT NULL,
    jumlah INT NOT NULL,
    subtotal INT NOT NULL,
    FOREIGN KEY (no_nota) REFERENCES distribusi(no_nota) ON DELETE CASCADE
);

CREATE TABLE pekerja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    mulai_bekerja VARCHAR(20),
    no_telp VARCHAR(15),
    alamat TEXT
);
