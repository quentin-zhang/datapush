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

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
public class EsFUtil {

  private RestClient restClient = RestClient.builder(new HttpHost("172.16.0.22", 10200, "http"),
      new HttpHost("172.16.0.18", 10200, "http"), new HttpHost("172.16.0.14", 10200, "http")).setDefaultHeaders(new Header[] {new BasicHeader("Content-Type", "application/json")}).build();



/*  public void  delete ()
  {
      RestHighLevelClient client = getClient();
      DeleteByQueryRequest request = new DeleteByQueryRequest("advertdata");

      try {
          request.setQuery(new TermQueryBuilder("OLD_ID", "111"));
          BulkByScrollResponse resp = client.deleteByQuery(request, RequestOptions.DEFAULT);
          client.close();
      } catch (IOException e) {

      }
  }*/


  public void close() {
      try {
          restClient.close();
      }
      catch(IOException e) {

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
    finally {
      this.close();
    }
    return result;
  }

    /**
     * 优化统一显示
     * @param map
     * @return
     */
  public String homeData(Map<String,String> map)
  {
      String result = null;
      String keytype = "search"; //查类型：search 查数据   count 查总数
      if(map.containsKey("keytype")){
          keytype = map.get("keytype");
      }
      Request request = new Request("GET", "/advertall/_"+keytype);

      JSONObject param = new JSONObject();
      if(map.containsKey("size") && StringUtils.isNotBlank(map.get("size"))){
          param.put("size", map.get("size"));
      }

      if(map.containsKey("from")){
          param.put("size", map.get("size"));
          param.put("from", map.get("from"));
         /* JSONArray must1 = new JSONArray();
          JSONObject must11 = JSONObject.parseObject("{\"PLAY_DATE\":{\"order\":\"desc\"}}");
          must1.add(must11);
          JSONObject must12 = JSONObject.parseObject("{\"BEGIN_TIME\":{\"order\":\"desc\"}}");
          must1.add(must12);
          param.put("sort",must1);*/
      }
      if(map.containsKey("groupBy") && StringUtils.isNotBlank(map.get("groupBy"))){
          param.put("aggs", JSONObject.parse("{\"countAll\":{\"terms\":{\"field\":\""+map.get("groupBy")+"\",\"size\":100000}}}"));
      }

      JSONObject bool = new JSONObject();
      JSONArray must = new JSONArray();

      //区域编号
      if(map.containsKey("startAreaCode") && StringUtils.isNotBlank(map.get("startAreaCode"))){
         // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
          JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("endAreaCode")+"}}}");
          must.add(must1);
      }

      //区域编号相等
      if(map.containsKey("areaCode") && StringUtils.isNotBlank(map.get("areaCode"))){
          // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
          JSONObject must1 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+map.get("areaCode")+"\"}}}");
          must.add(must1);
      }
      //时间
      if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
          JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lte\":\""+map.get("endTime")+"\"}}}");
          must.add(must2);
      }
      if(map.containsKey("insertStartTime") && StringUtils.isNotBlank(map.get("insertStartTime"))){
          JSONObject must2 = JSONObject.parseObject("{\"range\":{\"INSERT_TIME\":{\"gte\":\""+map.get("insertStartTime")+"\",\"lte\":\""+map.get("insertEndTime")+"\"}}}");
          must.add(must2);
      }

      //根据sourceTypetin查询
      if(map.containsKey("sourceTypes") && StringUtils.isNotBlank(map.get("sourceTypes"))){
         JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceTypes")+"]}}");
         must.add(must4);
      }

      //广告类型id
      if(map.containsKey("adTypeId") && StringUtils.isNotBlank(map.get("adTypeId"))){
          JSONObject must5 = JSONObject.parseObject("{\"term\":{\"AD_TYPE_ID\":{\"value\":\""+map.get("adTypeId")+"\"}}}");
          must.add(must5);
      }
      if(map.containsKey("customerId") && StringUtils.isNotBlank(map.get("customerId"))) {
          //客户id
          JSONObject must3 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\"" + map.get("customerId") + "\"}}}");
          must.add(must3);
      }
      if(map.containsKey("sourceId") && StringUtils.isNotBlank(map.get("sourceId"))) {
          //客户id
          JSONObject must3 = JSONObject.parseObject("{\"term\":{\"SOURCE_ID\":{\"value\":\"" + map.get("sourceId") + "\"}}}");
          must.add(must3);
      }
      //媒体名称
      if(map.containsKey("sourceName") && StringUtils.isNotBlank(map.get("sourceName"))){
          JSONObject must6 = JSONObject.parseObject("{\"match\":{\"SOURCE_NAME\":\""+map.get("sourceName")+"\"}}");
          must.add(must6);
      }
      //广告名称
      if(map.containsKey("adName") && StringUtils.isNotBlank(map.get("adName"))){
          JSONObject must6 = JSONObject.parseObject("{\"match\":{\"AD_NAME\":\""+map.get("adName")+"\"}}");
          must.add(must6);
      }
       //是否删除
      if(map.containsKey("isDelete") && StringUtils.isNotBlank(map.get("isDelete"))){
          JSONObject must5 = JSONObject.parseObject("{\"term\":{\"IS_DELETE\":{\"value\":\""+map.get("isDelete")+"\"}}}");
          must.add(must5);
      }

      //是否违法
      if(map.containsKey("isLegal") && StringUtils.isNotBlank(map.get("isLegal"))){
          JSONObject must5 = JSONObject.parseObject("{\"term\":{\"IS_LEGAL\":{\"value\":\""+map.get("isLegal")+"\"}}}");
          must.add(must5);
      }
      //违法编码查询
      if(map.containsKey("illegalCode") && StringUtils.isNotBlank(map.get("illegalCode"))){//
          JSONObject must6 = JSONObject.parseObject("{\"match\":{\"ILLEGAL_CODE\":\""+map.get("illegalCode")+"\"}}");
          must.add(must6);
      }

      if(map.containsKey("areaCodes") && StringUtils.isNotBlank(map.get("areaCodes"))){
          // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
          JSONObject must1 = JSONObject.parseObject("{\"terms\":{\"AREA_CODE\":["+map.get("areaCodes")+"]}}");
          must.add(must1);
      }


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
         // log.info("dddd:::"+result);
      } catch (IOException e) {
          // TODO: handle exception
          e.printStackTrace();
      }
      finally {
          this.close();
      }
      return result;
  }
  public String del(String oldId,String insertTime,int isDel)
  {
      String result = null;
      Request request = new Request("POST", "/advertall/_update_by_query");
      String param= "{\"script\":{\"source\":\"ctx._source.IS_DEL="+isDel+"\",\"lang\":\"painless\"},\"query\":{\"bool\":{\"must\":[{\"term\":{\"OLD_ID\":{\"value\":\""+oldId+"\"}}},{\"term\":{\"INSERT_TIME\":{\"value\":\""+insertTime+"\"}}}]}}}";
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
      finally {
          this.close();
      }
      return result;

        }

    /**
     * 查询按地域排名的条数，违法条数，条次，违法条次
     *
     */
    public String exportData(Map<String,String> map){

        String result = null;
        Request request = new Request("GET", "/advertall/_search");

        JSONObject param = new JSONObject();
        if(map.containsKey("size") && map.get("size").equals("1")){
            param.put("size",1);
        }else{
            param.put("size",0);
        }

        if(map.containsKey("keytype") && map.get("keytype").equals("1")){
            //按sourcetype媒体类型查询条数和条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100,\"order\":{\"_key\":\"asc\"}},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("2")){
            //按sourceType媒体类型查询监测媒体数量
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"SOURCE_ID\"}}}}}}}"));

        }else if(map.containsKey("keytype") && map.get("keytype").equals("3")) {
            //媒体发布情况（目前没有户外，户外需要单独走）
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":10000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":10},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("4")) {
            //季度广告发布情况
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10,\"order\":{\"_key\":\"asc\"}},\"aggs\":{\"count\":{\"date_histogram\":{\"field\":\"PLAY_DATE\",\"format\":\"yyyy-MM\",\"calendar_interval\":\"month\"},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":10},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("5")) {
            //广告类别发布情况
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"IS_LEGAL\",\"size\":10}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("6")) {
            //户外监测范围
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("7")){
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("8")){
            //传统媒体类型获取播放总秒数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("-1")){
            //传统媒体类型获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("9")){
            //传统广告类型获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
         }else if(map.containsKey("keytype") && map.get("keytype").equals("10")){
            //传统广告类型获取播放总秒数和总条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("11")){
            //按地域编号获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("12")){
            //传统广告类型获取播放总秒数和总条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("13")){
            //按媒体编号获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("14")){
            //按媒体编号获取总时长
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("14")){
            //按小时
            param.put("aggs", JSONObject.parse("{\"count\":{\"date_histogram\":{\"field\":\"BEGIN_TIME\",\"calendar_interval\":\"hour\",\"format\":\"HH\"}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("15")){
            //首页合法总计
            param.put("aggs", JSONObject.parse("{\"count\":{\"date_histogram\":{\"field\":\"PLAY_DATE\",\"calendar_interval\":\"day\",\"format\":\"yyyy-MM-dd\"}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("16")){
            //首页合法总计
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":50},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":500}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("17")){
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"CUSTOMER_ID\",\"size\":100}}}"));
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
        //区域编号相等
        if(map.containsKey("areaCode") && StringUtils.isNotBlank(map.get("areaCode"))){
            // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
            JSONObject must1 = JSONObject.parseObject("{\"term\":{\"AREA_CODE\":{\"value\":\""+map.get("areaCode")+"\"}}}");
            must.add(must1);
        }

        if(map.containsKey("areaCodes") && StringUtils.isNotBlank(map.get("areaCodes"))){
            // JSONObject must1 = JSONObject.parseObject("{\"range\":{\"AREA_CODE\":{\"gte\":"+map.get("startAreaCode")+",\"lt\":"+map.get("startAreaCode")+"}}}]}}");
            JSONObject must1 = JSONObject.parseObject("{\"terms\":{\"AREA_CODE\":["+map.get("areaCodes")+"]}}");
            must.add(must1);
        }
        //广告名称
        if(map.containsKey("adName") && StringUtils.isNotBlank(map.get("adName"))){
            JSONObject must6 = JSONObject.parseObject("{\"match\":{\"AD_NAME\":{\"query\":\""+ map.get("adName")+"\"}}}");
            must.add(must6);
        }
        //根据sourceType条件查询
        if(map.containsKey("sourceType") && StringUtils.isNotBlank(map.get("sourceType"))){
            //JSONObject must4 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+map.get("sourceType")+"\"}}}");
           // JSONObject must100 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\"2\"}}}");
            //must.add(must100);
        }
        //根据sourceTypetin查询
        if(map.containsKey("sourceTypes") && StringUtils.isNotBlank(map.get("sourceTypes"))){
            JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceTypes")+"]}}");
            must.add(must4);
        }
        //时间
        if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
            JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lt\":\""+map.get("endTime")+"\"}}}");
            must.add(must2);
        }
        if(map.containsKey("insertStartTime") && StringUtils.isNotBlank(map.get("insertStartTime"))){
            JSONObject must2 = JSONObject.parseObject("{\"range\":{\"INSERT_TIME\":{\"gte\":\""+map.get("insertStartTime")+"\",\"lte\":\""+map.get("insertEndTime")+"\"}}}");
            must.add(must2);
        }

        //根据id获取数据
        if(map.containsKey("id") && StringUtils.isNotBlank(map.get("id"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"_id\":{\"value\":\""+map.get("id")+"\"}}}");
            must.add(must5);
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
            log.info("返回数据："+result);
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally {
            this.close();
        }
        return result;
    }
    public String exportData1(Map<String,String> map) throws Exception {

        String result = null;
        Request request = new Request("POST", "/advertall/_search");

        JSONObject param = new JSONObject();
        if(map.containsKey("size") && map.get("size").equals("1")){
            param.put("size",1);
        }else{
            param.put("size",0);
        }

        if(map.containsKey("keytype") && map.get("keytype").equals("7")){
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("8")){
            //传统媒体类型获取播放总秒数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("-1")){
            //传统媒体类型获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("9")){
            //传统广告类型获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("10")){
            //传统广告类型获取播放总秒数和总条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("11")){
            //按地域编号获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("12")){
            //传统广告类型获取播放总秒数和总条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("13")){
            //按媒体编号获取总条数
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":1000},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AD_TYPE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"cardinality\":{\"field\":\"AD_ID\"}}}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("14")){
            //按媒体编号获取总时长
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("15")){
            //按小时
            param.put("aggs", JSONObject.parse("{\"count\":{\"date_histogram\":{\"field\":\"BEGIN_TIME\",\"calendar_interval\":\"hour\",\"format\":\"HH\",\"min_doc_count\":0},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}"));
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

//        if(map.containsKey("queryAreaLevel") && StringUtils.isNotBlank(map.get("queryAreaLevel"))){
//            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"QUERY_AREA_LEVEL\":{\"value\":\""+map.get("queryAreaLevel")+"\"}}}");
//            must.add(must5);
//        }
        //根据id获取数据
        if(map.containsKey("queryAreaLevel") && StringUtils.isNotBlank(map.get("queryAreaLevel"))){
            JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"QUERY_AREA_LEVEL\":["+map.get("queryAreaLevel")+"]}}");
            must.add(must4);
        }

        //根据sourceType条件查询
        if(map.containsKey("sourceType") && StringUtils.isNotBlank(map.get("sourceType"))){
            JSONObject must4 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+map.get("sourceType")+"\"}}}");
            must.add(must4);
        }
        //根据sourceTypetin查询
        if(map.containsKey("sourceTypes") && StringUtils.isNotBlank(map.get("sourceTypes"))){
            JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceTypes")+"]}}");
            must.add(must4);
        }
        //时间
        if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
            JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lt\":\""+map.get("endTime")+"\"}}}");
            must.add(must2);
        }

        //根据id获取数据
        if(map.containsKey("id") && StringUtils.isNotBlank(map.get("id"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"_id\":{\"value\":\""+map.get("id")+"\"}}}");
            must.add(must5);
        }

        //广告名称
        if(map.containsKey("adName") && StringUtils.isNotBlank(map.get("adName"))){
            JSONObject must6 = JSONObject.parseObject("{\"match\":{\"AD_NAME\":{\"query\":\""+ map.get("adName")+"\"}}}");
            must.add(must6);
        }
        JSONObject mustObj = new JSONObject();
        mustObj.put("must",must);
        bool.put("bool", mustObj);
        param.put("query", bool);
        System.out.println(param.toString());
        try {
            NStringEntity nStringEntity = new NStringEntity(param.toString(),"utf-8");
            request.setEntity(nStringEntity);
            Response response = restClient.performRequest(request);
            result = EntityUtils.toString(response.getEntity());
            log.info("返回数据："+result);
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally {
            this.close();
        }
        return result;
    }


    public String exportNingXia(Map<String,String> map) throws Exception {

        String result = null;
        Request request = new Request("POST", "/advertall/_search");

        JSONObject param = new JSONObject();
        if(map.containsKey("size") && map.get("size").equals("1")){
            param.put("size",1);
        }else{
            param.put("size",0);
        }

        if(map.containsKey("keytype") && map.get("keytype").equals("1")){
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("2")) {
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse("{\"count\":{\"terms\":{\"field\":\"SOURCE_TYPE\",\"size\":10},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100},\"aggs\":{\"count\":{\"terms\":{\"field\":\"AREA_CODE\",\"size\":100},\"aggs\":{\"count\":{\"sum\":{\"field\":\"PLAY_LEAN\"}}}}}}}}}"));
        }else if(map.containsKey("keytype") && map.get("keytype").equals("3")){
            String dd = "{\"count\":{\"date_histogram\":{\"field\":\"BEGIN_TIME\",\"calendar_interval\":\"hour\"},\"aggs\":{\"count\":{\"terms\":{\"field\":\"SOURCE_ID\",\"size\":100}}}}}";
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse(dd));

        }else if(map.containsKey("keytype") && map.get("keytype").equals("4"))
        {
            String dd= "{\"count\":{\"date_histogram\":{\"field\":\"PLAY_DATE\",\"calendar_interval\":\"day\"}}}";
            //传统媒体类型获取违法条数和条次
            param.put("aggs", JSONObject.parse(dd));
        }
        JSONObject bool = new JSONObject();
        JSONArray must = new JSONArray();

        if(map.containsKey("customerId") && StringUtils.isNotBlank(map.get("customerId"))){
            JSONObject must5 = JSONObject.parseObject("{\"term\":{\"CUSTOMER_ID\":{\"value\":\""+map.get("customerId")+"\"}}}");
            must.add(must5);
        }

        //根据sourceType条件查询
        if(map.containsKey("sourceType") && StringUtils.isNotBlank(map.get("sourceType"))){
            JSONObject must4 = JSONObject.parseObject("{\"term\":{\"SOURCE_TYPE\":{\"value\":\""+map.get("sourceType")+"\"}}}");
            must.add(must4);
        }
        //根据sourceTypetin查询
        if(map.containsKey("sourceTypes") && StringUtils.isNotBlank(map.get("sourceTypes"))){
            JSONObject must4 = JSONObject.parseObject("{\"terms\":{\"SOURCE_TYPE\":["+map.get("sourceTypes")+"]}}");
            must.add(must4);
        }
        //时间
        if(map.containsKey("startTime") && StringUtils.isNotBlank(map.get("startTime"))){
            JSONObject must2 = JSONObject.parseObject("{\"range\":{\"PLAY_DATE\":{\"gte\":\""+map.get("startTime")+"\",\"lt\":\""+map.get("endTime")+"\"}}}");
            must.add(must2);
        }

        //广告名称
        if(map.containsKey("adName") && StringUtils.isNotBlank(map.get("adName"))){
            JSONObject must6 = JSONObject.parseObject("{\"match\":{\"AD_NAME\":{\"query\":\""+ map.get("adName")+"\"}}}");
            must.add(must6);
        }
        JSONObject mustObj = new JSONObject();
        mustObj.put("must",must);
        bool.put("bool", mustObj);
        param.put("query", bool);
        System.out.println(param.toString());
        try {
            NStringEntity nStringEntity = new NStringEntity(param.toString(),"utf-8");
            request.setEntity(nStringEntity);
            Response response = restClient.performRequest(request);
            result = EntityUtils.toString(response.getEntity());
            log.info("返回数据："+result);
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        finally {
            this.close();
        }
        return result;
    }
    public static void main(String[] arg) throws Exception{

        String teString=new String("公益".getBytes("ISO-8859-1"),"UTF-8");
        System.out.println(teString);
        System.out.println(URLEncoder.encode("公益","utf-8"));
    }

}