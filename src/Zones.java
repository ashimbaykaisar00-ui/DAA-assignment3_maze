import java.util.*;

/**
 * Missions 4 and 5. The slides: a directed graph. Cells are 0 .. n-1, slide i goes from s.from()[i] to s.to()[i]
 * and takes s.time()[i]. There are no slides from a cell to itself, and the exit can always be reached from the start.
 *
 * Ready for you: Graphs.slidesFrom(s)[u] is the list of the slides that leave cell u, and Graphs.slidesInto(s)[v] is the list
 * of the slides that enter cell v (reversed graph). The lists hold slide numbers i; the cell at the other end is s.to()[i]
 * or s.from()[i].
 */
public class Zones {
    //mission 4: strongly connected components
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();
        int[][] adj = Graphs.slidesFrom(s);
        int[][] revAdj = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        Deque<Integer> finishOrder = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs1(i, s, adj, visited, finishOrder);
            }
        }

        Arrays.fill(visited, false);
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);
        int zoneId = 0;

        for (int u : finishOrder) {
            if (!visited[u]) {
                dfs2(u, s, revAdj, visited, zoneOf, zoneId);
                zoneId++;
            }
        }

        return zoneOf;
    }

    private static void dfs1(int start, Input.Slides s, int[][] adj, boolean[] visited, Deque<Integer> finishOrder) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.peek();
            if (!visited[u]) {
                visited[u] = true;
            }
            
            boolean hasUnvisited = false;
            for (int slideIdx : adj[u]) {
                int v = s.to()[slideIdx];
                if (!visited[v]) {
                    stack.push(v);
                    hasUnvisited = true;
                    break;
                }
            }
            
            if (!hasUnvisited) {
                stack.pop();
                finishOrder.push(u);
            }
        }
    }

    private static void dfs2(int start, Input.Slides s, int[][] revAdj, boolean[] visited, int[] zoneOf, int zoneId) {
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                visited[u] = true;
                zoneOf[u] = zoneId;
                for (int slideIdx : revAdj[u]) {
                    int v = s.from()[slideIdx];
                    if (!visited[v]) {
                        stack.push(v);
                    }
                }
            }
        }
    }

    

    //mission 5: longest path and fastest route
    public static long[] routes(Input.Slides s, int[] zoneOf, int zoneCount) {
        List<int[]>[] zoneAdj = new ArrayList[zoneCount];
        for (int i = 0; i < zoneCount; i++) {
            zoneAdj[i] = new ArrayList<>();
        }

        for (int i = 0; i < s.count(); i++) {
            int u = s.from()[i];
            int v = s.to()[i];
            int w = s.time()[i];
            int z1 = zoneOf[u];
            int z2 = zoneOf[v];
            if (z1 != z2) {
                zoneAdj[z1].add(new int[]{z2, w});
            }
        }

        long[] longestPath = new long[zoneCount];
        long[] minCost = new long[zoneCount];
        Arrays.fill(minCost, Long.MAX_VALUE);

        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];

        minCost[startZone] = 0;
        longestPath[startZone] = 1;

        for (int i = 0; i < zoneCount; i++) {
            if (minCost[i] == Long.MAX_VALUE) continue;

            for (int[] edge : zoneAdj[i]) {
                int v = edge[0];
                int weight = edge[1];

                if (longestPath[i] + 1 > longestPath[v]) {
                    longestPath[v] = longestPath[i] + 1;
                }

                if (minCost[i] + weight < minCost[v]) {
                    minCost[v] = minCost[i] + weight;
                }
            }
        }

        long maxStages = 1;
        for (int i = 0; i < zoneCount; i++) {
            if (longestPath[i] > maxStages) {
                maxStages = longestPath[i];
            }
        }

        long routeCost = minCost[exitZone];
        return new long[]{maxStages, routeCost};
    }
}
