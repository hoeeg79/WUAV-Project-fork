package BE;

public class TechDoc {
    private int id;
    private String setupName;
    private String filePathDiagram;
    private String setupDescription;
    private String filepathPicture;
    private String extraInfo;
    private String deviceLoginInfo;

    public TechDoc(int id, String setupName) {
        this.id = id;
        this.setupName = setupName;
    }

    public TechDoc(String setupName) {
        this.setupName = setupName;
    }

    public int getId() {
        return id;
    }

    public String getSetupName() {
        return setupName;
    }

    public String getFilePathDiagram() {
        return filePathDiagram;
    }

    public void setFilePathDiagram(String filePathDiagram) {
        this.filePathDiagram = filePathDiagram;
    }

    public String getSetupDescription() {
        return setupDescription;
    }

    public void setSetupDescription(String setupDescription) {
        this.setupDescription = setupDescription;
    }

    public String getFilepathPicture() {
        return filepathPicture;
    }

    public void setFilepathPicture(String filepathPicture) {
        this.filepathPicture = filepathPicture;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getDeviceLoginInfo() {
        return deviceLoginInfo;
    }

    public void setDeviceLoginInfo(String deviceLoginInfo) {
        this.deviceLoginInfo = deviceLoginInfo;
    }
}
