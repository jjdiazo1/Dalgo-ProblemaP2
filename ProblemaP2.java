import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ProblemaP2 { 
    /* -------------------- METODOS PARA RESOLVER EL PROBLEMA ----------------------------------- */
    public static List<String> solveCompoundProblem(List<TestCase> testCases) {

        //METODO PRINCIPAL
        List<String> results = new ArrayList<>();

        for (TestCase caseData : testCases) {
            int n = caseData.n;
            int w1 = caseData.w1;
            int w2 = caseData.w2;
            List<Element> elements = caseData.elements;

            Graph graph = new Graph();
            graph.buildGraph(elements, w1, w2);

            // Determinar si todos los elementos fundamentales pueden conectarse
            List<Element> usedElements = graph.canConnectAll(elements);
            
            //Aca ya nos devuelven los elementos que se pueden conectar en orden (1,2) (2,3) etc.
            boolean canConnectAll = !usedElements.isEmpty();

            //FUNCION FINAL
            if (canConnectAll && usedElements.size() == n) {

                StringBuilder result = new StringBuilder();

                Map<Pair<Integer, Integer>, List<Integer>> shortestPaths = graph.floydMap(elements);

                int finalCost = 0;
    
                for (int i = 1; i < elements.size(); i+=2){ //TODO problemas con la iteracion, con el mapeo

                    element1 = elements.get(i-1);
                    result.append(element1.toString()).append(",");

                    element2 = elements.get(i);
    
                    atom1 = graph.atomMap.get(element1.atom1);
                    atom2 = graph.atomMap.get(element2.atom1*-1);
    
                    Pair<Integer, Integer> nodes = new Pair<>(atom1, atom2);
                    //Saco si es (1,2) (2,3) sacaria del 2 al 2.

                    List<Integer> path = shortestPaths.get(nodes);
                    for (int node : path) {
                        result.append(graph.reverseMap.get(node)).append(",");
                    }

                    result.append(element2.toString()).append(",");
    
                    finalCost += graph.lpts[atom1][atom2];
                }
                result.append(" ").append(finalCost); 
                results.add(result.toString());
            } else {
                results.add("NO SE PUEDE");
            }
        }

        return results;
    }

    public static void main(String[] args) throws IOException {
        ProblemaP2 instance = new ProblemaP2();
        instance.solveProblems();
    }

    public void solveProblems() throws IOException {

        int totalCases = 0;
        List<ProblemaP2.TestCase> testCases = new ArrayList<>();

        try (InputStreamReader is=new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(is);) {
			String line = br.readLine();
			totalCases = Integer.parseInt(line);
            int cases = 0;
			for(;line!=null  && line.length()>0 && !"0".equals(line) && cases < totalCases;cases++) {
				int w1, w2;
				try {
                    //Primer parte del caso
                    line = br.readLine();
					String [] dataStr = line.split(" ");
					int n = Integer.parseInt(dataStr[0]);
					w1 = Integer.parseInt(dataStr[1]);
                    w2 = Integer.parseInt(dataStr[2]);
					
                    // Leer elementos fundamentales (FORMADOS POR 2 ATOMOS)
                    List<ProblemaP2.Element> elements = new ArrayList<>();
                    for (int j = 0; j < n; j++) {
                        line = br.readLine();
					    dataStr = line.split(" ");
                        int atom1 = Integer.parseInt(dataStr[0]);
                        int atom2 = Integer.parseInt(dataStr[1]);
                        elements.add(new ProblemaP2.Element(atom1, atom2));
                    }

                    // Agregar caso de prueba a la lista
                    testCases.add(new ProblemaP2.TestCase(n, w1, w2, elements));
				}  catch (NumberFormatException e) {
					throw new IOException("Error parsing case: "+(cases+1), e);
				}
            }
        
            // Resolver los casos de prueba
            List<String> results = ProblemaP2.solveCompoundProblem(testCases);

            // Imprimir resultados al archivo redirigido
            for (String result : results) {
                System.out.println(result);
                line = br.readLine();
            }
        }
        }

    /* ------------------ CLASES PARA REPRESENTAR OBJETOS DEL PROBLEMA ------------------------ */
    
    // Clase para representar casos de prueba
    static class TestCase {
        int n;
        int w1;
        int w2;
        List<Element> elements;

        public TestCase(int n, int w1, int w2, List<Element> elements) {
            this.n = n;
            this.w1 = w1;
            this.w2 = w2;
            this.elements = elements;
        }
    }

     // Clase para representar un elemento fundamental
     static class Element {
        int atom1;
        int atom2;

        public Element(int atom1, int atom2) {
            this.atom1 = atom1;
            this.atom2 = atom2;
        }

        @Override
        public String toString() {
            return "(" + atom1 + "," + atom2 + ")";
        }
    }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair<?, ?> p = (Pair<?, ?>) o;
            return Objects.equals(p.first, first) && Objects.equals(p.second, second);
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }
    }

     // Clase para representar el grafo y el algoritmo de Floyd-Warshall
     static class Graph {

        Map<Integer, Integer> atomMap;
        Map<Integer, Integer> reverseMap;

        int[][] lpts; // With indexes representing masses listed in the reverse map.

        public Graph() {
            this.reverseMap = new HashMap<>();
            this.atomMap = new HashMap<>();
        }

        // Método para calcular el LTP entre dos átomos
        public static int calculateLTP(int m1, boolean c1, int m2, boolean c2, int w1, int w2) {

            if(m1 == m2 && c1 == c2) {
                return Integer.MAX_VALUE;
            }
            if(m1 == m2 && c1 != c2) {
                return 0;
            }
            if (c1 == c2) {
                return 1 + Math.abs(m1 - m2) % w1;
            } else {
                return w2 - Math.abs(m1 - m2) % w2;
            }
        }

        // Construcción del grafo
        public void buildGraph(List<Element> elements, int w1, int w2) {

            for (Element el : elements) {
                
                key = 0;
                //ANADIR ATOMOS LIBRES POSIBLES
                if (!(reverseMap.containsKey(el.atom1))) {
                    reverseMap.put(key, el.atom1);
                    key++;
                    if(!(reverseMap.containsKey(Math.abs(el.atom1)))) {
                        reverseMap.put(key, Math.abs(el.atom1));
                        key++;
                    } else {
                        reverseMap.put(key, el.atom1*-1);
                        key++;
                    }
                }

                if (!(reverseMap.containsKey(el.atom2))){
                    reverseMap.put(key, el.atom2);
                    key++;
                    if(!(reverseMap.containsKey(Math.abs(el.atom2)))) {
                        reverseMap.put(key, Math.abs(el.atom2));
                        key++;
                    } else {
                        reverseMap.put(key, el.atom2*-1);
                        key++;
                    }
                }

                //ANADIR ATOMOS AL OTRO MAPA
                int value = 0;
                if (!(atomMap.containsKey(el.atom1))) {
                    atomMap.put(el.atom1, value);
                    value++;
                    if(!(atomMap.containsKey(Math.abs(el.atom1)))) {
                        atomMap.put(Math.abs(el.atom1), value);
                        value++;
                    } else {
                        atomMap.put(el.atom1*-1, value);
                        value++;
                    }
                }
                
                if (!(atomMap.containsKey(el.atom2))){
                    atomMap.put(el.atom2, value);
                    value++;
                    if(!(atomMap.containsKey(Math.abs(el.atom2)))) {
                        atomMap.put(Math.abs(el.atom2), value);
                        value++;
                    } else {
                        atomMap.put(el.atom2*-1, value);
                        value++;
                    }
                }
            }

            lpts = new int[reverseMap.size()][reverseMap.size()];

            // Calcular el costo de conectar elementos
            for (int atom1; atom1 < reverseMap.size(); atom1++) {
                for (int atom2; atom2 < reverseMap.size(); atom2++) {
                        lpts[atom1][atom2] = calculateLTP(reverseMap.get(atom1), reverseMap.get(atom1) > 0, reverseMap.get(atom2), reverseMap.get(atom2) > 0, w1, w2);  
                        //TODO: Acortar el ciclo a la mitad porque como es dirigido basta con recorrer en diagonal                  
                }
            }
        }

        //Implementacion de algoritmo para saber si se puede conectar todos los elementos con eulerian path  
        //tratando los elementos como un grafo no dirigido, diferente al que ya tenemos arriba que es el grafo de los costos.
        public List<Element> canConnectAll(List<Element> elements) {
            Map<Integer, List<Integer>> adj = new HashMap<>();
            Map<Integer, Integer> degrees = new HashMap<>();
            
            // Build adjacency list and degree counts
            for (Element e : elements) {
                adj.computeIfAbsent(e.atom1, k -> new ArrayList<>()).add(e.atom2);
                adj.computeIfAbsent(e.atom2, k -> new ArrayList<>()).add(e.atom1);
                degrees.put(e.atom1, degrees.getOrDefault(e.atom1, 0) + 1);
                degrees.put(e.atom2, degrees.getOrDefault(e.atom2, 0) + 1);
            }
    
            // Check if graph is connected
            if (!isConnected(adj)) {
                return Collections.emptyList(); // Not connected
            }
    
            // Determine start node for Eulerian path
            Integer startNode = adj.keySet().iterator().next();
            for (Map.Entry<Integer, Integer> entry : degrees.entrySet()) {
                if (entry.getValue() % 2 != 0) { // Start with an odd-degree node if any
                    startNode = entry.getKey();
                    break;
                }
            }
    
            // Find Eulerian path using Hierholzer's algorithm
            Stack<Integer> stack = new Stack<>();
            List<Integer> path = new ArrayList<>();
            stack.push(startNode);
    
            while (!stack.isEmpty()) {
                Integer node = stack.peek();
                if (adj.get(node).isEmpty()) {
                    path.add(stack.pop());
                } else {
                    Integer nextNode = adj.get(node).get(0);
                    stack.push(nextNode);
                    adj.get(node).remove(nextNode);
                    adj.get(nextNode).remove(node);
                }
            }
    
            Collections.reverse(path); // Ensure the correct order
            
            // Convert path of nodes to a list of elements
            List<Element> result = new ArrayList<>();
            for (int i = 0; i < path.size() - 1; i++) {
                result.add(new Element(path.get(i), path.get(i + 1)));
            }
    
            return result;
        }
    
        private static boolean isConnected(Map<Integer, List<Integer>> adj) {
            Set<Integer> visited = new HashSet<>();
            Queue<Integer> queue = new LinkedList<>();
            queue.add(adj.keySet().iterator().next());
    
            while (!queue.isEmpty()) {
                Integer node = queue.poll();
                if (!visited.contains(node)) {
                    visited.add(node);
                    for (Integer neighbor : adj.get(node)) {
                        if (!visited.contains(neighbor)) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
            return visited.size() == adj.size(); // All nodes are visited
        }

        // Implementación de Floyd-Warshall para encontrar todos los caminos más cortos
        public void floydWarshall(List<Element> elements) {
            
            int [][] dp = new int[reverseMap.size()][reverseMap.size()];

            for (int i = 0; i < this.lpts.length; i++) {
                for (int j = 0; j < this.lpts.length; j++) {
                    dp[i][j] = this.lpts[i][j];
                }
            }
            //TODO quitar esto y hacerlo sobre la matriz original

            for (int k; k < this.lpts.length; k++) {
                for (int i; i< this.lpts.length; i++) {
                    for (int j; j < this.lpts.length; j++) {

                        if (dp[i][k] + dp[k][j] < dp[i][j] && dist[k][j] != Integer.MAX_VALUE && dist[i][k] != Integer.MAX_VALUE) {
                            dp[i][j] = dp[i][k] + dp[k][j];
                        }
                    }
                }
            }
        }

        public Map<Pair<Integer, Integer>, List<Integer>> floydMap(List<Element> elements) {
            int[][] dp = new int[reverseMap.size()][reverseMap.size()];
            int[][] next = new int[reverseMap.size()][reverseMap.size()];
        
            for (int i = 0; i < this.lpts.length; i++) {
                for (int j = 0; j < this.lpts.length; j++) {
                    dp[i][j] = this.lpts[i][j];
                    if (i == j)
                        next[i][j] = i;
                    else if (dp[i][j] != Integer.MAX_VALUE)
                        next[i][j] = i;
                    else
                        next[i][j] = -1;
                }
            }
        
            for (int k = 0; k < this.lpts.length; k++) {
                for (int i = 0; i < this.lpts.length; i++) {
                    for (int j = 0; j < this.lpts.length; j++) {
                        if (dp[i][k] + dp[k][j] < dp[i][j] && dp[i][k] != Integer.MAX_VALUE && dp[k][j] != Integer.MAX_VALUE) {
                            dp[i][j] = dp[i][k] + dp[k][j];
                            next[i][j] = next[k][j];
                        }
                    }
                }
            }
        
            Map<Pair<Integer, Integer>, List<Integer>> shortestPaths = new HashMap<>();
            for (int i = 0; i < this.lpts.length; i++) {
                for (int j = 0; j < this.lpts.length; j++) {
                    if (i != j) {
                        List<Integer> path = new ArrayList<>();
                        if (next[i][j] != -1) {
                            path.add(i);
                            while (i != j) {
                                i = next[i][j];
                                path.add(i);
                            }
                        }
                        shortestPaths.put(new Pair<>(i, j), path);
                    }
                }
            }
        
            return shortestPaths;
        }
        
    }
