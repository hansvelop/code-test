import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.StringTokenizer;

class Solution {


    private static final int CMD_INIT = 100;
    private static final int CMD_PUSH = 200;
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static StringTokenizer st;


    static class RockState{
        int y;
        int x;
        int dir;
        int cnt;

        public RockState(int y, int x, int dir, int cnt) {
            this.y = y;
            this.x = x;
            this.dir = dir;
            this.cnt = cnt;
        }
    }

    static class Pos{
        int y;
        int x;

        public Pos(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    static int size;
    static char[][] map;
    static int[] dy = new int[]{-1,0,1,0};
    static int[] dx = new int[]{0,1,0,-1};
    public static void init(int N, char[][] mMap) {
        size = N;
        map = new char[size][size];
        for(int y=0; y<size; y++){
            for(int x=0; x<size; x++) {
                map[y][x] = mMap[y][x];
            }
        }
    }


    public static boolean personMove(int mPersonR, int mPersonC, int maroR, int maroC) {
        boolean flag = false;
        ArrayDeque<RockState> pmQ = new ArrayDeque<>();
        boolean[][] pmV = new boolean[size][size];
        pmV[mPersonR][mPersonC]=true;
        pmQ.add(new RockState(mPersonR, mPersonC, 0, 0));
        while (!pmQ.isEmpty()){
            RockState personC = pmQ.poll();
            for(int d=0; d<4; d++) {
                int nny = personC.y - dy[d];
                int nnx = personC.x - dx[d];
                if (nny == maroR && nnx == maroC) {
                    if (map[nny][nnx] == '#' || map[nny][nnx] == 'R') continue;
                    if (pmV[nny][nnx]) continue;
                    pmV[nny][nnx] = true;
                    pmQ.add(new RockState(nny, nnx, d, personC.cnt + 1));
                    flag = true;
                }
            }
        }
        return flag;
    }

    public static int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
        ArrayDeque<RockState> stoneQ = new ArrayDeque<>();
        boolean[][][] stoneV = new boolean[size][size][4];
        stoneV[mRockR][mRockC][mDir]=true;
        stoneQ.add(new RockState(mRockR, mRockC, mDir, 0));
        while (!stoneQ.isEmpty()){
            RockState stoneC = stoneQ.poll();
            if(stoneC.y==mGoalR && stoneC.x==mGoalC){
                return stoneC.cnt;
            }
            movePush(stoneQ, stoneV, stoneC);
        }
        return -1;
    }

    private static void movePush(ArrayDeque<RockState> stoneQ, boolean[][][] stoneV, RockState stoneC){
        int findCnt=0;
        if(map[stoneC.y][stoneC.x+1]=='#' || map[stoneC.y][stoneC.x-1]=='#') findCnt+=2;
        if(map[stoneC.y+1][stoneC.x]=='#' || map[stoneC.y-1][stoneC.x]=='#') findCnt+=2;
        if(findCnt==4) return;
        ArrayDeque<Pos> personQ = new ArrayDeque<>();
        boolean[][] personV = new boolean[size][size];
        int personY = stoneC.y + dy[stoneC.dir];
        int personX = stoneC.x + dx[stoneC.dir];
        personV[personY][personX]=true;
        personQ.add(new Pos(personY, personX));
        while (!personQ.isEmpty()){
            Pos personC = personQ.poll();
            for(int d=0; d<4; d++){
                int ny = personC.y - dy[d];
                int nx = personC.x - dx[d];
                if(map[ny][nx] == '#') continue;
                if(ny==stoneC.y && nx==stoneC.x){
                    int nny = stoneC.y - dy[d];
                    int nnx = stoneC.x - dx[d];
                    if(map[nny][nnx]=='#') continue;
                    if(stoneV[nny][nnx][d]) continue;
                    stoneV[nny][nnx][d]=true;
                    stoneQ.add(new RockState(nny, nnx, d, stoneC.cnt + 1));
                    continue;
                }
                if(personV[ny][nx]) continue;
                personV[ny][nx]=true;
                personQ.add(new Pos(ny, nx));

            }
        }
    }




    static void print(int q, String cmd, int ans, int uans, Object... obj){
        if(ans!=uans) System.out.println("====오답====");
        System.out.println("["+q+"]" + cmd +"/"+ans+"="+uans+"/"+ Arrays.deepToString(obj));
    }

    private static int run(BufferedReader br) throws Exception {

        int N;
        boolean okay = false;
        int p[] = new int[2]; //소년
        int r[] = new int[2]; //바위
        int d[] = new int[2]; //구멍

        st = new StringTokenizer(br.readLine(), " ");
        N = Integer.parseInt(st.nextToken());
        char[][] region = new char[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            String rowStr = st.nextToken();
            for (int j = 0; j < N; j++) {
                region[i][j] = rowStr.charAt(j);
                if(region[i][j] == 'D'){
                    d = new int[]{i,j};
                }else if(region[i][j] == 'R'){
                    r = new int[]{i,j};
                }else if(region[i][j] == 'P'){
                    p = new int[]{i,j};
                }

            }
        }
        int dir = 0;
        if(r[0] < p[0] && r[1] == p[1] && personMove(p[0],p[1],r[0]+1,r[1])){
            dir = 2;
        }else if(r[0] == p[0] && r[1] < p[1] && personMove(p[0],p[1],r[0],r[1]+1)){
            dir = 1;
        }else if(r[0] == p[0] && r[1] > p[1] && personMove(p[0],p[1],r[0],r[1]-1)){
            dir = 3;
        }
        init(N, region);
        return push(r[0], r[1], dir, d[0], d[1]);
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("input_b.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(stinit.nextToken());
//        int MARK = Integer.parseInt(stinit.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br);
            System.out.println("#" + testcase + " " + score);
        }
        br.close();
    }
}