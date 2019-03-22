package lab01;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMonitor extends Remote{
 public void setReadings(String readings) throws RemoteException;
 public void setInput(ISensor o) throws RemoteException;
 public int getId() throws RemoteException;
 public String getNameMy() throws RemoteException;
}