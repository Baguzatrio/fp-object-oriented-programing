-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 28, 2025 at 05:08 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ikanlautbarokah`
--

-- --------------------------------------------------------

--
-- Table structure for table `bahan_baku`
--

CREATE TABLE `bahan_baku` (
  `id` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `stok` double DEFAULT NULL,
  `satuan` varchar(10) DEFAULT NULL,
  `harga_per_unit` double DEFAULT NULL,
  `tanggal_beli` date DEFAULT NULL,
  `tanggal_kadaluarsa` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `bahan_baku`
--

INSERT INTO `bahan_baku` (`id`, `nama`, `stok`, `satuan`, `harga_per_unit`, `tanggal_beli`, `tanggal_kadaluarsa`) VALUES
(2, 'Tepung', 40, 'kg', 5, '2025-04-20', '2030-07-07'),
(3, 'penyedap', 10, 'kg', 10000, '2025-05-26', '2027-05-26'),
(4, 'ikan', 10, 'kg', 20000, '2025-06-20', '2025-06-30');

-- --------------------------------------------------------

--
-- Table structure for table `distribusi`
--

CREATE TABLE `distribusi` (
  `id` int(11) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `produk` varchar(100) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `tujuan` varchar(100) DEFAULT NULL,
  `harga_total` double DEFAULT NULL,
  `metode_bayar` enum('Tunai','Tempo') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `distribusi`
--

INSERT INTO `distribusi` (`id`, `tanggal`, `produk`, `jumlah`, `tujuan`, `harga_total`, `metode_bayar`) VALUES
(1, '2025-05-26', 'Tempura', 12, 'jombang', 120000, 'Tunai'),
(2, '2025-05-26', 'bakso ikan', 24, 'kediri', 100000, 'Tunai');

-- --------------------------------------------------------

--
-- Table structure for table `produk`
--

CREATE TABLE `produk` (
  `id` int(11) NOT NULL,
  `nama_produk` varchar(100) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `harga` decimal(10,2) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `produksi`
--

CREATE TABLE `produksi` (
  `id` int(11) NOT NULL,
  `tanggal` date DEFAULT NULL,
  `produk` varchar(100) DEFAULT NULL,
  `jumlah_batch` int(11) DEFAULT NULL,
  `total_kg` double DEFAULT NULL,
  `jumlah_kemasan` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produksi`
--

INSERT INTO `produksi` (`id`, `tanggal`, `produk`, `jumlah_batch`, `total_kg`, `jumlah_kemasan`) VALUES
(1, '2025-05-26', 'Tempura', 2, 12, 24);

-- --------------------------------------------------------

--
-- Table structure for table `produk_olahan`
--

CREATE TABLE `produk_olahan` (
  `id` int(11) NOT NULL,
  `nama_produk` varchar(100) DEFAULT NULL,
  `resep_id` int(11) DEFAULT NULL,
  `ukuran_kemasan` int(11) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `produk_olahan`
--

INSERT INTO `produk_olahan` (`id`, `nama_produk`, `resep_id`, `ukuran_kemasan`, `jumlah`) VALUES
(1, 'Bakso Ikan', 2, 500, 100),
(2, 'baso', 3, 500, 10),
(3, 'otak-otak', 2, 500, 12);

-- --------------------------------------------------------

--
-- Table structure for table `resep`
--

CREATE TABLE `resep` (
  `id` int(11) NOT NULL,
  `nama_produk` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep`
--

INSERT INTO `resep` (`id`, `nama_produk`) VALUES
(2, 'Tempura'),
(3, 'Bakso Ikan'),
(4, 'otak-otak');

-- --------------------------------------------------------

--
-- Table structure for table `resep_detail`
--

CREATE TABLE `resep_detail` (
  `id` int(11) NOT NULL,
  `resep_id` int(11) DEFAULT NULL,
  `bahan_baku_id` int(11) DEFAULT NULL,
  `jumlah` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `resep_detail`
--

INSERT INTO `resep_detail` (`id`, `resep_id`, `bahan_baku_id`, `jumlah`) VALUES
(1, 2, 2, 5),
(2, 3, 2, 2),
(3, 3, 3, 1),
(4, 4, 2, 10);

-- --------------------------------------------------------

--
-- Table structure for table `upah`
--

CREATE TABLE `upah` (
  `id` int(11) NOT NULL,
  `nama_pegawai` varchar(100) DEFAULT NULL,
  `jumlah_produksi` int(11) DEFAULT NULL,
  `upah_per_unit` double DEFAULT NULL,
  `total_upah` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `upah`
--

INSERT INTO `upah` (`id`, `nama_pegawai`, `jumlah_produksi`, `upah_per_unit`, `total_upah`) VALUES
(1, 'jamal', 12, 5000, 60000),
(2, 'andy', 3, 5000, 15000);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` enum('admin','pegawai') DEFAULT 'pegawai'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`) VALUES
(1, 'admin', 'admin123', 'admin');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `bahan_baku`
--
ALTER TABLE `bahan_baku`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `distribusi`
--
ALTER TABLE `distribusi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `produk`
--
ALTER TABLE `produk`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `produksi`
--
ALTER TABLE `produksi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `produk_olahan`
--
ALTER TABLE `produk_olahan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `resep_id` (`resep_id`);

--
-- Indexes for table `resep`
--
ALTER TABLE `resep`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `resep_detail`
--
ALTER TABLE `resep_detail`
  ADD PRIMARY KEY (`id`),
  ADD KEY `resep_id` (`resep_id`),
  ADD KEY `bahan_baku_id` (`bahan_baku_id`);

--
-- Indexes for table `upah`
--
ALTER TABLE `upah`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `bahan_baku`
--
ALTER TABLE `bahan_baku`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `distribusi`
--
ALTER TABLE `distribusi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `produk`
--
ALTER TABLE `produk`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `produksi`
--
ALTER TABLE `produksi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `produk_olahan`
--
ALTER TABLE `produk_olahan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `resep`
--
ALTER TABLE `resep`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `resep_detail`
--
ALTER TABLE `resep_detail`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `upah`
--
ALTER TABLE `upah`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `produk_olahan`
--
ALTER TABLE `produk_olahan`
  ADD CONSTRAINT `produk_olahan_ibfk_1` FOREIGN KEY (`resep_id`) REFERENCES `resep` (`id`);

--
-- Constraints for table `resep_detail`
--
ALTER TABLE `resep_detail`
  ADD CONSTRAINT `resep_detail_ibfk_1` FOREIGN KEY (`resep_id`) REFERENCES `resep` (`id`),
  ADD CONSTRAINT `resep_detail_ibfk_2` FOREIGN KEY (`bahan_baku_id`) REFERENCES `bahan_baku` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
