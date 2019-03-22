package lab01;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

//import lab01.IMonitorClass;
import lab01.ISensorClass;

public class ManagerClass extends JFrame implements Runnable, ActionListener {

	public List<ISensor> sensorList = new ArrayList<ISensor>();//lista INSTANCJI sensor�w
	public List<IMonitor> monitorList;//lista NAMIASTEK monitor�w
	int startingPort = 1997;
	IRejestr rej;
	
	int licznik_id;

	void getMonitors() {	//-------DONE
		// Bind the remote object's stub in the registry
		Registry registry;//utworzenie rejestru RMIregistry
		try {
			registry = LocateRegistry.getRegistry(2000);//get ju� utworzonego we wcze�niej uruchomionym IRejestrClass
														//rejestru na znanym porcie
			// registry.bind("IRejestr", namiastkaSensora);
			rej = (IRejestr) registry.lookup("IRejestr");//u�ywam namiastki rejestru RMI utworzonego w IRejestrClass
														// do pobrania namiastki IRejestru
			monitorList = rej.getMonitors();//u�ywam otrzymanej namiastki IRejestru do wywo�ania jego metody
			System.out.println("Pobrano liste monitor�w");
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//refresh monitor list model
		this.model_monitory.clear();
		for(int n = 0; n < this.monitorList.size(); n++) {
			//System.out.println(this.monitorList.get(n).getName());
			try {
				model_monitory.addElement(this.monitorList.get(n).getNameMy());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void createNewSensor() {		//---------DONE
		ISensorClass new_sensor = new ISensorClass(this.licznik_id);//do listy sensor�w dodaj� INSTANCJE nie namiastki
		this.sensorList.add(new_sensor);
		try {
			model_sensory.addElement(new_sensor.getNameMy());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Dodano nowy sensor.");
		this.licznik_id++;
	}
	
	public void showMonitors() {			//--------DONE
		System.out.println("Wy�wietlam monitory:");
		for (int i = 0; i<this.monitorList.size(); i++){
			try {
				System.out.println(this.monitorList.get(i).getId());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Koniec monitor�w.");
	}
	
	public void showSensors() {		//--------DONE
		System.out.println("Wy�wietlam sensory:");
		for (int i = 0; i<this.sensorList.size(); i++){
			try {
				System.out.println(this.sensorList.get(i).getId());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Koniec sensor�w.");
	}
	
	JLabel sensoryLabel = new JLabel("Sensory:");
	JLabel monitoryLabel = new JLabel("Monitory:");
	JButton addButton = new JButton("Add sensor");
	JButton bindButton = new JButton("Bind");
	JButton refreshButton = new JButton("Refresh");
	
	JList<String> list_sensory;
	DefaultListModel<String> model_sensory = new DefaultListModel<String>();
	JScrollPane scrollSensory;
	JList<String> list_monitory;
	DefaultListModel<String> model_monitory = new DefaultListModel<String>();
	JScrollPane scrollMonitory;
	
	public ManagerClass() {
		super("Manager monitor�w i sensor�w");
		
		this.getMonitors();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(550, 400);

		licznik_id = 1;
		
		GridLayout layout = new GridLayout(4, 2, 20, 20);
		JPanel panel = new JPanel();
		panel.setSize(550, 150);
		panel.setLayout(layout);
		
		panel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		this.addButton.addActionListener(this);
		this.bindButton.addActionListener(this);
		this.refreshButton.addActionListener(this);
		
		list_sensory = new JList<String>(model_sensory);
		list_monitory = new JList<String>(model_monitory);
		
		scrollSensory = new JScrollPane(list_sensory);
		scrollMonitory = new JScrollPane(list_monitory);
		
		panel.add(sensoryLabel);
		panel.add(monitoryLabel);
		panel.add(scrollSensory);
		panel.add(scrollMonitory);
		panel.add(addButton);
		panel.add(bindButton);
		panel.add(refreshButton);
		
		this.add(panel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object e = event.getSource();
		if(e == addButton) {// dodaj sensor
			this.createNewSensor();
		}
		if(e == refreshButton) {//od�wie� liste monitor�w
			this.getMonitors();
		}
		if(e == bindButton) {
			//pobieranie id sensorai id monitora do po��czenia. 
			//Wygl�da zle, ale �adna magia sie tu nie dzieje
			int sens = -1;//id
			int mon = -1;
			int t = 0;
			int index = 0;
			String temp;
			
			IMonitor namiastkaMon = null;
			
			temp = JOptionPane.showInputDialog("Podaj id sensora:");
			t = Integer.parseInt(temp);
			if (Integer.parseInt(temp) < licznik_id) {
				for(int i = 0; i< this.sensorList.size(); i++) {
					try {
						if(t == this.sensorList.get(i).getId()) {
							sens = t;//id
							index = i;//index
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			temp = JOptionPane.showInputDialog("Podaj id monitora:");
			try {
				t = Integer.parseInt(temp);
			}catch(Exception exc) {
				JOptionPane.showMessageDialog(this, "Wprowadzono z�e warto�ci!");
			}
			for(int i = 0; i< this.monitorList.size(); i++) {
				try {
					if(t == this.monitorList.get(i).getId()) {
						mon = t;//id
						namiastkaMon = this.monitorList.get(i);
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//sprawdzenie czy podano dobre dane do bindowania
			if ((sens <=0) || (mon <=0)) {
				JOptionPane.showMessageDialog(this, "Podano nieistniej�ce identyfikatory!");
			}else {
				JOptionPane.showMessageDialog(this, "Podano sensor: "+sens+"\nPodano monitor: "+mon+"\nBinduje...");
				
				//musz� stworzy� namiastk� ISensora! nareszcie namiastka isensora...
				//do czego� si� jednak przyda
				try {
					ISensor sensnam = (ISensor) UnicastRemoteObject.exportObject(this.sensorList.get(index), 0);//namiastka sensora
					namiastkaMon.setInput(sensnam);//wysy�am namiastk� sensora do monitora poprzez wywo�anie metody namiastki
					this.sensorList.get(index).setOutput(namiastkaMon);//wysy�am namiastk� monitora do sensora
					
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//tworzenie namiastki monitora
				
			}
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		System.out.println("Uruchomiono Manager.");
		ManagerClass mng = new ManagerClass();
		mng.createNewSensor();//usuni�cie static-�w.
		mng.createNewSensor();
		mng.createNewSensor();
		mng.createNewSensor();
		mng.showSensors();
		//mng.getMonitors(); /////////////
		mng.showMonitors();
		try {
			mng.monitorList.get(0).getId();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
