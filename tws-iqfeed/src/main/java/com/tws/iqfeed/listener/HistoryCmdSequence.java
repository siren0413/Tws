package com.tws.iqfeed.listener;

import com.tws.iqfeed.common.Sequence;
import com.tws.iqfeed.common.SequenceAction;
import com.tws.iqfeed.common.SequenceContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

/**
 * Created by yijunmao on 2/19/16.
 */
public class HistoryCmdSequence implements Sequence<String>{

    private List<SequenceAction<String>> children;

    @Required
    public void setChildren(List<SequenceAction<String>> children) {
        this.children = children;
    }

    @Override
    public String execute(String msg) {
        SequenceContext context = new SequenceContext();
        for(SequenceAction<String> action: children){
            if(!action.execute(context, msg)){
                break;
            }
        }
        return msg;
    }
}
