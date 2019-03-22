package lab01;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class IMonitorClass extends JFrame implements IMonitor, Runnable, ActionListener {
	
	int id;
	ISensor sensor;
	
	public IMonitorClass() {
		super("Monitor - brak id");
		id = 1;
		this.sensor = null;
	}
	
	JLabel sensorLabel = new JLabel();//potem set text!!!!
	JButton startButton = new JButton("Start");
	JButton stopButton = new JButton("Stop");
	JTextField textBox = new JTextField();
	
	public IMonitorClass(int i) {
		super("Monitor - ID:"+i);
		id = i;
		this.sensor = null;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(550, 400);
		
		GridLayout layout = new GridLayout(4, 1, 20, 20);
		JPanel panel = new JPanel();
		panel.setSize(350, 100);
		panel.setLayout(layout);
		
		panel.setBorder(new EmptyBorder(15, 15, 15, 15));
		
		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		sensorLabel.setText("Nie po³¹czono z sensorem.");
		
		panel.add(sensorLabel);
		panel.add(textBox);
		panel.add(startButton);
		panel.add(stopButton);
		
		this.add(panel);
		setVisible(true);
	}
	
	@Override
	public String getNameMy() throws RemoteException {
		int idk = -1;
		try {
			if (sensor!=null) {
				idk = sensor.getId();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(idk == -1) {
			return "Monitor: "+id+" --- WOLNY";
		}
		return "Monitor: "+id+" skojarzony z sensorem: "+idk;
	}

	@Override
	public void setReadings(String readings) throws RemoteException {	//-------DONE
		// TODO Auto-generated method stub
		//System.out.println(readings+" - done");
		this.textBox.setText(readings);
	}

	@Override
	public void setInput(ISensor o) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("IMonitorClass method: SETINPUT invoked - done testing");
		this.sensor = o;
		sensorLabel.setText("Po³¹czono z sensorem: "+o.getId());
	}

	@Override
	public int getId() throws RemoteException {	//-------DONE
		System.out.println("IMonitorClass method: GETID invoked - done");
		return id;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object e = event.getSource();
		if(e==startButton) {
			try {
				if(sensor != null)
					this.sensor.start();
			} catch (RemoteException | NullPointerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e==stopButton) {
			try {
				if(sensor != null)
					this.sensor.stop();
			} catch (RemoteException | NullPointerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		System.out.println("Uruchomiono Monitor.");
		try {

            // Bind the remote object's stub in the registry <--- nie! nie bind
            Registry registry = LocateRegistry.getRegistry(2000);//pobranie utworzonego wczeœniej rejestru rmi
            IRejestr ir = (IRejestr)registry.lookup("IRejestr");//pobranie namiastki Irejestru z RMIRegistry
            
            //teraz muszê znalezc wolne id w liscie monitorow
            int ostatni = 1;
            for (int i = 0; i < ir.getMonitors().size(); i++) {//---------To od tej pêtli jest tyle wywo³añ getMonitors w IRejestrze
            	if (ir.getMonitors().get(i).getId() >= ostatni) {
            		ostatni = ir.getMonitors().get(i).getId()+1;
            	}
            }
            //monit.id = ostatni;
            IMonitorClass monit = new IMonitorClass(ostatni);//tworzenie instancji monitora
            System.out.println("Id stworzonego monitora: " + monit.id + ".");
            
            IMonitor monitnam = (IMonitor) UnicastRemoteObject.exportObject(monit, 0);//tworzenie namiastki monitora
            ir.register(monitnam);//zarejestrowanie mojej namiastki monitora w rejestrze klasy IRejestr

            System.out.println("Server waiting.");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
		
	}
	
}
