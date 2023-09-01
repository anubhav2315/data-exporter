package com.test.service;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.*;

@Service
public class RestService {



    public Map<String , Map<String , String>> service(String url) {
        Map<String , Map<String , String>> variableMap = new LinkedHashMap<String , Map<String, String>>();



        //        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        HttpEntity <String> entity = new HttpEntity<String>(headers);
//        ResponseEntity<String> res=  restTemplate.exchange(url , HttpMethod.GET , entity, String.class);
        try {
            Document document = Jsoup.connect(url)
                                     .timeout(5000)
                                     .get();
            Elements divChildren = document.getElementsByClass("blockanaliz").select("ul").first().children();
            Map<String, String> urlMap = new LinkedHashMap<String , String>();
            for (Element elem : divChildren) {
                String urlPath =  elem.getElementsByTag("span").first().children().first().getElementsByTag("a").attr("href");
                String value = String.valueOf(elem.getElementsByTag("span").first().children().first().getElementsByTag("a").first().childNodes().get(0));
                urlMap.put(urlPath , value);
                variableMap.put(value , nestedService(url,urlPath,value));
                System.out.println();
            }
            System.out.println(document.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return variableMap;

    }


    public Map<String,String> nestedService(String url ,String urlPath, String value) {
        String completeUrl = url+urlPath;
        Map<String,String> nestedVariableMap = new LinkedHashMap<String , String>();
        try {
            Document document = call(completeUrl);
            String functionParams =
                    String.valueOf(document.getElementsByClass("field field--name-field-script field--type-text-long field--label-hidden field--item").first().children().first().getElementsByTag("script").first().childNodes().get(0));
            String variables[] = functionParams.split("\n");

            List<String> variableList = new ArrayList<>();
            List<String> valList = new ArrayList<>();
            for(int i=0;i< variables.length ;i++) {
                String val = variables[i].trim();
                if(val.contains("var label") || val.contains("var\tlabel")) {
                    if(val.contains("")) {
                        String strVal = val.substring(val.indexOf('"') + 1, val.lastIndexOf('"')).trim();
                        variableList.add(strVal);
                    }
                }
                if(val.contains("this.mass")) {
                    if((val.contains("=") && val.contains("//")) && (val.indexOf("=") <= val.lastIndexOf("//"))) {
                        String strVal = val.substring(val.indexOf('=') + 1, val.lastIndexOf("//")).trim();
                        valList.add(strVal);
                    }

                }
            }

            if(valList.size() != variableList.size()) {
                System.out.println("not handled");
            }
            for(int i=0;i< variableList.size();i++) {
                if((!variableList.get(i).isEmpty() && !valList.get(i).isEmpty()) && (valList.get(i).charAt(0) != '/')) {
                    nestedVariableMap.put(variableList.get(i), valList.get(i));
                }
            }




            System.out.println();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return nestedVariableMap;

    }


    @Retryable(maxAttempts = 3)
    public Document call(String completeUrl) {

        Document document = null;
        try {
            document = Jsoup.connect(completeUrl)
                                     .timeout(5000)
                                     .get();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return document;
    }
}
