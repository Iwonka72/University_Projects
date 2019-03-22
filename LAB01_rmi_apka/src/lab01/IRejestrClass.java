package lab01;

import java.util.ArrayList;
import java.util.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import lab01.IMonitor;

public class IRejestrClass implements IRejestr {
	
	public List<IMonitor> monitory_list = new ArrayList<IMonitor>(); //lista NAMIASTEK monitorów 

	@Override
	public int register(IMonitor o) throws RemoteException {	//--------DONE
		System.out.println("IRejestrClass method REGISTER invoked - done");
		this.monitory_list.add(o);
		return 0;
	}
	
	@Override
	public boolean unregister(int id) throws RemoteException {	//---------
		// TODO Auto-generated method stub
		System.out.println("IRejestrClass method UNREGISTER invoked - done testing");
		for (int i = 0; i < this.monitory_list.size(); i++) {
			if (this.monitory_list.get(i).getId() == id) {
				this.monitory_list.remove(i);
				System.out.println("Usuniêto monitor na pozycji: "+i);
			}
		}
		return false;
	}

	@Override
	public List<IMonitor> getMonitors() throws RemoteException {	//-------DONE
		// TODO Auto-generated method stub
		System.out.println("IRejestrClass method GETMONITORS invoked - done");
		return monitory_list;
	}
	
	public static void main(String[] args) {//serwer czy klient?
		System.out.println("Uruchomiono IRejestr.");
		try {
            IRejestrClass obj = new IRejestrClass();//instancja rejestru
            IRejestr namiastkaRejestru = (IRejestr) UnicastRemoteObject.exportObject(obj, 2001);//tworzenie namiastki rejestru
            																// i wysy³anie jej do rejestru rmi (rejestrowanie)
            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.createRegistry(2000);//tworzenie rejestru rmi registry na znanym porcie 2000
            registry.bind("IRejestr", namiastkaRejestru);//to jest rejestrowanie w rejestrze. Czyli: wsadzam do rejestru jedn¹ namiastkê:
            											// namiastke IRejestru
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
		System.out.println("Server waiting.");
		//IMonitorClass mon = new IMonitorClass();
		//IRejestrClass irej = new IRejestrClass();
		//irej.monitory_list.add(mon);
		
	}

}