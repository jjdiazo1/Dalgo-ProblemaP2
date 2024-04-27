import java.util.*;

public class ProblemaP2 { 
    /* -------------------- METODOS PARA RESOLVER EL PROBLEMA ----------------------------------- */
    public static List<String> solveCompoundProblem(List<TestCase> testCases) {
        List<String> results = new ArrayList<>();

        for (TestCase caseData : testCases) {
            int n = caseData.n;
            int w1 = caseData.w1;
            int w2 = caseData.w2;
            List<Element> elements = caseData.elements;

            Graph graph = new Graph();
            graph.buildGraph(elements, w1, w2);
            graph.floydWarshall(elements);

            // Determinar si todos los elementos fundamentales pueden conectarse
            boolean canConnectAll = true;
            int minTotalCost = 0;
            Set<Element> usedElements = new HashSet<>();

            for (int i = 0; i < elements.size() - 1; i++) {
                Element el1 = elements.get(i);
                Element el2 = elements.get(i + 1);

                Pair<Element, Element> key = new Pair<>(el1, el2);

                if (graph.distances.get(key) == null || graph.distances.get(key) == Integer.MAX_VALUE) {
                    canConnectAll = false;
                    break;
                } else {
                    minTotalCost += graph.distances.get(key);
                    usedElements.add(el1);
                    usedElements.add(el2);
                }
            }

            if (canConnectAll && usedElements.size() == n) {
                StringBuilder result = new StringBuilder();
                for (Element el : elements) {
                    result.append(el.toString()).append(",");
                }
                result.append(" ").append(minTotalCost);
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
			line = br.readLine();
			for(;line!=null  && line.length()>0 && !"0".equals(line) && cases < totalCases;cases++) {
				int [] utilities;
				int [] weights;
				int w1, w2;
				try {
                    //Primer parte del caso
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
        }
            // Resolver los casos de prueba
            List<String> results = ProblemaP2.solveCompoundProblem(testCases);

            // Imprimir resultados al archivo redirigido
            for (String result : results) {
                System.out.println(result);
                line = br.readLine();
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

    // Clase para almacenar pares de elementos
    static class Pair<U, V> {
        public final U first;
        public final V second;

        public Pair(U first, V second) {
            this.first = first;
            this.second = second;
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
        Map<Pair<Element, Element>, Integer> distances;

        public Graph() {
            this.distances = new HashMap<>();
        }

        // Método para calcular el LTP entre dos átomos
        public static int calculateLTP(int m1, boolean c1, int m2, boolean c2, int w1, int w2) {
            if (c1 == c2) {
                return 1 + Math.abs(m1 - m2) % w1;
            } else {
                return w2 - Math.abs(m1 - m2) % w2;
            }
        }

        // Construcción del grafo
        public void buildGraph(List<Element> elements, int w1, int w2) {
            Set<Integer> masses = new HashSet<>();

            for (Element el : elements) {
                masses.add(Math.abs(el.atom1));
                masses.add(Math.abs(el.atom2));
            }

            // Inicializar distancias con infinito
            for (int m1 : masses) {
                for (int m2 : masses) {
                    if (m1 != m2) {
                        distances.put(new Pair<>(new Element(m1, m2), new Element(m1, m2)), Integer.MAX_VALUE);
                    }
                }
            }

            // Calcular el costo de conectar elementos
            for (Element el1 : elements) {
                for (Element el2 : elements) {
                    if (el1.atom2 == el2.atom1) { // Ver si pueden conectarse directamente
                        int cost = calculateLTP(el1.atom2, el1.atom1 > 0, el2.atom2, el2.atom1 > 0, w1, w2);
                        distances.put(new Pair<>(el1, el2), cost);
                    }
                }
            }
        }

        // Implementación de Floyd-Warshall para encontrar todos los caminos más cortos
        public void floydWarshall(List<Element> elements) {
            List<Element> nodes = new ArrayList<>(elements);

            for (Element k : nodes) {
                for (Element i : nodes) {
                    for (Element j : nodes) {
                        Pair<Element, Element> ij = new Pair<>(i, j);
                        Pair<Element, Element> ik = new Pair<>(i, k);
                        Pair<Element, Element> kj = new Pair<>(k, j);

                        if (distances.get(ik) != null && distances.get(kj) != null) {
                            int newDist = distances.get(ik) + distances.get(kj);
                            if (newDist < distances.get(ij)) {
                                distances.put(ij, newDist);
                            }
                        }
                    }
                }
            }
        }
    }
}
