package com.tws.data;

import com.ib.client.*;
import com.tws.cassandra.model.Tick;
import com.tws.cassandra.repository.TickRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yijunmao on 1/17/16.
 */
public class DBEWrapper implements EWrapper {

    private static final Logger logger = LoggerFactory.getLogger(DBEWrapper.class);

    @Autowired
    private TickRepository tickRepository;

    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
        Tick tick = new Tick();
        tick.setSymbol("AAPL");
        tick.setTimestamp(System.currentTimeMillis());
        tick.setLastPrice(price);
        tickRepository.save(tick);
    }

    public void tickSize(int tickerId, int field, int size) {

    }

    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {

    }

    public void tickGeneric(int tickerId, int tickType, double value) {

    }

    public void tickString(int tickerId, int tickType, String value) {

    }

    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {

    }

    public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {

    }

    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {

    }

    public void openOrderEnd() {

    }

    public void updateAccountValue(String key, String value, String currency, String accountName) {

    }

    public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {

    }

    public void updateAccountTime(String timeStamp) {

    }

    public void accountDownloadEnd(String accountName) {

    }

    public void nextValidId(int orderId) {

    }

    public void contractDetails(int reqId, ContractDetails contractDetails) {

    }

    public void bondContractDetails(int reqId, ContractDetails contractDetails) {

    }

    public void contractDetailsEnd(int reqId) {

    }

    public void execDetails(int reqId, Contract contract, Execution execution) {

    }

    public void execDetailsEnd(int reqId) {

    }

    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {

    }

    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {

    }

    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {

    }

    public void managedAccounts(String accountsList) {

    }

    public void receiveFA(int faDataType, String xml) {

    }

    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {

    }

    public void scannerParameters(String xml) {

    }

    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {

    }

    public void scannerDataEnd(int reqId) {

    }

    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {

    }

    public void currentTime(long time) {

    }

    public void fundamentalData(int reqId, String data) {

    }

    public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {

    }

    public void tickSnapshotEnd(int reqId) {

    }

    public void marketDataType(int reqId, int marketDataType) {

    }

    public void commissionReport(CommissionReport commissionReport) {

    }

    public void position(String account, Contract contract, double pos, double avgCost) {

    }

    public void positionEnd() {

    }

    public void accountSummary(int reqId, String account, String tag, String value, String currency) {

    }

    public void accountSummaryEnd(int reqId) {

    }

    public void verifyMessageAPI(String apiData) {

    }

    public void verifyCompleted(boolean isSuccessful, String errorText) {

    }

    public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {

    }

    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {

    }

    public void displayGroupList(int reqId, String groups) {

    }

    public void displayGroupUpdated(int reqId, String contractInfo) {

    }

    @Override
    public void error(Exception e) {
        logger.error(EWrapperMsgGenerator.error(e));
    }

    @Override
    public void error(String str) {
        logger.error(EWrapperMsgGenerator.error(str));
    }

    @Override
    public void error(int id, int errorCode, String errorMsg) {
        logger.error(EWrapperMsgGenerator.error(id, errorCode, errorMsg));
    }

    public void connectionClosed() {

    }

    public void connectAck() {

    }
}
