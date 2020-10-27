package com.example.demo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class EsUtil {
    private RestClient restClient = RestClient.builder(new HttpHost("172.16.0.22", 10200, "http"),
            new HttpHost("172.16.0.18", 10200, "http"), new HttpHost("172.16.0.14", 10200, "http")).setDefaultHeaders(new Header[] {new BasicHeader("Content-Type", "application/json")}).build();

    public void test() {
    Request request = new Request("GET", "/advertdata/_count");

    try {
      NStringEntity nStringEntity = new NStringEntity("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"CUSTOMER_ID\":{\"value\":\"111\"}}}]}}}");
      request.setEntity(nStringEntity);
      Response response = restClient.performRequest(request);
      String string = EntityUtils.toString(response.getEntity());
      System.out.println(string);
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }

  }


  /**
   *首页近三十天得数据
   * @param id  用户ID   customer_ID
   * @param startTime  开始时间
   * @param endTime    结束时间
   */
  public String homeForDay(String id ,String startTime ,String endTime) {

    String result = null;
    Request request = new Request("GET", "/advertall/_search");
   String param= "{\"size\":0,\"aggs\":{\"zongshu\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100},\"aggs\":{\"groupDate\":{\"date_histogram\":{\"field\":\"PLAY_DATE\",\"calendar_interval\":\"day\",\"format\":\"yyyy-MM-dd\"}}}}},\"query\":{\"bool\":{\"must\":[{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+id+"\"}}},{\"range\":{\"PLAY_DATE\":{\"gte\":\""+startTime+"\",\"lte\":\""+endTime+"\"}}}]}}}";
   // String param = "{\"size\":0,\"aggs\":{\"groupDate\":{\"date_histogram\":{\"field\":\"PLAY_DATE\",\"calendar_interval\":\"day\",\"format\":\"yyyy-MM-dd\"}}},\"query\":{\"bool\":{\"must\":[{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+id+"\"}}},{\"range\":{\"PLAY_DATE\":{\"gte\":\""+startTime+"\",\"lte\":\""+endTime+"\"}}}]}}}";

    try {
      NStringEntity nStringEntity = new NStringEntity(param);
      request.setEntity(nStringEntity);
      Response response = restClient.performRequest(request);
      result = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return result;
  }

    /**
     * 查询首页地图
     * @param map
     * @return
     */
  public String homeData(Map<String,String> map)
  {
      String result = null;
      Request request = new Request("GET", "/advertall/_search");

      JSONObject param = new JSONObject();
      param.put("size", "0");
      //param.put("aggs", JSONObject.parse("{\"countAll\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100000}}}"));
      param.put("aggs", JSONObject.parse("{\"countAll\":{\"terms\":{\"field\":\""+map.get("groupBy")+"\",\"size\":100000}}}"));
      JSONObject bool = new JSONObject();
      JSONArray must = new JSONArray();
      if(map.containsKey("startAreaCode")){
         // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
          JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("endAreaCode")+"}}}");
          must.add(must1);
      }
      //区域编号相等
      if(map.containsKey("areaCode") && StringUtils.isNotBlank(map.get("areaCode"))){
          JSONObject must1 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+map.get("areaCode")+"\"}}}");
          must.add(must1);
      }
      //JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":"+map.get("startTime")+",\"lte\":"+map.get("endTime")+"}}}]}}");
      JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lt\":\""+map.get("endTime")+"\"}}}");
      JSONObject must3 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+map.get("customerId")+"\"}}}");

      if(map.containsKey("sourceType")){
          JSONObject must4 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+map.get("sourceType")+"\"}}}");
          must.add(must4);
      }
      if(map.containsKey("adTypeId")){
          JSONObject must5 = JSONObject.parseObject("{\"term\":{\"AD_TYPE_ID\":{\"value\":\""+map.get("adTypeId")+"\"}}}");
          must.add(must5);
      }

      must.add(must2);
      must.add(must3);


      JSONObject mustObj  = new JSONObject();
      mustObj.put("must",must);
      bool.put("bool", mustObj);
      param.put("query", bool);
        log.info("dddd:::"+param.toString());
      try {
          NStringEntity nStringEntity = new NStringEntity(param.toString());
          request.setEntity(nStringEntity);
          Response response = restClient.performRequest(request);
          result = EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
          // TODO: handle exception
          e.printStackTrace();
      }
      return result;
  }
  /**
   *
   * @param size
   * @param frome
   * @param IS_LEGAL
   * @param SOURCE_TYPE
   * @param content
   * @param AREA_CODE
   * @return
   */
  public String searchCount(int size,int frome,String IS_LEGAL,String SOURCE_TYPE,String content,String AREA_CODE) {
      String result = null;
      Request request = new Request("GET", "/advertall/_count");
      JSONObject param = new JSONObject();



      JSONObject bool = new JSONObject();
      JSONArray must = new JSONArray();

      JSONObject must1 = JSONObject.parseObject("{\"term\":{\"IS_LEGAL\":{\"value\":\""+IS_LEGAL+"\"}}}");
      JSONObject must2 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+SOURCE_TYPE+"\"}}}");
      JSONObject must3 = JSONObject.parseObject("{\"match\":{\"SOURCE_NAME\":\""+content+"\"}}");
      JSONObject must4 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":370300,\"lt\":370400}}}]}},");

      must.add(must1);
      must.add(must2);
      must.add(must3);
      must.add(must4);

      bool.put("bool", must);

      param.put("query", bool);




      try {
        NStringEntity nStringEntity = new NStringEntity(param.toString());
        request.setEntity(nStringEntity);
        Response response = restClient.performRequest(request);
        result = EntityUtils.toString(response.getEntity());
      } catch (IOException e) {
        // TODO: handle exception
        e.printStackTrace();
      }
      return result;
    }
  /**
   *
   *
   * @param size
   * @param frome
   * @param IS_LEGAL
   * @param SOURCE_TYPE
   * @param content
   * @param AREA_CODE   code1:code2  370300    370300:370400
   * @return
   */
  public String searchAll(int size,int frome,String IS_LEGAL,String SOURCE_TYPE,String content,String AREA_CODE) {
        String result = null;
        Request request = new Request("GET", "/advertall/_search");

        JSONObject param = new JSONObject();
        param.put("size", size);
        param.put("from", frome);

        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();

        JSONObject must1 = JSONObject.parseObject("{\"term\":{\"IS_LEGAL\":{\"value\":\""+IS_LEGAL+"\"}}}");
        JSONObject must2 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+SOURCE_TYPE+"\"}}}");
        JSONObject must3 = JSONObject.parseObject("{\"match\":{\"SOURCE_NAME\":\""+content+"\"}}");
        JSONObject must4 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":370300,\"lt\":370400}}}]}},");

        must.add(must1);
        must.add(must2);
        must.add(must3);
        must.add(must4);

        bool.put("bool", must);

        param.put("query", bool);




        JSONArray o = JSONArray.parseArray("[{\"PLAY_DATE\":{\"order\":\"desc\"}}]");

        param.put("sort", o);



        try {
          NStringEntity nStringEntity = new NStringEntity(param.toString());
          request.setEntity(nStringEntity);
          Response response = restClient.performRequest(request);
          result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
          // TODO: handle exception
          e.printStackTrace();
        }
        return result;
      }
  /*private void close() {
    try {
      restClient.close();
    }
    catch(IOException e) {

    }
  }*/

  public static void main(String arg[]) {
    EsUtil eu = new EsUtil();
    Map<String,String> map = new HashMap<>();
      map.put("customerId","147");

      map.put("sourceType","1");
      map.put("siteType","1,2,3,4,5,6,7");

      map.put("startTime","2020-09-01 00:00:00");
      map.put("endTime","2020-09-30 23:59:59");

      //查询总条数，总条次，违法条数，违法条次
      String result = null;
          map.put("keytype","area_tstc");
          result = eu.searchAreaTimes(map);
          log.info("ddddd:"+result);
      List<Map<String,String>> mapList = JsonUtilBack.totalTsTc(result);
  }
  /**
   *
   * @param isLegal 是否合法，1是，0否
   * @param sourceType 媒体类型
   * @param sourceName 媒体名称
   * @param areaCode   code1,code2  370300    370300,370400
   * @return
   */
  public Integer searchCount(String isLegal,String sourceType,String sourceName,String areaCode,int customerId,String time) {
    String result = null;
    Request request = new Request("GET", "/advertall/_count");
    JSONObject param = new JSONObject();

    JSONObject bool = new JSONObject();
    JSONArray must = new JSONArray();

    JSONObject must1 = JSONObject.parseObject("{\"term\":{\"IS_LEGAL\":{\"value\":\""+isLegal+"\"}}}");
    JSONObject must2 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+sourceType+"\"}}}");
    JSONObject must3 = JSONObject.parseObject("{\"match\":{\"SOURCE_NAME\":\""+sourceName+"\"}}");
    JSONObject must4 = null;
    JSONObject must5 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+areaCode+"\"}}}");
    JSONObject must6 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+customerId+"\"}}}");
    if(StringUtils.isNotBlank(isLegal)){
      must.add(must1);
    }
    if(StringUtils.isNotBlank(sourceType)){
      must.add(must2);
    }
    if(StringUtils.isNotBlank(sourceName)){
      must.add(must3);
    }
    if(StringUtils.isNotBlank(areaCode)){
      if(areaCode.indexOf(",")>-1){
        String[] areaCodeArr = areaCode.split(",");
        must4 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+areaCodeArr[0]+",\"lt\":"+areaCodeArr[1]+"}}}");
        must.add(must4);
      } else{
        must.add(must5);
      }
    }
    if(StringUtils.isNotBlank(time)) {
      if(time.indexOf(",")>-1) {
        String[] split = time.split(",");
        JSONObject must7 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+split[0]+"\",\"lte\":\""+split[1]+"\"}}}");
        must.add(must7);
      }
    }

    must.add(must6);
    JSONObject mustObj = new JSONObject();
    mustObj.put("must",must);
    bool.put("bool", mustObj);
    param.put("query", bool);
    System.out.println(param.toString());
    try {
      NStringEntity nStringEntity = new NStringEntity(param.toString());
      request.setEntity(nStringEntity);
      Response response = restClient.performRequest(request);
      result = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }

    return JsonUtilBack.getInteger(result,"count");
  }



  /**
   * 查询按地域排名的条数，违法条数，条次，违法条次
   *  CUSTOMER_ID 客戶id
   *  QUERY_AREA_LEVEL  行政級別 1县级 2：市级 3：省级
   *  AD_TYPE_ID 广告类别
   *  SOURCE_TYPE 媒体分类 1：互联网 2 户外 3：广播 4：电视 5：报纸
   * @return
   */
  public String searchAreaTimes(Map<String,String> map) {

    String result = null;
    Request request = new Request("GET", "/advertall/_search");

    JSONObject param = new JSONObject();
    param.put("size",0);
    System.out.println("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":30},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":30},\"aggs\":{\"ad_id_all\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}");

    if(map.containsKey("keytype") && map.get("keytype").equals("area_len")){//按地域排名时长获取
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"lenCount\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}"));
    }else if(map.containsKey("keytype") && map.get("keytype").equals("area_tstc")){//按地域排名广告数量
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"ad_id_all\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":500},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));

    }else if(map.containsKey("keytype") && map.get("keytype").equals("ati_len")) {//广告排名获取总时长
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"lenCount\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}"));
    }else if(map.containsKey("keytype") && map.get("keytype").equals("ati_tstc")) {//广告排名获取广告数量
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"ad_id_all\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":500},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));

    }else if(map.containsKey("keytype") && map.get("keytype").equals("source_len")) {//媒体获取总时长
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":1500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"lenCount\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}}}"));
    }else if(map.containsKey("keytype") && map.get("keytype").equals("source_tstc")) {//媒体获取广告数量
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":1500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"ad_id_all\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));

    }else if(map.containsKey("keytype") && map.get("keytype").equals("media_len")) {//媒介机构获取总时长
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":1500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"lenCount\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}}}"));
    }else if(map.containsKey("keytype") && map.get("keytype").equals("media_tstc")) {//媒介机构获取广告数量
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":1500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":1500,\"order\": {\"_key\": \"desc\"}},\"aggs\":{\"ad_id_all\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));
        //param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        param.put("aggs", JSONObject.parse("{\"cityCount\":{\"terms\":{\"field\":\"MEDIA_NAME.keyword\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":500},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}}}"));

    }

    JSONObject bool = new JSONObject();
    JSONArray must = new JSONArray();

    if(map.containsKey("customerId") && StringUtils.isNotBlank(map.get("customerId"))){
      JSONObject must5 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+map.get("customerId")+"\"}}}");
      must.add(must5);
    }
    if(map.containsKey("adTypeId") && StringUtils.isNotBlank(map.get("adTypeId"))){
      JSONObject must5 = JSONObject.parseObject("{\"term\":{\"AD_TYPE_ID\":{\"value\":\""+map.get("adTypeId")+"\"}}}");
      must.add(must5);
    }

    if(map.containsKey("queryAreaLevel") && StringUtils.isNotBlank(map.get("queryAreaLevel"))){
      JSONObject must5 = JSONObject.parseObject("{\"term\":{\"QUERY_AREA_LEVEL\":{\"value\":\""+map.get("queryAreaLevel")+"\"}}}");
      must.add(must5);
    }

      if(map.containsKey("sourceType") && StringUtils.isNotBlank(map.get("sourceType"))){
          JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceType")+"]}}");
          must.add(must4);
      }

      if(map.containsKey("siteType") && StringUtils.isNotBlank(map.get("siteType"))){
          JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SITE_TYPE\":["+map.get("siteType")+"]}}");
          must.add(must4);
      }
    //时间
    if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
      JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lte\":\""+map.get("endTime")+"\"}}}");
      must.add(must2);
    }
      JSONObject must7=null;
      must7 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":371400,\"lt\":371500}}}");

        must.add(must7);

    /*if(StringUtils.isNotBlank(map.get("areaCode"))) {
        String areaCode = map.get("areaCode");
        String[] areaCodeArr = areaCode.split(",");

        JSONObject must7=null;
        if("true".equals(map.get("cross"))||areaCodeArr.length==1){
            if(areaCodeArr.length==1){
                must7 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+areaCodeArr[0]+"\"}}}");
            }else{
                must7 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gt\":" + areaCodeArr[0] + ",\"lt\":" + areaCodeArr[1] + "}}}");
            }

        }else{
            must7 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":" + areaCodeArr[0] + ",\"lt\":" + areaCodeArr[1] + "}}}");
        }
        must.add(must7);

    }*/
    JSONObject mustObj = new JSONObject();
    mustObj.put("must",must);
    bool.put("bool", mustObj);
    param.put("query", bool);
    System.out.println(param.toString());
    try {
      NStringEntity nStringEntity = new NStringEntity(param.toString());
      request.setEntity(nStringEntity);
      Response response = restClient.performRequest(request);
      result = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    return result;
  }
    public String searchDistinctSource(Map<String,String> map) {
        String result = null;
        Request request = new Request("GET", "/advertall/_search");
        JSONObject param = new JSONObject();
        param.put("size",0);
        param.put("aggs", JSONObject.parse("{\"sourceDist\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":200},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10}}}}}"));
        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();

        if(map.containsKey("customerId") && StringUtils.isNotBlank(map.get("customerId"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+map.get("customerId")+"\"}}}");
            must.add(must5);
        }
        if(map.containsKey("adTypeId") && StringUtils.isNotBlank(map.get("adTypeId"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"AD_TYPE_ID\":{\"value\":\""+map.get("adTypeId")+"\"}}}");
            must.add(must5);
        }

        if(map.containsKey("queryAreaLevel") && StringUtils.isNotBlank(map.get("queryAreaLevel"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"QUERY_AREA_LEVEL\":{\"value\":\""+map.get("queryAreaLevel")+"\"}}}");
            must.add(must5);
        }
        if(map.containsKey("sourceType") && StringUtils.isNotBlank(map.get("sourceType"))){
            JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceType")+"]}}");
            must.add(must4);
        }
        //时间
        if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
            JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lte\":\""+map.get("endTime")+"\"}}}");
            must.add(must2);
        }
        if(StringUtils.isNotBlank(map.get("areaCode"))) {
            String areaCode = map.get("areaCode");
            String[] areaCodeArr = areaCode.split(",");

            JSONObject must7=null;
            if("true".equals(map.get("cross"))||areaCodeArr.length==1){
                if(areaCodeArr.length==1){
                    must7 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+areaCodeArr[0]+"\"}}}");
                }else{
                    must7 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gt\":" + areaCodeArr[0] + ",\"lt\":" + areaCodeArr[1] + "}}}");
                }

            }else{
                must7 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":" + areaCodeArr[0] + ",\"lt\":" + areaCodeArr[1] + "}}}");
            }
            must.add(must7);

        }
        JSONObject mustObj = new JSONObject();
        mustObj.put("must",must);
        bool.put("bool", mustObj);
        param.put("query", bool);
        System.out.println(param.toString());
        try {
            NStringEntity nStringEntity = new NStringEntity(param.toString());
            request.setEntity(nStringEntity);
            Response response = restClient.performRequest(request);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }
}