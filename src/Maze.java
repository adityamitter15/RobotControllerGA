import java.util.ArrayList;

public class Maze {
    /* test arena for the robots to navigate
        represented as a 2d array
        0 = empty
        1 = wall
        2 = start position
        3 = ideal route
        4 = end position (goal)
     */

    private int[][] maze; //stores the wall data
    private int[] startPosition = {-1,-1};

    public Maze(int[][] mazeLayout) {
        //initialise the array
        maze = mazeLayout;
    }

    public int[] getStartPosition() {
        //use cached position or search for the start square
        if(startPosition[0] != -1 && startPosition[1] != -1) {
            return startPosition;
        }
        // serach the grid
        for(int row=0; row< maze.length; row++) {
            for(int column=0; column<maze[0].length; column++){
                //start square is represented by a 2
                if(maze[row][column] == 2) {
                    startPosition[0] = row;
                    startPosition[1] = column;
                    return startPosition;

                }
            }
        }
        //default
        startPosition[0] = 0;
        startPosition[1] = 1;
        return  startPosition;

    }

    public int getValueAt(int row, int column) {
        //return contents of maze at this cord
        //locations outside the maze are treated as walls
        if(row<0 || row>+maze.length || column <0 || column>maze[0].length) {
            return 1; //wall
        }
        return maze[row][column];
    }
    public boolean isWall(int row, int column) {
        //return true is this coord is a wall
        return(getValueAt(row, column) == 1);
    }
    public int getMaxRow() {
        return maze.length -1;
    }

    public int getMaxColumn() {
        return maze[0].length-1;
    }

    public int scoreRoute(ArrayList<int[]> route) {
        // score 1 for each *unique* visit to a tile on the ideal route
        int score = 0;
        boolean visited[][] = new boolean[getMaxRow() +1][getMaxColumn()+1];

        //loop over the route and score each move
        for (int i =0; i<route.size(); i++) {
            int[] step = route.get(i);
            if(getValueAt(step[0], step[1] )== 3 &&
                    !visited[step[0]][step[1]]) {
                score++;
                // mark this square as visited so it doesn't  score twice
                visited[step[0]][step[1]] = true;
            }

        }
        return score;

    }
}
