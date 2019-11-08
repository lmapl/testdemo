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
 *  方法：选第一个元素为基准。 双向指针：从左向右找第一个大于基准的元素； 从右向左找第一个小于基准的元素，交换这两个元素； 然后继续找下一对，知道 双向指针交汇相等。需要注意此时，基准元素还在第一个位置，需要替换到交汇位置。
 *
 * 3 递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。 递归的最底部情形，是数列的大小是零或一，也就是永远
 * 经被排序好了。虽然一直递归下去，但是这个算法总会退出，因为在每次的迭代（iteration）中，它至少会把一个元素摆到它最后的位置去。
 *
 * 算法复杂度待补充
 *
 */
public class Quicksort {

    public static void quickSort(int[] arr,int low,int high){
        int i,j,temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        //temp就是基准位
        temp = arr[low];

        while (i<j) {
            //先看右边，依次往左递减
            while (temp<=arr[j]&&i<j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp>=arr[i]&&i<j) {
                i++;
            }
            //如果满足条件则交换
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;

        //递归调用左半数组
        quickSort(arr, low, j-1);
        //递归调用右半数组
        quickSort(arr, j+1, high);
    }

    public static void main(String[] args){

        //验证100次
        for(int j = 0;j<100;j++){

            int[] arr = new int[10];
            for(int i =0;i< arr.length;i++){
                arr[i] = (int) ( 100 * Math.random());
            }

            quickSort(arr,0,arr.length -1);

            for(int i =0;i< arr.length;i++){
                System.out.print(arr[i] +"  ");
            }
            System.out.println("");


        }

    }





}
