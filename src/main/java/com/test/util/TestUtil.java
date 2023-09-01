package com.test.util;

import com.test.SpringBootStarter;
import com.test.constant.Constant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class TestUtil {


    public void fetchColumnMap(XSSFSheet sheet,Map<Integer , String> columnMap) {
        for (Row row: sheet) {
            Cell cell = row.getCell(0);
            if ( cell == null || cell.getRowIndex() ==  0) {
                continue;
            }
            columnMap.put(cell.getRowIndex(), cell.getStringCellValue());
        }
    }

    public void fetchHeaderMap(XSSFSheet sheet,Map<Integer , String> headerMap) {
        for(Row row: sheet)     //iteration over row using for each loop
        {
            for (Cell cell : row)    //iteration over cell using for each loop
            {
                if(cell.getRowIndex() == 0) {
                    if(cell.getColumnIndex() == 0) {
                        continue;
                    }
                    else if (!cell.getStringCellValue().toLowerCase().contains("sd")) {
                        headerMap.put(cell.getColumnIndex(),cell.getStringCellValue().trim());
                    }
                    else {
                        headerMap.put(cell.getColumnIndex(),headerMap.get(cell.getColumnIndex()-1).trim()+"-sd");
                    }
                }

            }

        }


    }


    public Map<String , String> fetchOutputFileHeaderMap(XSSFSheet sheet , int rowNum , boolean operation , String operationName) {
        Map<String , String> headerMap= new LinkedHashMap<>();
        Row row = sheet.getRow(rowNum);  //iteration over row using for each loop
        {
            for (Cell cell : row)    //iteration over cell using for each loop
            {
                String cellVal = cell.getStringCellValue();
                if((cell.getColumnIndex() < SpringBootStarter.postInterventionIndex && operationName.equals("pre")) || (cell.getColumnIndex() >= SpringBootStarter.postInterventionIndex && operationName.equals("post")) || operationName.equals("")) {
                    if (operation) {
                        if (cellVal.isEmpty() || cell.equals("")) {
                            headerMap.put(cellVal.toLowerCase(), String.valueOf(cell.getColumnIndex()));
                        } else if (!cellVal.contains("(")) {
                            headerMap.put(cellVal.trim().toLowerCase(), String.valueOf(cell.getColumnIndex()));
                        } else {
                            try {
                                String categoryStr = cellVal.trim().substring(0, cellVal.indexOf('(')-1).trim();
                                String unitStr = cellVal.trim().substring(cellVal.trim().indexOf('(') + 1, cellVal.trim().length() - 1);
                                headerMap.put(categoryStr.trim().toLowerCase(), String.valueOf(cell.getColumnIndex()));
                                headerMap.put(String.valueOf(cell.getColumnIndex()).toLowerCase(), unitStr);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        headerMap.put(String.valueOf(cell.getColumnIndex()).toLowerCase(), cellVal);
                    }
                }
            }

        }
        return headerMap;
    }


    public void fetchSecondHeaderMap(XSSFSheet sheet,Map<Integer, String> headerMap , int start , int end , int rowNum ) {

        for (Cell cell : sheet.getRow(rowNum))    //iteration over cell using for each loop
            {
                if(cell.getColumnIndex() >=start && cell.getColumnIndex() <= end) {
                    headerMap.put(cell.getColumnIndex(),cell.getStringCellValue());
                }

            }


    }



    public int fetchColNum(int rowNum , String columnName , XSSFSheet sheet) {
        for (Cell cell : sheet.getRow(rowNum)) {
            if(cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                return cell.getColumnIndex();
            }
        }
        return -1;
    }






    public int fetchEmptyRowNum(int colNum , XSSFSheet sheet) {
        for(Row row: sheet) {
            for (Cell cell : row) {
                if(cell.getColumnIndex() == colNum) {
                    if(cell.getStringCellValue().equals("") || cell.getStringCellValue().isEmpty()) {
                        return cell.getRowIndex() + 3;
                    }
                    break;
                }
            }
        }
        return -1;
    }




    public void populateEmptyData(XSSFSheet sheet , int rowNum , String data , int num) {
        for (int i=0;i<num;i++) {
            Cell cell = sheet.getRow(rowNum).getCell(i);
            if(cell == null) {
                sheet.getRow(rowNum).createCell(i);
            }
        }

    }

    public void createData(XSSFSheet sheet , int rowNum , int colNum , String data ) {
//        for(Cell cell:sheet.getRow(rowNum)) {
//            System.out.println("column index :"+ cell.getColumnIndex());
//            if(cell.getColumnIndex() == colNum) {
//                Cell cellVal = sheet.getRow(rowNum).createCell(colNum);
//                cellVal.setCellValue(data);
//            }
//        }
//        if(ConversionUtil.isNumeric(data) && data.contains(".") && data.substring(data.indexOf(".")+1).length()>4) {
//            data = data.substring(0,data.indexOf(".")+5);
//        }

        Cell cell = sheet.getRow(rowNum).createCell(colNum);
        cell.setCellValue(data);
    }


    public Map<String , String> fetchUnitsMap(Map<String ,String> unitsMap , Map<String ,String> unitsReverseMap,String query) {
        String str[] = query.split("\n");
        for(int i=1;i<str.length;i++) {
            String nestedStr[] = str[i].split("\t");
            unitsMap.put(nestedStr[0], nestedStr[1]);
            unitsReverseMap.put(nestedStr[1],nestedStr[0] );
        }
        return unitsMap;
    }


    public static void populateSynonymHeader(Map<String , String > hm) {
        List<List<String>> parameters = new ArrayList<>();
        String query = Constant.PARAMETERS_FULL_FORM;
        String queryArr[] = query.split("\n");
        for(int i=1;i<queryArr.length;i++) {
            String internalArr[] = queryArr[i].split("\t");
            boolean flag = false;
            String elementMap = "";
            for(int j=0;j<internalArr.length;j++) {
                if(hm.containsKey(internalArr[j].toLowerCase().trim())) {
                    flag = true;
                    elementMap = hm.get(internalArr[j].toLowerCase().trim());
                }
            }
            if(flag) {
                for(int j=0;j<internalArr.length;j++) {
                    hm.put(internalArr[j].toLowerCase().trim() , elementMap);
                }
            }
        }
    }


    public static List<List<String>> populateParametersMap(Map<String , Map<String , String>> hm) {
        List<List<String>> parameters = new ArrayList<>();
        String query = Constant.PARAMETERS_FULL_FORM;
        String queryArr[] = query.split("\n");

        for(int i=1;i<queryArr.length;i++) {
            String internalArr[] = queryArr[i].split("\t");
            boolean flag = false;
            Map<String,String> elementMap = new HashedMap();
            for(int j=0;j<internalArr.length;j++) {
                if(hm.containsKey(internalArr[j].toLowerCase().trim())) {
                    flag = true;
                    elementMap = hm.get(internalArr[j].toLowerCase().trim());
                }
            }
            if(flag) {
                for(int j=0;j<internalArr.length;j++) {
                    hm.put(internalArr[j].toLowerCase().trim() , elementMap);
                }
            }
        }

       return parameters;
    }


    public Map<String , Map<String, String>> fetchQueryMap(Map<String , Map<String, String>> variableMap , String query , String options  ) {
        String str[] = query.split("\n");
        for(int i=1;i<str.length;i++) {
            String nestedStr[] = str[i].split("\t");
            if(nestedStr.length >=3 && !nestedStr[0].isEmpty()) {
                List<String> nestedList = new ArrayList<>();
                if(nestedStr[0].contains(",")) {
                    String str1[] = nestedStr[0].split(",");
                    for(int k=0;k<str1.length;k++) {
                        if(str1[k].contains("(") && str1[k].contains(")")) {
                            String answer = str1[k].substring(str1[k].indexOf("(")+1, str1[k].indexOf(")"));
                            nestedList.add(answer.trim());
                            nestedList.add(str1[k].substring(0,str1[k].indexOf("(")).trim());
                        }
                        else {
                            nestedList.add(str1[k].trim());
                        }
                    }
                }
                else {
                    if(nestedStr[0].contains("(") && nestedStr[0].contains(")")) {
                        String answer = nestedStr[0].substring(nestedStr[0].indexOf("(")+1, nestedStr[0].indexOf(")"));
                        nestedList.add(answer);
                        nestedList.add(nestedStr[0].substring(0,nestedStr[0].indexOf("(")).trim());
                    }
                    else {
                        nestedList.add(nestedStr[0].trim());
                    }

                }
                Map<String, String> nestedMap = new LinkedHashMap<>();
                String leftStr = "";
                String rightStr = "";

                if(options.equals("second") && !nestedStr[1].isEmpty() && !nestedStr[2].isEmpty() && !nestedStr[2].equals("NA") && !nestedStr[2].equals("-")) {
                    String firstUnit = nestedStr[1];
                    String secondUnit = nestedStr[2];
                    String thirdUnit[] = nestedStr[2].split("=");
                    leftStr = thirdUnit[0].trim().split(" ")[1];
                    rightStr = thirdUnit[1].trim().split(" ")[0] + "*" + thirdUnit[1].trim().split(" ")[1];
                    nestedMap.put(leftStr.toLowerCase() , rightStr);
                }

                else if(!nestedStr[1].isEmpty() && !nestedStr[2].isEmpty() && !nestedStr[2].equals("NA") && !nestedStr[3].isEmpty() && !nestedStr[3].equals("NA")) {


                    leftStr = nestedStr[1];
                    String firstUnit = nestedStr[1];
                    String secondUnit = nestedStr[2];
                    String lastNum[] = nestedStr[3].replace(",", "").split(" ");
                    BigDecimal conversionNum = new BigDecimal(lastNum[lastNum.length - 1]);
                    BigDecimal resultNum = null;
                    if (nestedStr[3].toLowerCase().contains("divide")) {
                        resultNum = new BigDecimal(1).divide(conversionNum, new MathContext(4));
                    } else if (nestedStr[3].toLowerCase().contains("multiply")) {
                        resultNum = new BigDecimal(1).multiply(conversionNum);
                    } else {
                        System.out.println("Error coming ");
                    }
                    nestedMap.put(nestedStr[1].toLowerCase(), resultNum.floatValue() + "*" + nestedStr[2].toLowerCase());
                }


                    for(String nest : nestedList) {
                        String key = nest.replace("\"", "");
                        if(leftStr.toLowerCase().contains("mol") || nestedStr[2].toLowerCase().contains("mol")) {
                            if(variableMap.containsKey(key.toUpperCase())) {
                                Map<String , String> data = variableMap.get(key.toUpperCase());
                                data.putAll(nestedMap);
                            }
                            else {
                                variableMap.put(key.toUpperCase(), nestedMap);
                            }
                        }
                        else {
                            if(variableMap.get("STANDARD") == null) {
                                variableMap.put("STANDARD" , nestedMap);
                            }
                            else {
                                Map<String,String> test = variableMap.get("STANDARD");
                                if(test.get(leftStr.toLowerCase()) == null) {
                                    test.putAll(nestedMap);
                                }
                                else {
                                    if(options.equals("first")) {
                                        nestedMap.put(nestedStr[1].toLowerCase(),test.get(nestedStr[1].toLowerCase()) + ","+ fetchMultiplier(nestedStr , options));
                                    }
                                    else {
                                        nestedMap.put(leftStr.toLowerCase() ,test.get(leftStr.toLowerCase()) + ","+ rightStr );
                                    }

                                    test.putAll(nestedMap);
                                }
                            }
                        }
                    }
                }
            }

        return variableMap;
    }



    public static String fetchMultiplier(String nestedStr[] , String options) {
        String firstUnit = nestedStr[1];
        String secondUnit = nestedStr[2];
        String lastNum[] = nestedStr[3].replace(",","").split(" ");
        BigDecimal conversionNum = new BigDecimal(lastNum[lastNum.length-1]);
        BigDecimal resultNum = null;
        if(nestedStr[3].toLowerCase().contains("divide")) {
            resultNum = new BigDecimal(1).divide(conversionNum , new MathContext(4));
        }
        else if(nestedStr[3].toLowerCase().contains("multiply")) {
            resultNum = new BigDecimal(1).multiply(conversionNum);
        }
        else {
            System.out.println("Error coming ");
        }
        return resultNum.floatValue()+"*"+nestedStr[2].toLowerCase();
    }





}
