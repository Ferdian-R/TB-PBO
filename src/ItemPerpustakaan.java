// Abstract class sebagai superclass item di perpustakaan
public abstract class ItemPerpustakaan {

    // Atribut yang dapat diakses oleh subclass
    protected String judul;
    protected String penulis;

    // Constructor superclass
    public ItemPerpustakaan(String judul, String penulis) {
        this.judul = judul;
        this.penulis = penulis;
    }

    // Method abstract yang wajib dioverride oleh subclass
    public abstract void displayInfo();
}
