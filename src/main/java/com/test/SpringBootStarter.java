package com.test;

import com.test.constant.Constant;
import com.test.service.RestService;
import com.test.util.ConversionUtil;
import com.test.util.FileUtil;
import com.test.util.TestUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@ComponentScan
public class SpringBootStarter implements ApplicationRunner {

    public static int preInterventionIndex = 0;

    public static int postInterventionIndex = 0;

    public static int lastColumnIndex = 0;

    Map<String, String> headerMap = new LinkedHashMap<>();

    Map<String, String> secondPreHeader = new LinkedHashMap<>();

    Map<String, String> secondPostHeader = new LinkedHashMap<>();

    Map<String, String> thirdHeaderMap = new LinkedHashMap<>();

    Map<String, String> componentHeader = new LinkedHashMap<>();

    Map<String , Integer> componentNameHeader = new LinkedHashMap<>();

    public int outputColNum = 0;

    public int outputRowNum = -1;


     public static Map<String , Map<String , String>> convertUnit ;

    Map<String , String> standardUnit ;

    @Autowired
    RestService restService;


    public static void main(String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {


//        Map<String , Map<String, String>> variableMap = restService.service("https://unitslab.com/");

        //restService.service("https://unitslab.com/");
        //restService.service("https://academic.oup.com/amamanualofstyle/si-conversion-calculator");
        standardUnit = ConversionUtil.convertStandardUnit(System.getProperty("conversionFile"));
        convertUnit = ConversionUtil.convertMolUnitsUnits(System.getProperty("conversionFile"));
        TestUtil.populateParametersMap(convertUnit);
        TestUtil.populateSynonymHeader(secondPreHeader);
        TestUtil.populateSynonymHeader(secondPostHeader);
        Map<String , Map<String, String>> variableMap = new LinkedHashMap<>();
        Map<String , String> unitsMap = new HashedMap();
        Map<String , String> unitsReverseMap = new HashedMap();
        new TestUtil().fetchUnitsMap(unitsMap , unitsReverseMap , Constant.UNIT_FULL_FORM);
        variableMap = new TestUtil().fetchQueryMap(variableMap, Constant.MQL_QUERY_SHEET_1 , "first");
        variableMap.putAll(new TestUtil().fetchQueryMap(variableMap, Constant.MQL_QUERY_SHEET_2 , "second"));



        int tempOutputRowNum = 4;
        File dir = new File(System.getProperty("inputFile"));
        if (dir.exists() && dir.isDirectory()) {
            File files[] = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                FileInputStream file = new FileInputStream(files[i]);
                System.out.println("files absolute path :" + files[i].getAbsolutePath());
                XSSFWorkbook wb=null;
                try {
                    wb = new XSSFWorkbook(file);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    continue;
                }
                Map<String, Map<String, Object>> initialDataMap = fetchDataMap(wb, 0);
                Map<String, Map<String, Object>> preDataMap = fetchDataMap(wb, 1);
                Map<String, Map<String, Object>> postDataMap = fetchDataMap(wb, 2);

                fetchOutputHeaderMap();

                FileInputStream outputFile = FileUtil.fetchFile();
                //FileInputStream outputFile = new FileInputStream(new File(System.getProperty("outputFile")));
                XSSFWorkbook outputWb = new XSSFWorkbook(outputFile);
                fetchComponentHeader(preInterventionIndex);
                outputRowNum = tempOutputRowNum;
                populateData(outputWb, initialDataMap, preInterventionIndex, false , "componentName");
                outputRowNum =tempOutputRowNum;
                outputColNum = preInterventionIndex;
                populateData(outputWb, preDataMap, preInterventionIndex, false,"pre");
                outputRowNum = tempOutputRowNum;
                outputColNum = postInterventionIndex;
                populateData(outputWb, postDataMap, postInterventionIndex, false,"post");
                outputRowNum =tempOutputRowNum;
                outputColNum = preInterventionIndex;
                populateData(outputWb, preDataMap, postInterventionIndex, true,"pre");
                outputRowNum = tempOutputRowNum;
                outputColNum = postInterventionIndex;
                populateData(outputWb, postDataMap, postInterventionIndex, true,"post");
                outputRowNum = tempOutputRowNum;
                outputColNum = 2;
                tempOutputRowNum = tempOutputRowNum+2;
                System.out.println("File completed ");
                file.close();

            }

        }
        System.exit(0);
    }


