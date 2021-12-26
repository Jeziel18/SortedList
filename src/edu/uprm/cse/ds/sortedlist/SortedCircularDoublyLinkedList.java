package edu.uprm.cse.ds.sortedlist;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class SortedCircularDoublyLinkedList<E extends Comparable<E>> implements SortedList<E> {

    private static class Node<E>{
        private E element;
        private Node<E> next;
        private Node<E> prev;


        private Node(E element, Node<E> next, Node<E> prev) {  //Constructor to create the Node
            this.element = element;
            this.next = next;
            this.prev = prev;
        }

        public void setNext(Node<E> next) {  //setter for Next
            this.next = next;
        }

        public Node<E> getNext() {  //getter for Next
            return next;
        }

        public void setPrev(Node<E> prev) {  //setter for previous
            this.prev = prev;
        }

        public Node<E> getPrev() {   //getter for previous
            return prev;
        }

        public void setElement(E element) {   //setter for element
            this.element = element;
        }

        public E getElement() {   //getter for element
            return element;
        }


        public Node(){
            this.element = null;
            this.next = null;
            this.prev = null;
        }

    }

    private Node<E> header;
    private int currentSize;

    public SortedCircularDoublyLinkedList() { //creating the circular list
        this.currentSize = 0;
        this.header = new Node<>();  // creating header

        this.header.setNext(this.header);
        this.header.setPrev(this.header);    //Making the list circular


    }



    @Override
    public boolean add(E obj) {
        if(obj == null){   //checking if the object is a valid entry
            return false;
        }

        if(this.isEmpty()){  // if empty, put in the first node
            Node<E> nNode = new Node<E>();  //Creating new node
            nNode.setElement(obj);
            this.header.setNext(nNode);
            this.header.setPrev(nNode);
            nNode.setNext(this.header);
            nNode.setPrev(this.header);
            currentSize++;
            return true;
        }

        else{
            Node temp = null;
            for(temp = this.header.getNext(); temp != this.header; temp = temp.getNext()){   //iterating through the list
                if(obj.compareTo((E) temp.getElement()) <= 0){   // if the comparison is less or equal, add after the existing node in the list
                    Node nNode = new Node();
                    nNode.setNext(temp);
                    nNode.setPrev(temp.getPrev());
                    nNode.setElement(obj);
                    temp.getPrev().setNext(nNode);
                    temp.setPrev(nNode);
                    currentSize++;
                    return true;
                }
            }

            if(temp == this.header){  //if existing node is header, add in the list
                Node nNode = new Node();
                nNode.setNext(temp);
                nNode.setPrev(temp.getPrev());
                nNode.setElement(obj);
                temp.getPrev().setNext(nNode);
                temp.setPrev(nNode);
                currentSize++;
                return true;
            }
        }

        return false;
    }

    @Override
    public int size() {
        return this.currentSize;   // return the current size of the list
    }

    @Override
    public boolean remove(E obj) {
        if(obj == null){   //if object = null, just return false
            return false;
        }
        Node temp = null;  //temporary node to compare
        for(temp = this.header.getNext(); temp != this.header; temp = temp.getNext()){  //iterating through the list
            if(temp.getElement().equals(obj)){   //obj to be removed found!!
                temp.getPrev().setNext(temp.getNext());
                temp.getNext().setPrev(temp.getPrev());
                temp.setNext(null);           // setting everything to null so it can be removed
                temp.setPrev(null);
                temp.setElement(null);
                this.currentSize--;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(int index) {
        if(index<0 || index>=this.currentSize){   //index is not valid entry
            throw new IndexOutOfBoundsException();
        }
        Node temp = null; //temporary node to compare
        int i = 0;  //created to know the index of the node to be removed

        for(temp = this.header.getNext(); i < index; temp = temp.getNext(), i++);
        temp.getPrev().setNext(temp.getNext());   //removing the element from the index
        temp.getNext().setPrev(temp.getPrev());
        temp.setNext(null);
        temp.setPrev(null);
        temp.setElement(null);
        this.currentSize--;
        return true;

    }

    @Override
    public int removeAll(E obj) {
        int removed = 0;   //times it have been removed

        while(this.contains(obj)){  //if the contains find obj, removed
            this.remove(obj);
            removed++;
        }
        return removed;
    }

    @Override
    public E first() {
        return header.getNext().getElement();    // get the first element from the double linked list
    }

    @Override
    public E last() {
        return header.getPrev().getElement();   // get the last element from the double linked list
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= this.currentSize) {   //index not valid entry
            throw new IndexOutOfBoundsException();
        }
        Node temp = this.header.getNext();   //temporary node
        for (int i = 0; i < index; i++, temp = temp.getNext());
            return (E) temp.getElement();  //return the element in the index

    }

    @Override
    public void clear() {  //clear all the elements in the list
        while(this.size() != 0){   //if size not equal to 0, keep removing the first element in the list
            this.remove(0);
        }
    }

    @Override
    public boolean contains(E e) {
        for(int i = 0; i< this.size(); i++){
            if(e.equals(this.get(i))){   // if provided elements is the same, it is in the list
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;   //true if size = 0
    }

    @Override
    public Iterator<E> iterator(int index) {
        return new CircularDoublyListIterator(index);
    }

    @Override
    public int firstIndex(E e) {
        for(int i = 0; i<this.size(); i++){
            if(e.equals(this.get(i))){   // if e = the element from i, return the index i
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndex(E e) {
        int i = 0;
        int lastIndex = -1;
        Node<E> temp = this.header;

        while (i < this.size()) {
            if (temp.getNext().getElement().equals(e))   //found the element!!
                lastIndex = i;
            temp = temp.getNext();
            i++;
        }
        return lastIndex;
    }

    @Override
    public ReverseIterator<E> reverseIterator() {
        return new CircularDoublyReverseListIterator();
    }

    @Override
    public ReverseIterator<E> reverseIterator(int index) {
        return new CircularDoublyReverseListIterator(index);
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularDoublyListIterator();
    }

    private class CircularDoublyListIterator implements Iterator<E>{

        private Node nextNode;
        public CircularDoublyListIterator(){
            this.nextNode=header.getNext();
        }
        public CircularDoublyListIterator(int index){
            if(index<0 || index>=currentSize){   //invalid index
                throw new IndexOutOfBoundsException();
            }
            int i = 0;
            for(this.nextNode = header.getNext(); i < index; nextNode = nextNode.getNext(), i++);  //iterating through the list
        }
        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return this.nextNode!=header;
        }

        @Override
        public E next() {
            // TODO Auto-generated method stub
            if(this.hasNext()){   //if is has next, enter here
                E result = (E) this.nextNode.getElement();
                this.nextNode = this.nextNode.getNext();   //getting the next node
                return result;   //return the next node
            }
            else{
                throw new NoSuchElementException();
            }
        }
        public void remove() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException();
        }
    }

    private class CircularDoublyReverseListIterator implements ReverseIterator<E>{

        private Node prevNode;

        public CircularDoublyReverseListIterator(){
            Node temp = null;
            for(temp = header.getNext(); temp.getNext() != header; temp = temp.getNext());
            this.prevNode=temp;
        }
        public CircularDoublyReverseListIterator(int index){
            if(index<0 || index >= currentSize){  //invalid index
                throw new IndexOutOfBoundsException();
            }
            int i = 0;
            for(this.prevNode = header.getNext(); i < index;this.prevNode = this.prevNode.getNext(), i++);
        }
        @Override
        public boolean hasPrevious() {
            // TODO Auto-generated method stub
            return this.prevNode!=header;   //if is has previous node, return that
        }
        @Override
        public E previous() {
            // TODO Auto-generated method stub
            if(this.hasPrevious()){   //if is has previous node, return that
                E result = (E) this.prevNode.getElement();
                this.prevNode = this.prevNode.getPrev();
                return result;
            }
            else{
                throw new NoSuchElementException();
            }
        }
    }

}
