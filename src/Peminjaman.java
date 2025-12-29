import java.time.LocalDate;

// Class untuk merepresentasikan data peminjaman buku
public class Peminjaman {

    // Atribut peminjaman
    private String nama;
    private String idPeminjam;
    private String judulBuku;
    private LocalDate tanggalPinjam;
    private LocalDate tanggalKembali;

    // Constructor peminjaman
    public Peminjaman(String nama, String idPeminjam, String judulBuku, int lamaHari) {
        this.nama = nama;
        this.idPeminjam = idPeminjam;
        this.judulBuku = judulBuku;

        // Mengambil tanggal hari ini
        this.tanggalPinjam = DateUtil.hariIni();

        // Menghitung tanggal kembali
        this.tanggalKembali = DateUtil.hitungTanggalKembali(lamaHari);
    }

    // ===== GETTER (agar tidak muncul warning unused field) =====
    public String getNama() {
        return nama;
    }

    public String getIdPeminjam() {
        return idPeminjam;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public LocalDate getTanggalPinjam() {
        return tanggalPinjam;
    }

    public LocalDate getTanggalKembali() {
        return tanggalKembali;
    }
}
