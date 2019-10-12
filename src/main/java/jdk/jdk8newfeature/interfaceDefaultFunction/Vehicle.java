package jdk.jdk8newfeature.interfaceDefaultFunction;

public interface Vehicle {

   //接口默认方法实现
   default void print(){
      //System.out.println("我是一辆Vehicle!");
   }

   //静态接口
    static void blowHorn(){
      //System.out.println("按喇叭!!!");
   }
}