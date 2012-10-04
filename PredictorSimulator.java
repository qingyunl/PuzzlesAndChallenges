/*
This is part of coursework assignment simulating different types of predictors namely: bimodal, gshare and tournament on a demo trace file containing 1 million intructions.
See PART B of https://www.cis.upenn.edu/~milom/cis501-Fall11/homework/hw3/
*/
import java.lang.Exception;
import java.lang.Long;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;

class PredictorSimulator
{

	@SuppressWarnings("null")
	public static void simulate(InputStream incomingStream, PrintStream outputStream) throws Exception 
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

		//Question 1
		long totalBranchTakenPositive = 0;
		long totalBranchTakenNegative = 0;

		//Question 2

		int nSize = 3;
		long  totalBranchIns = 0;
		long bimodalTotalPredictionSuccess = 0;
		int tempSize = (int)Math.pow(2,nSize);
		String bimodalPrediction = null;
		String bimodalBHTCopy = null;
		int predictionIndex = -1;
		
		ArrayList<String> bimodalBHT = new ArrayList<String>(tempSize);
		for(int i=0;i<tempSize;i++){
			bimodalBHT.add("N");
		}
		bimodalBHTCopy = bimodalBHT.toString();
		//Question 3
		int historyBits = 4;
		int gShareTotalPredictionSuccess = 0;
		String gSharePrediction = null;
		int gSharePredictionIndex = -1;
		int gShareBHTBits = 4;
		int gshareSize = (int)(Math.pow(2,gShareBHTBits));
		ArrayList<String> gShareBHT = new ArrayList<String>(gshareSize);
		long[] historyTable = new long[historyBits];
		
		for(int i=0;i<historyBits;i++){
			historyTable[i] = 0;
		}
		
		for(int i=0;i<gshareSize;i++){
			gShareBHT.add("N");
		}

		//Question 4 - Tournament predictor
		int tournamentCounterSize = 3;
		long tournamentTotalPredictionSuccess = 0;
		int tournamentSize = (int)Math.pow(2,tournamentCounterSize);
		String tournamentPrediction = null;
		String tournamentBHTCopy = null;
		int tournamentPredictionIndex = -1;
		String tournamentPredictedValue = "N";
		int tournamentCorrectIncorrect = 0;
		ArrayList<String> tournamentBHT = new ArrayList<String>(tournamentSize);
		for(int i=0;i<tournamentSize;i++){
			tournamentBHT.add("B");
		}
			
		BufferedReader r = new BufferedReader(new InputStreamReader(incomingStream));
		String line;

		outputStream.format("Processing trace...\n");
		outputStream.format("\t\t\t\t\tQuestion 2: Bimodal Predictor\n");

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

			{// For Question 1 Starts

				if(conditionRegister.equals("R") && (TNnotBranch.equals("-") == false)){
					if(TNnotBranch.equals("T")){
						// Increment totalBranchTakenPositive count
						totalBranchTakenPositive++;
					}
					else{
						//Increment totalBranchTakenNegative count
						totalBranchTakenNegative++;
					}
				}
			}// For Question 1 ends

			{// For Question 2 starts - Bimodal predictor

				if(conditionRegister.equals("R") && (TNnotBranch.equals("-") == false)){
					//This is a branch. Increment totalBranchIns
					totalBranchIns++;

					//Predict whether branch or not and correspondingly update the BHT
					predictionIndex = ((int)instructionAddress)%tempSize;
					bimodalPrediction = bimodalBHT.get(predictionIndex);
				}
			}// For Question 2 ends

