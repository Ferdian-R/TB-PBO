// Interface untuk autentikasi user (login & register)
public interface AuthInterface {

    // Method untuk mendaftarkan user baru
    boolean register(String username, String password);

    // Method untuk login user
    boolean login(String username, String password);
}
