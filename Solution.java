import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.StringTokenizer;


class UserSolution {

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

    int size;
    int[][] map;
    int[] dy = new int[]{-1,0,1,0};
    int[] dx = new int[]{0,1,0,-1};
    public void init(int N, int[][] mMap) {
        size = N;
        map = new int[size][size];
        for(int y=0; y<size; y++){
            for(int x=0; x<size; x++) {
                map[y][x] = mMap[y][x];
            }
        }
        return;
    }

    public int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
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
        return 0;
    }

    private void movePush(ArrayDeque<RockState> stoneQ, boolean[][][] stoneV, RockState stoneC){
        int findCnt=0;
        if(map[stoneC.y][stoneC.x+1]==1 || map[stoneC.y][stoneC.x-1]==1) findCnt+=2;
        if(map[stoneC.y+1][stoneC.x]==1 || map[stoneC.y-1][stoneC.x]==1) findCnt+=2;
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
                if(map[ny][nx] == 1) continue;
                if(ny==stoneC.y && nx==stoneC.x){
                    int nny = stoneC.y - dy[d];
                    int nnx = stoneC.x - dx[d];
                    if(map[nny][nnx]==1) continue;
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
}

public class Solution {


    private static final int CMD_INIT = 100;
    private static final int CMD_PUSH = 200;

    private static UserSolution userSolution = new UserSolution();

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static StringTokenizer st;


    static void print(int q, String cmd, int ans, int uans, Object... obj){
        if(ans!=uans) System.out.println("====오답====");
        System.out.println("["+q+"]" + cmd +"/"+ans+"="+uans+"/"+ Arrays.deepToString(obj));
    }

    private static boolean run(BufferedReader br) throws Exception {

        int cmd, ans, ret;
        int N, r, c, dir, r2, c2;
        int Q = 0;
        boolean okay = false;
        int[][] region = new int[30][30];

        st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        for (int q = 0; q < Q; ++q) {

            st = new StringTokenizer(br.readLine(), " ");
            cmd = Integer.parseInt(st.nextToken());

            switch (cmd) {
                case CMD_INIT:
                    N = Integer.parseInt(st.nextToken());
                    for (int i = 0; i < N; i++) {
                        st = new StringTokenizer(br.readLine());
                        for (int j = 0; j < N; j++) {
                            region[i][j] = Integer.parseInt(st.nextToken());
                        }
                    }
                    userSolution.init(N, region);
                    print(q,"init",0,0,N,region);
                    okay = true;
                    break;
                case CMD_PUSH:
                    r = Integer.parseInt(st.nextToken());
                    c = Integer.parseInt(st.nextToken());
                    dir = Integer.parseInt(st.nextToken());
                    r2 = Integer.parseInt(st.nextToken());
                    c2 = Integer.parseInt(st.nextToken());
                    ret = userSolution.push(r, c, dir, r2, c2);
                    ans = Integer.parseInt(st.nextToken());
                    print(q,"push",ans,ret,c, dir,r2,c2);
                    if (ret != ans)
                        okay = false;
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        System.setIn(new java.io.FileInputStream("input.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(stinit.nextToken());
        int MARK = Integer.parseInt(stinit.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }
        br.close();
    }
}