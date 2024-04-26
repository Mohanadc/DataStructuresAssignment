package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        Node<E> curr = tail.getNext();

        for(int j = 0; j < i; j++)
            curr = curr.getNext();

        return curr.getData();
    }

    /**
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {
        size++;
        Node<E> curr = tail.getNext();

        for(int j = 0; j < i - 1; j++)
            curr = curr.getNext();

        Node<E> nodeToAdd = new Node<E>(e, null);
        nodeToAdd.setNext(curr.getNext());
        curr.setNext(nodeToAdd);
    }

    @Override
    public E remove(int i) {
        Node<E> curr = tail.getNext();

        for(int j = 0; j < i - 1; j++)
            curr = curr.getNext();

        E dataRemoved = curr.getNext().getData();
        curr.setNext(curr.getNext().getNext());
        size--;
        return dataRemoved;
    }

    public void rotate() {
        tail = tail.getNext();
    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            return null;
        } else if (size == 1){
            E dataReturned = tail.getData();
            tail = null;
            size--;
            return dataReturned;
        } else {
            E dataReturned = tail.getNext().getData();
            tail.getNext().setNext(tail.getNext().getNext());
            size--;
            return dataReturned;
        }
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            return null;
        } else if (size == 1){
            E dataReturned = tail.getData();
            tail = null;
            size--;
            return dataReturned;
        } else {
            E dataReturned = tail.getData();
            Node<E> currNode = tail;

            for (int i = 0; i < size - 1; i++) {
                if (currNode != null)
                    currNode = currNode.getNext();
            }
            currNode.setNext(tail.getNext());
            tail = currNode;
            size--;
            return dataReturned;
        }
    }

    @Override
    public void addFirst(E e) {
        size++;

        if(tail == null)
        {
            tail = new Node<E>(e, null);
            tail.setNext(tail);
        } else {
            Node<E> nodeToAdd = new Node<E>(e, tail.getNext());
            tail.setNext(nodeToAdd);
        }
    }

    @Override
    public void addLast(E e) {
        size++;

        if(tail == null)
        {
            tail = new Node<E>(e, null);
            tail.setNext(tail);
        } else {
            Node<E> nodeToAdd = new Node<E>(e, tail.getNext());
            tail.setNext(nodeToAdd);
            tail = nodeToAdd;
        }
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        if (tail != null) {
            Node<E> curr = tail.getNext();
            do {
                sb.append(curr.data);
                if (curr.next != tail.getNext()) {
                    sb.append(", ");
                }
                curr = curr.next;
            } while (curr != tail.getNext());
        }
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        for (int i = 10; i < 20; ++i) {
            ll.addFirst(i);
        }

        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);
        ll.removeFirst();
        System.out.println(ll);
        ll.removeFirst();
        System.out.println(ll);
        ll.removeFirst();

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

    }
}
