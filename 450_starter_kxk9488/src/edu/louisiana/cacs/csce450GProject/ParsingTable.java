package edu.louisiana.cacs.csce450GProject;

import java.util.HashMap;
import java.util.Map;

public class ParsingTable {
	public Map<String, String[]> Table = new HashMap<String, String[]>();
	public void createParsingTable(){
	
	
		String[] id = {"S5",null,null,null,"S5",null,"S5","S5",null,null,null,null};
		String[] plus = {null,"S6","R2","R4",null,"R6",null,null,"S6","R1","R3","R5"};
		String[] star = {null,null,"S7","R4",null,"R6",null,null,null,"S7","R3","R5"};
		String[] leftparen = {"S4",null,null,null,"S4",null,"S4","S4",null,null,null,null};
		String[] rightparen = {null,null,"R2","R4",null,"R6",null,null,"S11","R1","R3","R5"};
		String[] dollar = {null,"accept","R2","R4",null,"R6",null,null,null,"R1","R3","R5"};
		String[] E = {"1",null,null,null,"8",null,null,null,null,null,null,null };
		String[] T = {"2",null,null,null,"2",null,"9",null,null,null,null,null };
		String[] F = {"3",null,null,null,"3",null,"3","10",null,null,null,null };
		
		
		Table.put("id" ,id );
		Table.put("plus", plus);
		Table.put("star", star);
		Table.put("leftparen",leftparen);
		Table.put("rightparen",rightparen);
		Table.put("dollar",dollar);
		Table.put("E",E);
		Table.put("T",T);
		Table.put("F",F);
		
	}
}
