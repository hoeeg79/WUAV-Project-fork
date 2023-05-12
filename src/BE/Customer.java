package BE;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String tlf;
    private String streetName;
    private String zipcode;
    private String city;
    private boolean docReadyForApproval;

    public Customer(int id, String name, String email, String tlf, String streetName, String zipcode, String city) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.streetName = streetName;
        this.zipcode = zipcode;
        this.city = city;
    }

    public Customer(String name, String email, String tlf, String streetName, String zipcode, String city) {
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.streetName = streetName;
        this.zipcode = zipcode;
        this.city = city;
    }

    public Customer(int id, String name, String email, String tlf, String streetName, String zipcode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.streetName = streetName;
        this.zipcode = zipcode;
    }

    public int getId() {
        return id;
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

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDocReadyForApproval() {
        return docReadyForApproval;
    }

    public void setDocReadyForApproval(boolean docReadyForApproval) {
        this.docReadyForApproval = docReadyForApproval;
    }

    @Override
    public String toString() {
        return name;
    }
}
