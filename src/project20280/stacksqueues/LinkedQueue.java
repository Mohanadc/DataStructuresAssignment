package project20280.stacksqueues;

import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

    private DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedQueue() {
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
    public void enqueue(E e) {
        ll.addLast(e);
    }

    @Override
    public E first() {
        if(this.isEmpty())
            return null;
        else{
            return ll.first();
        }
    }

    @Override
    public E dequeue() {
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
