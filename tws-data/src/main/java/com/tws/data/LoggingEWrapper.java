package com.tws.data;


import com.ib.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chris on 1/17/16.
 */
public class LoggingEWrapper implements EWrapper {

    private static final Logger logger = LoggerFactory.getLogger(LoggingEWrapper.class);

    @Override
    public void tickPrice(int tickerId, int field, double price, int canAutoExecute) {
        logger.info(EWrapperMsgGenerator.tickPrice(tickerId, field, price, canAutoExecute));
    }

    @Override
    public void tickSize(int tickerId, int field, int size) {
        logger.info(EWrapperMsgGenerator.tickSize(tickerId, field, size));
    }

    @Override
    public void tickOptionComputation(int tickerId, int field, double impliedVol, double delta, double optPrice, double pvDividend, double gamma, double vega, double theta, double undPrice) {
        logger.info(EWrapperMsgGenerator.tickOptionComputation(tickerId, field, impliedVol, delta, optPrice, pvDividend, gamma, vega, theta, undPrice));
    }

    @Override
    public void tickGeneric(int tickerId, int tickType, double value) {
        logger.info(EWrapperMsgGenerator.tickGeneric(tickerId, tickType, value));
    }

    @Override
    public void tickString(int tickerId, int tickType, String value) {
        logger.info(EWrapperMsgGenerator.tickString(tickerId, tickType, value));
    }

    @Override
    public void tickEFP(int tickerId, int tickType, double basisPoints, String formattedBasisPoints, double impliedFuture, int holdDays, String futureLastTradeDate, double dividendImpact, double dividendsToLastTradeDate) {
        logger.info(EWrapperMsgGenerator.tickEFP(tickerId, tickType, basisPoints, formattedBasisPoints, impliedFuture, holdDays, futureLastTradeDate, dividendImpact, dividendsToLastTradeDate));
    }

    @Override
    public void orderStatus(int orderId, String status, double filled, double remaining, double avgFillPrice, int permId, int parentId, double lastFillPrice, int clientId, String whyHeld) {
        logger.info(EWrapperMsgGenerator.orderStatus(orderId, status, filled, remaining, avgFillPrice, permId, parentId, lastFillPrice, clientId, whyHeld));
    }

