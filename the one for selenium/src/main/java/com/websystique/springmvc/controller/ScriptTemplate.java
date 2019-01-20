package com.websystique.springmvc.controller;

import com.websystique.springmvc.model.GenerateScript;
import com.websystique.springmvc.model.User;
import com.websystique.springmvc.model.UserDocument;


/**
 * Created by admin on 25/2/2016.
 */

public class ScriptTemplate {

    public static UserDocument createScript(GenerateScript tempScript, User user)  {

        UserDocument document = new UserDocument();
        document.setName(tempScript.getName()+".html");
        document.setDescription(tempScript.getDescription());
        document.setType("text/html");
        String str = content(tempScript);
        document.setContent(str.getBytes());
        document.setUser(user);
        document.setCron("");

        return document;
    }

    private static String content(GenerateScript tempScript){
        String name = tempScript.getName();
        String ip = tempScript.getIp();
        String username = tempScript.getUsername();
        String password = tempScript.getPassword();
        String sequences = tempScript.getSequence();
        String[] list = sequences.split(",");


        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\n" +
                "<head profile=\"http://selenium-ide.openqa.org/profiles/test-case\">\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");

        sb.append("<link rel=\"selenium.base\" href=\"https://"+ip+":8443/\" />");

        sb.append("<title>"+name+"</title>");

        sb.append("</head>\n" +
                "<body>\n" +
                "<table cellpadding=\"1\" cellspacing=\"1\" border=\"1\">\n" +
                "<thead>");

        sb.append("<tr><td rowspan=\"1\" colspan=\"3\">"+name+"</td></tr>\n" +
                "</thead><tbody>\n");

        sb.append("<tr>\n" +
                "\t<td>open</td>\n" +
                "\t<td>/insight/auth/login</td>\n" +
                "\t<td></td>\n" +
                "</tr>\n");

        sb.append("<tr>\n" +
                "\t<td>type</td>\n" +
                "\t<td>name=username</td>\n" +
                "\t<td>"+username+"</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "\t<td>type</td>\n" +
                "\t<td>name=password</td>\n" +
                "\t<td>"+password+"</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "\t<td>click</td>\n" +
                "\t<td>//button[@type='submit']</td>\n" +
                "\t<td></td>\n" +
                "</tr>");

        for(int e=0; e<list.length;e++){
            if(list[e].equals("View incidents"))
            {
                sb.append("\n<tr>\n" +
                        "\t<td>clickAndWait</td>\n" +
                        "\t<td>//div[@id='globalMenu']/ul/li[2]/a/span</td>\n" +
                        "\t<td></td>\n" +
                        "</tr>");
            }
            else if(list[e].equals("View rules")){
                sb.append("\n<tr>\n" +
                        "\t<td>clickAndWait</td>\n" +
                        "\t<td>//div[@id='globalMenu']/ul/li[3]/a/span</td>\n" +
                        "\t<td></td>\n" +
                        "</tr>");
            }
            else if(list[e].equals("View hash")){
                sb.append("\n<tr>\n" +
                        "\t<td>clickAndWait</td>\n" +
                        "\t<td>link=Hash</td>\n" +
                        "\t<td></td>\n" +
                        "</tr>");
            }

        }

        sb.append("\n<tr>\n" +
                "\t<td>click</td>\n" +
                "\t<td>css=strong</td>\n" +
                "\t<td></td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "\t<td>clickAndWait</td>\n" +
                "\t<td>link=Logout</td>\n" +
                "\t<td></td>\n" +
                "</tr>\n" +
                "\n" +
                "</tbody></table>\n" +
                "</body>\n" +
                "</html>\n");


        return sb.toString();
    }


}


