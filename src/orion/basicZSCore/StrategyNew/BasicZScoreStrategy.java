package orion.basicZSCore.StrategyNew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import orion.DataLoadfromTxt.*;

import orion.classModel.*;


import orion.utility.*;


public class BasicZScoreStrategy {
	private static final int LAST_N_EOD_ENTRIES_FOR_ZSCORE = 20;

	private static final int MAX_TRADES_IN_PAIR_THRESHOLD = 9;
	private static final int MAX_TRADES_IN_PAIR_PER_DAY = 3;
	private static final int MAX_HOLDING_DAY    = 1000;

	private static final double DIFF_TO_ENTER_NEXT_TRADE = 0.5;
	private static final double ENTRY_ZSCORE = -1.5;
	private static final double ZSCORE_DIFF_TO_EXIT_TRADE = 1;
	
	private static final double PROFIT_TAKING = 0.5;
	
	private static final double STOPLOSS_FIRST_2_BETS = -8;
	private static final double STOPLOSS_3RD_BETS = -4;
	private static final double EXPOSURE_PER_DAY = 10000;
	private static final int DURATION_OF_CURRENT_SINCE_OPEN = 3;
	private static final int DURATION_OF_5TH_DAY_SINCE_OPEN = 4;
	
	private final Map<StockPair,List<PairTrade>> openPairTradesByPair = new HashMap<StockPair,List<PairTrade>>();
	private final Map<Date,Map<StockPair,Integer>> numberOfTradesByPairByDate = new HashMap<>();
	
	private static final String OLD_PAIRS = "C:\\Users\\ARCHANA\\Pairs_Data-20190311T120701Z-001\\Pairs_Data\\Pairs.txt";
	
	private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyyMMdd|HH:mm:ss");
	
	private final Map<Date, Map<Asset, List<OHLCData>>> ohlcDataListByAssetsByDate;
	private final Map<Date, Map<Asset,Double>> eodPricesByAssetsByDate;
	private final Set<Trade> allTrades = new HashSet<Trade>();
	private final Set<PairTrade> allPairTrades = new TreeSet<>();
	private final Map<StockPair,Integer> numberOfTradesByStockPair = new HashMap<StockPair,Integer>();
	private final Map<StockPair,Double>  lastTradeOpenZScoreByStockPair = new HashMap<StockPair,Double>();
	private final Map<StockPair,List<PairTrade>> tradesByPair = new HashMap<StockPair,List<PairTrade>>();
	private final Set<PairTrade> allClosedPairTradesSortedOnCloseTime =  new TreeSet<>(new TradeSorterOnTradeCloseTime());
	private final Map<StockPair,List<PairTrade>> closePairTradesByPair = new HashMap<StockPair,List<PairTrade>>();
	
	private final Set<StockPair> oldPairs ;
	private final List<Date> tradingDays ;
	private  List<Double> avrageReturn = new ArrayList<Double>();
	private  double averageOfreturn;
	

