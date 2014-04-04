package competition.uu2013.astartester;

import java.util.*;

/**
 *  Simple A* algorithm that finds it way through a maze.  Created to 
 *  test correctness of algorithm.
 *  
 *  @author Ciaran Kearney
 *  @version 1.0
 *  @since 17/01/2014
 */
public class AStar
{
    
    /** The maze to exit. */
    private int [][] map;
    
    /** The algorithms current x location. */
    private int xLoc;
    
    /** The algorithms current x location. */
    private int yLoc;
    
    /** The target x location. */
    private int targetX;
    
    /** The target y location. */
    private int targetY;
    
    /** The open list. */
    private SortedList<Node> open;
    
    /** The closed list. */
    private SortedList<Node> closed;
    
    /** The plan. */
    private Stack<Node> plan;

    /**
     * The main method. launches a new instance of this class and 
     * begins the search. 
     *
     * @param args not used.
     */
    public static void  main(String args[])
    {
    	//lets go from one end to the other
        AStar a = new AStar(8,1,1,8);
        a.search();
    }


    /**
     * Instantiates a new a star.
     *
     * @param y the y
     * @param x the x
     * @param tY the t y
     * @param tX the t x
     */
    AStar(int y, int x, int tY, int tX)
    {
    	//set the target and start locations
        this.xLoc = x;
        this.yLoc = y;
        this.targetX = tX;
        this.targetY = tY;

        //init our lists
        open = new SortedList<Node>();
        closed = new SortedList<Node>();
        plan = new Stack<Node>();

        //create the map
        map = new int[10][];
        map[0] = new int[]{1,1,1,1,1,1,1,1,1,1};
        map[1] = new int[]{1,0,0,0,0,0,0,0,0,1};
        map[2] = new int[]{1,0,0,0,0,0,1,1,1,1};
        map[3] = new int[]{1,1,1,1,1,0,0,0,0,1};
        map[4] = new int[]{1,0,0,0,1,0,0,0,0,1};
        map[5] = new int[]{1,0,0,0,1,0,0,0,0,1};
        map[6] = new int[]{1,0,0,0,1,0,0,1,1,1};
        map[7] = new int[]{1,0,0,1,1,0,0,0,0,1};
        map[8] = new int[]{1,0,0,0,0,0,0,1,1,1};
        map[9] = new int[]{1,1,1,1,1,1,1,1,1,1};
    }

    /**
     *  THe A* search.
     *
     * @return true, because it is.
     */
    private boolean search() {
    	//create our starting node, calculate its cost and add it to the open list
        Node current = new Node(this.yLoc, this.xLoc);
        current.setGCost(0);
        open.add(current);

        //while there are still nodes
        while (open.size() != 0) 
        {
        	//grab the best node on the open list
            current = open.getFirst();
            
            //finish if this is the goal
            if (current.getXLoc() == this.targetX && current.getYLoc() == this.targetY) 
            {
                return extractPlan(current);
            }


            //add the node to the closed list
            open.remove(current);
            closed.add(current);

            //generate the successor nodes
            for (Node n : current.generateChildren(this.map)) 
            {
            	
                boolean isBetter;

                //if this node is on the closed list
                if (closed.contains(n)) 
                {
                	//skip it
                    continue;
                }
                
                //if we can access the node
                if (!n.isBlocked()) 
                {
                	//if it's not on the open list
                    if (!open.contains(n)) 
                    {
                    	//add it.
                        open.add(n);
                        //we've found a possible successor
                        isBetter = true;
                    } 
                    //if the node has a lower g cost, then it's a better path to this point
                    else if (n.getGCost() < current.getGCost()) 
                    {
                        isBetter = true;
                    } 
                    else 
                    {
                    	//or not
                        isBetter = false;
                    }
                    //if we found a successor
                    if (isBetter) 
                    {
                    	//set this as the parent
                        n.setParent(current);
                        //prefer going straight
                        if ((n.getXLoc() == current.getXLoc()) && (n.getYLoc() != current.getYLoc()) || ((n.getXLoc() != current.getXLoc()) && (n.getYLoc() == current.getXLoc()))) 
                        {
                            n.setGCost(current.getGCost() + 10);
                        } 
                        else 
                        {
                            n.setGCost(current.getGCost() + 14);
                        }
                        n.setEstimateHCost(this.targetX, this.targetY);
                    }
                }
            }

        }
        return extractPlan(current);
    }

    /**
     * Extract's the plan for the algorithm by moving up 
     * through pointers to the parent node 
     *
     * @param planNode the goal node
     * @return true, we did it!
     */
    private boolean extractPlan(Node planNode)
    {

        while (planNode != null)
        {
            plan.push(planNode);
            planNode = planNode.getParent();
        }
        printPlan();
        return true;
    }

