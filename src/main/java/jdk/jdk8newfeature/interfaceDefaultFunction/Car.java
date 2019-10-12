package jdk.jdk8newfeature.interfaceDefaultFunction;

public class Car implements Vehicle, FourWheeler {
   public void print() {
      Vehicle.super.print();
      FourWheeler.super.print();
      Vehicle.blowHorn();
      //System.out.println("我是一辆汽车!");
   }
}