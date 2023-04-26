package BE;

public class Customer {
    private int id;
    private String name;
    private String email;
    private int tlf;

    public Customer(int id, String firstName, String email, int tlf) {
        this.id = id;
        this.name = firstName;
        this.email = email;
        this.tlf = tlf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTlf() {
        return tlf;
    }

    public void setTlf(int tlf) {
        this.tlf = tlf;
    }
}
