import java.time.LocalDate;

// Class utilitas untuk manipulasi tanggal
public class DateUtil {

    // Mengambil tanggal hari ini (realtime)
    public static LocalDate hariIni() {
        return LocalDate.now();
    }

    // Menghitung tanggal kembali berdasarkan lama peminjaman
    public static LocalDate hitungTanggalKembali(int lamaHari) {
        return LocalDate.now().plusDays(lamaHari);
    }
}
