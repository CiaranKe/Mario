package competition.uu2013.common.hueristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class SortedList<T extends SortedListItem<T>>  implements Iterable<T>
{
    private ArrayList<T> list;

    public SortedList()
    {
        list = new ArrayList<T>();
    }

    public T getFirst()
    {
        return list.get(0);
    }

    public void clear()
    {
        list.clear();
    }

    public void add(T element)
    {
        list.add(element);
        Collections.sort(list, new SortedListComparator());
    }

    public void remove(T element)
    {
        list.remove(element);
    }

    public int size()
    {
        return list.size();
    }

    public boolean contains(T element)
    {

        if (list.contains(element))
        {
            return true;
        }

        for ( T t : list)
        {
            if (t.matches(element))
            {
                list.add(element);
                return true;
            }
        }

        return false;
    }

    public ArrayList<T> getList()
    {
        return this.list;
    }

    public void prune(int newSize)
    {

        for (int x = newSize; x < list.size(); x++)
        {
            list.remove(x);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public void addAll(SortedList<T> children)
    {
        for (T t : children)
        {
            list.add(t);
        }
    }

    class SortedListComparator implements Comparator<T>
    {

        @Override
        public int compare(T o1, T o2)
        {
            return o1.compareTo(o2);
        }
    }
}