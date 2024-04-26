package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }
        if (head == null) {
            throw new NoSuchElementException("List is empty");
        }
        Node<E> nodeToGet = this.head;
        for(int i = 0; i < position; i++)
        {
            nodeToGet = nodeToGet.getNext();
        }
        return nodeToGet.getElement();
    }

    @Override
    public void add(int position, E e) {
        if (position < 1 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }
        else {
            Node<E> currNode = head;
            for(int i = 0; i < position - 1; i++){
                currNode = currNode.getNext();
            }
            Node<E> nodeToAdd = new Node<E>(e, currNode.getNext());
            currNode.setNext(nodeToAdd);
        }
        size++;
    }


    @Override
    public void addFirst(E e) {
        if (head == null) {
            head = new Node<E>(e, null);
        } else {
            Node<E> nodeToAdd = new Node<E>(e, head);
            head = nodeToAdd;
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        if(head == null)
        {
            head = new Node<E>(e, null);
            size++;
        } else
        {
            Node<E> currNode = head;

            //loops to the end of the list
            while (currNode.getNext() != null )
                currNode = currNode.getNext();

            currNode.setNext(new Node<E>(e, null));
            size++;
        }
    }

    @Override
    public E remove(int position) {
        if (position < 1 || position >= size) {
            throw new IndexOutOfBoundsException("Invalid position: " + position);
        }
        else {
            Node<E> currNode = head;
            //finds the node before the one we want to remove
            for(int i = 0; i < position - 1; i++){
                currNode = currNode.getNext();
            }

            E removedValue = currNode.getNext().getElement();
            currNode.setNext(currNode.getNext().getNext());
            size--;
            return removedValue;
        }
    }

    @Override
    public E removeFirst() {
        if(head == null)
            return null;
        else {
            E removedValue = head.getElement();
            head = head.getNext();
            size--;
            return removedValue;
        }
    }

    @Override
    public E removeLast() {
        if(head == null){
            return null;
        } else if (head.getNext() == null) {
            return removeFirst();
        } else {
            size--;
            Node<E> currNode = head;
            //loops to the node before the last
            while(currNode.getNext().getNext() != null)
                currNode = currNode.getNext();

            E removedValue = currNode.getNext().getElement();
            currNode.setNext(null);
            return removedValue;
        }
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            E element = curr.getElement();
            if (element != null) {
                sb.append(element);
            } else {
                sb.append("null");
            }
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }


}
