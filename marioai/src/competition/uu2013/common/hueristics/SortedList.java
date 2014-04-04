package competition.uu2013.common.hueristics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Wrapper around the ArrayList class to sort nodes as they are added
 * 
 * @author Ciaran Kearney
 *
 */
public class SortedList<T extends SortedListItem<T>>  implements Iterable<T>
{
	/** The actual list	 */
    private ArrayList<T> list;

    /**
     *  Creates a new SortedList
     */
    public SortedList()
    {
        list = new ArrayList<T>();
    }

    /**
     * Returns the first item in the list
     * 
     * @return the first list item
     */
    public T getFirst()
    {
        return list.get(0);
    }

    /**
     * Empties the list
     */
    public void clear()
    {
        list.clear();
    }

    /**
     * Adds an element to the list and sorts it into position
     * 
     * @param element the element to add
     */
    public void add(T element)
    {
        list.add(element);
        Collections.sort(list, new SortedListComparator());
    }

    /**
     * Removes an element from the list
     * 
     * @param element the element to remove
     */
    public void remove(T element)
    {
        list.remove(element);
    }

    /**
     * Gets the size of the list
     *  
     * @return the current number of list members
     */
    public int size()
    {
        return list.size();
    }

    /**
     * Checks if an element is on the list
     * 
     * @param element the element to check
     * 
     * @return true, if found
     */
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

    /**
     * Returns the actual list
     * 
     * @return
     */
    public ArrayList<T> getList()
    {
        return this.list;
    }

    /**
     * Reduces the size of the list by the specified 
     * number, elements at the end of the list are 
     * removed.
     * 
     * @param newSize the new list size
     */
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
    
    /**
     * Adds a collection of nodes to the list and
     * resorts
     * 
     * @param nodes
     */
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