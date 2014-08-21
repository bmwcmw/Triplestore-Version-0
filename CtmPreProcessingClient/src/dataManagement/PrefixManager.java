package dataManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>RDF prefix data manager which stores and manages prefix definitions in the file system</p>
 * 
 * @author Cedar
 *
 */
public class PrefixManager {

	private Map<String, String> _prefix = null;

	public PrefixManager(){
		_prefix = new Hashtable<String,String>();
	}

	public static PrefixManager loadFile(String filename){
		PrefixManager res = new PrefixManager();
		res.load(filename);
		return res;
	}

	
	public void addPrefix(String prefix, String uri){
		_prefix.put(prefix, uri);
	}

	/**
	 * <p>Gets the uri of a given prefix</p>
	 */
	public String getUri(String prefix){
		return _prefix.get(prefix);
	}

	public boolean contains(String prefix){
		return _prefix.containsKey(prefix);
	}
	
	/**
	 * <p>Creates a set of all prefix definitions</p>
	 */
	public Set<Map.Entry<String,String>> getAll(){
		return _prefix.entrySet();
	}
	
	/**
	 * <p>Gets the iterator of all prefix definitions</p>
	 */
	public Iterator<Map.Entry<String, String> > getIterator(){
		return _prefix.entrySet().iterator();
	}

	/**
	 * <p>Saves prefix definitions to a specified file</p>
	 */
	public void save(String filename){
		File file = new File(filename);  
		FileOutputStream f;
		ObjectOutputStream s;
		try {
			f = new FileOutputStream(file);
			s = new ObjectOutputStream(f);
			s.writeObject(_prefix);
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}

	/**
	 * <p>Prints prefix definitions</p>
	 */
	public void print(){
		if(_prefix.isEmpty()){
			System.out.println("No namespace");
		}
		else {
			for(Map.Entry<String, String> entry : _prefix.entrySet()){
				System.out.println(entry.getKey() + " " + entry.getValue());
			}
		}
	}
	
	/**
	 * <p>Loads prefix definitions from a specified file</p>
	 */
	@SuppressWarnings("unchecked")
	public void load(String filename){
		File file = new File(filename);
		if(file.exists()){
			FileInputStream f;
			ObjectInputStream s;
			try {
				f = new FileInputStream(file);
				s = new ObjectInputStream(f);
				_prefix = (Hashtable<String, String>) s.readObject();
				s.close();
			}catch (FileNotFoundException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
