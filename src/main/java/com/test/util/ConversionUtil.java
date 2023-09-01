package com.test.util;
import com.test.SpringBootStarter;
import org.apache.commons.collections.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.jsoup.nodes.Document.OutputSettings.Syntax.html;

public class ConversionUtil {

    //divChildren.get(1).getElementsByTag("tr").get(0).getElementsByClass("js-si-calc").get(0).attr("data-calc")
    //divChildren.get(1).getElementsByTag("tr").get(8).getElementsByTag("td").get(2).children().get(0).attr("id")
    public static Map<String , Map<String , String>> convertMolUnitsUnits(String filePath) {

        Map<String , Map<String , String>> value = new LinkedHashMap<>();
        try {
            Document document = Jsoup.parse(new File(filePath), "ISO-8859-1");
            Elements divChildren = document.getElementsByClass("siTable").get(1).children().get(1).getElementsByTag("tr");


            for (Element elem : divChildren)  {
                Elements tableDataComp = elem.getElementsByTag("td");
                if(tableDataComp.size() >=4) {
                    Map<String , String> innerHm = new LinkedHashMap<>();
                    String compData = tableDataComp.get(0).childNodes().get(0).toString();
                    System.out.println(compData);
                    String unitsStr = tableDataComp.get(2).childNodes().get(1).attr("id");
                    unitsStr = unitsStr.substring(0,unitsStr.length()-4);
                    String left = "";
                    String right = "";
                    if(unitsStr.contains("Âµ")) {
                        unitsStr = unitsStr.replaceAll("Âµ","µ");
                        unitsStr = unitsStr.replaceAll("<span_class=_no-wrap_>","");
                        unitsStr = unitsStr.replaceAll("</span>" ,"");
                        String unitsArr[] = unitsStr.split("_");
                        left = unitsArr[unitsArr.length-2];
                        right = unitsArr[unitsArr.length-1];
                        System.out.println(unitsStr);
                    }
                    else {
                        String unitsArr[] = unitsStr.split("_");
                        left = unitsArr[unitsArr.length-2];
                        right = unitsArr[unitsArr.length-1];
                    }
                    System.out.println(unitsStr);
                    String conversionValue = tableDataComp.get(3).childNodes().get(0).toString();
                    System.out.println(conversionValue);
                    innerHm.put(left.toLowerCase() , conversionValue+"*"+right);
                    value.put(compData.toLowerCase(),innerHm);
                    System.out.println();
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return value;
    }


    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



    public static String fetchSource(String s) {
        String sourceUnit = "";
        if(s.contains("(") && s.contains(")")) {
            sourceUnit = s.substring(s.indexOf("(")+1 , s.indexOf(")"));
        }
        return sourceUnit;
    }



    public static String fetchConversionValue(Map<String, Object> dataMapValue , String r , Double conversionFactor) {

        String str = dataMapValue.get(r).toString();
        if(str.contains(",")) {
            String[] strArr = str.split(",");
            String result = "";
            for(int i=0;i< strArr.length;i++) {
                if(ConversionUtil.isNumeric(strArr[i])) {
                    Double dataVal = Double.parseDouble(strArr[i]);
                    Double finalVal = conversionFactor*dataVal;
                    result+=finalVal+",";
                }
            }
            return result.substring(0,result.length()-1);
        }

        if(ConversionUtil.isNumeric(dataMapValue.get(r).toString())) {
            Double dataVal = Double.parseDouble((dataMapValue.get(r).toString()));
            Double finalVal = conversionFactor*dataVal;
            return finalVal.toString();
        }
        else {
            return dataMapValue.get(r).toString();
        }
    }


    public static boolean isMicroConvertable(String source , String target) {
        if(source.contains("µ") && target.contains("u")) {
            String str1 = source.replaceAll("µ" , "");
            String str2 = target.replaceAll("u" , "");
            if(str1.equals(str2)) {
                return true;
            }
        }
        else if(target.contains("µ") && source.contains("u")) {
            String str1 = target.replaceAll("µ" , "");
            String str2 = source.replaceAll("u" , "");
            if(str1.equals(str2)) {
                return true;
            }
        }
        else if(source.contains("μ") && target.contains("u")) {
            String str1 = source.replaceAll("μ" , "");
            String str2 = target.replaceAll("u" , "");
            if(str1.equals(str2)) {
                return true;
            }
        }
        else if(target.contains("μ") && source.contains("u")) {
            String str1 = target.replaceAll("μ" , "");
            String str2 = source.replaceAll("u" , "");
            if(str1.equals(str2)) {
                return true;
            }
        }
        /// 88.4*µmol/l
        return false;
    }


    public static Double molConverter(String source , String target , Map<String , String> convertedUnit) {
        if(convertedUnit.containsKey(target.toLowerCase())) {
            String str = convertedUnit.get(target.toLowerCase()).toLowerCase();
            if(str.toLowerCase().contains(source.toLowerCase()) || isMicroConvertable(str.toLowerCase().substring(str.indexOf("*")+1),source.toLowerCase())) {
                if(str.contains("*")) {
                    String operator = str.substring(0,str.indexOf("*"));
                    return Double.valueOf(1) /Double.valueOf(operator);
                }
            }
        }
        else if(convertedUnit.containsKey(source.toLowerCase())) {
            String str = convertedUnit.get(source.toLowerCase()).toLowerCase();
            if (str.toLowerCase().contains(target.toLowerCase()) || isMicroConvertable(str.toLowerCase().substring(str.indexOf("*")+1),target.toLowerCase() )) {
                if (str.contains("*")) {
                    String operator = str.substring(0, str.indexOf("*"));
                    return Double.valueOf(operator);
                }
            }
            else {
                System.out.println("test");
            }
        }
        Map.Entry<String, String> firstEntry = convertedUnit.entrySet().iterator().next();
        String str = firstEntry.getValue().toLowerCase();
        String strKey = firstEntry.getKey().toLowerCase();
        String subStr = str.toLowerCase().substring(str.indexOf("*")+1);
        if(str.contains(target.toLowerCase()) || isMicroConvertable(subStr,target.toLowerCase() )) {
            if (str.contains("*")) {
                String operator = str.substring(0, str.indexOf("*"));
                Double cf = fetchStandardConversion(strKey , source);
                return  cf/Double.valueOf(operator);
            }
        }
        else if(str.contains(source.toLowerCase()) || isMicroConvertable(subStr,source.toLowerCase())) {
            if(str.contains("*")) {
                String operator = str.substring(0,str.indexOf("*"));
                Double cf = fetchStandardConversion(strKey , target);
                return Double.valueOf(operator)/cf;
            }
        }

        else {

            System.err.println("Mole conversion not found");
        }
        return Double.valueOf(1) ;


    }
    public static Double convertUnits(String source , String target , String targetHeaderName) {
        if(source == null || target == null || source.toLowerCase().trim().equals(target.toLowerCase().trim()) || source.isEmpty() || target.isEmpty() || isMicroConvertable(source.toLowerCase() , target.toLowerCase())) {
            return Double.valueOf(1);
        }


        if(source.contains("mol") || target.contains("mol")) {
            Map<String , Map<String , String>> convertUnit = SpringBootStarter.convertUnit;
            if(convertUnit.containsKey(targetHeaderName.toLowerCase())) {
                return molConverter(source,target,convertUnit.get(targetHeaderName.toLowerCase()));
            }
            else {
                System.out.println("Not Matching for this header : "+ targetHeaderName);
            }
        }

        else {
            String url = "https://www.convertunits.com/from/"+source+"/to/"+target;
            try {
                Document document = Jsoup.connect(url)
                                         .timeout(500000)
                                         .get();
                String  str = document.getElementsByTag("head").get(0).getElementsByTag("meta").get(4).attr("content");
                String[] strArr = str.split(" ");
                Double element = Double.valueOf(0);
                for(int i=0;i< strArr.length;i++) {
                    if(strArr[i].equals("=")) {
                       element = Double.parseDouble(strArr[i+1]);
                       break;
                    }
                }
                return element;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Double.valueOf(1);

    }


    public static Double fetchStandardConversion(String source , String target) {
        String url = "https://www.convertunits.com/from/"+source+"/to/"+target;
        Double element = null;
        try {
            Document document = Jsoup.connect(url)
                                     .timeout(500000)
                                     .get();
            String  str = document.getElementsByTag("head").get(0).getElementsByTag("meta").get(4).attr("content");
            String[] strArr = str.split(" ");
            element = Double.valueOf(0);
            for(int i=0;i< strArr.length;i++) {
                if(strArr[i].equals("=")) {
                    element = Double.parseDouble(strArr[i+1]);
                    break;
                }
            }

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return element;
    }

    public static Map<String , String> convertStandardUnit(String filePath) {

        try {
            Document document = Jsoup.parse(new File(filePath) , "ISO-8859-1");
            Elements divChildren = document.getElementsByClass("siTable").first().children();
            Map<String , String> value = new HashedMap();
            for (Element elem : divChildren)  {
                Elements tagChildren = elem.getElementsByTag("tr");
                for (Element tagElem : tagChildren) {
                    if(tagElem.getElementsByTag("td") != null && tagElem.getElementsByTag("td").size() !=0) {
                        Elements tagDataElem = tagElem.getElementsByTag("td");
                        if(tagDataElem.size() == 3) {
                            Element leftTagElement = tagDataElem.get(0);
                            Element leftTagElementElementsByTag = leftTagElement.getElementsByTag("label").first();
                            String leftField = leftTagElementElementsByTag.childNodes().get(0).toString();
                            String leftAttr = leftField.substring(leftField.lastIndexOf("(")+1 , leftField.lastIndexOf(")"));

                            String calcString = tagDataElem.get(1).childNodes().toString();
                            calcString = calcString.substring(1,calcString.length()-1);

                            Element rightTagElement = tagDataElem.get(2);
                            Element rightTagElementElementsByTag = rightTagElement.getElementsByTag("label").first();
                            String rightField = rightTagElementElementsByTag.childNodes().get(0).toString();
                            String rightAttr = "";
                            if(!rightField.contains(")")) {
                                rightAttr = rightField.substring(rightField.lastIndexOf("(")+1);
                            }
                            else {
                                rightAttr = rightField.substring(rightField.lastIndexOf("(") + 1, rightField.lastIndexOf(")"));
                            }

                            if(value.get(leftAttr) == null) {
                                value.put(leftAttr , calcString+"*"+rightAttr);
                            }
                            else {
                                String str= value.get(leftAttr);
                                value.put(leftAttr , str+','+calcString+"*"+rightAttr);
                            }
                            //value.put(leftAttr , calcString+"*"+rightAttr);
                        }
                    }
                    else {
                            System.out.println("Not found ");
                    }
                }
            }
            return value;

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static Map<String , String > convertDirectUnit(String str) {
        return null;
    }
}
