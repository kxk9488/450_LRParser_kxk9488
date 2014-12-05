package edu.louisiana.cacs.csce450GProject;
import java.io.IOException;
import java.util.*;

public class Parser{
	static Stack<String> mainStack = new Stack<String>();
	static LinkedList<String> inputTokens = new LinkedList<String>();
	static Stack<String> parseTreeStack = new Stack<String>();
	static Stack<String> parseTreeOutput = new Stack<String>();
	static String actionLookup = null;
	static String actionValue = null;
	static String lhsValue = null;
	static int rhsLength = 0;
	static Stack<String> tempStack = new Stack<String>();
	static String gotoLookup = null;
	static int gotoValue = 0;
	static String stackAction = null;
	static String parseTreeElements = "";
	static String filePath = "";
	static String parseTreeStackTemp = "";
	static String display = "";
	static int error = 0;
	public Parser(String fileName){
		filePath = fileName;
		mainStack.push("0");
		System.out.println("File to parse : "+fileName);
	}
	
	public void printParseTree(){
		System.out.println("Hello World from " + getClass().getName());
		String parsetree = parseTreeOutput.lastElement();
		parsetree = parsetree.replace("[", "");
		parsetree = parsetree.replace("]", "");
		parsetree = parsetree.replace(" ", "");
		//System.out.print(parsetree);
		int spaces = 0;
		int intend = 0;
		int level = 0;
		System.out.println("");
		System.out.println("Parse Tree");
		for(int i = 0, n = parsetree.length() ; i < n ; i++) { 
		    char c = parsetree.charAt(i);
		    if (Character.isLetterOrDigit(c)){
		    	if ((""+c).equalsIgnoreCase("i")){
		    		System.out.println(addSpaces("id",level));
		    	}
		    	else if (!(""+c).equalsIgnoreCase("d"))
		    	{
		    		System.out.println(addSpaces(""+c,level));
		    	}
		    	level++;
		    }
		    else{
		    	intend++;
		    	level = intend;
		    	System.out.println(addSpaces(""+c,intend));
		    }
		    
		}
		System.out.println("$");
	}

	
	public void parse() throws IOException{
		ParsingTable parsecrt = new ParsingTable();
		parserGrammar parseGram = new parserGrammar();
		parsecrt.createParsingTable();
		parseGram.createGrammarTable();
		Map<String, String> symbols = new HashMap<String, String>();
		Scanner.getTokens(filePath , inputTokens);
		symbols.put("id","id");
		symbols.put("+","plus");
		symbols.put("*","star");
		symbols.put("(","leftparen");
		symbols.put(")","rightparen");
		symbols.put("$","dollar");
		
		
		System.out.print(formatting("Step")+formatting("Stack")+formatting("input tokens")+formatting("action lookup")+formatting("action value")+formatting("value of LHS")+formatting("length of RHS")+formatting("temp stack")+formatting("gotolookup")+formatting("goto value")+formatting("stack action")+formatting("parse tree stack"));
		
		int Step = 1;
		while (actionValue != "accept")
		{
			int lastElem = Integer.parseInt(mainStack.lastElement());
			String lasteleStack = inputTokens.element();
			actionLookup =  "["+lastElem+","+lasteleStack+"]";
			actionValue = parsecrt.Table.get(symbols.get(lasteleStack))[lastElem];
			System.out.println("");
			if (actionValue == null){
				System.out.println("Input String is grammatically incorrect");
				error = 1;
				break;
			}
			if (actionValue.substring(0, 1).equalsIgnoreCase("S")){
				stackAction = "push "+lasteleStack+actionValue.substring(1, 2);
				buildparseTreeElements(lasteleStack,"0");
				if (display.equalsIgnoreCase("")){
					display = parseTreeElements+parseTreeStackTemp ; 
				}
				System.out.println(formatting(""+Step)+formatting(formStackString(mainStack))+ formatting(formQueueString(inputTokens))+formatting(actionLookup)+formatting(actionValue) + formatting("")+formatting("")+formatting("")+formatting("")+formatting("")+formatting(stackAction)+formatting(display));
				display = "";
				shiftOperation( lasteleStack ,actionValue.substring(1, 2));
				
				
			}
			else if (actionValue.substring(0, 1).equalsIgnoreCase("R")){
				String gramTerm = parseGram.Grammar.get(actionValue.substring(1, 2));
				String lhs = gramTerm.split("->")[0];
				String rhs = gramTerm.split("->")[1];
				lhsValue = lhs ;
				rhsLength = rhs.replaceAll("id", "i").length();
				createTempStack();
				buildgotoLookup();
				gotoValue = Integer.parseInt(parsecrt.Table.get(lhs)[Integer.parseInt(tempStack.lastElement())]);
				stackAction = "push "+lhs+gotoValue;
				buildparseTreeElements(lhs,actionValue.substring(1, 2));
				if (display.equalsIgnoreCase("")){
					display = parseTreeElements+parseTreeStackTemp ; 
				}
				System.out.println(formatting(""+Step)+formatting(formStackString(mainStack))+ formatting(formQueueString(inputTokens))+formatting(actionLookup)+formatting(actionValue) + formatting(lhsValue)+formatting(""+rhsLength)+formatting(formStackString(tempStack))+formatting(gotoLookup)+formatting(""+gotoValue)+formatting(stackAction)+formatting(display));
				display = "";
				reduceOperation();
				
			}
			else if (actionValue.equalsIgnoreCase("accept")){
				System.out.println(formatting(""+Step)+formatting(formStackString(mainStack))+ formatting(formQueueString(inputTokens))+formatting(actionLookup)+formatting(actionValue) + formatting("")+formatting("")+formatting("")+formatting("")+formatting("")+formatting("")+formatting(parseTreeOutput.lastElement()));
			}
			Step++;
			
		}
		if (error == 0)
		{
			printParseTree();
		}
		
		
	}
	
