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
    		while ((!txt.substring(start,start+2).equals("{{")) && start>0)	start--;
    		if (start ==0 && !txt.substring(start,start+2).equals("{{")) { // {{ miss
    			txt =txt.substring(0, end)+txt.substring(end+2,txt.length());
    			continue;			
    		}
    		tmp = txt.substring(start+2,end);
    		if (tmp.length()<8) { 
    			   txt = txt.substring(0,start)+txt.substring(start+2,end)+txt.substring(end+2,txt.length());	
    		}
    		else
    		if (tmp.substring(0,7).equals("Infobox"))  {    // check
    			 int p = tmp.indexOf("\n");
    			 int q;
    			 tmp = tmp.substring(p+1,tmp.length());
    			while (tmp.indexOf("\n")>0) {   // Infobox  is split by \n and  |
    				   p= tmp.indexOf("\n"); 
    			
    				   tmp2 = tmp.substring(0,p);
    				   tmp = tmp.substring(p+1,tmp.length());
    				   if (tmp.length()==0) break;
    				   while (tmp.substring(0,1).equals("*"))  {// not another infobox  
    					   p = tmp.indexOf("\n"); 
    					   if (p<0)  tmp2 = tmp2 + tmp;
    					   else  tmp2 = tmp2 + tmp.substring(0,p);
    					   tmp = tmp.substring(p+1,tmp.length());
    					   if (tmp.length()==0) break;
    				   }
    				    p = tmp2.indexOf("=");
    				    q = tmp2.indexOf("|");
    				    if (p<0 || q<0 || q>p) continue;
    					Key = tmp2.substring(q+1,p);
    					Value = getCleanTxt(tmp2.substring(p+1,tmp2.length()));
    					
    					if (!Key.equals("")) Infobox = Infobox + "[" + Key + "="+ Value + "]";
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
    		while ((!txt.substring(start,start+2).equals("[[")) && start>0)	start--;
    		if (start ==0 && !txt.substring(start,start+2).equals("[[")) { // [[ miss
    			txt =txt.substring(0, end)+txt.substring(end+2,txt.length());
    			continue;			
    		}
    		tmp = txt.substring(start+2,end);
    		if (tmp.indexOf(":")>0)  {    // [[ : ]] is category
    			int p = tmp.indexOf(":");
    			tmp2 = tmp.substring(0,p);
    			if (tmp2.equals("Category")) { // check 
 				tmp = tmp.substring(p+1,tmp.length());
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
    		while ((!txt.substring(start,start+2).equals("[[")) && start>0)	start--; 
    		if (start ==0 && !txt.substring(start,start+2).equals("[[")) { // [[ miss
    			txt =txt.substring(0, end)+txt.substring(end+2,txt.length());
    			continue;			
    		}
    		tmp = txt.substring(start+2,end);
    		
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
    
	public String removeHtmlForm(String txt) {
		while (txt.indexOf("</ref>")>0) {     
			int end = txt.indexOf("</ref>");
			int start = end;
    		while ((!txt.substring(start,start+5).equals("<ref>")) && start>0)	start--;
    	    txt = txt.substring(0,start)+txt.substring(end+6,txt.length());
		}  
		while (txt.indexOf("</ref>")>0) {     
			int end = txt.indexOf("</ref>");
			int start = end;
    		while ((!txt.substring(start,start+5).equals("<ref>")) && start>0)	start--;
    	    txt = txt.substring(0,start)+txt.substring(end+6,txt.length());
		}  
		return txt;
			
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
    		int p = txt.indexOf("''");
    		txt =txt.substring(0,p) + txt.substring(p+2, txt.length());
    	}
    	
    	while (txt.indexOf("</ref>")>0) {     
			int end = txt.indexOf("</ref>");
			int start = end;
    		while ((!txt.substring(start,start+5).equals("<ref>")) && start>0)	start--;
    	    txt = txt.substring(0,start)+txt.substring(end+6,txt.length());
		}  
		while (txt.indexOf(">")>0) {     
			int end = txt.indexOf(">");
			int start = end;
    		while ((!txt.substring(start,start+1).equals("<")) && start>0)	start--;
    	    txt = txt.substring(0,start)+txt.substring(end+1,txt.length());
		}  
    	
    	return txt;
    	
    }
}
