import java.sql.*;               
import java.util.*;               
import java.sql.Date;            
import java.time.LocalDate;       
import java.time.temporal.ChronoUnit; 

// Class utama aplikasi perpustakaan
public class LibraryApp implements AuthInterface {

    private static Scanner sc = new Scanner(System.in);          
    private static List<String> logAktivitas = new ArrayList<>();

    public static void main(String[] args) {
        DatabaseHelper.setupDatabase();   
        new LibraryApp().authMenu();     
    }

    // Header utama sistem
    private void showHeader() {
        System.out.println("""
╔══════════════════════════════════════════════════════════╗
║     SISTEM MANAJEMEN PERPUSTAKAAN SMPN 18 SIJUNJUNG      ║
╠══════════════════════════════════════════════════════════╣
║        Buku - Peminjaman - Pengembalian - Denda          ║
╚══════════════════════════════════════════════════════════╝
""");
    }

    // ================= MENU AUTENTIKASI =================
    public void authMenu() {
        while (true) {
            showHeader();
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Panduan");
            System.out.println("4. Exit");
            System.out.print("Pilih: ");

            int pilih = Integer.parseInt(sc.nextLine());

            try {
                switch (pilih) {
                    case 1 -> {
                        System.out.print("Username: ");
                        String u = sc.nextLine();
                        System.out.print("Password: ");
                        String p = sc.nextLine();
                        if (login(u, p)) mainMenu();
                        else System.out.println("Login gagal!");
                    }
                    case 2 -> {
                        System.out.print("Username: ");
                        String u = sc.nextLine();
                        System.out.print("Password: ");
                        String p = sc.nextLine();
                        register(u, p);
                    }
                    case 3 -> showGuide();
                    case 4 -> System.exit(0);
                }
            } catch (Exception e) {
                System.out.println("Input tidak valid!");
            }
        }
    }

    private void showGuide() {
        System.out.println("""
Panduan:
1. Login/Register admin
2. Kelola data buku
3. Peminjaman & pengembalian
4. Sistem hitung denda otomatis
Tekan ENTER untuk kembali...
""");
        sc.nextLine();
    }

    @Override
    public boolean register(String u, String p) {
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("INSERT INTO users VALUES (?, ?)")) {

            ps.setString(1, u.toLowerCase());
            ps.setString(2, p.toLowerCase());
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Username sudah terdaftar!");
            return false;
        }
    }

    @Override
    public boolean login(String u, String p) {
        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")) {

            ps.setString(1, u.toLowerCase());
            ps.setString(2, p.toLowerCase());
            return ps.executeQuery().next();

        } catch (SQLException e) {
            return false;
        }
    }

    // ================= MENU UTAMA =================
    public void mainMenu() {
        boolean aktif = true;

        while (aktif) {
            showHeader();
            System.out.println("""
1. Tambah Buku
2. Lihat Stok Buku
3. Update Stok
4. Hapus Buku
5. Peminjaman Buku
6. Pengembalian Buku
7. Lihat Data Peminjaman
8. Logout
""");
            System.out.print("Pilih: ");
            int menu = Integer.parseInt(sc.nextLine());

            try {
                switch (menu) {

                    case 1 -> {
                        System.out.println("""
====================================
        TAMBAH DATA BUKU
====================================
""");
                        tambahBuku();
                    }

                    case 2 -> {
                        System.out.println("""
====================================
        DAFTAR STOK BUKU
====================================
""");
                        lihatBuku();
                    }

                    case 3 -> {
                        System.out.println("""
====================================
        UPDATE STOK BUKU
====================================
""");
                        updateStok();
                    }

                    case 4 -> {
                        System.out.println("""
====================================
        HAPUS DATA BUKU
====================================
""");
                        hapusBuku();
                    }

                    case 5 -> {
                        System.out.println("""
====================================
        PEMINJAMAN BUKU
====================================
""");
                        pinjamBuku();
                    }

                    case 6 -> {
                        System.out.println("""
====================================
    PENGEMBALIAN BUKU & DENDA
====================================
""");
                        kembalikanBuku();
                    }

                    case 7 -> {
                        System.out.println("""
====================================
        DATA PEMINJAMAN BUKU
====================================
""");
                        lihatPeminjaman();
                    }

                    case 8 -> {
                        aktif = false;
                        System.out.println("\nLog Aktivitas:");
                        logAktivitas.forEach(l -> System.out.println("- " + l));
                    }
                }
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan!");
            }
        }
    }

    // ================= CRUD BUKU =================
