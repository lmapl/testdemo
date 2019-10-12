package junit;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by ma peiliang
 * Create Date: 2018/6/15 16:43
 * Description: ${DESCRIPTION}
 */
public class AssertMain {

    @Test
    public void assertThatTest(){
        Long testedNumber = 1L;
        assertThat(testedNumber, is(0L));
    }
}
