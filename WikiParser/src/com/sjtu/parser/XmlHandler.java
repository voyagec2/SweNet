package com.sjtu.parser;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory; 

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  

import com.mysql.jdbc.Connection;

public class XmlHandler extends DefaultHandler {
	private List<Page> pages =null;
	private Page page = null;
	private Contributor contributor = null;
	private Revision revision = null;
	private Stack<String> state = null;
	private String preTag = "";
	private int counter = 0;
	
	public List<Page> getPages(InputStream xmlStream) throws Exception{
		SAXParserFactory factory = SAXParserFactory.newInstance();  
        SAXParser parser = factory.newSAXParser();  
        XmlHandler handler = new XmlHandler();  
        parser.parse(xmlStream, handler);  
        return handler.getPages();  
	}
	
	public List<Page> getPages(){
		return pages;
	}
	
	@Override
	public void startDocument() throws SAXException {
		pages = new ArrayList<Page>();
		state = new Stack<String>();
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equals("page")){
			page = new Page();
			state.push("page");
		}
		else if(qName.equals("revision")){
			revision = new Revision();
			state.push("revision");
		}
		else if(qName.equals("contributor")){
			contributor = new Contributor();
			state.push("contributor");
		}else if(qName.equals("redirect")){
			if(page!=null){
				page.setRedirect(attributes.getValue(0));
			}
		}
		preTag = qName;
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(state.empty()){
			return;
		}
		if(qName.equals("page")){
			// Add impl of database here.
            
			MarkDownParser MDparser = new MarkDownParser();
			String LinkComment = MDparser.getLink(page.getRevision().getComment());
			String LinkText = MDparser.getLink(page.getRevision().getText());
			String Category = MDparser.getCategory(page.getRevision().getText());
			String Infobox = MDparser.getInfobox(page.getRevision().getText());
			
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/swenet";
			String dbuser = "root";
			String dbpassword = "root";
			try {
				Class.forName(driver);
				Connection conn = (Connection) DriverManager.getConnection(url, dbuser, dbpassword);
				if(!conn.isClosed())
					System.out.println("Succeeded connecting to the Database!");


				String sql = "INSERT INTO Page"
						+ "(idPage,title,redirect_title,idRevision,idParent,"
						+ "timestamp,idContributor,ContributorName,comment,text,"
						+ "LinkComment,LinkText,infobox,Category)"
						+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				java.sql.PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1,page.getId());
				ps.setString(2,page.getTitle());
				ps.setString(3,page.getRedirect());
				ps.setInt(4,page.getRevision().getId());
				ps.setInt(5,page.getRevision().getParentid());
				ps.setString(6,page.getRevision().getTimestamp());
				ps.setInt(7,page.getRevision().getContributor().getId());
				ps.setString(8,page.getRevision().getContributor().getUsername());
				ps.setString(9,page.getRevision().getComment());
				ps.setString(10,page.getRevision().getText());	
				ps.setString(11,LinkComment);	
				ps.setString(12,LinkText);	
				ps.setString(13,Infobox);	
				ps.setString(14,Category);	
				int count=ps.executeUpdate();
				conn.close();    
			} catch(ClassNotFoundException e) {   
				System.out.println("Sorry,can`t find the Driver!");   
				e.printStackTrace();   
			} catch(SQLException e) {   
				e.printStackTrace();   
			} catch(Exception e) {   
				e.printStackTrace();   
			}

			pages.add(page);
			counter ++;
			System.out.println("Finish Page "+counter+": "+page.getTitle());
			page = null;
			state.pop();
		}
		else if(qName.equals("revision")){
			if(page!=null){
				page.setRevision(revision);
			}
			revision = null;
			state.pop();
			
		}
		else if(qName.equals("contributor")){
			if(revision!=null){
				revision.setContributor(contributor);
			}
			contributor = null;
			state.pop();
		}
		preTag = "";
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(state.empty()){
			return;
		}
		String top = state.peek();
		String content = new String(ch,start,length); 
		if(top.equals("page") && page != null){  
            if(preTag.equals("title")){  
                page.setTitle(content);  
            }else if(preTag.equals("ns")){  
                page.setNs(Integer.parseInt(content));  
            }else if(preTag.equals("id")){  
                page.setId(Integer.parseInt(content));  
            }
        } else if(top.equals("revision") && revision != null){
        	if(preTag.equals("id")){  
                revision.setId(Integer.parseInt(content));  
            }else if(preTag.equals("parentid")){  
            	revision.setParentid(Integer.parseInt(content)); 
            }else if(preTag.equals("timestamp")){  
            	revision.setTimestamp(content); 
            }else if(preTag.equals("comment")){  
            	revision.setComment(revision.getComment()+content); 
            }else if(preTag.equals("model")){  
            	revision.setModel(content); 
            }else if(preTag.equals("format")){  
            	revision.setFormat(content); 
            }else if(preTag.equals("text")){
            	
            	revision.setText(revision.getText()+content); 
            }else if(preTag.equals("sha1")){  
            	revision.setSha1(content); 
            }
        } else if(top.equals("contributor") && contributor != null){
        	if(preTag.equals("username")){  
            	contributor.setUsername(content); 
            }else if(preTag.equals("id")){  
            	contributor.setId(Integer.parseInt(content)); 
            }else if(preTag.equals("ip")){
            	contributor.setIp(content);
            }
        }
	}	
}


