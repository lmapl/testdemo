package jdk.jdk8newfeature.interfaceDefaultFunction;

public interface FourWheeler {
   default void print(){
      System.out.println("我是一辆FourWheeler!");
   }
}