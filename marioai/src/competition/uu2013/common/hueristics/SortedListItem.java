package competition.uu2013.common.hueristics;

/**
 * Created with IntelliJ IDEA.
 * User: fluffy
 * Date: 24/02/14
 * Time: 10:43
 * To change this template use File | Settings | File Templates.
 */
public interface SortedListItem<T> extends Comparable<T>
{
    public boolean matches (T t);

    public int compareTo(T t);
}
