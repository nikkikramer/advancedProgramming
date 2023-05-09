package nl.vu.labs.phoenix.ap;

public class Set<T extends Comparable<T>> implements SetInterface<T> {
	private ListInterface<T> linkedList;
	
	public Set() { //default constructor
		this.init();
	}
	
	public SetInterface<T> init() { 
		linkedList = new LinkedList<T>();
		return this;
	}
	
	public boolean isEmpty() {
		return linkedList.isEmpty();
	}
	
	public boolean isInSet(T element) {
		return linkedList.find(element);
	}
	@Override
	public boolean add(T element) { 
		if(this.isInSet(element)) {
			return false;
		} 
		linkedList.insert(element);
		return true;
	}

	@Override
	public T get() { 
		return linkedList.retrieve();
	}

	@Override
	public boolean remove(T element) {
		if(this.isInSet(element)) {
			linkedList.remove();
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return linkedList.size();
	}

	@Override
	public SetInterface<T> copy() { 
		Set<T> copySet = new Set<T>();
		if(this.isEmpty()) {
			return copySet;
		}
		copySet.linkedList = this.linkedList.copy();
		return copySet;
	}
	
	public SetInterface<T> difference(SetInterface<T> set2) {
		SetInterface<T> differenceSet = this.copy(); 
		if(set2.isEmpty()) { //best case
			return differenceSet;
		}
		if(isEmpty()) { //best case
			return differenceSet;
		}
		linkedList.goToFirst();
		if(set2.isInSet(this.get())) {
			differenceSet.remove(this.get());
		}
		while(linkedList.goToNext()) {
			if(set2.isInSet(this.get())) {
				differenceSet.remove(this.get());
			}
		}	
		return differenceSet;
	}
	
	public SetInterface<T> union(SetInterface<T> set2) { 
		if(isEmpty()) { //best case
			return set2;
		}
		if(set2.isEmpty()) { //best case
			return this;
		}
		SetInterface<T> unionSet = set2.copy(); 
		this.linkedList.goToFirst(); 
		
		unionSet.add(this.get());
		
		while(this.linkedList.goToNext()) {
			unionSet.add(this.get());
		}
		return unionSet;
	}
	
	public SetInterface<T> intersection(SetInterface<T> set2) { 
		SetInterface<T> intersectionSet = new Set<T>();
		if (set2.isEmpty()) { //best case
			return intersectionSet;
		}
		if(isEmpty()) { //best case
			return intersectionSet;
		}
		this.linkedList.goToFirst();
		if(set2.isInSet(this.get())) {
			intersectionSet.add(this.get());
		}
		while(this.linkedList.goToNext()) {
			if(set2.isInSet(this.get())) {
				intersectionSet.add(this.get());
			}	
		}
		return intersectionSet;
	}
	
	public SetInterface<T> symmetricDifference(SetInterface<T> set2) { 
		SetInterface<T> unionSet = this.union(set2);
		SetInterface<T> intersectionSet = this.intersection(set2);
		return unionSet.difference(intersectionSet);
	}
	
	public boolean equalSets(SetInterface<T> set2) { 
		this.linkedList.goToFirst();
		if(set2.isInSet(this.get())) {
			//do nothing
		} else {
			return false;
		}
		while(this.linkedList.goToNext()) {
			if(set2.isInSet(this.get())) {
				//do nothing
			} else {
				return false;
			}
		}
		SetInterface<T> copySet1 = this.copy();
		SetInterface<T> diffSet = set2.difference(copySet1);
		if(diffSet.isEmpty()) {
			return true;
		} else {
			return false;
		}	
	} 
	public boolean smallerSet(SetInterface<T> set2) {
		SetInterface<T> intersectionSet = this.intersection(set2);
		if(intersectionSet.equalSets(this)) {
			return true;
		}
		return false;
	}
	
	public boolean greaterSet(SetInterface<T> set2) { 
		SetInterface<T> intersectionSet = this.intersection(set2);
		if(intersectionSet.equalSets(set2)) {
			return true;
		} 
		return false;
	}

}
