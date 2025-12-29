// Class Buku merupakan subclass dari ItemPerpustakaan
public class Buku extends ItemPerpustakaan {

    // Atribut tambahan buku
    private String tema;
    private int stok;

    // Constructor buku
    public Buku(String judul, String penulis, String tema, int stok) {
        // Memanggil constructor superclass
        super(judul.toLowerCase(), penulis.toLowerCase());
        this.tema = tema.toLowerCase();
        this.stok = stok;
    }

    // Implementasi method abstract displayInfo
    @Override
    public void displayInfo() {
        System.out.println(judul + " | " + penulis + " | " + tema + " | Stok: " + stok);
    }
}
