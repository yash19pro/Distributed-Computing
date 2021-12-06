import java.rmi.*;

public interface checkBal extends Remote {
    public double checkBalance(String acc_no, String password) throws RemoteException;
    public void AddUser(String acc_no, String password, double balance) throws RemoteException;
    public boolean transfer(String d_acc_no, String cred_acc_no, String password, double amt) throws RemoteException;
    public void receiveRequest(int i, int n) throws RemoteException;
    public boolean AddBalance(String d_acc_no, String password, double amt) throws RemoteException;
    public boolean RemoveBalance(String d_acc_no, String password, double amt) throws RemoteException;
}