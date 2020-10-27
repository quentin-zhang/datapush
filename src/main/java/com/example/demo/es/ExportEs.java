package com.example.demo.es;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.es.pojo.ExportEsRequest;
import com.example.demo.es.pojo.TestData;
import com.example.demo.utils.DateUtil3;
import com.example.demo.utils.EsFUtil;
import com.example.demo.utils.JsonUtilBack;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "导出数据")
@RequestMapping("/esExport")
public class ExportEs {

    private String writeFilePath;
    private String url = "E:/database/昆明2020";
    //private String url = "/home/develop/database";

    @PostMapping("/export")
    @ResponseBody
    public String export(@RequestBody ExportEsRequest exportEsRequest) throws Exception {
        try {

            int form = 100000;

            EsFUtil esl = new EsFUtil();
            Map<String, String> mapl = new HashMap<>();
            mapl.put("keytype", "17");
            mapl.put("startTime", exportEsRequest.getStartTime());
            mapl.put("endTime", exportEsRequest.getEndTime());
            if(StringUtils.isNotBlank(exportEsRequest.getSourceTypes()))
                mapl.put("sourceTypes",exportEsRequest.getSourceTypes());
            if(StringUtils.isNotBlank(exportEsRequest.getCustomerId()))
                mapl.put("customerId",exportEsRequest.getCustomerId());
            if(StringUtils.isNotBlank(exportEsRequest.getAreaCode()))
                mapl.put("areaCode",exportEsRequest.getAreaCode());
            String resultl = esl.exportData(mapl);

            List<String> listl = JsonUtilBack.getList(resultl,"aggregations,count,buckets",",");
            //循环客户
            for (int aa=0 ; aa <listl.size(); aa++) {
                Map mapsl =  JSONObject.parseObject(listl.get(aa));
                String customerIdl = mapsl.get("key").toString();



                EsFUtil esn = new EsFUtil();
                Map<String, String> map = new HashMap<>();
                map.put("keytype", "16");
                map.put("customerId", customerIdl);

                Map<String,String> mapMoth = new HashMap<>();
                map.put("startTime", exportEsRequest.getStartTime());
                map.put("endTime",exportEsRequest.getEndTime());
                if(StringUtils.isNotBlank(exportEsRequest.getSourceTypes()))
                    map.put("sourceTypes",exportEsRequest.getSourceTypes());

                String resultsn =esn.exportData(map);
                List<String> listlen = JsonUtilBack.getList(resultsn,"aggregations,count,buckets",",");
                /*String url = null;
                if(System.getProperties().getProperty("os.name").indexOf("Windows")>0){
                    url = winurl;
                }else{
                    url = linuxurl;
                }*/
                String path = url+"/"+exportEsRequest.getEndTime().substring(0,4)+"/"+exportEsRequest.getStartTime().substring(0,10)+"到"+exportEsRequest.getEndTime().substring(0,10)+"/";
                log.info("path：" + url);
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                this.writeFilePath = path+"newFile-0.txt";
                int num = 0;
                for (String line: listlen) {//循环第一层按sourceType来循环
                    Map maps =  JSONObject.parseObject(line);
                    String areaCode1 = maps.get("key").toString();

                    List<String> listlen1 = JsonUtilBack.getList(line,"count,buckets",",");

                    for (String line1: listlen1) {
                        Map mapss =  JSONObject.parseObject(line1);
                        String sourceId = mapss.get("key").toString();
                        EsFUtil es = new EsFUtil();
                        map.put("areaCode",areaCode1);
                        map.put("sourceId",sourceId);
                        map.put("keytype", "count");
                        map.remove("size");
                        log.info("sssss:"+sourceId);
                        String result = es.homeData(map);
                        int count = JsonUtilBack.getInteger(result, "count");
                        log.info("总数量：" + count);
                        if (count > 0) {


                            // for (int k = 0; k < dd; k++) {
                            File writeFiles = new File(this.writeFilePath);
                            long lineFile = getLineNumber(writeFiles);
                            if (lineFile >500000L ) {
                                num++;
                                this.writeFilePath = path +"newFile"+num+ ".txt";
                                File writeFile = new File(this.writeFilePath);
                                if (!writeFile.exists()) {
                                    try {
                                        writeFile.createNewFile();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }

                            }
                            EsFUtil es1 = new EsFUtil();
                            String listString = null;

                            map.put("size", count + "");

                            map.put("keytype", "search");
                            listString = es1.homeData(map);



                            if (listString != null) {

                                JSONObject json = JSONObject.parseObject(listString);
                                JSONObject json2 = json.getJSONObject("hits");
                                JSONArray json3 = json2.getJSONArray("hits");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                log.info("count::" + json3.size());
                                for (int i = 0; i < json3.size(); i++) {
                                    JSONObject jsonObject = (JSONObject) json3.get(i);
                                    JSONObject obj = jsonObject.getJSONObject("_source");
                                    Map<String, Object> ad = new HashMap<>();
                                    if (obj.containsKey("AD_ADDRESS")) {
                                        ad.put("AD_ADDRESS", obj.get("AD_ADDRESS").toString());
                                    } else {
                                        ad.put("AD_ADDRESS", "");
                                    }

                                    if (obj.containsKey("AD_BEGIN_SNAPSHOT")) {
                                        ad.put("AD_BEGIN_SNAPSHOT", obj.get("AD_BEGIN_SNAPSHOT").toString());
                                    } else {
                                        ad.put("AD_BEGIN_SNAPSHOT", "");
                                    }
                                    if (obj.containsKey("AD_BEGIN_URL")) {
                                        ad.put("AD_BEGIN_URL", obj.get("AD_BEGIN_URL").toString());
                                    } else {
                                        ad.put("AD_BEGIN_URL", "");
                                    }
                                    if (obj.containsKey("AD_GROUND_SNAPSHOT")) {
                                        ad.put("AD_GROUND_SNAPSHOT", obj.get("AD_GROUND_SNAPSHOT").toString());
                                    } else {
                                        ad.put("AD_GROUND_SNAPSHOT", "");
                                    }
                                    if (obj.containsKey("BEGIN_TIME")) {
                                        ad.put("BEGIN_TIME", obj.get("BEGIN_TIME").toString());
                                    } else {
                                        ad.put("BEGIN_TIME", "00:00:00");
                                    }
                                    if (obj.containsKey(("AD_GROUND_URL"))) {
                                        ad.put("AD_GROUND_URL", obj.get("AD_GROUND_URL").toString());
                                    }
                                    if (obj.containsKey("AD_ID")) {
                                        ad.put("AD_ID", obj.get("AD_ID").toString());
                                    }
                                    if (obj.containsKey("AD_NAME")) {
                                        ad.put("AD_NAME", obj.get("AD_NAME").toString());
                                    }
                                    if (obj.containsKey("AD_TYPE_ID")) {
                                        ad.put("AD_TYPE_ID", obj.get("AD_TYPE_ID").toString());
                                    }
                                    if (obj.containsKey("AD_TYPE_NAME")) {
                                        ad.put("AD_TYPE_NAME", obj.get("AD_TYPE_NAME").toString());
                                    }
                                    if (obj.containsKey("AREA_CODE")) {
                                        ad.put("AREA_CODE", obj.get("AREA_CODE").toString());
                                    }
                                    if (obj.containsKey("CITY_NAME")) {
                                        ad.put("CITY_NAME", obj.get("CITY_NAME").toString());
                                    }

                                    if (obj.containsKey("COUNTY_NAME")) {
                                        ad.put("COUNTY_NAME", obj.get("COUNTY_NAME").toString());
                                    }
                                    if (obj.containsKey("CUSTOMER_ID")) {
                                        ad.put("CUSTOMER_ID", obj.get("CUSTOMER_ID").toString());
                                    }
                                    //ad.put("CUSTOMER_ID", "120");
                                    if (obj.containsKey("END_TIME")) {
                                        ad.put("END_TIME", obj.get("END_TIME").toString());
                                    }
                                    if (obj.containsKey("FTP_PATH")) {
                                        ad.put("FTP_PATH", obj.get("FTP_PATH").toString());
                                    }

                                    if (obj.containsKey("ILLEGAL_CODE") && obj.get("ILLEGAL_CODE") != null) {
                                        if (obj.get("ILLEGAL_CODE").toString().startsWith("[")) {
                                            ad.put("ILLEGAL_CODE", obj.get("ILLEGAL_CODE").toString().substring(1, obj.get("ILLEGAL_CODE").toString().length() - 1));
                                            //} else {
                                            // String[] dds = obj.get("ILLEGAL_CODE").toString().split("、");
                                            //ad.put("ILLEGAL_CODE", Arrays.toString(dds));
                                        } else {
                                            ad.put("ILLEGAL_CODE", obj.get("ILLEGAL_CODE").toString());
                                        }
                                    }

                                    if (obj.containsKey("ILLEGAL_CONTENT")) {
                                        ad.put("ILLEGAL_CONTENT", obj.get("ILLEGAL_CONTENT").toString());
                                    }

                                    if (obj.containsKey("INSERT_TIME")) {
                                        ad.put("INSERT_TIME", obj.get("INSERT_TIME").toString());
                                    }
                                    if (obj.containsKey("IS_DEL") && obj.get("IS_DEL") != null) {
                                        ad.put("IS_DEL", obj.get("IS_DEL").toString());
                                    }
                                    if (obj.containsKey("IS_DISPLAY_ON_PLAT")) {
                                        ad.put("IS_DISPLAY_ON_PLAT", obj.get("IS_DISPLAY_ON_PLAT").toString());
                                    }
                                    if (obj.containsKey("IS_LEGAL")) {
                                        ad.put("IS_LEGAL", obj.get("IS_LEGAL").toString());
                                    } else {
                                        ad.put("IS_LEGAL", "1");
                                    }
                                    if (obj.containsKey("LATITUDE")) {
                                        ad.put("LATITUDE", obj.get("LATITUDE").toString());
                                    }
                                    if (obj.containsKey("LOCATION")) {
                                        ad.put("LOCATION", (Map<String, String>) obj.get("LOCATION"));
                                    }
                                    if (obj.containsKey("LONGITUDE")) {
                                        ad.put("LONGITUDE", obj.get("LONGITUDE").toString());
                                    }

                                    if (obj.containsKey("MEDIA_ID") && obj.get("MEDIA_ID") != null) {
                                        ad.put("MEDIA_ID", obj.get("MEDIA_ID").toString());
                                    }
                                    if (obj.containsKey("MEDIA_NAME") && obj.get("MEDIA_NAME") != null) {
                                        ad.put("MEDIA_NAME", obj.get("MEDIA_NAME").toString());
                                    }
                                    if (obj.containsKey("OLD_ID") && obj.get("OLD_ID") != null) {
                                        ad.put("OLD_ID", obj.get("OLD_ID").toString());
                                    }
                                    if (obj.containsKey("PLAY_DATE")) {
                                        ad.put("PLAY_DATE", obj.get("PLAY_DATE").toString());
                                    } else {
                                        ad.put("PLAY_DATE", obj.get("PLAY_DATE").toString());
                                    }
                                    if (obj.containsKey("PLAY_LEAN") && obj.get("PLAY_LEAN") != null) {
                                        ad.put("PLAY_LEAN", obj.get("PLAY_LEAN").toString());
                                    } else {
                                        ad.put("PLAY_LEAN", "0");
                                    }
                                    if (obj.containsKey("PROOF_PATH") && obj.get("PROOF_PATH") != null) {
                                        ad.put("PROOF_PATH", obj.get("PROOF_PATH").toString());
                                    } else {
                                        ad.put("PROOF_PATH", "");
                                    }
                                    if (obj.containsKey("PROVINCE_NAME") && obj.get("PROVINCE_NAME") != null) {
                                        ad.put("PROVINCE_NAME", obj.get("PROVINCE_NAME").toString());
                                    } else {
                                        ad.put("PROVINCE_NAME", "");
                                    }
                                    if (obj.containsKey("QUERY_AREA_LEVEL") && obj.get("QUERY_AREA_LEVEL") != null) {
                                        ad.put("QUERY_AREA_LEVEL", obj.get("QUERY_AREA_LEVEL").toString());
                                    } else {
                                        ad.put("QUERY_AREA_LEVEL", "");
                                    }
                                    if (obj.containsKey("SITE_TYPE") && obj.get("SITE_TYPE") != null) {
                                        ad.put("SITE_TYPE", obj.get("SITE_TYPE").toString());
                                    } else {
                                        ad.put("SITE_TYPE", "");
                                    }
                                    if (obj.containsKey("IS_DELETE") && obj.get("IS_DELETE") != null) {
                                        ad.put("IS_DELETE", obj.get("IS_DELETE").toString());
                                    } else {
                                        ad.put("IS_DELETE", "0");
                                    }
                                    if (obj.containsKey("SOURCE_ID")) {
                                        ad.put("SOURCE_ID", obj.get("SOURCE_ID").toString());
                                    }
                                    if (obj.containsKey("SOURCE_NAME")) {
                                        ad.put("SOURCE_NAME", obj.get("SOURCE_NAME").toString());
                                    }
                                    if (obj.containsKey("SOURCE_TYPE")) {
                                        ad.put("SOURCE_TYPE", obj.get("SOURCE_TYPE").toString());
                                    }
                                    if (obj.containsKey("SOURCE_TYPE_NAME")) {
                                        ad.put("SOURCE_TYPE_NAME", obj.get("SOURCE_TYPE_NAME").toString());
                                    }
                                    if (obj.containsKey("STATE")) {
                                        ad.put("STATE", obj.get("STATE").toString());
                                    } else {
                                        ad.put("STATE", "0");
                                    }
                                    if (obj.containsKey("SON_TYPE_ID")) {
                                        ad.put("SON_TYPE_ID", obj.get("SON_TYPE_ID").toString());
                                    } else {
                                        ad.put("SON_TYPE_ID", "");
                                    }
                                    if (obj.containsKey("SON_TYPE_NAME")) {
                                        ad.put("SON_TYPE_NAME", obj.get("SON_TYPE_NAME").toString());
                                    } else {
                                        ad.put("SON_TYPE_NAME", "");
                                    }
                                    if (obj.containsKey("DATAPUSH_ID")) {
                                        ad.put("DATAPUSH_ID", obj.get("DATAPUSH_ID").toString());
                                    } else {
                                        ad.put("DATAPUSH_ID", "");
                                    }
                                    if (obj.containsKey("ILLEGAL_LV")) {
                                        ad.put("ILLEGAL_LV", obj.get("ILLEGAL_LV").toString());
                                    } else {
                                        ad.put("ILLEGAL_LV", "");
                                    }
                                    if (obj.containsKey("BRAND")) {
                                        ad.put("BRAND", obj.get("BRAND").toString());
                                    } else {
                                        ad.put("BRAND", "");
                                    }
                                    if (obj.containsKey("REMARKS1")) {
                                        ad.put("REMARKS1", obj.get("REMARKS1").toString());
                                    } else {
                                        ad.put("REMARKS1", "");
                                    }
                                    if (obj.containsKey("REMARKS2")) {
                                        ad.put("REMARKS2", obj.get("REMARKS2").toString());
                                    } else {
                                        ad.put("REMARKS2", "");
                                    }
                                    if (obj.containsKey("REMARKS3")) {
                                        ad.put("REMARKS3", obj.get("REMARKS3").toString());
                                    } else {
                                        ad.put("REMARKS3", "");
                                    }
                                    if (obj.containsKey("REMARKS4")) {
                                        ad.put("REMARKS4", obj.get("REMARKS4").toString());
                                    } else {
                                        ad.put("REMARKS4", "");
                                    }
                                    if (obj.containsKey("REMARKS5")) {
                                        ad.put("REMARKS5", obj.get("REMARKS5").toString());
                                    } else {
                                        ad.put("REMARKS5", "");
                                    }
                                    if (obj.containsKey("REMARKS6")) {
                                        ad.put("REMARKS6", obj.get("REMARKS6").toString());
                                    } else {
                                        ad.put("REMARKS6", "");
                                    }
                                    if (obj.containsKey("REMARKS7")) {
                                        ad.put("REMARKS7", obj.get("REMARKS7").toString());
                                    } else {
                                        ad.put("REMARKS7", "");
                                    }
                                    if (obj.containsKey("REMARKS8")) {
                                        ad.put("REMARKS8", obj.get("REMARKS8").toString());
                                    } else {
                                        ad.put("REMARKS8", "");
                                    }
                                    if (obj.containsKey("REMARKS9")) {
                                        ad.put("REMARKS9", obj.get("REMARKS9").toString());
                                    } else {
                                        ad.put("REMARKS9", "");
                                    }
                                    if (obj.containsKey("REMARKS10")) {
                                        ad.put("REMARKS10", obj.get("REMARKS10").toString());
                                    } else {
                                        ad.put("REMARKS10", "");
                                    }

                                    FileOutputStream fos = null;
                                    PrintStream ps = null;
                                    try {
                                        File file1 = new File(this.writeFilePath);
                                        fos = new FileOutputStream(file1, true);// 文件输出流	追加
                                        ps = new PrintStream(fos);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    String string = JSONObject.toJSONString(ad) + "\r\n";// +换行
                                    ps.print(string); // 执行写操作
                                    ps.close();    // 关闭流
                                }
                            }


                        }
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return  "成功";


    }

    public long getLineNumber (File file){
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                LineNumberReader lineNumberReader = new LineNumberReader(fileReader);
                lineNumberReader.skip(Long.MAX_VALUE);
                long lines = lineNumberReader.getLineNumber() + 1;
                fileReader.close();
                lineNumberReader.close();
                return lines;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static void main(String[] arg){
        System.out.println("===========操作系统是:"+System.getProperties().getProperty("os.name"));
    }
}