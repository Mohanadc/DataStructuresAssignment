package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        // TODO
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        Node<E> currNode = head.getNext();

        for(int j = 0; j < i; j++)
        {
            currNode = currNode.getNext();
        }
        return currNode.getData();
    }

    @Override
    public void add(int i, E e) {
        Node<E> prevNode = head.getNext();

        for(int j = 0; j < i - 1; j++)
            prevNode = prevNode.getNext();

        Node<E> nextNode = prevNode.getNext();
        Node<E> nodeToAdd = new Node<>(e, prevNode, nextNode);
        nextNode.prev = nodeToAdd;
        prevNode.next = nodeToAdd;
    }

    @Override
    public E remove(int i) {
        Node<E> currNode = head.getNext();

        for(int j = 0; j < i; j++)
            currNode = currNode.getNext();

        E dataRemoved = currNode.getData();
        currNode.getPrev().next = currNode.getNext();
        currNode.getNext().prev = currNode.getPrev();
        size--;
        return dataRemoved;
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr =  (Node<E>) head.getNext();

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the list");
            }
            E res = curr.data;
            curr = curr.next;
            return res;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        // TODO
        return null;
    }

    public E first() {
        if(head.getNext() == null)
            return null;
        return head.getNext().getData();
    }

    public E last() {
        if(tail.getPrev() == null)
            return null;
        return tail.getPrev().getData();
    }

    @Override
    public E removeFirst() {
        if(isEmpty())
            return null;

        E dataRemoved = head.getNext().getData();
        head.next = head.getNext().getNext();
        if(head.getNext() != null ) {
            head.getNext().prev = head;
        } else {
            tail.prev = head;
        }
        size--;
        return dataRemoved;
    }

    @Override
    public E removeLast() {

        if (isEmpty())
            return null;

        E dataRemoved = tail.getPrev().getData();
        tail.prev = tail.getPrev().getPrev();

        tail.getPrev().next = tail;
        return dataRemoved;

    }

    @Override
    public void addLast(E e) {
        size++;
        if(size == 1){
            Node<E> nodeToAdd = new Node<E>(e, head, tail);
            head.next = nodeToAdd;
            tail.prev = nodeToAdd;
        } else {
            Node<E> nodeToAdd = new Node<E>(e, tail.getPrev(), tail);
            tail.getPrev().next = nodeToAdd;
            tail.prev = nodeToAdd;
        }

    }

    @Override
    public void addFirst(E e) {
        size++;
        if(size == 1){
            Node<E> nodeToAdd = new Node<E>(e, head, tail);
            head.next = nodeToAdd;
            tail.prev = nodeToAdd;
        } else {
            Node<E> nodeToAdd = new Node<E>(e, head, head.getNext());
            head.getNext().prev = nodeToAdd;
            head.next = nodeToAdd;
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.getNext();
        while (curr != tail) {
            E element = curr.getData();
            if (element != null) {
                sb.append(element);
            } else {
                sb.append("null");
            }
            if (curr.getNext() != tail) {
                sb.append(", ");
            }
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(1);
        ll.addFirst(-1);
        ll.addFirst(-1);
        ll.addFirst(-1);
        ll.addFirst(1);
        ll.addLast(-2);
        ll.addLast(-2);
        ll.addLast(-2);
        ll.addLast(-2);
        ll.addLast(-2);
        System.out.println(ll);


        ll.removeFirst();
        ll.removeFirst();
        ll.removeFirst();
        System.out.println(ll);
        ll.removeLast();
        ll.removeLast();
        System.out.println(ll);
        ll.removeFirst();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

        DoublyLinkedList<Integer> s = new DoublyLinkedList<Integer>();
        for (int i = 0; i < 10; ++i)
            s.addLast(i);
        DoublyLinkedList<Integer> s1 = new DoublyLinkedList<Integer>();
        for (int i = 0; i < 10; ++i)
            s1.addFirst(i);

        System.out.println(s);
        System.out.println(s1);
    }
}