package log4j2;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

import java.io.Serializable;

/**
 * Created by ma peiliang
 * Create Date: 2018/7/16 16:49
 * Description: ${DESCRIPTION}
 */
public class MyAppender extends AbstractAppender {

    protected MyAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    @Override
    public void append(LogEvent logEvent) {

    }
}
