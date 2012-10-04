/*
This is part of coursework assignment simulating different types caches and impact of different cache configurations on demo trace file containing 1 million intructions.
See https://www.cis.upenn.edu/~milom/cis501-Fall11/homework/hw4/
*/
import java.lang.Exception;
import java.lang.Long;
import java.lang.String;
import java.lang.System;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

class CacheSimulator
{
	//Question 2
	// Calculate the cache Parameters : offset bits, index bits, tag bits, no. of sets
	public static int[] calCacheParameters(long cacheSize, long blockSize, int associativity){
		int[] cacheParameter = new int[4];
		cacheParameter[0] = (int)(Math.log(blockSize)/Math.log(2)); //offset
		cacheParameter[1] = (int)Math.ceil((Math.log(Math.ceil(cacheSize/(blockSize*associativity))) / Math.log(2))); //index
		cacheParameter[2] = 64 - (int)(cacheParameter[0] + cacheParameter[1]); //tag
		cacheParameter[3] = (int)(cacheSize/(blockSize*associativity)); // No of sets

		return cacheParameter;
	}

	//Question 3
	//Find the offset bits, index bits and tag bits
	public static int[] calCacheBits(long address, int num_tag_bits, int num_index_bits, int num_offset_bits){
		int[] calCacheBits = new int[3];
		calCacheBits[0] = (int)(address & ((long)Math.pow(2, num_offset_bits) - 1)); //offset bits
		calCacheBits[1] = (int)((address>>>num_offset_bits) & ( (long)(Math.pow(2, num_index_bits) - 1)));//index bits
		calCacheBits[2] = (int)((address>>>(num_offset_bits+num_index_bits))); //& ( (long)(Math.pow(2, num_tag_bits) - 1)));//tag bits

		return calCacheBits;
	}

	//Find the Max LRU
	public static int calLRU(int[] LRU){
		int maxLRU = 0;

		for(int i=0;i<LRU.length;i++){
			if(LRU[maxLRU] < LRU[i]){
				//Swap it
				maxLRU = i;
			}
		}
		return maxLRU;
	}

