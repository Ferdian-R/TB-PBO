import java.sql.*;

// Class helper untuk koneksi dan inisialisasi database
public class DatabaseHelper {

    // Konfigurasi database
    private static final String URL = "jdbc:mysql://localhost:3306/db_manajemen_perpustakaan";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection conn;

    // Method untuk mendapatkan koneksi database
    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                // Load driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(URL, USER, PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL tidak ditemukan");
            }
        }
        return conn;
    }
    
    // Method untuk membuat tabel database jika belum ada
    public static void setupDatabase() {
        try (Connection c = getConnection(); Statement st = c.createStatement()) {

            // Tabel user
            st.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(50) PRIMARY KEY,
                    password VARCHAR(50) NOT NULL
                )
            """);

            // Tabel buku
            st.execute("""
                CREATE TABLE IF NOT EXISTS buku (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    judul VARCHAR(100) UNIQUE NOT NULL,
                    penulis VARCHAR(100) NOT NULL,
                    tema VARCHAR(50) NOT NULL,
                    stok INT NOT NULL CHECK (stok >= 0)
                )
            """);

            // Tabel peminjaman
            st.execute("""
                CREATE TABLE IF NOT EXISTS peminjaman (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nama_peminjam VARCHAR(100) NOT NULL,
                    id_peminjam VARCHAR(20) NOT NULL,
                    judul_buku VARCHAR(100) NOT NULL,
                    tanggal_pinjam DATE NOT NULL,
                    tanggal_kembali DATE NOT NULL
                )
            """);

        } catch (SQLException e) {
            System.out.println("Gagal setup database: " + e.getMessage());
        }
    }
}
