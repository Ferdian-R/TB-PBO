// Class User untuk merepresentasikan data akun admin/user
public class User {

    // Atribut username dan password
    private String username;
    private String password;

    // Constructor untuk inisialisasi user
    public User(String username, String password) {
        // Username diubah menjadi huruf kecil agar konsisten
        this.username = username.toLowerCase();
        this.password = password;
    }

    // Getter username
    public String getUsername() {
        return username;
    }

    // Getter password
    public String getPassword() {
        return password;
    }
}
