package com.sjtu;

import com.sjtu.parser.XmlHandler;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * If the code works, it was written by qqiangwu at 1:02 PM 10/9/15, otherwise I
 * don't know who wrote it.
 */
public class MainTest {
    private static String xmlPath = "E://data//enwiki-20150901-pages-articles.xml";

    @Test
    public void testFile() throws Exception {
        XmlHandler sax = new XmlHandler();
        InputStream input = new FileInputStream(xmlPath);
        sax.getPages(input);
    }
}
