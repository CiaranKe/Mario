package competition.uu2013.common.hueristics;

/**
 * SortedList Interface, extends Comparable to add additional
 * method header for level discretisation
 */
public interface SortedListItem<T> extends Comparable<T>
{
	/** 
	 *  Checks if two elements are logically identical
	 *  
	 *  @return true, if match found
	 */
    public boolean matches (T t);

    /**
     * Compares two elements
     * 
     * @return -1, if element is before
     */
    public int compareTo(T t);
}
