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

    public SortedCircularDoublyLinkedList() {
        this.currentSize = 0;
        this.header = new Node<>();

        this.header.setNext(this.header);
        this.header.setPrev(this.header);    //Making the list circular


    }



    @Override
    public boolean add(E obj) {
        if(obj == null){   //checking if the object is a valid entry
            return false;
        }

        if(this.isEmpty()){  // if empty, put in the first node
            Node<E> nNode= new Node<E>();  //Creating new node
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
            for(temp = this.header.getNext(); temp != this.header; temp = temp.getNext()){
                if(obj.compareTo((E) temp.getElement()) <= 0){
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

            if(temp == this.header){
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
        return this.currentSize;
    }

    @Override
    public boolean remove(E obj) {
        if(obj==null){
            return false;
        }
        Node temp=null;
        for(temp=this.header.getNext();temp!=this.header;temp=temp.getNext()){
            if(temp.getElement().equals(obj)){
                temp.getPrev().setNext(temp.getNext());
                temp.getNext().setPrev(temp.getPrev());
                temp.setNext(null);
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
        if(index<0 || index>=this.currentSize){
            throw new IndexOutOfBoundsException();
        }
        Node temp=null;
        int counter=0;
        for(temp=this.header.getNext();counter<index;temp=temp.getNext(),counter++);
        temp.getPrev().setNext(temp.getNext());
        temp.getNext().setPrev(temp.getPrev());
        temp.setNext(null);
        temp.setPrev(null);
        temp.setElement(null);
        this.currentSize--;
        return true;

    }

    @Override
    public int removeAll(E obj) {
        int removed = 0;

        while(this.contains(obj)){
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
        if(index<0 || index>=this.currentSize){
            throw new IndexOutOfBoundsException();
        }
        Node temp=this.header.getNext();
        for(int counter=0;counter<index;counter++,temp=temp.getNext());
        return (E) temp.getElement();
    }

    @Override
    public void clear() {  //clear all the elements in the list
        while(this.size() != 0){
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
        return this.size() == 0;
    }

    @Override
    public Iterator<E> iterator(int index) {
        return new CircularDoublyListIterator(index);
    }

    @Override
    public int firstIndex(E e) {
        for(int i = 0; i<this.size(); i++){
            if(e.equals(this.get(i))){
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndex(E e) {
        int index = 0;
        int lastIndexPos = -1;
        Node<E> temp = this.header;

        while (index < this.size()) {
            if (temp.getNext().getElement().equals(e))
                lastIndexPos = index;
            temp = temp.getNext();
            index ++;
        }
        return lastIndexPos;
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

    private Node<E> getNode(int index){   //helper method for getting the node from the index position
        int position = 0;
        Node<E> temp = null;

        temp = header.getNext();
        while (position < index){
            temp = temp.getNext();
            position++;
        }
        return temp;
    }

    private class CircularDoublyListIterator implements Iterator<E>{

        private Node nextNode;
        public CircularDoublyListIterator(){
            this.nextNode=header.getNext();
        }
        public CircularDoublyListIterator(int index){
            if(index<0 || index>=currentSize){
                throw new IndexOutOfBoundsException();
            }
            int counter=0;
            for(this.nextNode=header.getNext();counter<index;nextNode=nextNode.getNext(),counter++);
        }
        @Override
        public boolean hasNext() {
            // TODO Auto-generated method stub
            return this.nextNode!=header;
        }

        @Override
        public E next() {
            // TODO Auto-generated method stub
            if(this.hasNext()){
                E result = (E) this.nextNode.getElement();
                this.nextNode=this.nextNode.getNext();
                return result;
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
            Node temp=null;
            for(temp=header.getNext();temp.getNext()!=header;temp=temp.getNext());
            this.prevNode=temp;
        }
        public CircularDoublyReverseListIterator(int index){
            if(index<0 || index>=currentSize){
                throw new IndexOutOfBoundsException();
            }
            int counter=0;
            for(this.prevNode=header.getNext();counter<index;this.prevNode=this.prevNode.getNext(),counter++);
        }
        @Override
        public boolean hasPrevious() {
            // TODO Auto-generated method stub
            return this.prevNode!=header;
        }
        @Override
        public E previous() {
            // TODO Auto-generated method stub
            if(this.hasPrevious()){
                E result= (E) this.prevNode.getElement();
                this.prevNode=this.prevNode.getPrev();
                return result;
            }
            else{
                throw new NoSuchElementException();
            }
        }
    }

}
