package lab01;

import java.rmi.RemoteException;
import java.util.concurrent.TimeUnit;

public class ISensorClass implements ISensor{
	
	int id;
	//int input;
	int output;
	boolean running;
	IMonitor monitor;
	
	String readings[] = {"Kotek", "Piesek", "Kura", "¯uk", "Bóbr", "£osoœ", "Szop"};
	
	Thread thread;/* = new Thread(){
	    public void run(){
	    	int i = 0;
	    	do {
	    		try {
					monitor.setReadings(readings[i]);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		i++;
	    		if(i>6) {
	    			i=0;
	    		}
	    	}while(running);
	    }
	  };*/
	
	public ISensorClass (int id, int output, int input) {
		this.id = id;
		this.output = output;
		this.running = false;
		this.monitor = null;
	}
	
	public ISensorClass (int id) {
		this.id = id;
		this.output = -1;
		this.running = false;
		this.monitor = null;
	}
	
	@Override
	public String getNameMy() throws RemoteException {
		if (this.output == -1) {
			return "Sensor: "+id+" --- WOLNY";
		}
		return "Sensor: "+id+" skojarzony z monitorem: "+output;
	}

	@Override
	public void start() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("ISensorClass method: START invoked - done testing");
		running = true;
		thread  = new Thread(){
		    public void run(){
		    	int i = 0;
		    	do {
		    		try {
						monitor.setReadings(readings[i]);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    		try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    		i++;
		    		if(i>6) {
		    			i=0;
		    		}
		    	}while(running);
		    }
		  };
		thread.start();
	}

	@Override
	public void stop() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("ISensorClass method: STOP invoked - done testing");
		running = false;
	}

	@Override
	public int getId() throws RemoteException {	//--------DONE
		// TODO Auto-generated method stub
		// System.out.println("ISensorClass method: GETID invoked");
		return id;
	}

	@Override
	public void setOutput(IMonitor o) throws RemoteException {	//--------done testing
		// TODO Auto-generated method stub
		this.monitor = o;
	}

}