	public BasicZScoreStrategy(Map<Date, Map<Asset, List<OHLCData>>> historicalDataByAssets, Map<Date, Map<Asset, Double>> eodPricesByAssetsByDate) throws Exception {
		this.ohlcDataListByAssetsByDate = historicalDataByAssets;
		this.eodPricesByAssetsByDate = eodPricesByAssetsByDate;
		this.oldPairs = GetStockPairs.getStockPairSet(OLD_PAIRS);
		this.tradingDays = new ArrayList<Date>(eodPricesByAssetsByDate.keySet());
	}
	
public void runBackTest(Date startDate, Date endDate) throws Exception {
		
	System.out.println("Trading Days" +tradingDays);
		//populateZScoreCalculatorByPairByDate(startDate,endDate);
	for (int i = LAST_N_EOD_ENTRIES_FOR_ZSCORE; i< tradingDays.size(); i++) {
		
		for(StockPair pair:oldPairs) {
			
				
			if( ! pair.toString().equals("[AET|CNC]")){ continue; }
				 
			//System.out.println( "valueof i " +i);
			Asset longAsset = pair.getLongAsset();
			Asset shortAsset = pair.getShortAsset();
			//System.out.println("long" +longAsset+ " short"+shortAsset);
			
			List<Double> eodRatioPrice = new ArrayList <Double>();
			
			for (int j=i-1; j!=((i-LAST_N_EOD_ENTRIES_FOR_ZSCORE)-1);j--) {
				//System.out.println( "valueof j " +j);

			
		        Double eodPrice1 = eodPricesByAssetsByDate.get(tradingDays.get(j)).get(longAsset);
		       // System.out.println(+j+ "value of j and eodPrice1" +eodPrice1);
		       				
				Double eodPrice2 = eodPricesByAssetsByDate.get(tradingDays.get(j)).get(shortAsset);
				 
				eodRatioPrice.add(eodPrice1/eodPrice2);
			}
			
			//System.out.println("eodRationSize : "+eodRatioPrice.size());
			
			//System.out.println("first day ; "+tradingDays.get(i));System.exit(0);
		
			double mean = Calculator.calculateMean(eodRatioPrice);
			double standardDeviation = Calculator.calculateStandardDeviation(eodRatioPrice);
			
			//System.out.println("eodRationSize : "+eodRatioPrice.size()+ " mean" +mean+ " stdevi" +standardDeviation);
			
			List<OHLCData> longOhlcList = ohlcDataListByAssetsByDate.get(tradingDays.get(i)).get(longAsset);
			List<OHLCData> shortOhlcList = ohlcDataListByAssetsByDate.get(tradingDays.get(i)).get(shortAsset);
			
			//System.out.println(longOhlcList);
			
			for (int k=0; k<longOhlcList.size(); k++) {
				OHLCData ohlcLong = longOhlcList.get(k);
				OHLCData ohlcShort = shortOhlcList.get(k);
				
				
				int numberOfTradesOpenedToday = 0;
				
				double currentPairRatio = (ohlcLong.getClose()/ohlcShort.getClose());
				//System.out.println("current pair ratio " +currentPairRatio);
				
				double ZScoreCurrentPair = (currentPairRatio - mean ) / standardDeviation ; 
				//System.out.println("ZScore for current pair " +ZScoreCurrentPair);
				
				
				
				List<PairTrade> currentOpenTrades = openPairTradesByPair.get(pair);
				boolean closedAnyTrade = false;
				
				if(currentOpenTrades != null ) {
					closedAnyTrade = tryToCloseOpenTrades(longAsset, shortAsset, ohlcLong, ohlcShort, ZScoreCurrentPair, currentOpenTrades);
						//continue;
				}
				
				if(!closedAnyTrade) {
				
				if(openPairTradesByPair.get(pair) == null || (numberOfTradesOpenedToday < MAX_TRADES_IN_PAIR_PER_DAY && openPairTradesByPair.get(pair).size() < MAX_TRADES_IN_PAIR_THRESHOLD)) {
					tryToOpenNewTrade(pair, longAsset, shortAsset, ohlcLong, ohlcShort, ZScoreCurrentPair);
					
					
				}
				}
				
			}	
		}	
	
	}
		
}

	
	
