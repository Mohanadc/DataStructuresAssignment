package project20280.stacksqueues;

import project20280.interfaces.Queue;

public class ArrayQueue<E> implements Queue<E> {

    private static int CAPACITY = 1000;
    private E[] data;
    private int rear = 0;
    private int front = -1;
    private int size = 0;

    public ArrayQueue(int capacity) {
        CAPACITY =  capacity;
        this.data = (E[]) new Object[capacity];
    }

    public ArrayQueue() {
        this(CAPACITY);
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void enqueue(E e) {
        if (size == CAPACITY) {
            throw new IllegalStateException("Queue is full");
        }
        front = (front + 1) % CAPACITY;
        data[front] = e;
        size++;
    }

    @Override
    public E first() {
        return isEmpty() ? null : data[rear];
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        E removedItem = data[rear];
        data[rear] = null;
        rear = (rear + 1) % CAPACITY;
        size--;
        return removedItem;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; ++i) {
            E res = data[(rear + i) % CAPACITY];
            sb.append(res);
            if (i != size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Queue<Integer> qq = new ArrayQueue<>();
        System.out.println(qq);

        int N = 10;
        for (int i = 0; i < N; ++i) {
            qq.enqueue(i);
        }
        System.out.println(qq);

        for (int i = 0; i < N / 2; ++i) qq.dequeue();
        System.out.println(qq);

    }
}
