package com.tws.data;

import com.ib.client.Contract;
import com.tws.shared.Symbol;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chris on 1/18/16.
 */
public class ContractMapper implements InitializingBean{

    private Map<Symbol,Contract> contractMap = new HashMap<Symbol, Contract>();

    public Contract getContract(Symbol symbol){
        return contractMap.get(symbol);
    }

    public Contract getContract(String symbol){
        Symbol sym = Symbol.get(symbol);
        return contractMap.get(sym);
    }

    public void afterPropertiesSet() throws Exception {
        for(Symbol symbol: Symbol.values()){
            Contract contract = new Contract();
            contract.symbol(symbol.text());
            contract.secType(symbol.secType());
            contract.exchange(symbol.exchange());
            contract.primaryExch(symbol.primaryExchange());
            contract.currency(symbol.currency());
            contractMap.put(symbol, contract);
        }
    }
}
