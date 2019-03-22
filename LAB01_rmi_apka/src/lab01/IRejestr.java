package lab01;

import java.util.List;
import java.rmi.Remote;
import java.rmi.RemoteException;
import lab01.IMonitor;

public interface IRejestr extends Remote{
 public int register(IMonitor o) throws RemoteException; 
 public boolean unregister(int id) throws RemoteException;
 public List<IMonitor> getMonitors() throws RemoteException; //dodac tutaj IMonitor do listy
}