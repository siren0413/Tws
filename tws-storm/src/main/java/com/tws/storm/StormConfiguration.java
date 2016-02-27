package com.tws.storm;

import com.tws.shared.common.TimeUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.propertyeditors.TimeZoneEditor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by chris on 2/26/16.
 */
public class StormConfiguration implements InitializingBean {

    private boolean mock;
    private String mockStartDateTime;
    private String symbolString;
    private List<String> symbolList;

    @Override
    public void afterPropertiesSet() throws Exception {
        symbolList = new LinkedList<>();
        StringTokenizer tokenizer = new StringTokenizer(symbolString, ",");
        while(tokenizer.hasMoreTokens()){
            symbolList.add(tokenizer.nextToken().trim());
        }
        Utils.currentZonedDateTime = LocalDateTime.parse(mockStartDateTime, TimeUtils.dateTimeSecFormatter).atZone(TimeUtils.ZONE_EST);
    }

    public boolean isMock() {
        return mock;
    }

    public List<String> getSymbolList() {
        return symbolList;
    }



    @Required
    public void setMock(boolean mock) {
        this.mock = mock;
    }

    @Required
    public void setSymbolString(String symbolString) {
        this.symbolString = symbolString;
    }

    @Required
    public void setMockStartDateTime(String mockStartDateTime) {
        this.mockStartDateTime = mockStartDateTime;
    }
}
