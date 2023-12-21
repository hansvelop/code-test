import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        System.setIn(new FileInputStream("input.txt"));
        Scanner sc = new Scanner(System.in);
		int caseCnt = sc.nextInt();
        for(int c=0; c<caseCnt; c++){
            int mapSize = sc.nextInt();
            int map[][] = new int[mapSize][mapSize];

            for(int my=0; my<mapSize; my++){
                String yL = sc.next();
                for(int mx=0; mx<mapSize; mx++){
                    map[my][mx] = yL.charAt(mx);
                }
            }
        }
    }
}
