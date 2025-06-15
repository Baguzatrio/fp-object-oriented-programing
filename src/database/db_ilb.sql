CREATE DATABASE db_ilb;
USE db_ilb;

CREATE TABLE bahan_baku (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    stok DOUBLE NOT NULL,
    satuan VARCHAR(20) NOT NULL,
    harga_per_unit DOUBLE NOT NULL,
    tanggal_beli DATE NOT NULL,
    tanggal_kadaluarsa DATE NOT NULL,
    berat_per_unit DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE customer (
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    telp_customer VARCHAR(20) NOT NULL,
    alamat_customer TEXT NOT NULL
);

CREATE TABLE pekerja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    mulai_bekerja DATE NOT NULL,
    no_telp VARCHAR(20) NOT NULL,
    alamat TEXT NOT NULL,
     upah_per_jam INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE upah_pekerja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pekerja INT NOT NULL,
    tanggal DATE NOT NULL,
    jumlah DOUBLE NOT NULL,
    produk VARCHAR(100) NOT NULL,
    upah_per_unit INT NOT NULL,
    total_upah INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pekerja) REFERENCES pekerja(id) ON DELETE CASCADE,
    INDEX idx_tanggal (tanggal),
    INDEX idx_pekerja (id_pekerja)
);

CREATE TABLE produk_olahan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL UNIQUE,
    ukuran_kemasan INT NOT NULL COMMENT 'in grams',
    stok INT NOT NULL DEFAULT 0,
    harga INT NOT NULL,
    upah INT NOT NULL
);

CREATE TABLE produksi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    produk VARCHAR(100) NOT NULL,
    id_pekerja INT,  -- Removed NOT NULL to allow ON DELETE SET NULL
    jumlah_batch INT NOT NULL,
    total_kg DOUBLE NOT NULL,
    jumlah_kemasan INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pekerja) REFERENCES pekerja(id) ON DELETE SET NULL,
    FOREIGN KEY (produk) REFERENCES produk_olahan(nama_produk),
    INDEX idx_tanggal (tanggal),
    INDEX idx_produk (produk)
);

CREATE TABLE distribusi (
    no_nota VARCHAR(50) PRIMARY KEY,
    id_customer INT NOT NULL,
    nama_customer VARCHAR(100) NOT NULL,
    alamat TEXT NOT NULL,
    status ENUM('Lunas', 'Belum Lunas') NOT NULL,
    tanggal DATE NOT NULL,
    total INT NOT NULL,
    dibayar INT NOT NULL,
    kembalian INT NOT NULL,
    FOREIGN KEY (id_customer) REFERENCES customer(id_customer)
);

CREATE TABLE detail_distribusi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    no_nota VARCHAR(50) NOT NULL,
    nama_produk VARCHAR(100) NOT NULL,
    harga INT NOT NULL,
    jumlah INT NOT NULL,
    subtotal INT NOT NULL,
    FOREIGN KEY (no_nota) REFERENCES distribusi(no_nota)
);

CREATE TABLE resep (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(100) NOT NULL,
    produk_olahan_id INT,
    FOREIGN KEY (nama_produk) REFERENCES produk_olahan(nama_produk),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (produk_olahan_id) REFERENCES produk_olahan(id) ON DELETE CASCADE,
    UNIQUE KEY uk_produk_olahan (produk_olahan_id)
);

CREATE TABLE resep_detail (
    id INT AUTO_INCREMENT PRIMARY KEY,
    resep_id INT NOT NULL,
    bahan_baku_id INT NOT NULL,
    jumlah DOUBLE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (resep_id) REFERENCES resep(id) ON DELETE CASCADE,
    FOREIGN KEY (bahan_baku_id) REFERENCES bahan_baku(id) ON DELETE CASCADE,
    UNIQUE KEY uk_resep_bahan (resep_id, bahan_baku_id)
);

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS laporan_keuangan (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tanggal DATETIME NOT NULL,
    jenis ENUM('pendapatan', 'pengeluaran') NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    jumlah DECIMAL(12,2) NOT NULL,
    keterangan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tanggal (tanggal),
    INDEX idx_jenis (jenis),
    INDEX idx_kategori (kategori)
);

CREATE TABLE IF NOT EXISTS pengeluaran_lain (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tanggal DATE NOT NULL,
    jenis VARCHAR(50) NOT NULL,
    kategori VARCHAR(50) NOT NULL,
    jumlah INT NOT NULL,
    keterangan TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tanggal (tanggal),
    INDEX idx_jenis (jenis)
);

CREATE TABLE IF NOT EXISTS ongkir (
    id INT AUTO_INCREMENT PRIMARY KEY,
    area VARCHAR(100) NOT NULL,
    biaya INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_area (area)
);

CREATE TABLE IF NOT EXISTS nota (
    nomor_nota VARCHAR(20) PRIMARY KEY,
    nama_customer VARCHAR(100) NOT NULL,
    alamat TEXT NOT NULL,
    tanggal DATETIME NOT NULL,
    total DECIMAL(12,2) NOT NULL,
    bayar DECIMAL(12,2) NOT NULL,
    kembali DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_tanggal (tanggal)
);

CREATE TABLE IF NOT EXISTS detail_nota (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomor_nota VARCHAR(20) NOT NULL,
    nama_barang VARCHAR(100) NOT NULL,
    jumlah INT NOT NULL,
    harga DECIMAL(12,2) NOT NULL,
    subtotal DECIMAL(12,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (nomor_nota) REFERENCES nota(nomor_nota) ON DELETE CASCADE,
    INDEX idx_nota (nomor_nota)
);

CREATE TABLE IF NOT EXISTS session_logs (
    log_id VARCHAR(36) PRIMARY KEY,
    session_id TEXT NOT NULL,
    username TEXT NOT NULL,
    module TEXT NOT NULL,
    action_type TEXT NOT NULL,
    entity_id TEXT,
    description TEXT,
    timestamp DATETIME NOT NULL,
    ip_address TEXT,
    user_agent TEXT,
    INDEX idx_session_user (username(50)),
    INDEX idx_session_module (module(50)),
    INDEX idx_session_time (timestamp)
);

ALTER TABLE produk_olahan 
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE produk_olahan 
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

DESCRIBE produk_olahan;

SHOW CREATE TABLE resep;
SELECT * FROM resep;

DESCRIBE resep;
ALTER TABLE resep DROP FOREIGN KEY resep_ibfk_1;
ALTER TABLE resep
DROP COLUMN nama_produk,                     
MODIFY COLUMN produk_olahan_id INT NOT NULL;
ALTER TABLE resep
ADD CONSTRAINT fk_resep_produk_olahan 
FOREIGN KEY (produk_olahan_id) REFERENCES produk_olahan(id) ON DELETE CASCADE;