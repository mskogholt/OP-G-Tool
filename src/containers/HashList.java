/**
 * 
 */
package containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import interfaces.Keyable;

/**
 * @author martin.skogholt
 *
 */
public class HashList<E extends Keyable> {

	private ArrayList<E> list;
	private HashSet<String> keys;
	
	public HashList() {
		list = new ArrayList<E>();
		keys = new HashSet<String>();
	}

	public void add(E e) {
		if(!keys.contains(e.getKey())) {
			list.add(e);
			keys.add(e.getKey());
		}
	}

	public void addAll(HashList<E> list) {
		for(int i=0; i<list.size(); i++) {
			this.add(list.get(i));
		}
	}
	
	public void addAll(Collection<? extends E> c) {
		for(E e : c) {
			add(e);
		}
	}

	public void clear() {
		this.list.clear();
		this.keys.clear();
	}

	public boolean contains(E e) {
		return keys.contains(e.getKey());
	}

	public E get(int index) {
		return list.get(index);
	}

	public void remove(E e) {
		list.remove(e);
		keys.remove(e.getKey());
	}

	public void remove(int index) {
		E e = list.remove(index);
		keys.remove(e.getKey());
	}

	public int size() {
		return list.size();
	}

	public HashList<E> subList(int fromIndex, int toIndex) {
		HashList<E> otherList = new HashList<E>();
		for(int i=fromIndex; i<toIndex; i++) {
			otherList.add(this.get(i));
		}
		return otherList;
	}
}