			{// Question 3 starts

				if(conditionRegister.equals("R") && (TNnotBranch.equals("-") == false)){
					//This is a branch. Increment totalBranchIns
					//Predict whether branch or not and correspondingly update the BHT
					gSharePredictionIndex = ((int)(instructionAddress^calcDecimal(historyTable)))%gshareSize;
					gSharePrediction = gShareBHT.get(gSharePredictionIndex);
				}
			}// Question 3 ends

				
			{// Question 4 starts -- Tournament Predictor
				if(conditionRegister.equals("R") && (TNnotBranch.equals("-") == false)){
					//This is a branch. Increment totalBranchIns
					//Predict whether branch or not and correspondingly update the BHT
					tournamentPredictionIndex = ((int)instructionAddress)%tournamentSize;
					tournamentPrediction = tournamentBHT.get(tournamentPredictionIndex);
					//If the tournament prediction is 'b' or 'B' then take Bimodal else gShare
					
					if(tournamentPrediction.equalsIgnoreCase("B")){// Choose the bimodal predictor
						tournamentPredictedValue = bimodalPrediction; 
						if(bimodalPrediction.equalsIgnoreCase(TNnotBranch) ){// Prediction is true. 
							// Update b->B, B->B
							//if(TNnotBranch.equals("T")){
							// update t->T and T->T
							tournamentCorrectIncorrect = 1;
							//If gShare didnt predicted correct then update to B
							if(!gSharePrediction.equalsIgnoreCase(TNnotBranch)){
								tournamentBHT.set(tournamentPredictionIndex,"B");
							}
							
						}
						else{ // Check if gShare predictor gives correct prediction.
							tournamentCorrectIncorrect = 0;
							tournamentTotalPredictionSuccess++;
							if(gSharePrediction.equalsIgnoreCase(TNnotBranch)){
								// gShare gives correct prediction. Update B->b,b->g
								if(tournamentPrediction.equals("B")){
									//Update B->b
									tournamentBHT.set(tournamentPredictionIndex,"b");
								}else{
									//Update b->g
									tournamentBHT.set(tournamentPredictionIndex,"g");
								}
							}

						}
					}else{ // Choose gShare predictor
						tournamentPredictedValue = gSharePrediction;
						if(gSharePrediction.equalsIgnoreCase(TNnotBranch) ){// Prediction is true. 
							// Update g->G, G->G
							tournamentCorrectIncorrect = 1;
							//If biModal didnt predicted correct then update to G
							if(!bimodalPrediction.equalsIgnoreCase(TNnotBranch)){
								tournamentBHT.set(tournamentPredictionIndex,"G");
							}
							
						}
						else{ // Check if bimodal predictor gives correct prediction.
							tournamentTotalPredictionSuccess++;
							tournamentCorrectIncorrect =0;
							if(bimodalPrediction.equalsIgnoreCase(TNnotBranch)){
								// gShare gives correct prediction. Update G->g,g->b
								if(tournamentPrediction.equals("G")){
									//Update B->b
									tournamentBHT.set(tournamentPredictionIndex,"g");
								}else{
									//Update b->g
									tournamentBHT.set(tournamentPredictionIndex,"b");
								}
							}

						}
					}
					String myStr = String.valueOf(historyTable[0]);
					
					for(int p = 1;p<historyTable.length;p++){
						myStr = myStr + String.valueOf(historyTable[p]) ;
					}
					
					if(tournamentCorrectIncorrect == 1){
						outputStream.format("%s\t %s\t %s\t %s\t %d\t %s\t %s\t %s\t %d\n",tournamentBHT.toString(),bimodalBHT.toString(),gShareBHT.toString(),myStr,instructionAddress,TNnotBranch,tournamentPredictedValue,"Correct",tournamentTotalPredictionSuccess);
					}else{
						outputStream.format("%s\t %s\t %s\t %s\t %d\t %s\t %s\t %s\t %d\n",tournamentBHT.toString(),bimodalBHT.toString(),gShareBHT.toString(),myStr,instructionAddress,TNnotBranch,tournamentPredictedValue,"InCorrect",tournamentTotalPredictionSuccess);
					}
						

						// Update the bimodal predictor
						if(bimodalPrediction.equalsIgnoreCase(TNnotBranch) ){// Prediction is true. 
							// Update n->N, N->N or t->T, T-T
							if(TNnotBranch.equals("T")){
								// update t->T and T->T
								bimodalBHT.set(predictionIndex,"T");
							}
							else{ //TNnotBranch is N. Update n->N and N->N
								bimodalBHT.set(predictionIndex,"N");
							}
						}else{ // Prediction Failed. Update N->n,n->t or T->t,t->n
							bimodalTotalPredictionSuccess++;
							if(TNnotBranch.equals("T")){
								// update N->n and n->t
								if(bimodalPrediction.equals("N")){
									//Update N->n
									bimodalBHT.set(predictionIndex,"n");
								}else{ //Update n->t
									bimodalBHT.set(predictionIndex,"t");
								}
							}
							else{ //TNnotBranch is N. Update t->n and T->t
								if(bimodalPrediction.equals("T")){
									//Update T->t
									bimodalBHT.set(predictionIndex,"t");
								}else{ //Update t->n
									bimodalBHT.set(predictionIndex,"n");
								}

							}
						}
						// Bimodal predictor updates ends

						//Update gShare predictor

						if(gSharePrediction.equalsIgnoreCase(TNnotBranch) ){// Prediction is true. 
							// Update n->N, N->N or t->T, T-T
							if(TNnotBranch.equals("T")){
								// update t->T and T->T
								gShareBHT.set(gSharePredictionIndex,"T");
							}
							else{ //TNnotBranch is N. Update n->N and N->N
								gShareBHT.set(gSharePredictionIndex,"N");
							}
						}else{ // Prediction Failed. Update N->n,n->t or T->t,t->n
							gShareTotalPredictionSuccess++;
							if(TNnotBranch.equals("T")){
								// update N->n and n->t
								if(gSharePrediction.equals("N")){
									//Update N->n
									gShareBHT.set(gSharePredictionIndex,"n");
								}else{ //Update n->t
									gShareBHT.set(gSharePredictionIndex,"t");
								}
							}
							else{ //TNnotBranch is N. Update t->n and T->t
								if(gSharePrediction.equals("T")){
									//Update T->t
									gShareBHT.set(gSharePredictionIndex,"t");		
								}else{ //Update t->n
									gShareBHT.set(gSharePredictionIndex,"n");
								}

							}
						}
						//Update the Branch history table corresponding to the latest sequence
						for(int k = 0 ;k < (historyBits -1);k++ ){
							historyTable[k] = historyTable[k+1];
						}
						historyTable[historyBits-1] = TNnotBranch.equals("T") ? 1 : 0;
						//Update gShare predictor ends




						/*//Print Output
								if(bimodalPrediction.equalsIgnoreCase(TNnotBranch)){
		                		outputStream.format("%s \t %d \t %s \t %s \t %s \t %d \n", bimodalBHTCopy, instructionAddress ,TNnotBranch,bimodalBHTCopy.charAt(predictionIndex),"correct",bimodalTotalPredictionSuccess);
		                	}else{
		                		outputStream.format("%s \t %d \t %s \t %s \t %s \t %d \n", bimodalBHTCopy, instructionAddress,TNnotBranch,bimodalBHTCopy.charAt(predictionIndex),"Incorrect",bimodalTotalPredictionSuccess++);
		                	}

		                	bimodalBHTCopy = bimodalBHT.toString();		*/
					}
				}// Question 4 ends
				// For each macro-op
				if (microOpCount == 1) {
					totalMacroops++;
				}
			}
		

			outputStream.format("Processed %d trace records.\n", totalMicroops);

			outputStream.format("Micro-ops: %d\n", totalMicroops);
			outputStream.format("Macro-ops: %d\n", totalMacroops);

			{// Output for Question 1 starts
				outputStream.format("\t\t\t\t\tQuestion 1\n");
				outputStream.format("1. Total no of branches: %d: \n", totalBranchTakenPositive + totalBranchTakenNegative);
				outputStream.format("1. Total branch taken when always 'TAKEN' %d: \n", totalBranchTakenPositive);
				outputStream.format("1. Total branch taken when always 'NOT TAKEN' %d: \n", totalBranchTakenNegative);
				// Output for Question 1 ends
			}

			{// Output for Question 2 starts
				outputStream.format("\t\t\t\t\tQuestion 2\n");
				outputStream.format("1. No of correct predictions %d: \n", totalBranchIns - bimodalTotalPredictionSuccess);
				outputStream.format("1. No of Incorrect predictions %d: \n", bimodalTotalPredictionSuccess);
				// Output for Question 2 ends
			}
			
			{// Output for Question 3 starts
				outputStream.format("\t\t\t\t\tQuestion 3\n");
				outputStream.format("1. No of correct predictions %d: \n", totalBranchIns - gShareTotalPredictionSuccess);
				outputStream.format("1. No of Incorrect predictions %d: \n", gShareTotalPredictionSuccess);
			// Output for Question 3 ends
			}
		
			{// Output for Question 4 starts
				outputStream.format("\t\t\t\t\tQuestion 4\n");
				outputStream.format("1. No of correct predictions %d: \n", totalBranchIns - tournamentTotalPredictionSuccess);
				outputStream.format("1. No of Incorrect predictions %d: \n", tournamentTotalPredictionSuccess);
			// Output for Question 4 ends
			}

	}

	public static long calcDecimal(long[] binary){//To convert a binary no to decimal one
		long decimal = 0;
		for(long i= binary.length-1 ; i>=0 ; i--){
			decimal += binary[(int) i] * Math.pow(2, (binary.length - 1) - i);
		}
		return decimal;
	}
	
	public static void main(String[] args) throws Exception
	{
		InputStream inputStream = System.in;
		PrintStream outputStream = System.out;

		if (args.length >= 1) {
			inputStream = new FileInputStream(args[0]);
		}

		if (args.length >= 2) {
			outputStream = new PrintStream(args[1]);
		}

		PredictorSimulator.simulate(inputStream, outputStream);
	}
}
