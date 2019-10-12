package disruptor.sequence;

import com.lmax.disruptor.Sequence;

/**
 * Created by ap
 * Create Date: 2018/6/12 17:08
 * Description: ${DESCRIPTION}
 */
public class Main {
    public static void main(String[] args){
        Sequence sequence = new Sequence();
        System.out.println(sequence.incrementAndGet());
        System.out.println(sequence.incrementAndGet());
        System.out.println(sequence.addAndGet(10L));
    }
}
