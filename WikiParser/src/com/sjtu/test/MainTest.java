package com.sjtu.test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.sjtu.parser.Page;
import com.sjtu.parser.XmlHandler;

public class MainTest {
	private static String xmlPath = "E://data//enwiki-20150901-pages-articles.xml";
	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
			XmlHandler sax = new XmlHandler();
			InputStream input = new FileInputStream(xmlPath);
			sax.getPages(input);		
	}
}