	public static void simulate(InputStream incomingStream, PrintStream outputStream, int pCacheBlock, int pCacheSize, int pAssociativity, int wayPredictor) throws Exception 
	{
		// See the documentation to understand what these variables mean.
		long microOpCount;
		long instructionAddress;
		long sourceRegister1;
		long sourceRegister2;
		long destinationRegister;
		String conditionRegister;
		String TNnotBranch;
		String loadStore;
		long immediate;
		long addressForMemoryOp;
		long fallthroughPC;
		long targetAddressTakenBranch;
		String macroOperation;
		String microOperation;

		long totalMicroops = 0;
		long totalMacroops = 0;

		BufferedReader r = new BufferedReader(new InputStreamReader(incomingStream));
		String line;

		//Question 4 variables
		int[] directCacheParamBits = new int[3];
		int[] directCacheParam = calCacheParameters((long)Math.pow(2,pCacheSize),(long)Math.pow(2,pCacheBlock),1); // Associativity is 1 because direct mapped
		long[] directCache = new long[(int)(Math.pow(2, pCacheSize)/Math.pow(2, pCacheBlock))];
		int localTagBit = 0;
		int cacheMiss = 0;

		//Question 5 variables
		int[] cacheParamBits = new int[3];
		int[] cacheParam = calCacheParameters((long)Math.pow(2,pCacheSize),(long)Math.pow(2,pCacheBlock),pAssociativity); 
		long[][] setAsso = new long[cacheParam[3]][pAssociativity];
		int setAssoFlag = 0;
		int missSetAsso = 0;
		int[][] LRU = new int[directCacheParam[3]][(int)Math.ceil(Math.log(pAssociativity)/Math.log(2))];
		int maxLRU = 0;
		int[] iLRU = new int[directCacheParam[3]];
		int set  = 0;

		//Question 6
		int countStore = 0;
		int countWriteBack = 0;
		long inTraffic = 0;
		long outTraffic = 0;
		int[][] dirtyBit = new int[directCacheParam[3]][2];

		// Question 7
		int[] wayPredictorArray = new int[(int)Math.pow(2,wayPredictor)];
		int wayPredictorIndex = 0;
		int wayPredictorMiss = 0;
		int totalWayPredictions = 0;
		outputStream.format("Processing trace...\n");

		while (true) {
			line = r.readLine();
			if (line == null) {
				break;
			}
			String [] tokens = line.split("\\s+");

			microOpCount = Long.parseLong(tokens[0]);
			instructionAddress = Long.parseLong(tokens[1], 16);
			sourceRegister1 = Long.parseLong(tokens[2]);
			sourceRegister2 = Long.parseLong(tokens[3]);
			destinationRegister = Long.parseLong(tokens[4]);
			conditionRegister = tokens[5];
			TNnotBranch = tokens[6];
			loadStore = tokens[7];
			immediate = Long.parseLong(tokens[8]);
			addressForMemoryOp = Long.parseLong(tokens[9], 16);
			fallthroughPC = Long.parseLong(tokens[10], 16);
			targetAddressTakenBranch = Long.parseLong(tokens[11], 16);
			macroOperation = tokens[12];
			microOperation = tokens[13];

			// For each micro-op
			totalMicroops++;

			// For each macro-op
			if (microOpCount == 1) {
				totalMacroops++;
			}

			if(loadStore.compareTo("L")==0 || loadStore.compareTo("S") == 0){
				{// Question 4
					//Get the offset,index and tag bits
					directCacheParamBits = calCacheBits(addressForMemoryOp,directCacheParam[2],directCacheParam[1],directCacheParam[0]);
					//cacheParamBits = calCacheBits(addressForMemoryOp,cacheParam[2],cacheParam[1],cacheParam[0]);
					localTagBit = (int)((directCache[directCacheParamBits[1]]) >>> (directCacheParam[1] + directCacheParam[0]));
					if(localTagBit != directCacheParamBits[2]){
						//There is a cache miss
						++cacheMiss;
						//Update the direct cache
						directCache[directCacheParamBits[1]] = addressForMemoryOp;
						//outputStream.format("Address %x Miss \n", addressForMemoryOp);	
					}
					else{
						/*//Check for offset
                	int localOffsetBit = (int)((directCache[directCacheParamBits[1]]) & ((long)Math.pow(2, directCacheParam[0]) - 1));
						 */
						//if(addressForMemoryOp >= directCache[directCacheParamBits[1]] && addressForMemoryOp <= (directCache[directCacheParamBits[1]] + 32)){
						// Hit. address lies within 32B block limit
						//outputStream.format("Address %x Hit \n",addressForMemoryOp);
						//	}
						/*	else{
                		// Miss
                		++cacheMiss;
                    	//Update the direct cache
                    	directCache[directCacheParamBits[1]] = addressForMemoryOp;
                    	outputStream.format("Address %x Miss \n",addressForMemoryOp);
                	}*/

					}

				}
				{
					//Question 5 and 6
					cacheParamBits = calCacheBits(addressForMemoryOp,cacheParam[2],cacheParam[1],cacheParam[0]);
					//Check whether tag matches with each way

					//if((int)((setAsso[cacheParamBits[1]][0]) >>> (cacheParam[1] + cacheParam[0])) == cacheParamBits[2] || (int)((setAsso[cacheParamBits[1]][1]) >>> (cacheParam[1] + cacheParam[0])) == cacheParamBits[2])
					
					wayPredictorIndex = (int)((addressForMemoryOp >>> (cacheParam[0])) & ((long)(Math.pow(2, wayPredictor) - 1)));
					
					// Hit . Set the LRU
					
					if((int)((setAsso[cacheParamBits[1]][0]) >>> (cacheParam[1] + cacheParam[0])) == cacheParamBits[2] ){
						iLRU[cacheParamBits[1]] = 1;
						set = cacheParamBits[1];
						
						
						if(loadStore.compareTo("S") == 0){
							++countStore;
							if(dirtyBit[cacheParamBits[1]][0] == 0){
								//outTraffic += (int)Math.pow(2,pCacheBlock);
								dirtyBit[cacheParamBits[1]][0] = 1;
							}
						}
						
						//way predictor
						totalWayPredictions++;
						if(wayPredictorArray[wayPredictorIndex] != 0){
							//Way predictor mis predicted Update
							++wayPredictorMiss;
							wayPredictorArray[wayPredictorIndex] = 0;
						}
							//outputStream.format(" Set %d Address %x Hit Way = 0 \n",set, addressForMemoryOp);
					}else if((int)((setAsso[cacheParamBits[1]][1]) >>> (cacheParam[1] + cacheParam[0])) == cacheParamBits[2]){
						iLRU[cacheParamBits[1]] = 0;
						set = cacheParamBits[1];
						
						if(loadStore.compareTo("S") == 0){
							++countStore;
							if(dirtyBit[cacheParamBits[1]][1] == 0){
								//outTraffic += (int)Math.pow(2,pCacheBlock);
								dirtyBit[cacheParamBits[1]][1] = 1;
							}
						}
						
						totalWayPredictions++;
						if(wayPredictorArray[wayPredictorIndex] != 1){
							//Way predictor mis predicted Update
							++wayPredictorMiss;
							wayPredictorArray[wayPredictorIndex] = 1;
						}
						
						//outputStream.format("Set %d Address %x Hit Way = 1 \n",set, addressForMemoryOp);
					}else{
						++missSetAsso;
						inTraffic += (int)Math.pow(2, pCacheBlock);
						if(dirtyBit[cacheParamBits[1]][iLRU[cacheParamBits[1]]] == 1){
							outTraffic += (int)Math.pow(2,pCacheBlock);
						}
						
						if(loadStore.compareTo("L") == 0){
							dirtyBit[cacheParamBits[1]][iLRU[cacheParamBits[1]]] = 0;
						}
						
						if(loadStore.compareTo("S") == 0){
							dirtyBit[cacheParamBits[1]][iLRU[cacheParamBits[1]]] = 1;
							//outTraffic += (int)Math.pow(2,pCacheBlock);
							++countStore;
						}
						
						//way Predictor
						wayPredictorArray[wayPredictorIndex] = iLRU[cacheParamBits[1]];
						
						
						//Replace with LRU	
						setAsso[cacheParamBits[1]][iLRU[cacheParamBits[1]]] = addressForMemoryOp;
						set = cacheParamBits[1];
						iLRU[cacheParamBits[1]] = 1 - iLRU[cacheParamBits[1]];

						//outputStream.format(" Set %d Address %x Miss Way = %d \n",set,  addressForMemoryOp, 1 - iLRU[cacheParamBits[1]]);
					}

				}/*
            		for(int i=0;i<pAssociativity;i++){
            			localTagBit = (int)((setAsso[cacheParamBits[1]][i]) >>> (cacheParam[1] + cacheParam[0]));
            			if(localTagBit == cacheParamBits[2]){
            				// Hit. Enjoy the break! :)
            				setAssoFlag = 1;
            				break;
            			}           			
            		}
            		if(setAssoFlag==0){
            			// Miss.
            			++missSetAsso;
            			//Check the LRU and swap it.
            			maxLRU = calLRU(LRU[cacheParamBits[1]]);
            			setAsso[cacheParamBits[1]][maxLRU] = addressForMemoryOp;
            			LRU[cacheParamBits[1]][maxLRU] = -1;
            		}

            		//Update the LRU bits
            		for(int i=0;i<LRU.length;i++){
            			LRU[cacheParamBits[1]][i] = LRU[cacheParamBits[1]][i]++;
            		}*/
				
			}

		}
		outputStream.format("Processed %d trace records.\n", totalMicroops);

		outputStream.format("Micro-ops: %d\n", totalMicroops);
		outputStream.format("Macro-ops: %d\n", totalMacroops);
		outputStream.format("Total cache misses for Direct Map %d\n", cacheMiss);
		outputStream.format("Total cache misses for Set Associative %d\n", missSetAsso);        

		outputStream.format("Total bytes of write through traffic : %d \n", (int)(inTraffic + countStore*4));        
		outputStream.format("Total bytes of write back traffic : %d \n", (int)(inTraffic + outTraffic));
		
		outputStream.format("Total way Mis prediction: %d \n", wayPredictorMiss);
		//outputStream.format("Total percentage of way Mis prediction:");
		System.out.println("Total percentage of way Mis prediction:  "+((wayPredictorMiss*100.00)/totalWayPredictions));
	}

	public static void main(String[] args) throws Exception
	{
		InputStream inputStream = System.in;
		PrintStream outputStream = System.out;
		int cacheSizeBits = 2;
		int cacheBlockBits = 3;
		int associativity = 1;
		int wayPredictor = 0;

		/*if (args.length >= 1) {
            inputStream = new FileInputStream(args[0]);
        }

        if (args.length >= 2) {
            outputStream = new PrintStream(args[1]);
        }*/
		if(args.length >=1 ){
			cacheBlockBits =  Integer.parseInt(args[0]);
		}

		if(args.length >=2 ){
			cacheSizeBits = Integer.parseInt(args[1]);
		}

		if(args.length >=3 ){
			associativity = Integer.parseInt(args[2]);
		}
		
		if(args.length >=4 ){
			wayPredictor = Integer.parseInt(args[3]);
		}

		CacheSimulator.simulate(inputStream, outputStream, (int)cacheBlockBits,(int)cacheSizeBits,(int)associativity,wayPredictor);

	}
}
