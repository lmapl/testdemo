package jdk.jdk8newfeature.functionParam;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ma PeiLiang
 * Create Date: 2018/4/18 17:25
 * Description: ${DESCRIPTION}
 */
public class CarTest {

    public static void main(String[] args){
        Car car = Car.create(Car::new);
        if(car != null){
            car.setName("car1");
        }

        Car car2 = Car.create(Car::new);
        if(car2 != null){
            car2.setName("car2");
        }

        final List< Car > cars = Arrays.asList(car,car2);

        //cars中的每个元素作为参数传入collide 方法
        //cars.forEach( Car::collide );

        //car中的每个元素调用自己的repair方法
       cars.forEach(Car::repair);

        //police 执行自己的follow 方法，参数是cars中的每个元素
        /*final Car police = Car.create( Car::new );
        police.setName("car3");
        cars.forEach( police::follow );*/

        //cars.forEach(System.out::println);
    }
}
