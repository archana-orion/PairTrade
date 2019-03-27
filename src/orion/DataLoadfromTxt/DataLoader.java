package orion.DataLoadfromTxt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import orion.classModel.Asset;
import orion.classModel.OHLCData;
import orion.utility.*;

public class DataLoader {
	
	private static  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd|HH:mm:ss");


	public static Map<Date, Map<Asset, List<OHLCData>>> loadDataFromFile( String FUTURE_lIST_filepath,String ASSET_MINUT_DATA_LOCATION, Date startDate, Date endDate) throws Exception {
		System.out.println("2");

		Map<Integer, Asset> assetkByIndexFromFutureFile = getAssetsByIndex(FUTURE_lIST_filepath);
		
		System.out.println("assetkByIndexFromFutureFile : "+assetkByIndexFromFutureFile);
		
		Map<Date, Map<Asset, List<OHLCData>>> historicalDataByAssets = getHistoricalDataByAssets(assetkByIndexFromFutureFile,ASSET_MINUT_DATA_LOCATION,startDate,endDate);

		return historicalDataByAssets;
	}

	

	private static Map<Integer, Asset> getAssetsByIndex(String FUTURE_lIST_filepath) throws Exception {
		
		File f = new File(FUTURE_lIST_filepath);
		
		Map<Integer, Asset> assetkByIndexFromFutureFile = new TreeMap<Integer, Asset>();
		
		if(f.isFile()) {
			//System.out.println("file name is "+f.getName());
			
			BufferedReader inputStream = null;
			
			try {
				inputStream = new BufferedReader(new FileReader(f));
				String line;
				int index = 0;
				
				while ((line = inputStream.readLine()) != null) {
					assetkByIndexFromFutureFile.put(index, new Asset(line, null, null, 0, null, null, null));
					index++;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		//System.out.println("number of total futures is : "+assetkByIndexFromFutureFile.size());
		return assetkByIndexFromFutureFile;
	}
	
	
	private static Map<Date, Map<Asset, List<OHLCData>>> getHistoricalDataByAssets(Map<Integer, Asset> assetkByIndexFromFutureFile, String aSSET_MINUT_DATA_LOCATION, Date startDate,
			Date endDate) throws Exception {
		File f = new File(aSSET_MINUT_DATA_LOCATION);
		Map<Date, Map<Asset, List<OHLCData>>> ohlcDataListByAssetByDate = new TreeMap<Date, Map<Asset, List<OHLCData>>>();
		
		if (f.isFile()) {
			
			System.out.println(f.getName());
        	BufferedReader inputStream = null;
        	
        	
        	try {
        		inputStream = new BufferedReader( new FileReader(f));
                String line = "";
                List<String> dataLines = new ArrayList<String>();
                
                while(line != null) {
                	line = inputStream.readLine();
                	if(line != null)
                		dataLines.add(line);
                }
                for (String dataLine : dataLines) {
                	
                	String[] prices = dataLine.split("\t");
            		Date time = dateFormat.parse(prices[0]);
            		
            		if(time.before(startDate) || time.after(endDate) )
            			continue;
 
            		 for(int i = 2 ; i< prices.length ; i++) {
	            		
	            		if(prices[i].equalsIgnoreCase("NA")) {
	            			prices[i] = "-1";
	            		}
	            		double price = Double.parseDouble(prices[i]);
	            		Asset asset = assetkByIndexFromFutureFile.get(i-2);
	            		OHLCData ohlcData = new OHLCData(asset, time, price, price, price, price, -1);
	            		
	                		if(ohlcDataListByAssetByDate.get(DateUtil.removeTime(time)) == null) {
	                			Map<Asset, List<OHLCData>> dataByAsset = new TreeMap<Asset, List<OHLCData>>();
	                			ohlcDataListByAssetByDate.put(DateUtil.removeTime(time), dataByAsset);
	            			
	                			for(int index : assetkByIndexFromFutureFile.keySet()) {
	                				Asset asset1 = assetkByIndexFromFutureFile.get(index);
	                				//System.out.println(asset1);
	                				
	
	                				dataByAsset.put(asset1, new ArrayList<OHLCData>());
	            				}
	                		}
	            		
	            		//System.out.println("Adding data for : "+dateFormat.format(DateUtil.removeTime(time)));
	            		ohlcDataListByAssetByDate.get(DateUtil.removeTime(time)).get(asset).add(ohlcData);
	            	}
	            }
		
		
        	}catch(Exception e) {
        		throw e;
        	}
		}
		return ohlcDataListByAssetByDate;
	}
	
	
/*public static Map<Date, Map<Asset, Double>> loadEODPrices(String FUTURE_lIST_filepath, String ASSET_MINUT_DATA_LOCATION,Date startDate, Date endDate) throws Exception {
		
		Map<Integer, Asset> assetkByIndexFromFutureFile = getAssetsByIndex(FUTURE_lIST_filepath);
		
		Map<Date,Map<Asset,Double>> eodPricesByAssetByDate = loadEODPrices(assetkByIndexFromFutureFile,ASSET_MINUT_DATA_LOCATION,startDate,endDate);

		return eodPricesByAssetByDate;
	} */


public static Map<Date, Map<Asset, Double>> loadEODPrices(String FUTURE_lIST_filepath,String ASSET_MINUT_DATA_LOCATION, Date startDate, Date endDate) throws Exception {
	
	File f = new File(ASSET_MINUT_DATA_LOCATION);
	
	Map<Date, Map<Asset, Double>> eodDataByAssetByDate = new TreeMap<Date, Map<Asset, Double>>();
	
	if(f.isFile()) {
    	//System.out.println(f.getName());
    	
        BufferedReader inputStream = null;

        try {
            inputStream = new BufferedReader( new FileReader(f));
            String line = "";
            List<String> dataLines = new ArrayList<String>();
            
            while(line != null) {
            	line = inputStream.readLine();
            	if(line != null)
            		dataLines.add(line);
            }
            
            for (String dataLine : dataLines) {
            	String[] prices = dataLine.split("\t");
        		Date time = DateUtil.removeTime(dateFormat.parse(prices[0]));
        		
        		if(time.before(startDate) || !time.before(endDate) )
        			continue;
        		
        		//Map<Asset,Double> eodDataByAsset = eodDataByAssetByDate.get(time);
        	
            	for(int i = 2 ; i< prices.length ; i++) {
            		
            		double price = Double.parseDouble(prices[i]);
            		Asset asset = getAssetsByIndex(FUTURE_lIST_filepath).get(i-2);
            		
            		if( eodDataByAssetByDate.get(time) == null) {
            			Map<Asset, Double> dataByAsset = new TreeMap<Asset, Double>();
            			eodDataByAssetByDate.put(time, dataByAsset);
            		}
        			
            		eodDataByAssetByDate.get(time).put(asset,price);
            	}
            }
        }catch (Exception e) {
			throw e;
		}
	
	}
	return eodDataByAssetByDate;
	
}
}