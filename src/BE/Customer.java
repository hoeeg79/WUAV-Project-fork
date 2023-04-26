package BE;

public class Customer {
    private int id;
    private String name;
    private String email;
    private int tlf;
    private String picture;

    public Customer(int id, String name, String email, int tlf, String picture) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.picture = picture;
    }

    public Customer(String name, String email, int tlf, String picture) {
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.picture = picture;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
