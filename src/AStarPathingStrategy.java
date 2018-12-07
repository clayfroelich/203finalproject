import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Node startNode = new Node(start,end, null);
        startNode.setg(0);
        PriorityQueue<Node> openList = new PriorityQueue<Node>(Comparator.comparing(Node::f).thenComparing(Node::g));
        HashSet<Node> closedList = new HashSet<Node>();
        List<Point> path = new ArrayList<Point>();

        openList.add(startNode);

        while(openList.size() > 0){
            Node current = openList.poll();
            if (withinReach.test(current.pos(), end)){
                Node temp = current;
                for(int i = 0; i < current.g(); i++){
                    path.add(0, temp.pos());
                    temp = temp.previous();
                }
                return path;
            }
            List<Point> tempList = potentialNeighbors.apply(current.pos()).filter(canPassThrough).collect(Collectors.toList());
            for(Point p : tempList){
                if (!nodeListContainsPoint(closedList, p)) {
                    if (!nodeListContainsPoint(openList, p)) {
                        openList.add(new Node(p, end, current));
                    }
                    int g = current.g() + 1;
                    Node n = getNode(openList, p);
                    if (g < n.g()) {
                        n.setg(g);
                    }
                    openList.remove(n);
                    openList.add(n);
                }
            }
            closedList.add(current);
        }
        return path;
    }

    private static int distance(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    private static boolean nodeListContainsPoint(Iterable<Node> nodeList, Point p){
        for(Node n : nodeList){
            if (n.pos().equals(p)) {
                return true;
            }
        }
        return false;
    }

    private static Node getNode(Iterable<Node> nodeList, Point p){
        for(Node n : nodeList){
            if(n.pos().equals(p)){
                return n;
            }
        }
        return null;
    }

    private class Node {
        private Point pos;
        private int g;
        private int h;
        private int f;
        private Node previous;

        public Node(Point pos, Point end, Node previous){
            this.pos = pos;
            this.previous = previous;
            g = Integer.MAX_VALUE;
            h = distance(pos, end);
        }
        public int g() {
            return g;
        }
        public void setg(int g){
            this.g = g;
        }
        public void seth(int h){
            this.h = h;
        }
        public int h() {
            return h;
        }
        public int f() {
            return g + h;
        }
        public Point pos() {
            return pos;
        }
        public Node previous() {
            return previous;
        }
    }
}
