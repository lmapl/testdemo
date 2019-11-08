package algorithm.base;

/**
 * 快速排序
 *
 * 快速排序使用分治法（Divide and conquer）策略来把一个串行（list）分为两个子串行（sub-lists）。
 *
 * 算法步骤：
 * 1 从数列中挑出一个元素，称为 "基准"（pivot），
 * 2 重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。
 *  一次排序后基准就处于数列的中间位置。这个称为分区（partition）操作。
 * 3 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。 递归的最底部情形，是数列的大小是零或一，也就是永远
 * 经被排序好了。虽然一直递归下去，但是这个算法总会退出，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。
 *
 * 算法复杂度待补充
 *
 */
public class Quicksort2 {

    public static void f1(int[] arr, int indexFirst ,int indexLast) {
        int start = indexFirst;
        int end = indexLast;
        if (indexFirst == indexLast) {
            return;
        }
        int mid = arr[start];
        int temp;

        System.out.println("------start="+indexFirst + " end="+indexLast +" mid="+mid + "-------------");
        print(arr);

        while (start < end) {
            if(arr[end] >= mid){
                end --;
                continue;
            }
            //找到一个左边大于mid，右边小于mid的组合，互换
            temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start = end;
        }

        print(arr);

        f1(arr,indexFirst,start );
        f1(arr,start+1,indexLast);
    }

    private static  void print(int[] arr){
        for(int i =0;i< arr.length;i++){
            System.out.print(arr[i] +"  ");
        }
        System.out.println("");
    }

    public static void main(String[] args){
        int[] arr = new int[10];
        for(int i =0;i< arr.length;i++){
            arr[i] = (int) ( 100 * Math.random());
        }

        f1(arr,0,arr.length -1);

        for(int i =0;i< arr.length;i++){
            System.out.print(arr[i] +"  ");
        }
    }

}
