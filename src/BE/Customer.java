package BE;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String tlf;
    private int customerType;
    private String streetName;
    private String zipcode;



    public Customer(int id, String name, String email, String tlf, int customerType, String streetName, String zipcode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.customerType = customerType;
        this.streetName = streetName;
        this.zipcode = zipcode;
    }

    public Customer(String name, String email, String tlf, int customerType, String streetName, String zipcode) {
        this.name = name;
        this.email = email;
        this.tlf = tlf;
        this.customerType = customerType;
        this.streetName = streetName;
        this.zipcode = zipcode;

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

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
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

    @Override
    public String toString() {
        return name;
    }
}