    public Map<String, Map<String, Object>> fetchDataMap(XSSFWorkbook wb, int sheetNo) {
        XSSFSheet sheet = wb.getSheetAt(sheetNo);
        Map<String, Map<String, Object>> dataMap = new LinkedHashMap<>();

        Map<Integer, String> rowMap = new HashMap<>();
        Map<Integer, String> columnMap = new HashMap<Integer, String>();
        TestUtil testUtil = new TestUtil();
        testUtil.fetchColumnMap(sheet, rowMap);
        testUtil.fetchHeaderMap(sheet, columnMap);
        //System.out.println("Initialization Completed ");

        for (Row row : sheet)     //iteration over row using for each loop
        {
            Map<String, Object> nestedMap = new LinkedHashMap<>();
            for (Cell cell : row)    //iteration over cell using for each loop
            {
                int rowNum = cell.getRowIndex();
                int columnNum = cell.getColumnIndex();
                if ((rowNum == 0 || columnNum == 0)) {
                    continue;
                } else {
                    String columnName = columnMap.get(columnNum);
                    String rowName = rowMap.get(rowNum);
                    DataFormatter fmt = new DataFormatter();
                    if (columnName == null || rowName == null || columnName.isEmpty() || rowName.isEmpty()) {
                        continue;
                    }
                    if (cell.getCellType().equals(CellType.NUMERIC)) {
                        nestedMap.put(columnName, fmt.formatCellValue(cell));
                    } else {
                        nestedMap.put(columnName, cell.getStringCellValue().trim());
                    }
                    dataMap.put(rowName, nestedMap);
                }
            }
        }
        return dataMap;
    }


    public void fetchComponentHeader(int preInterventionIndex) {

        AtomicInteger i = new AtomicInteger(0);
        headerMap.keySet().forEach(r -> {
            if (i.get() < preInterventionIndex) {
                componentHeader.put(r, headerMap.get(r));
                i.incrementAndGet();
            }
        });

    }


