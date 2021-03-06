import java.util.*;

public class FlightGraph {

    private static class Pair<K, V extends Comparable<? super V>> implements Comparable<Pair<K, V>> {
        private K key;
        private V value;

        private Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int compareTo(Pair<K, V> pair) {
            return this.value.compareTo(pair.value);
        }

    }

    private HashMap<String, Integer> vertices;
    private HashMap<String, LinkedList<Flight>> graph;

    public FlightGraph() {
    }

    public FlightGraph(Iterable<Airport> airportList) {
        vertices = new HashMap<>();
        graph = new HashMap<>();
        for (var airport : airportList) {
            vertices.put(airport.getAirportName(), airport.getConnectionTime());
            graph.put(airport.getAirportName(), new LinkedList<>());
        }
    }

    public void addEdge(Flight flight) {
        String departureAirport = flight.getSrcAirport();
        graph.get(departureAirport).add(flight);
    }

    /**
     * Use Dijkstra's algorithm to find single-pair shortest path
     */
    public Itinerary findShortestPath(String source, String dest, String departure) {
        if (!vertices.containsKey(source) || !vertices.containsKey(dest)) {
            return new Itinerary();
        }

        // Calculate the departure time in minutes
        int depTime = Util.convertStringToMinutes(departure);

        // Airports that haven't found the shortest itinerary
        HashSet<String> shortestPathNotFoundVertices = new HashSet<>(vertices.keySet());
        shortestPathNotFoundVertices.remove(source);

        // Min Heap
        PriorityQueue<Pair<String, Integer>> shortestPathMinHeap = new PriorityQueue<>();
        HashMap<String, Pair<LinkedList<Flight>, Integer>> itinerary = new HashMap<>();

        // get all flights starting from source
        var flights = graph.get(source);
        for (var v : shortestPathNotFoundVertices) {
            itinerary.put(v, new Pair<>(new LinkedList<>(), Integer.MAX_VALUE));
            shortestPathMinHeap.add(new Pair<>(v, Integer.MAX_VALUE));
        }
        for (var flight : flights) {
            // if the destination airport can be reached directly
            if (shortestPathNotFoundVertices.contains(flight.getDestAirport())) {
                int commuteTime = Util.calculateWaitTime(depTime, flight.getStartTime()) + flight.getCommuteDuration();
                var tmp = itinerary.get(flight.getDestAirport());
                if (commuteTime < tmp.value) {
                    tmp.key = new LinkedList<>();
                    tmp.key.add(flight);
                    tmp.value = commuteTime;
                    shortestPathMinHeap.add(
                            new Pair<>(
                                    flight.getDestAirport(),
                                    commuteTime
                            )
                    );
                }
            }
        }

        while (!shortestPathMinHeap.isEmpty()) {
            // get the minimum value out of minheap
            Pair<String, Integer> v = shortestPathMinHeap.poll();
            String airport = v.key;
            int commuteTime = v.value;
            if (airport.equals(dest)) {
                return new Itinerary(itinerary.get(dest).key);
            }
            shortestPathNotFoundVertices.remove(airport);

            int connectionTime = vertices.get(airport);
            int currentTime = itinerary.get(airport).key.getLast().getEndTime() + connectionTime;

            // iterate through all edges starting at airport
            for (var flight : graph.get(airport)) {
                // if there is a flight between v and destAirport
                // if the destination airport has not found a shortest path
                if (shortestPathNotFoundVertices.contains(flight.getDestAirport())) {
                    // get the current path from source to dest
                    var tmp = itinerary.get(flight.getDestAirport());
                    int currCommuteTime = tmp.value;
                    int newCommuteTime = commuteTime + connectionTime +
                            Util.calculateWaitTime(currentTime, flight.getStartTime()) + // wait time before taking off
                            flight.getCommuteDuration();
                    if (newCommuteTime < currCommuteTime) {
                        tmp.key = new LinkedList<>(itinerary.get(airport).key);
                        tmp.key.add(flight);
                        tmp.value = newCommuteTime;
                        shortestPathMinHeap.add(
                                new Pair<>(
                                        flight.getDestAirport(),
                                        newCommuteTime
                                )
                        );
                    }
                }
            }
        }
        return new Itinerary();
    }
}
