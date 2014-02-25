package competition.uu2013.astartester;

import java.util.*;

public class AStar
{
    private int [][] map;
    private int xLoc;
    private int yLoc;
    private int targetX;
    private int targetY;
    private SortedList<Node> open;
    private SortedList<Node> closed;
    private Stack<Node> plan;

    public static void  main(String args[])
    {
        AStar a = new AStar(8,1,1,8);
        a.search();
    }


    AStar(int y, int x, int tY, int tX)
    {
        this.xLoc = x;
        this.yLoc = y;
        this.targetX = tX;
        this.targetY = tY;

        open = new SortedList<Node>();
        closed = new SortedList<Node>();
        plan = new Stack<Node>();

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

    private boolean search() {
        Node current = new Node(this.yLoc, this.xLoc);
        current.setGCost(0);
        open.add(current);

        while (open.size() != 0) {

            current = open.getFirst();

            if (current.getXLoc() == this.targetX && current.getYLoc() == this.targetY) {
                return extractPlan(current);
            }


            open.remove(current);
            closed.add(current);

            for (Node n : current.generateChildren(this.map)) {
                boolean isBetter;

                if (closed.contains(n)) {
                    continue;
                }
                if (!n.isBlocked()) {
                    if (!open.contains(n)) {
                        open.add(n);
                        isBetter = true;
                    } else if (n.getGCost() < current.getGCost()) {
                        isBetter = true;
                    } else {
                        isBetter = false;
                    }
                    if (isBetter) {
                        n.setParent(current);
                        if ((n.getXLoc() == current.getXLoc()) && (n.getYLoc() != current.getYLoc()) || ((n.getXLoc() != current.getXLoc()) && (n.getYLoc() == current.getXLoc()))) {
                            n.setGCost(current.getGCost() + 10);
                        } else {
                            n.setGCost(current.getGCost() + 14);
                        }
                        n.setEstimateHCost(this.targetX, this.targetY);
                    }
                }
            }

        }
        return extractPlan(current);
    }

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

class Node implements Matches<Node>
{
    private int xLoc;
    private int yLoc;
    private Node parent;
    private int gCost;
    private int hCost;
    private boolean blocked;

    Node(int y, int x)
    {
        this.xLoc = x;
        this.yLoc = y;
    }

    public void setEstimateHCost(int tX, int tY)
    {
        this.hCost = 10*(Math.abs(this.xLoc - tX) + Math.abs(this.yLoc - tY));
    }

    public void setGCost(int cost)
    {
        gCost = cost;
    }


    public int getFCost()
    {
        return this.gCost + this.hCost;
    }

    public void setParent(Node _parent)
    {
        this.parent = _parent;
    }

    public Node getParent()
    {
        return this.parent;
    }

    @Override
    public boolean matches(Node m)
    {
        return (m.xLoc == this.xLoc) && (m.yLoc == this.yLoc);
    }


    public SortedList<Node> generateChildren(int[][] _map) {
        SortedList<Node> children = new SortedList<Node>();

        for (int y = yLoc - 1; y <= yLoc + 1; y++)
        {
            for (int x = xLoc - 1; x <= xLoc + 1; x++)
            {
                if (!(x<0) && !(y<0) && !(x > _map[0].length-1) && !(y > _map.length-1))
                {
                    Node n = new Node(y, x);
                    n.setParent(this);
                    n.setBlocked(_map[y][x] == 1);
                    if ((x == xLoc) && (y != yLoc) || ((x != xLoc) && (y == yLoc))) {
                        n.setGCost(this.gCost + 10);
                    } else {
                        n.setGCost(this.gCost + 14);
                    }
                    if (x != this.xLoc || y != this.yLoc) {
                        children.add(n);
                    }
                }
            }
        }
        return children;
    }

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

    public int getGCost() {
        return gCost;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public int getXLoc()
    {
        return this.xLoc;
    }

    public int getYLoc()
    {
        return yLoc;
    }
}

class SortedList<T extends Matches<T>> implements Iterable<T>
{
    private ArrayList<T> list;
    private T node;

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
        for (T t : list)
        {
            if (t.matches(element))
            {
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

    public void addAll(ArrayList<T> nodes)
    {
        list.addAll(nodes);
    }

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

interface Matches<T> extends Comparable<T>
{
    public boolean matches (T t);

    public int compareTo(T t);
}