	/*
	 * openTrades(startDate,endDate);
	 * 
	 * // fillTrades();
	 * 
	 * // closeTrades();
	 * 
	 * }
	 */

private boolean tryToCloseOpenTrades(Asset longAsset, Asset shortAsset, OHLCData ohlcLong, OHLCData ohlcShort, double ZScoreCurrentPair, List<PairTrade> openTrades) throws ParseException {
	
	List<PairTrade> pairTrades = new ArrayList<>(openTrades); // for concurrent modificationException
	
	boolean closedAnyTrade = false;
	
	for (PairTrade pairTrade : pairTrades) {
		//System.out.println("Trying to close trade number  : "+pairTrade.getPairTradeNumber());
		if(pairTrade.getCloseTime() != null) {
			continue;
		}
		
		
		Date openTime = pairTrade.getOpenTime();
		Date maxHoldingTime = pairTrade.getMaxHoldingTime();
		
		
		
		if (openTime.before(ohlcLong.getTime()) && ohlcLong.getClose() > 0 && ohlcShort.getClose() > 0 && InBetweenTradeCloseThresholdTime(ohlcLong.getTime())) {

			double netReturns = calculateNetReturnsForPair(pairTrade, ohlcLong, ohlcShort);
			double tradeOpenZScore = pairTrade.getOpenZScore();
			
			Date currentTime = ohlcLong.getTime();
			currentTime = DateUtil.removeTime(currentTime);
			

			/*try { if(true) {
			  System.out.println(dateFormat1.format(longOhlcData.getTime()) +"   current ZScore is : "+currentZScore+" net returns are : "+netReturns +
			  " trade openzScore is : "+tradeOpenZScore+
			  " tradeOpenZScore+ZSCORE_DIFF_TO_EXIT_TRADE : "+(tradeOpenZScore+
			  ZSCORE_DIFF_TO_EXIT_TRADE)); 
			  System.out.
			  println("currentZScore >= (tradeOpenZScore+ZSCORE_DIFF_TO_EXIT_TRADE : "+(
			  currentZScore >= (tradeOpenZScore+ZSCORE_DIFF_TO_EXIT_TRADE))); } } catch
			  (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); 
				  }*/
			
			

			if (ohlcLong.getTime().after(maxHoldingTime) || ohlcLong.getTime().equals(maxHoldingTime)) {
				//System.out.println("Closing trade number 1: "+pairTrade.getPairTradeNumber());
				
				pairTrade.setReturns(netReturns);
				closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
				avrageReturn.add(pairTrade.getReturns());
				pairTrade.setReasonOfClosingTrade("Max Holding Day");
				closedAnyTrade = true;
				//System.out.println(pairTrade);
				//break;
			} else  if (isStoplossReached(netReturns, pairTrade)) {
				//System.out.println("Closing trade number 2: "+pairTrade.getPairTradeNumber());

				closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
				pairTrade.setReturns(netReturns);
				avrageReturn.add(pairTrade.getReturns());
				pairTrade.setReasonOfClosingTrade("Stop Loss Reach");
				closedAnyTrade = true;
				//System.out.println(pairTrade);
				//break;
			} else if (ZScoreCurrentPair >= (tradeOpenZScore + ZSCORE_DIFF_TO_EXIT_TRADE)) {
				//System.out.println("Closing trade number 3: "+pairTrade.getPairTradeNumber());
				pairTrade.setReturns(netReturns);
				closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
				avrageReturn.add(pairTrade.getReturns());
				pairTrade.setReasonOfClosingTrade("ZSCORE Target Reach");
				closedAnyTrade = true;
				//System.out.println(pairTrade);
				//break;
			}/*else if (getIndex(currentTime)-getIndex(DateUtil.removeTime(pairTrade.getOpenTime())) == DURATION_OF_CURRENT_SINCE_OPEN) {
				
				Date dayThree = tradingDays.get(getIndex(currentTime)-1);
				Date dayTwo  = tradingDays.get(getIndex(currentTime)-2);
				Date dayOne = tradingDays.get(getIndex(currentTime)-3);
				
				List <OHLCData> dayThreeLongOHLCList = ohlcDataListByAssetsByDate.get(dayThree).get(longAsset);
				List <OHLCData> dayThreeShortOHLCList = ohlcDataListByAssetsByDate.get(dayThree).get(shortAsset);
				
				List <OHLCData> dayTwoLongOHLCList = ohlcDataListByAssetsByDate.get(dayTwo).get(longAsset);
				List <OHLCData> dayTwoShortOHLCList = ohlcDataListByAssetsByDate.get(dayTwo).get(shortAsset);
				
				List <OHLCData> dayOneLongOHLCList = ohlcDataListByAssetsByDate.get(dayOne).get(longAsset);
				List <OHLCData> dayOneShortOHLCList = ohlcDataListByAssetsByDate.get(dayOne).get(shortAsset);
				
				OHLCData dayThreeLongOHLC =   dayThreeLongOHLCList.get(dayThreeLongOHLCList.size()-1);
				OHLCData dayThreeShortOHLC =  dayThreeShortOHLCList.get(dayThreeShortOHLCList.size()-1);
				
				OHLCData dayTwoLongOHLC =  dayTwoLongOHLCList.get(dayTwoLongOHLCList.size()-1);
				OHLCData dayTwoShortOHLC = dayTwoShortOHLCList.get(dayTwoShortOHLCList.size()-1);
				
				OHLCData dayOneLongOHLC =  dayOneLongOHLCList.get(dayOneLongOHLCList.size()-1);
				OHLCData dayOneShortOHLC = dayOneShortOHLCList.get(dayOneShortOHLCList.size()-1);
				
				double dayThreeReturn = calculateNetReturnsForPair(pairTrade, dayThreeLongOHLC, dayThreeShortOHLC);
				double dayTwoReturns = calculateNetReturnsForPair(pairTrade, dayTwoLongOHLC, dayTwoShortOHLC);
				double dayOneReturns = calculateNetReturnsForPair(pairTrade, dayOneLongOHLC, dayOneShortOHLC);
				
				pairTrade.setDayThreeReturn(dayThreeReturn);
				pairTrade.setDayTwoReturns(dayTwoReturns);
				pairTrade.setDayOneReturns(dayOneReturns);
				
					if(dayThreeReturn>dayTwoReturns && dayThreeReturn>=0) {
						pairTrade.setReturns(netReturns);
						closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
						avrageReturn.add(pairTrade.getReturns());
						pairTrade.setReasonOfClosingTrade("Third Day Return is greater than Second Day Return");
						closedAnyTrade = true;
					}
				//System.out.println("getIndex(currentTime) " +getIndex(currentTime)+ " getIndex(pairTrade.getOpenTime()) " +getIndex(DateUtil.removeTime(pairTrade.getOpenTime()))+ " day three" +dayThree+ " day 2" +dayTwo);
				
				
			}else if(getIndex(currentTime)-getIndex(DateUtil.removeTime(pairTrade.getOpenTime())) == DURATION_OF_5TH_DAY_SINCE_OPEN) {
				Date dayFour = tradingDays.get(getIndex(currentTime)-1);
				Date dayThree  = tradingDays.get(getIndex(currentTime)-2);
				
				List<OHLCData> dayFourLongOHLCList = ohlcDataListByAssetsByDate.get(dayFour).get(longAsset);
				List<OHLCData> dayFourShortOHLCList = ohlcDataListByAssetsByDate.get(dayFour).get(shortAsset); 
				
				List <OHLCData> dayThreeLongOHLCList = ohlcDataListByAssetsByDate.get(dayThree).get(longAsset);
				List <OHLCData> dayThreeShortOHLCList = ohlcDataListByAssetsByDate.get(dayThree).get(shortAsset);
				
				OHLCData dayFourLongOHLC = dayFourLongOHLCList.get(dayFourLongOHLCList.size()-1);
				OHLCData dayFourShortOHLC = dayFourShortOHLCList.get(dayFourShortOHLCList.size()-1);
				
				OHLCData dayThreeLongOHLC =   dayThreeLongOHLCList.get(dayThreeLongOHLCList.size()-1);
				OHLCData dayThreeShortOHLC =  dayThreeShortOHLCList.get(dayThreeShortOHLCList.size()-1);
				
				double dayFourReturn = calculateNetReturnsForPair(pairTrade, dayFourLongOHLC, dayFourShortOHLC);
				double dayThreeReturn = calculateNetReturnsForPair(pairTrade, dayThreeLongOHLC, dayThreeShortOHLC);
				
				pairTrade.setDayFourReturn(dayFourReturn);
				pairTrade.setDayThreeReturn(dayThreeReturn);
				
					if(dayFourReturn>dayThreeReturn) {
						pairTrade.setReturns(netReturns);						closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
						avrageReturn.add(pairTrade.getReturns());
						pairTrade.setReasonOfClosingTrade("Fourth Day Return is greater than Third Day Return");
						closedAnyTrade = true;
					}
			}*/else if (getIndex(currentTime) > getIndex(DateUtil.removeTime(pairTrade.getOpenTime())) && (getIndex(currentTime)-getIndex(DateUtil.removeTime(pairTrade.getOpenTime()))) >= 2 &&
					(getIndex(currentTime) <= getIndex(DateUtil.removeTime(maxHoldingTime)) || getIndex(currentTime) <= (tradingDays.size()-1))) {
				
				Date oneDayBefore = tradingDays.get(getIndex(currentTime)-1);
				Date twoDayBefore = tradingDays.get(getIndex(currentTime)-2);
				
				List<OHLCData> oneDayBeforeLongOHLCList = ohlcDataListByAssetsByDate.get(oneDayBefore).get(longAsset);
				List<OHLCData> oneDayBeforeShortOHLCList = ohlcDataListByAssetsByDate.get(oneDayBefore).get(shortAsset);
				
				List<OHLCData> twoDayBeforeLongOHLCList = ohlcDataListByAssetsByDate.get(twoDayBefore).get(longAsset);
				List<OHLCData> twoDayBeforeShortOHLCList = ohlcDataListByAssetsByDate.get(twoDayBefore).get(shortAsset);
				
				OHLCData oneDayBeforeLongOHLC = oneDayBeforeLongOHLCList.get(oneDayBeforeLongOHLCList.size()-1);
				OHLCData oneDayBeforeShortOHLC = oneDayBeforeShortOHLCList.get(oneDayBeforeShortOHLCList.size()-1);
				
				OHLCData twoDayBeforeLongOHLC = twoDayBeforeLongOHLCList.get(twoDayBeforeLongOHLCList.size()-1);
				OHLCData twoDayBeforeShortOHLC = twoDayBeforeShortOHLCList.get(twoDayBeforeShortOHLCList.size()-1);
				
				double oneDayBeforeReturn = calculateNetReturnsForPair(pairTrade, oneDayBeforeLongOHLC, oneDayBeforeShortOHLC);
				double twoDayBeforeReturn = calculateNetReturnsForPair(pairTrade, twoDayBeforeLongOHLC, twoDayBeforeShortOHLC);
				
				pairTrade.setOneDayBeforeReturns(oneDayBeforeReturn);
				pairTrade.setTwoDayBeforeReturns(twoDayBeforeReturn);
				
					if(oneDayBeforeReturn>twoDayBeforeReturn) {
						pairTrade.setReturns(netReturns);
						closePairTrade(pairTrade, longAsset, shortAsset, ohlcLong, ohlcShort,ZScoreCurrentPair);
						avrageReturn.add(pairTrade.getReturns());
						pairTrade.setReasonOfClosingTrade((getIndex(currentTime)-getIndex(DateUtil.removeTime(pairTrade.getOpenTime())))+ " day Return is greater than " +(getIndex(oneDayBefore)-getIndex(DateUtil.removeTime(pairTrade.getOpenTime())))+ " day return");
						closedAnyTrade = true;
					}
			}
			
			
			
			/*else if(isProfitPointReached(netReturns)){
				//System.out.println("Closing on profit taking");
				closePairTrade(pairTrade,longAsset,shortAsset,longOhlcData,shortOhlcData,currentZScore);
			}*/

		}
	}
	return closedAnyTrade;	
}

private void tryToOpenNewTrade(StockPair pair, Asset longAsset, Asset shortAsset, OHLCData ohlcLong,
		OHLCData ohlcShort, double ZScoreCurrentPair) throws Exception {
	
	Date currentDate = DateUtil.removeTime(ohlcLong.getTime());
	//System.out.println(currentDate);
	int numberOfTradesForPair = 0;
	
	
	
	if(numberOfTradesByPairByDate.get(currentDate) != null) {
		
		/* System.out.println("numberOfTradesByPairByDate "+numberOfTradesByPairByDate);
		System.out.println("numberOfTradesByPairByDate.get(currentDate)" +numberOfTradesByPairByDate.get(currentDate)); 
		System.out.println("numberOfTradesByPairByDate.get(currentDate).get(pair)" +numberOfTradesByPairByDate.get(currentDate).get(pair));*/
		if (numberOfTradesByPairByDate.get(currentDate).get(pair) == null) {
			numberOfTradesForPair = 0 ;
		}
		else
			numberOfTradesForPair = numberOfTradesByPairByDate.get(currentDate).get(pair);

	}
	//int numberOfTradesForPair  = numberOfTradesByPairByDate.get(currentDate) == null ? 0 : numberOfTradesByPairByDate.get(currentDate).get(pair);
	
	if(numberOfTradesForPair >=  MAX_TRADES_IN_PAIR_PER_DAY)
		return;
	
	if(ohlcLong.getClose() > 0 && ohlcShort.getClose() > 0 && InBetweenTradeOpenThresholdTime(ohlcLong.getTime())) {
					
		if(ZScoreCurrentPair <= ENTRY_ZSCORE) {
			if(numberOfTradesForPair == 0) { // first Trade for the day
//			/	System.out.println("Opening First trade for the date : "+dateFormat1.format(currentDate)+" numberOfTrades : "+numberOfTradesForPair);
				openPairTrade(pair,longAsset,shortAsset,ohlcLong,ohlcShort,ZScoreCurrentPair);
			}else {
				double lastTradeOpenZScore = getLastTradesZScore(pair,ohlcLong.getTime());
				
				
			//	System.out.println("previous zScore : "+lastTradeOpenZScore+" currentZScore : "+currentZScore+ " Can enter : "+(currentZScore < lastTradeOpenZScore - DIFF_TO_ENTER_NEXT_TRADE));
				if(ZScoreCurrentPair < lastTradeOpenZScore - DIFF_TO_ENTER_NEXT_TRADE) {
					openPairTrade(pair,longAsset,shortAsset,ohlcLong,ohlcShort,ZScoreCurrentPair);
				}
			}
			
			
		}
		
	}
}

private Date getMaxHoldingThresholdTime(Date openTime) {
	
	int indexOfCurrentDate = tradingDays.indexOf(DateUtil.removeTime(openTime));
	
	Date thresholdTime;
	
	if(indexOfCurrentDate + MAX_HOLDING_DAY < tradingDays.size())
		thresholdTime = DateUtil.setMarketClosingTime(tradingDays.get(indexOfCurrentDate + MAX_HOLDING_DAY));
	else
		thresholdTime = DateUtil.setMarketClosingTime(tradingDays.get(tradingDays.size()-1));
	
	//System.out.println("Trade Open time is  : "+dateFormat1.format(openTime)+"\t Max Holding Time is  : "+dateFormat1.format(thresholdTime));
	
	return thresholdTime;
}

private void openPairTrade(StockPair pair, Asset longAsset, Asset shortAsset, OHLCData ohlcLong,OHLCData ohlcShort, double ZScoreCurrentPair) throws Exception {
	
	int id = allTrades.size();
	
	Date currentDate = DateUtil.removeTime(ohlcLong.getTime());
	double longQuantity = (EXPOSURE_PER_DAY/2)/ohlcLong.getClose() ; 
	double shortQuantity = (EXPOSURE_PER_DAY/2)/ohlcShort.getClose() ;
	
	Trade longTrade = new Trade(++id, longAsset, ohlcLong.getTime(), ohlcLong.getClose());
		longTrade.setQuantity(longQuantity);
	Trade shortTrade = new Trade(++id, shortAsset, ohlcShort.getTime(), ohlcShort.getClose());
		shortTrade.setQuantity(shortQuantity);
	
		//System.out.println("long trade pojo" +longTrade);
		
	PairTrade pairTrade = new PairTrade(pair, longTrade, shortTrade, ZScoreCurrentPair);
	pairTrade.setOpenTime(ohlcLong.getTime());
	
	Date maxHoldingTime = getMaxHoldingThresholdTime(pairTrade.getOpenTime());
	pairTrade.setMaxHoldingTime(maxHoldingTime);
	
	allTrades.add(longTrade);
	allTrades.add(shortTrade);
	allPairTrades.add(pairTrade);
	
	if(openPairTradesByPair.get(pair) == null) {
		openPairTradesByPair.put(pair, new ArrayList<>());
	}
	
	openPairTradesByPair.get(pair).add(pairTrade);
	
	
	if(tradesByPair.get(pair) == null) {
		tradesByPair.put(pair, new ArrayList<>());
	}
	
	tradesByPair.get(pair).add(pairTrade);
	
	if(numberOfTradesByStockPair.get(pair) == null) {
		numberOfTradesByStockPair.put(pair, 0);
	}
	
	if(numberOfTradesByPairByDate.get(currentDate) == null) {
	
		numberOfTradesByPairByDate.put(currentDate, new HashMap<>());
		numberOfTradesByPairByDate.get(currentDate).put(pair, 0);
		//System.out.println(numberOfTradesByPairByDate);

	}
	
	
	//int currentNumberOfTrades = numberOfTradesByPairByDate.get(currentDate).get(pair);
	
	int currentNumberOfTrades = 0;
	
	if(numberOfTradesByPairByDate.get(currentDate) != null) {
		/*System.out.println("pair : "+pair.toString()+" currentDate: "+dateFormat1.format(currentDate));
		//System.out.println(numberOfTradesByPairByDate.get(currentDate).get(pair));
*/			currentNumberOfTrades = numberOfTradesByPairByDate.get(currentDate).get(pair)== null ? 0 : numberOfTradesByPairByDate.get(currentDate).get(pair);
	}
	numberOfTradesByPairByDate.get(currentDate).put(pair, (currentNumberOfTrades+1));
	
	//System.out.println("Number of trades for : "+dateFormat1.format(currentDate)+" "+ numberOfTradesByPairByDate.get(currentDate).get(pair));
	
	int tradeNumber = numberOfTradesByStockPair.get(pair)+1;

	numberOfTradesByStockPair.put(pair, tradeNumber );
	
	pairTrade.setPairTradeNumber(tradeNumber);
	
	lastTradeOpenZScoreByStockPair.put(pair, ZScoreCurrentPair);
	
	//System.out.println(" trade Number : "+tradeNumber+ " pair : "+pair+ " currentZScore : "+ZScoreCurrentPair);
	//System.out.println("AllTrades : "+allPairTrades);
	//System.out.println("numberOfTradesByStockPair" +numberOfTradesByStockPair);
}


private double getLastTradesZScore(StockPair pair, Date time) {
	List<PairTrade> openTrades = openPairTradesByPair.get(pair);
	
	double zScore = 0.0;
	
	for(PairTrade pairTrade : openTrades) {
		zScore = pairTrade.getOpenZScore();
	}
	
	
	return zScore;
}

private boolean InBetweenTradeCloseThresholdTime(Date time) {
	if (time.getHours() == 9) {
		if (time.getMinutes() > 34)
			return true;
		return false;
	}
	return true;
}

private boolean InBetweenTradeOpenThresholdTime(Date time) {
	if (time.getHours() == 9) {
		if (time.getMinutes() > 34)
			return true;
		return false;
	}
	
	if(time.getHours() == 15) {
		if(time.getMinutes() < 56)
			return true;
		return false;
	}
	if(time.getHours() >= 16) {
		return false;
	}
	return true;
}

private boolean isStoplossReached(double netReturns, PairTrade pairTrade) {
	
	int tradeNumber = pairTrade.getPairTradeNumber();
	
	return (netReturns <= STOPLOSS_FIRST_2_BETS && tradeNumber <= 2) || (netReturns <= STOPLOSS_3RD_BETS && tradeNumber == 3);
}


private double calculateNetReturnsForPair(PairTrade pairTrade, OHLCData ohlcLong, OHLCData ohlcShort) {
	double percentageReturnOflong = getReturns(pairTrade.getLongTrade(),ohlcLong,true);
	double percentageReturnOfshort = getReturns(pairTrade.getShortTrade(),ohlcShort,false);
	
	
	/*pairTrade.getLongTrade().setReturns(percentageReturnOflong);
	pairTrade.getShortTrade().setReturns(percentageReturnOfshort);*/
	
	//pairTrade.setReturns(percentageReturnOflong+percentageReturnOfshort);
	
	return percentageReturnOflong+percentageReturnOfshort;
}

private double getReturns(Trade trade, OHLCData OhlcData, boolean isLong) {
	
	if(isLong) {
		return ((OhlcData.getClose()-trade.getOrderPrice())/trade.getOrderPrice())*100;
	}
	
	return ((OhlcData.getClose()-trade.getOrderPrice())/trade.getOrderPrice())*100*(-1);
}


