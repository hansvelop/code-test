import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

class Main {



    static class RockState {
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

    static class Pos {
        int y;
        int x;

        public Pos(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    static int _SIZE;
    static char[][] _MAP;
    static int[] _DY = new int[]{-1, 0, 1, 0};
    static int[] _DX = new int[]{0, 1, 0, -1};


    public static int push(int mRockR, int mRockC, int mDir, int mGoalR, int mGoalC) {
        ArrayDeque<RockState> stoneQ = new ArrayDeque<>();
        boolean[][][] stoneV = new boolean[_SIZE][_SIZE][4];
        stoneQ.add(new RockState(mRockR, mRockC, mDir, 0));
        stoneV[mRockR][mRockC][mDir] = true;
        while (!stoneQ.isEmpty()) {
            RockState stoneC = stoneQ.poll();
            if (stoneC.y == mGoalR && stoneC.x == mGoalC) {
                return stoneC.cnt;
            }
            movePush(stoneQ, stoneV, stoneC);
        }
        return -1;
    }

    private static void movePush(ArrayDeque<RockState> stoneQ, boolean[][][] stoneV, RockState stoneC) {

        ArrayDeque<Pos> personQ = new ArrayDeque<>();
        boolean[][] personV = new boolean[_SIZE][_SIZE];
        int personY = stoneC.y + _DY[stoneC.dir];
        int personX = stoneC.x + _DX[stoneC.dir];

        if (_MAP[personY][personX] == '#' || _MAP[personY][personX] == 'D') return;
        personQ.add(new Pos(personY, personX));
        personV[personY][personX] = true;


        int findCnt = 0;
        if (_MAP[stoneC.y][stoneC.x + 1] == '#' || _MAP[stoneC.y][stoneC.x - 1] == '#') findCnt += 2;
        if (_MAP[stoneC.y + 1][stoneC.x] == '#' || _MAP[stoneC.y - 1][stoneC.x] == '#') findCnt += 2;
        if (findCnt == 4) return;
        while (!personQ.isEmpty()) {
            Pos personC = personQ.poll();
            for (int d = 0; d < 4; d++) {
                int ny = personC.y - _DY[d];
                int nx = personC.x - _DX[d];
                if (ny < 1 || nx < 1 || ny > _SIZE - 1 || nx > _SIZE - 1) continue;

                if (_MAP[ny][nx] == '#' || _MAP[ny][nx] == 'D') continue;
                if (ny == stoneC.y && nx == stoneC.x) {
                    int nny = stoneC.y - _DY[d];
                    int nnx = stoneC.x - _DX[d];
                    if (nny < 1 || nnx < 1 || nny > _SIZE - 1 || nnx > _SIZE - 1) continue;
                    if (_MAP[nny][nnx] == '#') continue;
                    if (stoneV[nny][nnx][d]) continue;
                    stoneV[nny][nnx][d] = true;
                    stoneQ.add(new RockState(nny, nnx, d, stoneC.cnt + 1));

                    findCnt++;
                    if (findCnt == 4) continue;

                    continue;
                }
                if (personV[ny][nx]) continue;
                personV[ny][nx] = true;
                personQ.add(new Pos(ny, nx));

            }
        }
    }


    public static void main(String[] args) throws Exception {

        StringTokenizer st;
        System.setIn(new java.io.FileInputStream("input_b.txt"));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(stinit.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase) {

            int rockX = 0; //바위 X 좌표
            int rockY = 0; //바위 Y 좌표
            int hollX = 0; //구멍
            int hollY = 0; //구멍

            st = new StringTokenizer(br.readLine(), " ");
            _SIZE = Integer.parseInt(st.nextToken());
            _MAP = new char[_SIZE][_SIZE];
            for (int i = 0; i < _SIZE; i++) {
                st = new StringTokenizer(br.readLine());
                String rowStr = st.nextToken();
                for (int j = 0; j < _SIZE; j++) {
                    _MAP[i][j] = rowStr.charAt(j);
                    if (_MAP[i][j] == 'D') {
                        hollX = i;
                        hollY = j;
                    } else if (_MAP[i][j] == 'R') {
                        rockX = i;
                        rockY = j;
                    }

                }
            }

            // Rock BFS
            int value = Integer.MAX_VALUE;
            for (int i = 0; i < 4; i++) {
                int move = push(rockX, rockY, i, hollX, hollY);
                if (move != -1 && value > move) {
                    value = move;
                }
            }
            if(value == Integer.MAX_VALUE) value = -1;

            System.out.println("#" + testcase + " " + value);
        }
        br.close();
    }
}