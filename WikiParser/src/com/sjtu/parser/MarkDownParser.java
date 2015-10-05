package com.sjtu.parser;

import java.util.Stack;

public class MarkDownParser {
	
	
	public String getInfobox(String txt) {
		//System.out.println(txt);
		String Infobox = "";
		String tmp = null;
    	String tmp2 = null;
    	String Key = null;
    	String Value = null;
    	while (txt.indexOf("}}")>0) {    // InfoBox  is  in {{ and }} and have sign "Infobox"
    		int end = txt.indexOf("}}");
    		int start = end;
    		while ((!txt.substring(start,start+2).equals("{{")) && start>=0)	start--; 
    		tmp = txt.substring(start+2,end);
    		if (end-start<7) { 
    			   txt = txt.substring(0,start)+txt.substring(start+2,end)+txt.substring(end+2,txt.length());	
    		}
    		else
    		if (tmp.substring(0,7).equals("Infobox"))  {    // check
    			int p = tmp.indexOf("|");
    			tmp = tmp.substring(p+1,tmp.length());
    			while (tmp.indexOf("=")>0) {   // Infobox  is split by | and  =
    				    p = tmp.indexOf("=");
    					Key = tmp.substring(0,p);
    					tmp = tmp.substring(p+1,tmp.length());
    					p = tmp.indexOf("=");
    					if (p<0) {
    						Value = getCleanTxt(tmp);
    					}
    					else {
    						while ((!tmp.substring(p,p+1).equals("|")) && p>=0) p--;
    						Value = getCleanTxt(tmp.substring(0, p));
    						tmp = tmp.substring(p+1,tmp.length());
    					}
    					
    					if (!Key.equals("")) Infobox = Infobox + "[" + Key + "="+ Value + "]";
    					System.out.println("Key:"+Key);
    		    		System.out.println("Value:"+Value);
    			}
    			txt = txt.substring(0,start)+txt.substring(end+2,txt.length());
    		}
    		else 
    		   txt = txt.substring(0,start)+txt.substring(start+2,end)+txt.substring(end+2,txt.length());	
    	   
    	}
		
		return Infobox;
	}
	
	public String getCategory(String txt) {
				
		String Category = "";
    	String tmp = null;
    	String tmp2 = null;
    	while (txt.indexOf("]]")>0) {    // Category's sign is [[ and ]] and have :
    		int end = txt.indexOf("]]");
    		int start = end;
    		while ((!txt.substring(start,start+2).equals("[[")) && start>=0)	start--; 
    		tmp = txt.substring(start+2,end);
    		if (tmp.indexOf(":")>0)  {    // [[ : ]] is category
    			int p = tmp.indexOf(":");
    			tmp2 = tmp.substring(0,p);
    			if (tmp2.equals("Category")) { // check 
    				
    				
    				tmp = tmp.substring(p+1,tmp.length());
    				System.out.println("start:"+start);
    	    		System.out.println("end:"+end);
    	    		System.out.println(tmp);
    				while (tmp.indexOf("|")>0) {   // Category may be split by |
    					int mid = tmp.indexOf("|");
    					tmp2 = tmp.substring(0,mid);
    					if (!tmp2.equals("")) Category = Category + "["+tmp2+"]";
    					tmp = tmp.substring(mid+1, tmp.length());
    				}
    				if (!tmp.equals("")) Category = Category + "[" + tmp + "]";
    			}
    		}
    	    txt = txt.substring(0,start)+txt.substring(start+2,end)+txt.substring(end+2,txt.length());
    	}
		return Category;
	}
	
	public String getLink(String txt) {
    	String Link = "";
    	String tmp = null;
    	String tmp2 = null;
    	while (txt.indexOf("]]")>0) {    // Link's sign is [[ and ]] but don't have :
    		int end = txt.indexOf("]]");
    		int start = end;
    		while ((!txt.substring(start,start+2).equals("[[")) && start>=0)	start--; 
    	
    		tmp = txt.substring(start+2,end);
    	/*	System.out.println("start:"+start);
    		System.out.println("end:"+end);
    		System.out.println(tmp);*/
    		
    		if (tmp.indexOf(":")<0)  {    // [[ : ]] is category
    			while (tmp.indexOf("|")>0) {   // Link may be split by |
    				int mid = tmp.indexOf("|");
    				tmp2 = tmp.substring(0,mid);
    				if (!tmp2.equals("")) Link = Link + "["+tmp2+"]";
    				tmp = tmp.substring(mid+1, tmp.length());
    			}
    			if (!tmp.equals("")) Link = Link + "[" + tmp + "]";
    		}
    	    txt = txt.substring(0,start)+txt.substring(start+2,end)+txt.substring(end+2,txt.length());
    	}
    	return Link;
    }
    
    public String getCleanTxt(String txt) {
    	while (txt.indexOf("[[")>0) {
    		int p = txt.indexOf("[[");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	while (txt.indexOf("]]")>0) {
    		int p = txt.indexOf("]]");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	while (txt.indexOf("{{")>0) {
    		int p = txt.indexOf("{{");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	while (txt.indexOf("}}")>0) {
    		int p = txt.indexOf("}}");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	while (txt.indexOf("==")>0) {
    		int p = txt.indexOf("==");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	while (txt.indexOf("''")>0) {
    		int p = txt.indexOf("==");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	return txt;
    	
    }
}
