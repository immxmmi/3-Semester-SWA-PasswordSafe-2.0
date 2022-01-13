package utility;

public interface MasterKeyServices {
    void setNewMasterKey() throws Exception;

    boolean checkMasterKey(String masterPassword) throws Exception;
}
