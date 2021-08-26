package zongyi.settings.test;

import java.util.Scanner;

public class ProblemG {


    public static void main(String[] args) {

        threeArrayToZero();

    }

    private static void threeArrayToZero() {


        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入~");

        // 读取三个数组的长度和元素
        int array1Length = scanner.nextInt();
        int[] array1 = new int[array1Length];
        for (int i = 0; i < array1Length; i++) {
            array1[i] = scanner.nextInt();
        }

        int array2Length = scanner.nextInt();
        int[] array2 = new int[array2Length];
        for (int i = 0; i < array2Length; i++) {
            array2[i] = scanner.nextInt();
        }
        int array3Length = scanner.nextInt();
        int[] array3 = new int[array3Length];
        for (int i = 0; i < array3Length; i++) {
            array3[i] = scanner.nextInt();
        }

        int flag = 0;

        for (int i = 0; i < array1Length; i++) {

            for (int j = 0; j < array2Length; j++) {
                for (int k = 0; k < array3Length; k++) {
                    if(array1[i] + array2[j] + array3[k] == 0){
                        flag++;
                    }
                }
            }

        }

        System.out.println(flag);




    }


}