private void tambahBuku() throws SQLException {
    System.out.print("Judul: ");
    String j = sc.nextLine().toLowerCase();
    System.out.print("Penulis: ");
    String p = sc.nextLine();
    System.out.print("Tema: ");
    String t = sc.nextLine();
    System.out.print("Stok: ");
    int s = Integer.parseInt(sc.nextLine());

    try (Connection c = DatabaseHelper.getConnection();
         PreparedStatement ps =
                 c.prepareStatement(
                         "INSERT INTO buku (judul,penulis,tema,stok) VALUES (?,?,?,?)")) {

        ps.setString(1, j);
        ps.setString(2, p);
        ps.setString(3, t);
        ps.setInt(4, s);
        ps.executeUpdate();
        
        System.out.println("Buku berhasil ditambahkan!");

        logAktivitas.add("Tambah buku: " + j);
    }
}


    private void lihatBuku() throws SQLException {
        try (Connection c = DatabaseHelper.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM buku")) {

            String g = "+----+----------------------+----------------------+------------------+--------+";
            System.out.println(g);
            System.out.printf("| %-2s | %-20s | %-20s | %-16s | %-6s |\n",
                    "ID","JUDUL","PENULIS","TEMA","STOK");
            System.out.println(g);

            while (rs.next()) {
                System.out.printf("| %-2d | %-20s | %-20s | %-16s | %-6d |\n",
                        rs.getInt("id"),
                        rs.getString("judul"),
                        rs.getString("penulis"),
                        rs.getString("tema"),
                        rs.getInt("stok"));
            }
            System.out.println(g);
        }
    }

    private void updateStok() throws SQLException {
        System.out.print("Judul: ");
        String j = sc.nextLine().toLowerCase();
        System.out.print("Stok baru: ");
        int s = Integer.parseInt(sc.nextLine());

        try (Connection c = DatabaseHelper.getConnection();
            PreparedStatement ps =
                    c.prepareStatement("UPDATE buku SET stok=? WHERE judul=?")) {

            ps.setInt(1, s);
            ps.setString(2, j);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                System.out.println("Stok berhasil diupdate!");
                logAktivitas.add("Update stok: " + j);
            } else {
                System.out.println("Buku tidak ditemukan!");
            }
        }
    }



    private void hapusBuku() throws SQLException {
        System.out.print("Judul: ");
        String j = sc.nextLine().toLowerCase();

        try (Connection c = DatabaseHelper.getConnection();
            PreparedStatement ps =
                    c.prepareStatement("DELETE FROM buku WHERE judul=?")) {

            ps.setString(1, j);
            int hasil = ps.executeUpdate();

            if (hasil > 0) {
                System.out.println("Buku berhasil dihapus!");
                logAktivitas.add("Hapus buku: " + j);
            } else {
                System.out.println("Buku tidak ditemukan!");
            }
        }
    }


    // ================= PEMINJAMAN =================
    private void pinjamBuku() throws SQLException {

        System.out.print("Nama: ");
        String nama = sc.nextLine();
        System.out.print("ID: ");
        String id = sc.nextLine();
        System.out.print("Judul: ");
        String judul = sc.nextLine().toLowerCase();

        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("SELECT stok FROM buku WHERE judul=?")) {

            ps.setString(1, judul);
            ResultSet rs = ps.executeQuery();
            if (!rs.next() || rs.getInt("stok") <= 0) {
                System.out.println("Buku tidak tersedia!");
                return;
            }
        }

        System.out.print("Lama (hari): ");
        int lama = Integer.parseInt(sc.nextLine());

        LocalDate pinjam = LocalDate.now();
        LocalDate kembali = pinjam.plusDays(lama);

        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("""
                     INSERT INTO peminjaman
                     (nama_peminjam,id_peminjam,judul_buku,tanggal_pinjam,tanggal_kembali,status)
                     VALUES (?,?,?,?,?,'DIPINJAM')
                     """)) {

            ps.setString(1, nama);
            ps.setString(2, id);
            ps.setString(3, judul);
            ps.setDate(4, Date.valueOf(pinjam));
            ps.setDate(5, Date.valueOf(kembali));
            ps.executeUpdate();
        }

        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps =
                     c.prepareStatement("UPDATE buku SET stok = stok - 1 WHERE judul=?")) {

            ps.setString(1, judul);
            ps.executeUpdate();
        }

        logAktivitas.add("Peminjaman: " + judul + " oleh " + nama);
        System.out.println("Peminjaman berhasil!");
    }

    // ================= PENGEMBALIAN =================
    private void kembalikanBuku() throws SQLException {

        System.out.print("Nama: ");
        String nama = sc.nextLine();
        System.out.print("Judul: ");
        String judul = sc.nextLine().toLowerCase();

        String sql = """
            SELECT id, tanggal_kembali 
            FROM peminjaman 
            WHERE nama_peminjam=? AND judul_buku=? AND status='DIPINJAM'
        """;

        try (Connection c = DatabaseHelper.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, nama);
            ps.setString(2, judul);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Data tidak ditemukan!");
                return;
            }

            int idPinjam = rs.getInt("id");
            LocalDate batas = rs.getDate("tanggal_kembali").toLocalDate();
            long telat = ChronoUnit.DAYS.between(batas, LocalDate.now());
            long denda = telat > 0 ? telat * 1000 : 0;

            try (PreparedStatement psUp =
                         c.prepareStatement("UPDATE peminjaman SET status='DIKEMBALIKAN' WHERE id=?")) {
                psUp.setInt(1, idPinjam);
                psUp.executeUpdate();
            }

            try (PreparedStatement psUp =
                         c.prepareStatement("UPDATE buku SET stok = stok + 1 WHERE judul=?")) {
                psUp.setString(1, judul);
                psUp.executeUpdate();
            }

            System.out.println("Pengembalian berhasil. Denda: Rp" + denda);
            logAktivitas.add("Pengembalian: " + judul + " oleh " + nama);
        }
    }

    // ================= DATA PEMINJAMAN =================
    private void lihatPeminjaman() throws SQLException {

        try (Connection c = DatabaseHelper.getConnection();
             ResultSet rs = c.createStatement().executeQuery("SELECT * FROM peminjaman")) {

            String g = "+----+---------------+--------------------+------------+------------+--------------+";
            System.out.println(g);
            System.out.printf("| %-2s | %-13s | %-18s | %-10s | %-10s | %-12s |\n",
                    "ID","NAMA","JUDUL","PINJAM","KEMBALI","STATUS");
            System.out.println(g);

            while (rs.next()) {
                System.out.printf("| %-2d | %-13s | %-18s | %-10s | %-10s | %-12s |\n",
                        rs.getInt("id"),
                        rs.getString("nama_peminjam"),
                        rs.getString("judul_buku"),
                        rs.getDate("tanggal_pinjam"),
                        rs.getDate("tanggal_kembali"),
                        rs.getString("status"));
            }
            System.out.println(g);
        }
    }
}
