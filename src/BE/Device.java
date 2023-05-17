package BE;

public class Device {
    private int id;
    private String device;
    private String username;
    private String password;

    public Device(int id, String device, String username, String password) {
        this.id = id;
        this.device = device;
        this.username = username;
        this.password = password;
    }

    public Device(String device, String username, String password) {
        this.device = device;
        this.username = username;
        this.password = password;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Name: " + device + ", username: " + username + ", password: " + password;
    }
}
