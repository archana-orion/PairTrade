package orion.backtestProgram;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orion.DataLoadfromTxt.DataLoader;
import orion.basicZSCore.StrategyNew.BasicZScoreStrategy;
import orion.classModel.*;
import orion.utility.TableUtil;




public class BacktestProgramm {
	public static SimpleDateFormat dateFormat   = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat dateFormat1 = new SimpleDateFormat("d/M/yy HH:mm");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd|HH:mm:ss");

	
	public static String FUTURE_lIST_filepath = "C:\\Users\\ARCHANA\\Pairs_Data-20190311T120701Z-001\\Pairs_Data\\Futures_list-OldPairs.txt";
	public static String ASSET_MINUT_DATA_LOCATION = "C:\\Users\\ARCHANA\\Pairs_Data-20190311T120701Z-001\\Pairs_Data\\MinuteData-OldPairs-ClosePrices.txt";
	public static String ASSET_EOD_DATA_LOCATION = "C:\\Users\\ARCHANA\\Pairs_Data-20190311T120701Z-001\\Pairs_Data\\EodData-OldPairs-ClosePrices.txt";
	public static String SPY_MINUT_DATA_LOCATION = "C:\\Users\\ARCHANA\\Pairs-Data\\SPYIndexData.txt";
	
	public static void main(String[] args) throws Exception{
		System.out.println("Welcome to new Back Testing programm");
		
		basicZScoreStrategy();
		
	}

	private static void basicZScoreStrategy() throws ParseException, Exception {
		System.out.println("This is Basic Z Score Stategy");
		
		Date startDate = dateFormat.parse("20160303");
		Date endDate = dateFormat.parse("20160630");
		
		//Data Loading
		Map<Date,Map<Asset,List<OHLCData>>> ohlcListByAssetByTime = DataLoader.loadDataFromFile(FUTURE_lIST_filepath,ASSET_MINUT_DATA_LOCATION,startDate, endDate);
		
		Map<Date,Map<Asset,Double>> eodPricesByAssetsByDate = DataLoader.loadEODPrices(FUTURE_lIST_filepath,ASSET_EOD_DATA_LOCATION,startDate, endDate);
		
		//create backtest object for execution
		BasicZScoreStrategy basicZScoreStrategy = new BasicZScoreStrategy(ohlcListByAssetByTime,eodPricesByAssetsByDate);
		
			basicZScoreStrategy.runBackTest(startDate, endDate);
			
			printNicely(basicZScoreStrategy.getExecutedTrades());
			
			System.out.println("Number of trades are : "+basicZScoreStrategy.getAllTrades().size()); 
			
			System.out.println("Average of returns is : "+basicZScoreStrategy.getAverageOfreturn());
			
			
		
	}
	
private static void printNicely(Set<PairTrade> allPairTrades) throws IOException {
		
		List<String> headers = Arrays.asList(	"Pair", 
												"tradeNumber",
				 								"openTime",
				 							//	"maxHoldingTime",
				 								"openZScore",
				 								"closeTime",
				 								"closeZScore",
				 								"long_quantity",
				 								"short_quantity",
				 								"returns",
				 								"predicted_returns"
				 								);
		
		List<List<String>> rows = new ArrayList<>();
		
		rows.add(headers);
		
		System.out.println("Pair\ttradeNumber\topenTime\topenZScore\tcloseTime\tcloseZScore\t"
				+ "longTradeOpenPrice\tshortTradeOpenPrice\tlongTradeCloseprice\tshortTradeCloseprice"
				// "\tlongQuantity\tshortQuantity
				+"\treturns"
				+ "\toneDayBeforeReturn\ttwoDayBeforeReturn"
				 ///*dayFourReturn
				//"\tdayThreeReturn\tdayTwoReturn"
				//\tdayOneReturn\t
				+ "\tTradeClosingReason");
		for(PairTrade pairTrade : allPairTrades) {
			//System.out.println(pairTrade.getCloseTime());

			
			
			//if(pairTrade.getStockPair().toString().equals("[SCG|DTE]"))
			//System.out.println(""+pairTrade.getStockPair()+"|"+dateFormat1.format(pairTrade.getOpenTime())+"|"+pairTrade.getOpenZScore()+"|"+pairTrade.getFftValue().re()+"|"+pairTrade.getLmCoeff()) ;
			//System.out.println("longTrade : "+pairTrade.getLongTrade());
			//System.out.println("shortTrade : "+pairTrade.getShortTrade());
			
			
			System.out.println("" + pairTrade.getStockPair()+ "\t" +
								pairTrade.getPairTradeNumber()+"\t"+
								dateFormat2.format(pairTrade.getOpenTime())+ "\t"
								+pairTrade.getOpenZScore()+ "\t"
								+(pairTrade.getCloseTime()== null ? "null" : dateFormat2.format(pairTrade.getCloseTime()))+"\t" 
								+pairTrade.getClosingZScore()+ "\t" 
								+pairTrade.getLongTrade().getOrderPrice()+"\t"
								+pairTrade.getShortTrade().getOrderPrice()+"\t"
								+pairTrade.getLongTrade().getClosingPrice()+"\t"
								+pairTrade.getShortTrade().getClosingPrice()+"\t"
								//+pairTrade.getLongTrade().getQuantity()+ "\t" 
								//+pairTrade.getShortTrade().getQuantity()+ "\t" 
								+pairTrade.getReturns()+ "\t" 
								+pairTrade.getOneDayBeforeReturns()+ "\t"
								+pairTrade.getTwoDayBeforeReturns()+ "\t"
								//+pairTrade.getDayFourReturn()+ "\t"
								//+pairTrade.getDayThreeReturn()+ "\t"
								//+pairTrade.getDayTwoReturns()+ "\t"
								//+pairTrade.getDayOneReturns()+ "\t"
								+pairTrade.getReasonOfClosingTrade()
								);
			 
		}
		//System.out.println(TableUtil.formatAsTable(rows));
		//System.out.println(numberOfTradesByStockPair);
	}


}
