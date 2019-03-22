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
			System.out.println("	>Znaleziono element do usuniêcia!");
			Object o = classes.remove(className);
			System.out.println("	>Usuniêto obiekt: "+o);
			return true;
		}
		System.out.println("	>Nie znaleziono elementu do usuniêcia :/");
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
					 new FileInputStream("A:\\Rzeczy\\STUDIA\\Semestr_6)Szósty\\JAVA\\ClassLoader_L2\\components\\"+className+".class");//--moje zmiany
			 System.out.println("	> wyj¹tku nie wyrzuci³o tworzenie strumienia input.");
			 result = new byte[fi.available()];
			 System.out.println("	> wyj¹tku nie wyrzuci³o przypisanie kodu bytowego do result");
			 fi.read(result);
			 System.out.println("	> wyj¹tku nie wyrzuci³o czytanie ze strumienia input.");
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
			 System.out.println("Nie uda³o siê znalezæ, bo wylaz³ exception.");
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
		 // 1) znajdz klasê w liscie ju¿ za³adowanych klas
		 result = (Class)classes.get(className);
		 
		 //jeœli znaleziono w ³adowaczu wczesniej za³adowan¹ klasê o tej nazwie
		 if (result != null) {
			 	System.out.println("Zwracam juz wczeœniej za³adowan¹ tym ³adowaczem klasê.");
			 	return result;
		 }
		 System.out.println("1) klasa nie znaleziona na liscie wczeœniej za³adowanych klas.");
		 
		 // 2) ka¿ ³adowaczowi pierwotnemu za³adowaæ j¹ z systemowych bibliotek
		 try {
			 	result = super.findSystemClass(className);//szukanie pierwotnym ³adowaczem w bibliotekach javy
			 	System.out.println("returning system class (from CLASSPATH).");
			 	classes.put(className, result);
			 	return result;
		 } catch (ClassNotFoundException e) {
			 	System.out.println("It's not a system class.");
		 }
		 System.out.println("2) klasa nie znaleziona wœród klas systemowych.");
		 //jeœli i to siê nie uda³o...
		 
		 // 3) za³aduj klasê z mojego folderu. Tylko czy to dzia³a? -nie
		 classData = getClassImplFromDataBase(className);
		 if (classData == null) {
				System.out.println("No dobra, koniec nadziei. Ta klasa nie istnieje.");
			 	throw new ClassNotFoundException();
		 }
		 
		 if(classData != null) {
			 System.out.println("Gratulacje, mój classData kod bytowy jest pe³ny!");
			//tutaj nastêpuje przetworzenie uzyskanego strumienia bajtów w klasê.
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
