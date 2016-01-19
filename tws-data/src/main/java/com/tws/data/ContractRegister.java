package com.tws.data;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.tws.shared.Symbol;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by chris on 1/18/16.
 */
public class ContractRegister {

    final static String ALL_GENERIC_TICK_TAGS = "100,101,104,105,106,107,165,221,225,233,236,258,293,294,295,318";

    private ContractMapper contractMapper;
    private EClientSocket client;

    public void register(Symbol symbol) {
        Contract contract = contractMapper.getContract(symbol);
        client.reqMktData(symbol.index(), contract, ALL_GENERIC_TICK_TAGS, false, null);
    }

    public void register(Iterable<Symbol> iterable){
        for(Symbol symbol: iterable){
            register(symbol);
        }
    }

    @Required
    public void setContractMapper(ContractMapper contractMapper) {
        this.contractMapper = contractMapper;
    }

    @Required
    public void setClient(EClientSocket client) {
        this.client = client;
    }
}
