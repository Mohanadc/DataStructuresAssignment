package project20280.stacksqueues;

import project20280.interfaces.Stack;
import project20280.list.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {

    DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedStack() {
        ll = new DoublyLinkedList<E>();
    }
    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void push(E e) {
        ll.addFirst(e);
    }

    @Override
    public E top() {
        if(this.isEmpty())
            return null;
        else{
            return ll.first();
        }
    }

    @Override
    public E pop() {
        if(this.isEmpty())
            return null;
        else{
            E dataRemoved = ll.first();
            ll.removeFirst();
            return dataRemoved;
        }
    }

    public String toString() {
        return ll.toString();
    }
}
