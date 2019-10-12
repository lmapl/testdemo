package jdk.jdk8newfeature.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * Created by Ma PeiLiang
 * Create Date: 2018/4/18 18:16
 * Description: ${DESCRIPTION}
 */
public class StreamTest1 {
    public static void main(String[] args){


        //'forEach' 来迭代流中的每个数据。
        //List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        //strings.stream().forEach(System.out::println);

       // map 方法用于对每个元素执行某种操作后的对应的结果，以下代码片段使用 map 输出了元素对应的平方数：
        /*List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> squaresList = numbers.stream().map( i -> i*i).collect(Collectors.toList());
        numbers.stream().forEach(System.out::println);
        squaresList.stream().forEach(System.out::println);*/


       // filter 方法用于通过设置的条件过滤出元素。以下代码片段使用 filter 方法过滤出空字符串：
        /*List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> count = strings.stream().filter(string -> !string.isEmpty() &&  !string.equals("bc") ).collect(Collectors.toList());
        count.stream().forEach(System.out::println);*/

        //limit 方法用于获取指定数量的流。 以下代码片段使用 limit 方法打印出 10 条数据：
        /*List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        strings.stream().limit(4).forEach(System.out::println);*/

        //sorted 方法用于对流进行排序。以下代码片段使用 sorted 方法对输出的 10 个随机数进行排序：
        /*List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        strings.stream().sorted().forEach(System.out::println);*/


       // 并行（parallel）程序    parallelStream 是流并行处理程序的代替方法。以下实例我们使用 parallelStream 来输出空字符串的数量：
        /*List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        long count = strings.parallelStream().filter(string -> !string.isEmpty()).count();
        System.out.println(count);*/


        //Collectors 类实现了很多归约操作，例如将流转换成集合和聚合元素。Collectors 可用于返回列表或字符串：
        /*List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println("筛选列表: " + filtered);

        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);*/

        //统计 一些产生统计结果的收集器也非常有用。它们主要用于int、double、long等基本类型上，它们可以用来产生类似如下的统计结果。

        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        IntSummaryStatistics stats = numbers.stream().mapToInt(x -> x).summaryStatistics();
       // System.out.println("列表中最大的数 : " + stats.getMax());
        //System.out.println("列表中最小的数 : " + stats.getMin());
        //System.out.println("所有数之和 : " + stats.getSum());
       // System.out.println("平均数 : " + stats.getAverage());
    }
}
