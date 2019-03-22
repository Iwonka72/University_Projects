import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MyClassLoader extends ClassLoader {
	
	private Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();
	//private List<Class> classes = new ArrayList<Class>();
	
	private String path;
	
	public MyClassLoader () {
	}
	
	/*public boolean removeFromClassLoader(String className) {
		System.out.println("	>ClassLoader: Usuwam...");
		Class result = (Class)classes.get(className);
		if(result != null) {
			System.out.println("	>Znaleziono element do usuni�cia!");
			Object o = classes.remove(className);
			System.out.println("	>Usuni�to obiekt: "+o);
			return true;
		}
		System.out.println("	>Nie znaleziono elementu do usuni�cia :/");
		return false;
	}*/
	
	/**
	 * This sample function for reading class implementations reads
	 * them from the local file system
	 */
	 private byte getClassImplFromDataBase(String className)[] {
		 System.out.println("Fetching the implementation of "+className);
		 byte result[];
		 try {
			 FileInputStream fi = 
					 new FileInputStream("A:\\Rzeczy\\STUDIA\\Semestr_6)Sz�sty\\JAVA\\ClassLoader_L2\\components\\"+className+".class");//--moje zmiany
			 System.out.println("	> wyj�tku nie wyrzuci�o tworzenie strumienia input.");
			 result = new byte[fi.available()];
			 System.out.println("	> wyj�tku nie wyrzuci�o przypisanie kodu bytowego do result");
			 fi.read(result);
			 System.out.println("	> wyj�tku nie wyrzuci�o czytanie ze strumienia input.");
			 return result;
			 
		 	/*InputStream stream = getClass().getClassLoader().getResourceAsStream(className);
	        int size = stream.available();
	        byte buff[] = new byte[size];
	        DataInputStream in = new DataInputStream(stream);
	        in.readFully(buff);
	        in.close();
	        return buff;*/
	        
		 } catch (Exception e) {
			 /*
			  * If we caught an exception, either the class wasnt found or it
			  * was unreadable by our process.
			  */
			 System.out.println("Nie uda�o si� znalez�, bo wylaz� exception.");
			 return null;
		 }
	 }
	 
	 
	 /**
	 * This is a simple version for external clients since they
	 * will always want the class resolved before it is returned
	 * to them.
	 */
	 public Class loadClass(String className) throws ClassNotFoundException {
		 return (loadClass(className, true));
	 }
	 
	 
	 /**
	 * This is the required version of loadClass which is called
	 * both from loadClass above and from the internal function
	 * FindClassFromClass.
	 */
	 public synchronized Class loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
		 Class result = null;
		 byte classData[];
		 System.out.println("Loading class : "+className+" in progress...");
		 
		 /* Check our local cache of classes */
		 // 1) znajdz klas� w liscie ju� za�adowanych klas
		 result = (Class)classes.get(className);
		 
		 //je�li znaleziono w �adowaczu wczesniej za�adowan� klas� o tej nazwie
		 if (result != null) {
			 	System.out.println("Zwracam juz wcze�niej za�adowan� tym �adowaczem klas�.");
			 	return result;
		 }
		 System.out.println("1) klasa nie znaleziona na liscie wcze�niej za�adowanych klas.");
		 
		 // 2) ka� �adowaczowi pierwotnemu za�adowa� j� z systemowych bibliotek
		 try {
			 	result = super.findSystemClass(className);//szukanie pierwotnym �adowaczem w bibliotekach javy
			 	System.out.println("returning system class (from CLASSPATH).");
			 	classes.put(className, result);
			 	return result;
		 } catch (ClassNotFoundException e) {
			 	System.out.println("It's not a system class.");
		 }
		 System.out.println("2) klasa nie znaleziona w�r�d klas systemowych.");
		 //je�li i to si� nie uda�o...
		 
		 // 3) za�aduj klas� z mojego folderu. Tylko czy to dzia�a? -nie
		 classData = getClassImplFromDataBase(className);
		 if (classData == null) {
				System.out.println("No dobra, koniec nadziei. Ta klasa nie istnieje.");
			 	throw new ClassNotFoundException();
		 }
		 
		 if(classData != null) {
			 System.out.println("Gratulacje, m�j classData kod bytowy jest pe�ny!");
			//tutaj nast�puje przetworzenie uzyskanego strumienia bajt�w w klas�.
			 //result = defineClass(classData, 0, classData.length);  <-- deprecated 
			 result = defineClass(className, classData, 0, classData.length);//NoClassDefFoundException
			 //"Before the Class can be used it must be resolved."
			 resolveClass(result); 
			 classes.put(className, result);
		 }
		 
		 
		 
		 System.out.println("Nic nie znaleziono - zwracam null.");
		 System.out.println("Result: "+result);
		 System.out.println("classData: "+classData);
		 return result;
		 //return classData;
	 }
	
	
	
	
}
