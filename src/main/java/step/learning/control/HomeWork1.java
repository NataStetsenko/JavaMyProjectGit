package step.learning.control;

public class HomeWork1 {
    int arrResult[][];
    {
        arrResult = new int[10][10];
        int arr[] = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int x : arr) {
            for (int y : arr) {
                arrResult[x - 1][y - 1] = x * y;
            }
        }
    }

    public void print() {
        for (int i = 0; i < arrResult.length; i++) {
            for (int j = 0; j < arrResult[i].length; j++) {
                if (j == 0 && i == 0)
                    System.out.print("" + "\t");
                else
                    System.out.print(arrResult[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
