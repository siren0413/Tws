package com.tws.iqfeed.common;

/**
 * Created by yijunmao on 2/19/16.
 */
public interface SequenceAction<T> {
    boolean execute(SequenceContext ctx, T item);
}