    @Override
    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        logger.info(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    @Override
    public void openOrderEnd() {
        logger.info(EWrapperMsgGenerator.openOrderEnd());
    }

    @Override
    public void updateAccountValue(String key, String value, String currency, String accountName) {
        logger.info(EWrapperMsgGenerator.updateAccountValue(key, value, currency, accountName));
    }

    @Override
    public void updatePortfolio(Contract contract, double position, double marketPrice, double marketValue, double averageCost, double unrealizedPNL, double realizedPNL, String accountName) {
        logger.info(EWrapperMsgGenerator.updatePortfolio(contract, (int) position, marketPrice, marketValue, averageCost, unrealizedPNL, realizedPNL, accountName));
    }

    @Override
    public void updateAccountTime(String timeStamp) {
        logger.info(EWrapperMsgGenerator.updateAccountTime(timeStamp));
    }

    @Override
    public void accountDownloadEnd(String accountName) {
        logger.info(EWrapperMsgGenerator.accountDownloadEnd(accountName));
    }

    @Override
    public void nextValidId(int orderId) {
        logger.info(EWrapperMsgGenerator.nextValidId(orderId));
    }

    @Override
    public void contractDetails(int reqId, ContractDetails contractDetails) {
        logger.info(EWrapperMsgGenerator.contractDetails(reqId, contractDetails));
    }

    @Override
    public void bondContractDetails(int reqId, ContractDetails contractDetails) {
        logger.info(EWrapperMsgGenerator.bondContractDetails(reqId, contractDetails));
    }

    @Override
    public void contractDetailsEnd(int reqId) {
        logger.info(EWrapperMsgGenerator.contractDetailsEnd(reqId));
    }

    @Override
    public void execDetails(int reqId, Contract contract, Execution execution) {
        logger.info(EWrapperMsgGenerator.execDetails(reqId, contract, execution));
    }

    @Override
    public void execDetailsEnd(int reqId) {
        logger.info(EWrapperMsgGenerator.execDetailsEnd(reqId));
    }

    @Override
    public void updateMktDepth(int tickerId, int position, int operation, int side, double price, int size) {
        logger.info(EWrapperMsgGenerator.updateMktDepth(tickerId, position, operation, side, price, size));
    }

    @Override
    public void updateMktDepthL2(int tickerId, int position, String marketMaker, int operation, int side, double price, int size) {
        logger.info(EWrapperMsgGenerator.updateMktDepthL2(tickerId, position, marketMaker, operation, side, price, size));
    }

    @Override
    public void updateNewsBulletin(int msgId, int msgType, String message, String origExchange) {
        logger.info(EWrapperMsgGenerator.updateNewsBulletin(msgId, msgType, message, origExchange));
    }

    @Override
    public void managedAccounts(String accountsList) {
        logger.info(EWrapperMsgGenerator.managedAccounts(accountsList));
    }

    @Override
    public void receiveFA(int faDataType, String xml) {

    }

    @Override
    public void historicalData(int reqId, String date, double open, double high, double low, double close, int volume, int count, double WAP, boolean hasGaps) {
        logger.info(EWrapperMsgGenerator.historicalData(reqId, date, open, high, low, close, volume, count, WAP, hasGaps));
    }

    @Override
    public void scannerParameters(String xml) {

    }

    @Override
    public void scannerData(int reqId, int rank, ContractDetails contractDetails, String distance, String benchmark, String projection, String legsStr) {

    }

    @Override
    public void scannerDataEnd(int reqId) {

    }

    @Override
    public void realtimeBar(int reqId, long time, double open, double high, double low, double close, long volume, double wap, int count) {
        logger.info(EWrapperMsgGenerator.realtimeBar(reqId, time, open, high, low, close, volume, wap, count));
    }

    @Override
    public void currentTime(long time) {
        logger.info(EWrapperMsgGenerator.currentTime(time));
    }

    @Override
    public void fundamentalData(int reqId, String data) {
        logger.info(EWrapperMsgGenerator.fundamentalData(reqId, data));
    }

    @Override
    public void deltaNeutralValidation(int reqId, DeltaNeutralContract underComp) {

    }

    @Override
    public void tickSnapshotEnd(int reqId) {
        logger.info(EWrapperMsgGenerator.tickSnapshotEnd(reqId));
    }

    @Override
    public void marketDataType(int reqId, int marketDataType) {
        logger.info(EWrapperMsgGenerator.marketDataType(reqId, marketDataType));
    }

    @Override
    public void commissionReport(CommissionReport commissionReport) {
        logger.info(EWrapperMsgGenerator.commissionReport(commissionReport));
    }

    @Override
    public void position(String account, Contract contract, double pos, double avgCost) {
        logger.info(EWrapperMsgGenerator.position(account, contract, pos, avgCost));
    }

    @Override
    public void positionEnd() {
        logger.info(EWrapperMsgGenerator.positionEnd());
    }

    @Override
    public void accountSummary(int reqId, String account, String tag, String value, String currency) {
        logger.info(EWrapperMsgGenerator.accountSummary(reqId, account, tag, value, currency));
    }

    @Override
    public void accountSummaryEnd(int reqId) {
        logger.info(EWrapperMsgGenerator.accountSummaryEnd(reqId));
    }

    @Override
    public void verifyMessageAPI(String apiData) {
        logger.info("verify message API, data [{}]", apiData);
    }

    @Override
    public void verifyCompleted(boolean isSuccessful, String errorText) {
        logger.info("verify message API complete.");
    }

    @Override
    public void verifyAndAuthMessageAPI(String apiData, String xyzChallange) {
        logger.info("verify and auth message API, data [{}], xyzChallenge [{}]", apiData, xyzChallange);
    }

    @Override
    public void verifyAndAuthCompleted(boolean isSuccessful, String errorText) {
        logger.info("verify and auth complete. isSuccessful [{}], error [{}]", isSuccessful, errorText);
    }

    @Override
    public void displayGroupList(int reqId, String groups) {

    }

    @Override
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

    @Override
    public void connectionClosed() {
        logger.info(EWrapperMsgGenerator.connectionClosed());
    }

    @Override
    public void connectAck() {
        logger.info("Connection ACK.");
    }
}