    /**
     * Prints the plan to STDOUT.
     */
    private void printPlan()
    {



        StringBuilder s = new StringBuilder();
        while (!plan.empty())
        {
            Node n = plan.pop();
            s.append("[" + n.getYLoc() + "," + n.getXLoc() + "]");
            for (int y = 0; y < map.length; y++)
            {
                for (int x = 0; x < map[y].length; x++)
                {
                    if (x!= n.getXLoc() || y != n.getYLoc())
                    {
                        System.out.print(map[y][x]+"\t");
                    }
                    else
                    {
                        System.out.print("x"+"\t");
                    }
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
        System.out.println(s.toString());
    }
}

/**
 * The nodes to be searched, implements Matches<T> 
 * which is just an extension on Comparable 
 * 
 * @author Ciaran Kearney
 *
 */
class Node implements Matches<Node>
{
	/** node locations */
    private int xLoc;
    private int yLoc;
    
    /** the parent node */
    private Node parent;
    
    
    /** costs */
    private int gCost;
    private int hCost;
    
    /** is the node walkable */
    private boolean blocked;

    /**
     * Creates a new node with the specified
     * X and Y locations
     * 
     * @param y
     * @param x
     */
    Node(int y, int x)
    {
        this.xLoc = x;
        this.yLoc = y;
    }

    /**
     * Sets the heuristic cost for a node 
     * @param tX target x location
     * @param tY target y location
     */
    public void setEstimateHCost(int tX, int tY)
    {
        this.hCost = 10*(Math.abs(this.xLoc - tX) + Math.abs(this.yLoc - tY));
    }

    /**
     *  sets the g cost for a node
     * @param cost the cost to set
     */
    public void setGCost(int cost)
    {
        gCost = cost;
    }

    /**
     * Returns the heuristic cost for this node
     * 
     * @return the heuristic cost
     */
    public int getFCost()
    {
        return this.gCost + this.hCost;
    }

    /**
     * Sets a new parent for this node
     * 
     * @param _parent the new parent
     */
    public void setParent(Node _parent)
    {
        this.parent = _parent;
    }

    /**
     *  Returns the parent for this node
     * @return the current parent
     * 
     */
    public Node getParent()
    {
        return this.parent;
    }

    /**
     * Compares the x and y locations of the passed in node to 
     * determine if they are the same
     * 
     * @param m the node to match
     * 
     * @return true, if nodes match
     */
    @Override
    public boolean matches(Node m)
    {
        return (m.xLoc == this.xLoc) && (m.yLoc == this.yLoc);
    }


    /**
     * Generates the successor nodes for this instance
     * 
     * @param _map the map on which the node exists
     * 
     * @return a list of the nodes successors
     */
    public SortedList<Node> generateChildren(int[][] _map) 
    {
    	//create our list
        SortedList<Node> children = new SortedList<Node>();

        for (int y = yLoc - 1; y <= yLoc + 1; y++)
        {
            for (int x = xLoc - 1; x <= xLoc + 1; x++)
            {
            	//dont go outside the map
                if (!(x<0) && !(y<0) && !(x > _map[0].length-1) && !(y > _map.length-1))
                {
                	//create new node, set this node as the parent and determine if it is
                	//walkable
                    Node n = new Node(y, x);
                    n.setParent(this);
                    n.setBlocked(_map[y][x] == 1);
                    //set the g cost for the node
                    if ((x == xLoc) && (y != yLoc) || ((x != xLoc) && (y == yLoc))) 
                    {
                        n.setGCost(this.gCost + 10);
                    } 
                    else 
                    {
                        n.setGCost(this.gCost + 14);
                    }
                    //set this node as the parent
                    if (x != this.xLoc || y != this.yLoc) 
                    {
                        children.add(n);
                    }
                }
            }
        }
        return children;
    }

    /**
     * compares this node to the passed in node, compares 
     * by heuristic cost
     * 
     * @param newNode, the node to compare to 
     * @return -1 if node has lower cost
     */
    @Override
    public int compareTo(Node newNode)
    {
        if (this.getFCost() > newNode.getFCost())
        {
            return 1;
        }
        else if (this.getFCost() < newNode.getFCost())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Returns the g cost for the node
     * @return nodes g cost
     */
    public int getGCost() 
    {
        return gCost;
    }

    /**
     * Sets the nodes walkable status
     * 
     * @param blocked, false if node is walkable
     */
    public void setBlocked(boolean blocked) 
    {
        this.blocked = blocked;
    }

    /**
     * Returns the walkable status for this node 
     * @return false if node is walkable
     */
    public boolean isBlocked() 
    {
        return blocked;
    }

    /** 
     * get the node x location
     * @return node x location
     */
    public int getXLoc()
    {
        return this.xLoc;
    }

    /** 
     * get the node y location
     * @return node y location
     */
    public int getYLoc()
    {
        return yLoc;
    }
}

/**
 * Wrapper around the ArrayList class to sort nodes as they are added
 * 
 * @author Ciaran Kearney
 *
 */
class SortedList<T extends Matches<T>> implements Iterable<T>
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
        for (T t : list)
        {
            if (t.matches(element))
            {
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

    /**
     * Adds a collection of nodes to the list and
     * resorts
     * 
     * @param nodes
     */
    public void addAll(ArrayList<T> nodes)
    {
        list.addAll(nodes);
        Collections.sort(this.list);
    }

    /**
     * Returns the iterator for the list
     * 
     *  @return The iterator
     */
    @Override
    public Iterator iterator()
    {
        return list.iterator();
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

/** 
 * Matches, extends comparable to add another
 * method signature
 *  
 */

interface Matches<T> extends Comparable<T>
{
    public boolean matches (T t);

    public int compareTo(T t);
}
