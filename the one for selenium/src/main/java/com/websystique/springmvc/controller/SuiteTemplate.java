package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.Suite;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserDocument;
import org.apache.commons.io.FilenameUtils;


/**
 * Created by admin on 25/2/2016.
 */

public class SuiteTemplate {

    public static UserDocument createSuite(Suite suite,User user)  {

        UserDocument document = new UserDocument();
        document.setName(suite.getName());
        document.setDescription(suite.getDescription());
        document.setType("application/octet-stream");
        String str = content(suite.getScript());
        document.setContent(str.getBytes());
        document.setUser(user);
        document.setCron("");

        return document;
    }

    private static String content(String name){
        String[] list = name.split(",");
        String[] noExtension = new String[list.length];
        for(int i =0; i <list.length;i++){
            noExtension[i] = FilenameUtils.removeExtension(list[i]);
        }
        //String eol = System.getProperty("line.separator");
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head>\n" +
                "  <meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\" />\n" +
                "  <title>Test Suite</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<table id=\"suiteTable\" cellpadding=\"1\" cellspacing=\"1\" border=\"1\" class=\"selenium\"><tbody>\n" +
                "<tr><td><b>Test Suite</b></td></tr>\n");
        for(int e=0; e<list.length;e++){
            sb.append("<tr><td><a href=\"" + list[e] + "\">"+ noExtension[e] +"</a></td></tr>\n");
        }

        sb.append("</tbody></table>\n" +
                "</body>\n" +
                "</html>\n");

        return sb.toString();
    }


}


