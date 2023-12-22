import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Main {
    static int[] dx = {0,1,0,-1};
    static int[] dy = {-1,0,1,0};
    static char[][] map;
    static int[][] dis;
    public static void main(String[] args) throws FileNotFoundException {
        System.setIn(new FileInputStream("input.txt"));
        Scanner sc = new Scanner(System.in);
		int caseCnt = sc.nextInt();
        for(int c=0; c<caseCnt; c++){
            int mapSize = sc.nextInt();
            map = new char[mapSize][mapSize];
            dis = new int[mapSize][mapSize];
            int p[] = new int[2]; //소년
            int r[] = new int[2]; //바위
            int d[] = new int[2]; //구멍
            for(int my=0; my<mapSize; my++){
                String yL = sc.next();
                for(int mx=0; mx<mapSize; mx++){
//                    System.out.print(yL.charAt(mx));
                    map[my][mx] = yL.charAt(mx);
                    if(yL.charAt(mx) == 'D'){
                        d = new int[]{my,mx};
                    }else if(yL.charAt(mx) == 'R'){
                        r = new int[]{my,mx};
                    }else if(yL.charAt(mx) == 'P'){
                        p = new int[]{my,mx};
                    }

                }
//                System.out.println();
            }


            bfs(r[0], r[1], mapSize);

            System.out.println(dis[d[0]][d[1]]);


        }
    }

    private static void bfs(int y, int x, int mapSize){
        Queue<Point> q = new LinkedList<>();
        q.offer(new Point(x,y));
        map[y][x] = 'x';
        while (!q.isEmpty()){
            Point tmp = q.poll();
            for(int i=0; i<4; ++i){
                int nx = tmp.x + dx[i];
                int ny = tmp.y + dy[i];
                if(nx >= 0
                        && nx < mapSize
                        && ny >= 0
                        && ny < mapSize
                        && map[ny][nx] == '.'
                ){
                    map[ny][nx] = 'x';
                    q.offer(new Point(nx, ny));
                    dis[nx][ny] = dis[tmp.x][tmp.y] + 1 ;
                }
            }
        }
    }
}

class Point{
    public int x,y;
    Point(int x, int y){
        this.x = x;
        this.y = y;
    }
}