    public void populateData(XSSFWorkbook wb, Map<String, Map<String, Object>> dataMap, int index , boolean isPostProcessing, String operationName) throws IOException {
        XSSFSheet sheet = wb.getSheetAt(0);
        if (operationName.equals("componentName")) {
            TestUtil util = new TestUtil();
            if (outputRowNum == -1) {
                outputRowNum = util.fetchEmptyRowNum(outputColNum, sheet);

            }
            for (String s : dataMap.keySet()) {
                outputColNum = util.fetchColNum(0, "Component name", sheet);
                componentNameHeader.put(s.toLowerCase().trim() , outputRowNum);
                //util.populateEmptyData(sheet , outputRowNum , ""  , 1000);
                util.createData(sheet, outputRowNum, outputColNum, s);
                Map<String, Object> nestedDataMap = dataMap.get(s);
                for (String nestedKey : nestedDataMap.keySet()) {
                    int colNum = util.fetchColNum(0, nestedKey, sheet);
                    if (colNum == -1) {
                        //logic to be written
                        continue;
                    }
                    util.createData(sheet, outputRowNum, colNum, String.valueOf(nestedDataMap.get(nestedKey)));
                    outputColNum = colNum;
                }
                outputRowNum++;
            }
        } else if (operationName.equals("pre")) {
            TestUtil util = new TestUtil();
            if (outputRowNum == -1) {
                outputRowNum = util.fetchEmptyRowNum(outputColNum, sheet)-1;
            }
            for (String s : dataMap.keySet()) {
                String category = s.trim();
                if(s.contains("(")) {
                    category = s.substring(0, s.indexOf('(')).trim();
                }

                if (secondPreHeader.containsKey(category.toLowerCase()) && !isPostProcessing) {
                    AtomicInteger value = new AtomicInteger(Integer.parseInt(secondPreHeader.get(category.toLowerCase())));
                    Map<String, Object> dataMapValue = dataMap.get(s);
                    String targetUnit = secondPreHeader.get(value.toString());
                    Double conversionFactor = ConversionUtil.convertUnits(ConversionUtil.fetchSource(s) , targetUnit , category);

                    //System.out.println("PRE Category Hit : " + category);
                    Map<String, String> val = new LinkedHashMap<>();
                    outputRowNum++;
                    dataMapValue.keySet().forEach(r -> {
                        if (!r.toLowerCase().contains("sd")) {
                            String str = dataMapValue.get(r).toString();
                            if(str.contains(",")) {
                                String[] strArr = str.split(",");
                                String result = "";
                                for(int i=0;i< strArr.length;i++) {
                                    if(ConversionUtil.isNumeric(strArr[i])) {
                                        Double dataVal = Double.parseDouble(strArr[i]);
                                        String finalVal = String.valueOf(conversionFactor*dataVal);
                                        if(ConversionUtil.isNumeric(finalVal) && finalVal.contains(".") && finalVal.substring(finalVal.indexOf(".")+1).length()>5) {
                                                       finalVal = finalVal.substring(0,finalVal.indexOf(".")+6);
                                                    }
                                        result+=finalVal+",";
                                    }
                                }
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), value.get(), result.substring(0,result.length()-1));
                            }
                            //System.out.println("Value inserted SD  : " + conversionFactor+"*"+value.get());
                            else if(ConversionUtil.isNumeric(dataMapValue.get(r).toString())) {
                                Double dataVal = Double.parseDouble((dataMapValue.get(r).toString()));
                                String finalVal = String.valueOf(conversionFactor*dataVal);
                                if(ConversionUtil.isNumeric(finalVal) && finalVal.contains(".") && finalVal.substring(finalVal.indexOf(".")+1).length()>5) {
                                    finalVal = finalVal.substring(0,finalVal.indexOf(".")+6);
                                }
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), value.get(), finalVal);
                            }
                            else {
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), value.get(), dataMapValue.get(r).toString());
                            }
                        } else {
                            //System.out.println("Value inserted SD  : " + value.get()+1);
                            String str = dataMapValue.get(r).toString();
                            if(str.contains(",")) {
                                String[] strArr = str.split(",");
                                String result = "";
                                for(int i=0;i< strArr.length;i++) {
                                    if(ConversionUtil.isNumeric(strArr[i])) {
                                        Double dataVal = Double.parseDouble(strArr[i]);
                                        String finalVal = String.valueOf(conversionFactor*dataVal);
                                        if(ConversionUtil.isNumeric(finalVal) && finalVal.contains(".") && finalVal.substring(finalVal.indexOf(".")+1).length()>5) {
                                            finalVal = finalVal.substring(0,finalVal.indexOf(".")+6);
                                        }
                                        result+=finalVal+",";
                                    }
                                }
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), value.get() + 1, result.substring(0,result.length()-1));
                            }
                            else if(ConversionUtil.isNumeric(dataMapValue.get(r).toString())) {
                                Double dataVal = Double.parseDouble(dataMapValue.get(r).toString());
                                String finalVal = String.valueOf(conversionFactor*dataVal);
                                if(ConversionUtil.isNumeric(finalVal) && finalVal.contains(".") && finalVal.substring(finalVal.indexOf(".")+1).length()>5) {
                                    finalVal = finalVal.substring(0,finalVal.indexOf(".")+6);
                                }
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), value.get() + 1, finalVal);
                            }
                            else {
                                util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), value.get() + 1, dataMapValue.get(r).toString());
                            }

                            outputRowNum--;
                        }
                    });
                    outputRowNum++;
                }
                else if( !secondPreHeader.containsKey(category.toLowerCase()) && isPostProcessing) {

                    System.err.println("Pre Category Missed : " + category);
                    for(int i=lastColumnIndex;i<lastColumnIndex+2;i++) {
                        util.createData(sheet , 0 , i , "" );
                    }
                    sheet.shiftColumns(postInterventionIndex , lastColumnIndex , 2);

                    util.createData(sheet, 0, postInterventionIndex, "");
                    util.createData(sheet, 0, postInterventionIndex+1, "");
                    //sheet.addMergedRegion(new CellRangeAddress(0,0,preInterventionIndex , postInterventionIndex+2));
                    util.createData(sheet, 1, postInterventionIndex, s);
                    util.createData(sheet, 1, postInterventionIndex+1, "");
                    sheet.addMergedRegion(new CellRangeAddress(1,1,postInterventionIndex , postInterventionIndex+1));
                    util.createData(sheet, 2, postInterventionIndex, "mean");
                    util.createData(sheet, 2, postInterventionIndex+1, "sd");
                    Map<String, Object> dataMapValue = dataMap.get(s);
                    //System.out.println("Category : " + category);
                    Map<String, String> val = new LinkedHashMap<>();
                    dataMapValue.keySet().forEach(r -> {
                        if (!r.toLowerCase().contains("sd")) {
                            //System.out.println("Value inserted SD  : " + postInterventionIndex);
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), postInterventionIndex, dataMapValue.get(r).toString());

                        } else {
                            //System.out.println("Value inserted SD  : " + postInterventionIndex+1);
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), postInterventionIndex + 1,
                                            dataMapValue.get(r).toString());
                            outputRowNum--;
                        }
                    });
                    postInterventionIndex = postInterventionIndex+2;
                    lastColumnIndex = lastColumnIndex+2;
                }
            }
        } else {
            TestUtil util = new TestUtil();
            if (outputRowNum == -1) {
                outputRowNum = util.fetchEmptyRowNum(outputColNum, sheet)-1;
            }
            for (String s : dataMap.keySet()) {
                String category = s.trim();
                if (s.contains("(")) {
                    category = s.substring(0, s.indexOf('(')).trim();
                }
                if (secondPostHeader.containsKey(category.toLowerCase())  && !isPostProcessing) {
                    AtomicInteger value = new AtomicInteger(Integer.parseInt(secondPostHeader.get(category.toLowerCase())));
                    Map<String, Object> dataMapValue = dataMap.get(s);
                    Map<String, String> val = new LinkedHashMap<>();
                    //System.out.println("POST Category Hit : " + category);
                    Double conversionFactor = ConversionUtil.convertUnits(ConversionUtil.fetchSource(s) , secondPostHeader.get(value.toString()) , category);
                    //System.out.println("value : "+ value.get());
                    outputRowNum++;
                    dataMapValue.keySet().forEach(r -> {
                        if (!r.toLowerCase().contains("sd")) {
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), value.get(), ConversionUtil.fetchConversionValue(dataMapValue , r ,conversionFactor));
                        } else {
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), value.get() + 1, ConversionUtil.fetchConversionValue(dataMapValue , r
                                    ,conversionFactor));
                            outputRowNum--;
                        }
                    });
                    outputRowNum++;
                }
                else if(!secondPostHeader.containsKey(category.toLowerCase())  && isPostProcessing) {
                    System.err.println("POST Category MISS : " + category);
                    for(int i=lastColumnIndex;i<=lastColumnIndex+2;i++) {
                        util.createData(sheet , 0 , i , "" );
                    }
                    util.createData(sheet, 0, lastColumnIndex, "");
                    util.createData(sheet, 0, lastColumnIndex+1, "");
                    //sheet.addMergedRegion(new CellRangeAddress(0,0,preInterventionIndex , postInterventionIndex+2));
                    util.createData(sheet, 1, lastColumnIndex, s);
                    util.createData(sheet, 1, lastColumnIndex+1, "");
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(1,1,lastColumnIndex , lastColumnIndex+1));
                    util.createData(sheet, 2, lastColumnIndex, "mean");
                    util.createData(sheet, 2, lastColumnIndex+1, "sd");
                    Map<String, Object> dataMapValue = dataMap.get(s);
                    Map<String, String> val = new LinkedHashMap<>();
                    outputRowNum++;
                    dataMapValue.keySet().forEach(r -> {
                        if (!r.toLowerCase().contains("sd")) {
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().trim()), lastColumnIndex, dataMapValue.get(r).toString());

                        } else {
                            util.createData(sheet, componentNameHeader.get(r.toLowerCase().substring(0,r.lastIndexOf("-"))), lastColumnIndex + 1, dataMapValue.get(r).toString());
                            outputRowNum--;
                        }
                    });
                    lastColumnIndex = lastColumnIndex+2;
                    //System.out.println("logic to write added columns functionality");

                }
            }
        }

        FileOutputStream out = new FileOutputStream(
                new File(System.getProperty("outputFileGen")));

        try {
            try {
                wb.write(out);
            } catch (Exception ex) {
                wb.write(out);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        out.flush();
        out.close();
    }

    public void fetchOutputHeaderMap() throws IOException {
        Map<Integer, String> outputMap = new LinkedHashMap<>();
        FileInputStream file = FileUtil.fetchFile();
        XSSFWorkbook wb = new XSSFWorkbook(file);
        XSSFSheet sheet = wb.getSheetAt(0);
        headerMap = new TestUtil().fetchOutputFileHeaderMap(sheet, 0, false, "");
        for (int i = 1; i < headerMap.size(); i++) {
            //System.out.println("header size :" + headerMap.get(String.valueOf(i)));
            if (headerMap.get(String.valueOf(i)).equalsIgnoreCase("pre-intervention concentrations / parameters")) {
                preInterventionIndex = i;
            } else if (headerMap.get(String.valueOf(i)).equalsIgnoreCase("Post-intervention ")) {
                postInterventionIndex = i;
            }
        }
        lastColumnIndex = headerMap.size();
        secondPreHeader = new TestUtil().fetchOutputFileHeaderMap(sheet, 1, true, "pre");
        secondPostHeader = new TestUtil().fetchOutputFileHeaderMap(sheet, 1, true, "post");
        thirdHeaderMap = new TestUtil().fetchOutputFileHeaderMap(sheet, 2, false, "");
    }


}
