package competition.uu2013.common.hueristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 17/02/14
 * Time: 13:58
 * To change this template use File | Settings | File Templates.
 */
public class SortedList<T extends Comparable<T>>
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
        return list.contains(element);
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

    class SortedListComparator implements Comparator<T>
    {

        @Override
        public int compare(T o1, T o2)
        {
            return o1.compareTo(o2);
        }
    }

}