	public void shiftOperation(String pushele1 , String pushele2){
		mainStack.push(pushele1);
		mainStack.push(""+pushele2);
		inputTokens.remove();
	}
	
	public void reduceOperation(){
		popMainStack(rhsLength*2);
		mainStack.push(lhsValue);
		mainStack.push(""+gotoValue);
		
	}
	@SuppressWarnings("unchecked")
	public void createTempStack(){
		tempStack = (Stack<String>) mainStack.clone();
		popTempStack(rhsLength*2);
		
	}
	public void popTempStack(int noOfPops){
		for(int i=0 ; i< noOfPops;i++)
			tempStack.pop();
	}
	public void popMainStack(int noOfPops){ 
		for(int i=0 ; i< noOfPops;i++)
			mainStack.pop();
	}
	public void buildgotoLookup(){
		gotoLookup = "["+tempStack.lastElement()+","+lhsValue+"]";
	}
	
	public String formStackString(Object o) {
		 String temp = "";
		 Iterator<String> iter = ((Vector<String>) o).iterator();

		 while (iter.hasNext()){
			 temp = temp + iter.next();
		 }
		 return temp;
	}
	 // ListIterator approach
	public String formQueueString(Queue<String> inputTokens){
		String temp = "";
		//System.out.println("ListIterator Approach: ");
	    ListIterator<String> listIterator =  (ListIterator<String>) inputTokens.iterator();
	    while (listIterator.hasNext()) {
	    	temp = temp + listIterator.next();
	    }
	    return temp;
    }
	public void buildparseTreeElements(String treeElement , String string){
		if (Integer.parseInt(string)%2 == 1){
			parseTreeStack.push(parseTreeElements);
			if(Integer.parseInt(string) == 1){
				
				display = "[E"+findElement("E",parseTreeStack)+"+"+findElement("T",parseTreeOutput)+"]" ;
				parseTreeOutput.push("[E"+findElement("E",parseTreeStack)+"+"+findElement("T",parseTreeOutput)+"]") ;
				//parseTreeElements = "";
				//actionValue.substring(1,2);
			}
			else if (Integer.parseInt(string) == 3){
				
				display = "[T"+findElement("T",parseTreeStack)+"*"+findElement("F",parseTreeStack)+findElement("E",parseTreeStack)+"]" ;
				parseTreeOutput.push("[T"+findElement("T",parseTreeStack)+"*"+findElement("F",parseTreeStack)+"]");//+findElement("E",parseTreeStack)) ;
				parseTreeElements = parseTreeOutput.lastElement();
			}	
			else if (Integer.parseInt(string) == 5){
				//parseTreeOutput.push("[T"+findElement("T",parseTreeStack)+"*"+findElement("F",parseTreeStack)+"]");//+findElement("E",parseTreeStack)) ;
				display = "[T"+findElement("T",parseTreeStack)+"*"+findElement("F",parseTreeStack)+"]";
				
			}
			
		}
		else
		{
				if (parseTreeElements.equals("") ){
					if(Character.isLetterOrDigit(treeElement.charAt(0))){
					parseTreeElements = treeElement ;
					}
				}
				else {
					if(Character.isLetterOrDigit(treeElement.charAt(0))){
						parseTreeElements = "[" + treeElement + " "+ parseTreeElements + "]";
						//System.out.println(treeElement.charAt(0));
					}
					else{
						parseTreeStack.push(parseTreeElements);
						parseTreeStackTemp = parseTreeElements + parseTreeStackTemp;
						parseTreeElements = "";
						//System.out.println(parseTreeStack.toString());
					}
				}			
		}
	}
	
	public String findElement(String searchChar, Stack<String> parseTreeStack)
	{
		String temp="";
		for(String obj : parseTreeStack)
		{
		    if (obj.substring(1,2).equalsIgnoreCase(searchChar))
		    		{
		    			temp = obj;
		    		}
		}
		return temp;
	}
	public String addSpaces(String str , int noofspaces){
		for (int i = 0 ; i < noofspaces ; i++)
		{
			str = "  " + str ;
		}
		return str;
	}
	public String formatting(String str){
		
		while (str.length()< 40){
			str = " "+str;
		}
		return str;
	}
	
}