package nl.vu.labs.phoenix.ap;

public class LinkedList<E extends Comparable<E>> implements ListInterface<E> {
	
	private Node currentNode;
	private Node firstElement;
	private Node lastElement;
	private int numbOfElements;
	
	public LinkedList() {
		this.init();
	}

    private class Node {

        E data;
        Node prior, next;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node prior, Node next) {
            this.data = data == null ? null : data;
            this.prior = prior;
            this.next = next;
        }
    }
    
    @Override
    public ListInterface<E> init() { 
    	currentNode = null;
    	firstElement = null;
    	lastElement = null;
    	numbOfElements = 0;
        return this;
    }
    
    @Override
    public int size() { 
        return numbOfElements;
    }

    @Override
    public boolean isEmpty() { 
        return numbOfElements == 0;
    }

    @Override
    public ListInterface<E> insert(E element) {
    	
    	if(this.isEmpty() ) { 
    		currentNode = lastElement = firstElement = new Node(element);
    	} else if(firstElement.data.compareTo(element) >= 0 ) {
    		currentNode = firstElement = firstElement.prior = new Node(element, null, firstElement);
    	} else if(lastElement.data.compareTo(element) <= 0) { 
    		currentNode = lastElement = lastElement.next = new Node(element, lastElement, null);
    	} else if(firstElement.data.compareTo(element) < 0 && lastElement.data.compareTo(element) > 0) { 
    		if(currentNode.data.compareTo(element) < 0) { 
    			while(currentNode.next.data.compareTo(element) < 0) {
    				currentNode = currentNode.next;
    			}
    			currentNode.next = currentNode.next.prior = new Node(element, currentNode, currentNode.next);
    		} else if(currentNode.data.compareTo(element) > 0 ) { 
    			while(currentNode.prior.data.compareTo(element) > 0 ) {
    				currentNode = currentNode.prior;
    			}
    			currentNode.prior = currentNode.prior.next = new Node(element, currentNode.prior, currentNode);  				
    		}
    	}
    	numbOfElements += 1;
        return this;
    }

    @Override
    public E retrieve() { 
        return currentNode.data;
    }

    @Override
    public ListInterface<E> remove() {
    	if(this.isEmpty()) { 
    		init();
    	} else if(numbOfElements == 1) { 
    		firstElement = lastElement = currentNode = null;
    	} else if(currentNode == firstElement) { 
    		currentNode = firstElement = currentNode.next;
    		currentNode.prior = null;
    	} else if(currentNode == lastElement) { 
    		currentNode = lastElement = currentNode.prior;
    		currentNode.next = null;
    	} else {								
    		currentNode.prior.next = currentNode.next; 
    		currentNode.next.prior = currentNode.prior; 
    		currentNode = currentNode.next; 	
    	}
        numbOfElements -= 1;
        return this;
    }

    @Override
    public boolean find(E element) {  	
    	if(this.isEmpty()) { //best case
    		currentNode = null;
    		return false; 
    	} else if(currentNode.data.compareTo(element) == 0) { //best case
    		while(currentNode.prior != null && currentNode.prior.data.compareTo(element) == 0 ) {
    			currentNode = currentNode.prior;
    		}
    		return true;
    	} else if(currentNode.data.compareTo(element) > 0) { 
    		while(currentNode.prior != null && currentNode.data.compareTo(element) > 0) {
    				while(currentNode.prior != null && currentNode.prior.data.compareTo(element) == 0) {
    					currentNode = currentNode.prior; //point to first node that contains 'element'
    				}
    				if(currentNode.data.compareTo(element) == 0) {
    					return true;
    				}
    			currentNode = currentNode.prior;
    		}
    		currentNode = firstElement;
    		return false;
    	} else if(currentNode.data.compareTo(element) < 0) { //right 
    		while(currentNode.next != null && currentNode.data.compareTo(element) < 0) {
    			currentNode = currentNode.next;
    			if(currentNode.data.compareTo(element) == 0) {
    				return true;
    			}
    		}
    		currentNode = lastElement;
    		return false;	
    	}
        return false;
    }

    @Override
    public boolean goToFirst() { 
    	if(this.isEmpty()) {
    		return false;
    	} else {
    		currentNode = firstElement;
    		return true;
    	}
    }

    @Override
    public boolean goToLast() { 
    	if(this.isEmpty()) {
    		return false;
    	} else {
    		currentNode = lastElement;
    		return true;
    	}
    }

    @Override
    public boolean goToNext() { 
    	if(this.isEmpty() || currentNode.next == null) {
    		return false;
    	} else {
    		currentNode = currentNode.next;
    		return true;
    	}
    }

    @Override
    public boolean goToPrevious() { 
    	if(this.isEmpty() || currentNode.prior == null) {
    		return false;
    	} else {
    		currentNode = currentNode.prior;
    		return true;
    	}
    }

    @Override
    public ListInterface<E> copy() {
    	ListInterface<E> copyLinkedList = new LinkedList<E>();
    	this.goToFirst();
    	
    	while(currentNode.next != null) {
    		copyLinkedList.insert(this.retrieve());
    		this.goToNext();
    	}
    	copyLinkedList.insert(this.retrieve());
        return copyLinkedList;
    }
}