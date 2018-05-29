public class ServerModel {
    private String ip;
    private int port;
    private long addedTime;

    public long getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(long addedTime) {
        this.addedTime = addedTime;
    }

    public String getIp() {
        return ip;
    }

    public ServerModel(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public ServerModel(){}
    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}