	/*
	 * private double (List <Double> avrageReturn ) {
	 * 
	 * double sum = 0;
	 * 
	 * for (Double i : avrageReturn1 ) sum = sum+i; return sum/avrageReturn1.size();
	 * 
	 * 
	 * }
	 */


private void closePairTrade(PairTrade pairTrade, Asset longAsset, Asset shortAsset, OHLCData ohlcLong,
		OHLCData ohlcShort, double ZScoreCurrentPair) {
	
	//System.out.println("Closing trades trade id: "+pairTrade.getId()+" open time : "+DateUtil.removeTime(pairTrade.getOpenTime())+" close Time : "+DateUtil.removeTime(pairTrade.getCloseTime()));
	pairTrade.setClosingZScore(ZScoreCurrentPair);

	Trade longTrade = pairTrade.getLongTrade();
	Trade shortTrade = pairTrade.getShortTrade();
	
	shortTrade.setClosingPrice(ohlcShort.getClose());
	shortTrade.setCloseTime(ohlcShort.getTime());
	
	longTrade.setClosingPrice(ohlcLong.getClose());
	longTrade.setCloseTime(ohlcLong.getTime());
	
	pairTrade.setCloseTime(ohlcLong.getTime());
	
	if(DateUtil.removeTime(pairTrade.getCloseTime()).equals(DateUtil.removeTime(pairTrade.getOpenTime()))){
		int numberOfTrades = numberOfTradesByPairByDate.get(DateUtil.removeTime(pairTrade.getCloseTime())).get(pairTrade.getStockPair());
		numberOfTradesByPairByDate.get(DateUtil.removeTime(pairTrade.getCloseTime())).put(pairTrade.getStockPair(), (numberOfTrades-1));
//		/System.out.println("Closing trade on same day tradeNumber: "+pairTrade.getId());
	}
	
	openPairTradesByPair.get(pairTrade.getStockPair()).remove(pairTrade);
	allClosedPairTradesSortedOnCloseTime.add(pairTrade);
	
	if(closePairTradesByPair.get(pairTrade.getStockPair()) == null) {
		closePairTradesByPair.put(pairTrade.getStockPair(), new ArrayList<>());
	}
	
	closePairTradesByPair.get(pairTrade.getStockPair()).add(pairTrade);
}

public Set<Trade> getAllTrades() {
	return allTrades;
}

public double getAverageOfreturn() {
	double sum = 0;
	double averageOfreturn = 0;
	for (Double d : avrageReturn) {
		sum = sum+d;
	}
	
	averageOfreturn = sum/avrageReturn.size();
	return averageOfreturn;
}
public Set<PairTrade> getExecutedTrades() { 
	return allPairTrades; 
	}
	
private int getIndex (Date date ) {
	int index = tradingDays.indexOf(date);
	return index;
}



}
