package guacaloco.core;

import guacaloco.core.utils.VMwareConnection;

public class VmwareManagerConnection {

    private static VmwareManagerConnection uniqInstance = null;

    private static VMwareConnection connection = null;
    private String serverName = null;
    private String userName = null;
    private String password = null;
    private Boolean isConnected = false;

    private VmwareManagerConnection() {
    }

    public static synchronized VmwareManagerConnection getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new VmwareManagerConnection();
        }
        return uniqInstance;
    }

    public void init(String serverName, String userName, String password)
            throws VsphereToolkitException {
        if (connection == null) {
            try {
                connection = new VMwareConnection(serverName, userName, password);
                this.serverName = serverName;
                this.userName = userName;
                this.password = password;
                this.isConnected = true;
            } catch (Exception e) {
                e.printStackTrace();
                throw new VsphereToolkitException("Unable to establish connection! Please verify your credentials", e);
            }
        }
    }

    public VMwareConnection getConnection() {
        return connection;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isConnected() {
        return isConnected;
    }

    protected void setConnected(Boolean isConnected) {
        this.isConnected = isConnected;
    }

}
