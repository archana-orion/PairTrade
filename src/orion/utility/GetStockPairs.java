package orion.utility;

import java.io.*;

import java.util.*;

import orion.classModel.*;


public class GetStockPairs {
public static Set<StockPair> getStockPairSet(String filePath) throws Exception {
		
		File f = new File(filePath);
		Set<StockPair> pairList = new HashSet<StockPair>();
		
		if(f.isFile()) {
        	//System.out.println(f.getName());
        	
            BufferedReader inputStream = null;

            try {
                inputStream = new BufferedReader(new FileReader(f));
                String line;
                int index = 1;
                
                while ((line = inputStream.readLine()) != null) {
                	String[] stockPairs = line.split("\t");
                	Asset longAsset = new Asset(stockPairs[0], Category.STK, null, 0, null, null, null);
                	Asset shortAsset = new Asset(stockPairs[1], Category.STK, null, 0, null, null, null);

                	if(!pairList.add(new StockPair(longAsset, shortAsset))) {
                		System.out.println("Cannot add ; "+line);
                		System.out.println(pairList);
                		System.exit(0);
                	}
                	
                }
            }catch (Exception e) {
				throw e;
			}
		}
		
		return pairList;
	}


}
