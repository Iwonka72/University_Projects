import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainWindow extends JFrame implements Runnable, ActionListener{

	private static final long serialVersionUID = 1L;
	int iterator_opcji;//to bedzie do usuniêcia jak zrobie te klasy
	
	int window_capacity;
	
	JPanel componentPanel = new JPanel();
	JMenuBar menubar = new JMenuBar();
	JButton optionButton = new JButton("Opcje");
	
	JButton addButton = new JButton("Add");
	JButton removeButton = new JButton("Remove");
	JButton useButton = new JButton("Use");
	
	JList<String> list;
	DefaultListModel<String> model = new DefaultListModel<String>();
	List<JComponent> componentList;
	
	List<ClassLoader> loaders;
	//private Hashtable<String, ClassLoader> loaders = new Hashtable<String, ClassLoader>();
	
	
	public MainWindow() {
		super("ClassLoader - okno g³ówne");
		window_capacity = 6;
		iterator_opcji = 0;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//------------------------------------- main window
		//this.setSize(700, 550);
		this.setSize(550, 650);
		this.setLayout(null);
		setLocation(500,350);
		
		//------------------------------------- panel sterowania
		Color color = new Color(90, 111, 34);
		JPanel panel = new JPanel();
		//panel.setSize(550, 150);
		//panel.setBounds(0, 0, 700, 160);
		panel.setBounds(0, 0, 550, 160);
		panel.setBackground(color);
		panel.setLayout(null);
			optionButton.addActionListener(this);
			addButton.addActionListener(this);
			removeButton.addActionListener(this);
			useButton.addActionListener(this);
	    list = new JList<String>(model);
	    componentList = new ArrayList<JComponent>();
	    loaders = new ArrayList<ClassLoader>();
	    //model.addElement("---");
		menubar.add(optionButton);
		this.setJMenuBar(menubar);
			panel.add(addButton);
			panel.add(removeButton);
			panel.add(useButton);
			panel.add(list);
		list.setBounds(10, 10, 130, 120);
		addButton.setBounds(150, 10, 90, 30);
		removeButton.setBounds(150, 50, 90, 30);
		useButton.setBounds(150, 90, 90, 30);
		
		//------------------------------------- panel ³adowanych komponentów
		Color componentColor = new Color(194, 226, 106);
		componentPanel.setLayout(new GridLayout(3,2));
		//componentPanel.setSize(200, 200);
		componentPanel.setBounds(0, 160, 550, 420);
		componentPanel.setBackground(componentColor);
			
		this.add(panel);
		this.add(componentPanel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == optionButton) {
			JOptionPane.showMessageDialog(null, "Podawanie œcie¿ki do katalogu startowego\n"+
												"wyszukiwania klas do ³adowania. - œcie¿ka jest na sta³e w kodzie");
		}
		if (event.getSource() == addButton) {
			
			FileDialog file_dialog = new FileDialog(this, "Wybierz komponent", FileDialog.LOAD);
			file_dialog.setDirectory("A:\\Rzeczy\\STUDIA\\Semestr_6)Szósty\\JAVA\\ClassLoader_L2\\components");
			file_dialog.setVisible(true);
			
			Class c = null;
			MyClassLoader loader = null;
			
			try {
				String name = file_dialog.getFile().replace(".class", "");
				//if(loaders.containsKey(name)) {
				//	System.out.println("Za³adowano stary ³adowacz klas.");
				//	loader = (MyClassLoader) loaders.get(name);//jeœli ju¿ ta klasa by³a ³adowana, u¿yj starego ³adowacza
				//}else {
					System.out.println("Stworzono nowy ³adowacz klas.");
					loader = new MyClassLoader();//jeœli nie, zrob nowego ³adowacza
					//loaders.put(""+name+componentList.size(), loader);
					if(loaders.add(loader)) {
						System.out.println(" *** Collection changed");
					}
					System.out.println("Zapisano ³adowacz: " + name+loaders.size() + " // " + loader);
				//}
				c = loader.loadClass(name);
				//Class c = loader.loadClass("Component1");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Nazwa mojej klasy: " + c.getName() );

			try {
				Constructor[] theConstructors = c.getConstructors();
				
				//teraz musze znalezc i wywo³aæ konstruktor mojej instancji
				//a potem dodaæ to, co nim stworzê, do mojej listy komponentów
				
				Class[] intArgsClass = new Class[] {int.class, int.class, int.class}; 
				Constructor constructor = c.getConstructor(intArgsClass);
				System.out.println("Konstruktor znaleziono: "+constructor);
				Integer x = new Integer(200);
				Integer y = new Integer(200);
				Integer diameter = new Integer(30);
				Object[] intArgs = new Object[] {x, y, diameter};
				
				componentList.add((JComponent) constructor.newInstance(intArgs));
				
			} catch (InstantiationException | IllegalAccessException | SecurityException | NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			model.addElement(""+c.getName());//dodanie komponentu do listy wyœwietlanej
			System.out.println("Dodano do listy: " + componentList.get(0));
			
			componentPanel.repaint();
			this.setVisible(true);
			
			//model.addElement("Komponent: "+name);
			//iterator_opcji++;
		}
		//}
		if (event.getSource() == removeButton) {
			String className = model.getElementAt(list.getSelectedIndex());//tutaj jest klucz hashtable mojego ³adowacza
			int index = list.getSelectedIndex();
			//Musze usun¹æ wszystkie instancje tej klasy
			System.out.println("Usuwam z okna: " + className);
			System.out.println("	>rozmiar loaders: " + loaders.size());
			System.out.println("	>wskaznik list: " + list.getSelectedIndex());
			//Object toRemove = componentList.get(list.getSelectedIndex());//zapisany obiekt do usuniêcia
			/*while(componentList.contains(toRemove)) {//usuwam wszystkie takie obiekty - nie, nie usuwam. 
													//Bo taki obiekt jest tylko jeden
				System.out.println("	>Znaleziono szukany obiekt: " + className);
				model.removeElementAt(componentList.indexOf(toRemove));
				componentList.remove(toRemove);
			}*/
			componentPanel.remove(componentList.get(list.getSelectedIndex()));
			try {
				System.out.println("Usuwam z list: " +list.getSelectedIndex());
				componentList.remove(list.getSelectedIndex());
				model.removeElementAt(list.getSelectedIndex());
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Nie zaznaczono elementu! (Lub inny b³¹d)");
			}
			
			try {
				//na koñcu usuwam ³adowacza tej klasy
				if(loaders.remove(index) != null) {
					System.out.println("Usuniêto z sukcesem ³adowacza tej klasy!");
				}
				
			}catch(Exception e) {
				//JOptionPane.showMessageDialog(null, "Element nie by³ wczeœniej dodany do ramki!");
				e.printStackTrace();
			}
			
			componentPanel.repaint();
			this.revalidate();
			this.setVisible(true);
		}
		if (event.getSource() == useButton) {
			//JOptionPane.showMessageDialog(null, "U¿ywanie zaznaczonej w liœcie klas klasy\n"+
			//									"na panelu. Dodanie jej instancji w oknie.");
			
			componentPanel.add(componentList.get(list.getSelectedIndex()));
			componentPanel.repaint();
			this.setVisible(true);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
