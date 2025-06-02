package Managers;

import java.util.ListIterator;

public class ListIteratorAdapter implements IteratorTarget {

    private ListIterator listIterator;

    public ListIteratorAdapter(ListIterator listIterator) {
        this.listIterator = listIterator;
    }

    @Override
    public Object myNext() {
        return listIterator.next();
    }

    @Override
    public Object myPrevious() {
        return listIterator.previous();
    }

    @Override
    public boolean myHasPrevious() {
        return listIterator.hasPrevious();
    }
}
