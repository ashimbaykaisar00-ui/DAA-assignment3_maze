# Maze Escape

**Student:** Ashimbay Kaisar, **ID:** 254093, **Group:** SE-2540

## Program output

My maze is 8 rows by 12 columns (96 cells).

```
Broken: 95
Kept: 77
Last broken wall: 147
MST cost: 30375
Heaviest broken wall: 828
Path length: 24
DFS visited: 57
Dead ends: 29
Zones: 96
Largest zone: 1
Is DAG: 1
Flow stages: 15
Fastest route cost: 21
```

Picture of my maze: `output/maze.png` (the page `output/maze.html` shows the missions on the picture).

## Answers to the questions

**Mission 1. Why is Broken always R*C - 1?**
At the start every cell is alone, so there are R*C groups. A wall is broken only when `union` returns true, and every such union joins two groups into one, so the number of groups goes down by exactly 1. The maze is connected at the end (one group), so there were exactly R*C - 1 successful unions. For my maze 96 - 1 = 95.

**Mission 1. Why is Kept always (R-1)*(C-1)?**
The maze has R*(C-1) walls between cells that are left and right of each other, and C*(R-1) walls between cells that are above and below each other. The total is 2*R*C - R - C. If I subtract the broken walls (R*C - 1), I get R*C - R - C + 1 = (R-1)*(C-1). For my maze 7 * 11 = 77, and 95 + 77 = 172 is the total number of walls.

**Mission 3. Why is there exactly one path?**
The broken walls join all R*C cells with R*C - 1 edges and never make a cycle (a wall that would make a cycle is not broken, because `union` returns false). A connected graph without cycles is a tree, and in a tree there is exactly one simple path between any two cells. If there were two different paths, together they would make a cycle.

**Mission 4. Why does the second pass go on the reversed graph?**
The cell that finishes last in the first pass is in a zone that has no slides coming in from other zones (a "source" zone). On the normal graph a DFS from this cell could also leave the zone and take other zones with it. On the reversed graph the DFS can reach only the cells that can reach our cell, and these are exactly the cells of its own zone. So every DFS of the second pass finds exactly one zone. Because the zones are found in this order (sources first), the numbers of the zones are a topological order.

**Mission 4. Why is there no cycle exactly when Zones equals the number of cells?**
If there is a cycle, the cells of the cycle can reach each other, so they are in one zone and the number of zones is smaller than the number of cells. If there is no cycle, no two cells can reach each other both ways, so every cell is a zone alone and Zones = number of cells. In my data Zones = 96 = number of cells, so Is DAG = 1.

**Mission 5. Why is going through the zones in the order of their numbers enough?**
Zone numbers are a topological order, so every slide between zones goes from a smaller number to a larger one. When I come to zone z, all zones that have a slide into z have smaller numbers, so they are already processed and the values for z (longest chain and cheapest cost) are final. So I do not need a priority queue: one pass is enough.

**Mission 5. What is the cost of the pass?**
O(n + m): every zone is visited once and every slide is looked at once (plus O(n + m) for Kosaraju, which comes before it).

## AI use

[Write the truth. For example:] I used Claude (Anthropic) as a helper. I used it to get a first version of the code for missions 0-5 and to explain how each algorithm and each line works. After that I read the code, ran the checks and my own data, and studied it until I could explain every line myself. The answers to the README questions are written by me. [Change this to what really happened.]

## Timing (Kruskal and Prim)

Time in milliseconds, average of 5 runs, output of `java -cp out Bench`:

| size | Kruskal (ms) | Prim (ms) |
|---|---|---|
| 100x100 | [ ] | [ ] |
| 300x300 | [ ] | [ ] |
| 1000x1000 | [ ] | [ ] |

[Write 2-3 sentences using your numbers. For example: When the maze side grows 3 times, the number of cells and walls grows about 9 times, and the time grows about 9 times or a bit more. Both algorithms are O(m log m), so the growth is a little faster than linear, and they have similar times. My own numbers: ...]

## Complexity

n = number of cells, m = number of walls or slides.

| Algorithm | Time | Space |
|---|---|---|
| DSU `find` / `union` | O(α(n)), almost constant | O(n) |
| Kruskal (sort + DSU) | O(m log m) | O(n + m) |
| Prim (priority queue) | O(m log m) | O(n + m) |
| DFS in the maze (iterative) | O(n) | O(n) |
| Kosaraju (two passes) | O(n + m) | O(n + m) |
| Flow stages and fastest route in the zone graph | O(n + m) | O(n + m) |