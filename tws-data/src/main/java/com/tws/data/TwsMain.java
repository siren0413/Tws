package com.tws.data;

import com.tws.shared.ib.Symbol;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 1/18/16.
 */
public class TwsMain {

    private static List<Symbol> symbols;

    static{
        symbols = new LinkedList<Symbol>();
        symbols.add(Symbol.SPX);
        symbols.add(Symbol.SPY);
        symbols.add(Symbol.DJX);
        symbols.add(Symbol.QQQ);
        symbols.add(Symbol.AAPL);
        symbols.add(Symbol.GOOG);
        symbols.add(Symbol.FB);
        symbols.add(Symbol.YHOO);
        symbols.add(Symbol.VZ);
        symbols.add(Symbol.MSFT);
        symbols.add(Symbol.NOK);
        symbols.add(Symbol.LNKD);
        symbols.add(Symbol.CME);
        symbols.add(Symbol.NKE);
        symbols.add(Symbol.AMZN);
        symbols.add(Symbol.VEEV);
        symbols.add(Symbol.SBUX);
        symbols.add(Symbol.EA);
        symbols.add(Symbol.TWTR);
        symbols.add(Symbol.SAP);
        symbols.add(Symbol.ATVI);
        symbols.add(Symbol.BABA);
        symbols.add(Symbol.JPM);
        symbols.add(Symbol.C);
        symbols.add(Symbol.AXP);
        symbols.add(Symbol.GPRO);
        symbols.add(Symbol.CTRP);
        symbols.add(Symbol.FIT);
        symbols.add(Symbol.QIHU);
        symbols.add(Symbol.DIS);
        symbols.add(Symbol.F);
        symbols.add(Symbol.GE);
        symbols.add(Symbol.GM);
        symbols.add(Symbol.GS);
        symbols.add(Symbol.HD);
        symbols.add(Symbol.IBM);
        symbols.add(Symbol.PYPL);
        symbols.add(Symbol.QCOM);
        symbols.add(Symbol.T);
        symbols.add(Symbol.TGT);
        symbols.add(Symbol.TWX);
        symbols.add(Symbol.V);
    }

    public static void main(String[] args){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("data-spring.xml");
        ContractRegister register = (ContractRegister)context.getBean("contractRegister");
        register.register(symbols);
    }
}
