package zongyi.settings.test;

import java.util.Scanner;

public class ProblemD {


    public static void main(String[] args) {



        sayNumber();
    }

    // dt的处理方法
    private static void sayNumber() {

        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        String strN = String.valueOf(N);
        // 1. 如果上一个同学反应不对，那么 dt 应该说 DENG DENG DONG
        if(strN.contains("3")){
            System.out.println("DENG DENG DENG");
            return;
        }else if(strN.contains("7")){
            System.out.println("DENG DENG DENG");
            return;
        }else if(N%3 == 0){
            System.out.println("DENG DENG DENG");
            return;
        }else if(N%7 == 0){
            System.out.println("DENG DENG DENG");
            return;
        }

        // 2. 如果上一个同学没错，dt 应该拍手的话
        int dtN = N + 1;
        String strDtN = String.valueOf(dtN);
        if(strDtN.contains("3")){
            System.out.println("clap");
            return;
        }else if(strDtN.contains("7")){
            System.out.println("clap");
            return;
        }else if(dtN%3 == 0){
            System.out.println("clap");
            return;
        }else if(dtN%7 == 0){
            System.out.println("clap");
            return;
        }

        // 3. 如果上一个同学没错，dt 此时应该直接报出他自己的数字
        System.out.println(dtN);

    }

}
