import java.sql.SQLOutput;
import java.util.*;

public class TreeorGraph {

    private static class Node{
        public Node(String n,String first){
            stationName = n;
            adjNodes = new LinkedList<>();
            addToList(first);
        }
        public void addToList(String s){
            adjNodes.add(s);
        }
        public LinkedList<String> adjNodes;
        public String stationName;
        public boolean hasIncEdge;
        public boolean foundParent;
        public boolean visited;
        public boolean visited2;
        public boolean cycleDet;
        public String toString(){
            String s = stationName + ":";
            Collections.reverse(adjNodes);
            for (String name: adjNodes){
                s+= " " + name;
            }
            return s;
        }
    }
    public static void main(String[] args) {
        
        // The "main" method starts by creating a scanner to read input from the console.
        Scanner k = new Scanner(System.in);
        int nodeCount, edgeCount;
        
        //It prompts the user to enter the number of nodes and edges in the graph, then reads in the edges one by one and stores them in a HashMap called "uaj" (which stands for "undirected adjacency list"). 
        //If a node has not been seen beforeit is added to the "uaj" map as a new node with an empty list of adjacent nodes.
        
        HashMap<String, Node> adjList = new HashMap<>();
        HashMap<String, Node> uaj = new HashMap<>();
        
        String line, startNode, endNode;
        String[] route;
        ArrayList<String> list1 = new ArrayList<>();
        System.out.println("Enter the number of taxi pickups:");
        nodeCount = k.nextInt();
        System.out.println("Enter the number of taxi rides:");
        edgeCount = k.nextInt();
        System.out.println("Enter the taxi rides:");
        boolean b = false;
        k.nextLine();
        
        // After building the adjacency lists, the code checks whether the graph is a tree structure by comparing the number of edges to the number of nodes minus one. 
        // If the two values are not equal, the code prints a message and exits.
        
        for (int i = 0; i < edgeCount; i++) {
            line = k.nextLine();
            route = line.split(" ");
            startNode = route[0];
            endNode = route[1];
            if(uaj.containsKey(startNode) && uaj.containsKey(endNode)){
                uaj.get(startNode).addToList(endNode);
                uaj.get(endNode).addToList(startNode);
            }
            else if(uaj.containsKey(startNode)){
                uaj.put(endNode,new Node(endNode, startNode));
                uaj.get(startNode).addToList(endNode);
            }
            else if(uaj.containsKey(endNode)){
                uaj.put(startNode,new Node(startNode,endNode));
                uaj.get(endNode).addToList(startNode);
            }
            else {
                uaj.put(startNode,new Node(startNode,endNode));
                uaj.put(endNode,new Node(endNode, startNode));
            }
            if (adjList.containsKey(startNode)) {
                adjList.get(startNode).addToList(endNode);
            } else {
                list1.add(startNode);
                adjList.put(startNode, new Node(startNode,endNode));
            }
        }
        for (String s: list1){
            System.out.println(adjList.get(s));
        }

        if(edgeCount != nodeCount-1){
            System.out.println("This ride network cannot be kept in a tree structure");
            System.exit(0);
        }
        if(checkIfGraph(adjList,uaj)) {
            System.out.println("This ride network cannot be kept in a tree structure.");
            System.exit(0);
        }
        System.out.println("This ride network can be kept in a tree structure.");

    }
    private static boolean checkIfGraph(HashMap<String,Node> adjList,HashMap<String,Node> uaj){
        Map.Entry<String,Node> entry = uaj.entrySet().iterator().next();
        dfs(entry.getValue(),uaj);
        for(Map.Entry<String,Node> e: uaj.entrySet()){
            if(!e.getValue().visited2) {
                return true;
            }
        }
        if(cycleDetection(adjList)){
            return true;
        }
        return false;
    } 
    
    // The "cycleDetection" method performs a depth-first search on each node in the "adjList" map to check whether it forms a cycle.
    // If a cycle is found, the method returns true.
    
    private static boolean cycleDetection(HashMap<String,Node> adjList){
        for(Map.Entry<String, Node> entry: adjList.entrySet()){
            if(dfsForCycle(entry.getValue(),adjList)){
                return true;
            }
        }
        return false;
    }
    
    
    // The "dfs" and "dfsForCycle" methods are both depth-first search algorithms that visit all nodes in the graph.
   
    private static void dfs(Node n, HashMap<String,Node> adjList){
        n.visited2 = true;
        Node current;
        for(String s: n.adjNodes) {
            current = adjList.get(s);
            if (current != null) {
                if (!current.visited2)
                    dfs(current, adjList);
            }
        }
    }
    
     // The "dfs" method is used to mark all nodes as visited, while the "dfsForCycle" method is used to check for cycles in the graph.
   
    private static boolean dfsForCycle(Node n, HashMap<String,Node> adjList){
        if(n.cycleDet == true)
            return true;
        if(n.visited)
            return false;
        n.visited= true;
        n.cycleDet = true;
        Node current;
        for(String s: n.adjNodes) {
            current = adjList.get(s);
            if (current != null) {
                if (dfsForCycle(current, adjList)) {
                    return true;
                }
            }
        }
        n.cycleDet = false;
        return false;
    }
}
