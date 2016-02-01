package com.tws.iqfeed.grizzly;

import com.tws.message.KafkaProducer;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Created by admin on 1/29/2016.
 */
public class ClientFilter extends BaseFilter {

    /**
     * Handle just read operation, when some message has come and ready to be
     * processed.
     *
     * @param ctx Context of {@link FilterChainContext} processing
     * @return the next action
     * @throws java.io.IOException
     */

    private static final Logger logger = LoggerFactory.getLogger(ClientFilter.class);

    @Override
    public NextAction handleRead(final FilterChainContext ctx) throws IOException {
        // We get String message from the context, because we rely prev. Filter in chain is StringFilter

        final String serverResponse = ctx.getMessage();
//        System.out.println("Server echo: " + serverResponse);
        logger.info(this.toString() +" " +serverResponse);
        return ctx.getStopAction();
    }
}
