package com.example.demo.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/***
 * 需引用fastjson-1.1.37.jar json解析
 *
 * @author wyy
 *
 */
@Slf4j
public class JsonUtilBack {

	public static void main(String[] args) throws Exception {
		//String start="2020-04-01";
		Map<String,String> map = new HashMap<>();
		map.put("startTime","2020-07-01 00:00:00");
		map.put("endTime","2020-09-30 23:59:59");
		map.put("sourceTypes","3,4,5");
		map.put("customerId","133");
		map.put("adName","公益");
		/*Map<String,String> areaCodeMap = ResultUtil.getAreaCode("610000");
		if(areaCodeMap.containsKey("startAreaCode") && StringUtils.isNotBlank(areaCodeMap.get("startAreaCode"))){
			map.put("startAreaCode",areaCodeMap.get("startAreaCode"));
			map.put("endAreaCode",areaCodeMap.get("endAreaCode"));
		}*/

		//获取总条数
		map.put("keytype","11");
		EsFUtil esFUtil2 = new EsFUtil();
		String result2 = esFUtil2.exportData1(map);
		List<Map<String,String>> mapList = JsonUtilBack.totalTs(result2);
		//totalTs(result2);


		//System.out.println(getInteger(jsonStr, "_source,tel_type", ","));
	}

	/***
	 * @param json 要解析的json串
	 * @param key  要取的key
	 * @return 数组
	 */
	public static List<String> getList(String json, String key) {
		List<String> list = new ArrayList<String>();
		JSONArray jsonArray = JSONObject.parseObject(json).getJSONArray(key);
		for (Object object : jsonArray) {
			list.add(object.toString());
		}
		return list;
	}

	/***
	 * @param json      要解析的json串
	 * @param key       要取的key
	 * @param separator key之间的分隔符
	 * @return 数组
	 */
	public static List<String> getList(String json, String key, String separator) {
		List<String> list = new ArrayList<String>();
		json = getString(json, key, separator);

		JSONArray jsonArray = JSONObject.parseArray(json);
		for (Object object : jsonArray) {
			list.add(object.toString());
		}

		return list;
	}

	/***
	 * @param json 要解析的json串
	 * @param key  要取的key
	 * @return 返回一个String
	 */
	public static String getString(String json, String key) {
		return JSONObject.parseObject(json).getString(key);
	}

	/***
	 * @param json      要解析的json串
	 * @param key       要取的key
	 * @param separator key之间的分隔符
	 * @return 返回一个String
	 */
	public static String getString(String json, String key, String separator) {
		String keyArray[] = StringUtils.split(key, separator);

		if (keyArray == null)
			return null;

		for (String string : keyArray) {
			json = getString(json, string);
		}
		return json;
	}

	/***
	 * @param json 要解析的json串
	 * @param key  要取的key
	 * @return 返回一个Integer
	 */
	public static Integer getInteger(String json, String key) {
		return JSONObject.parseObject(json).getInteger(key);
	}

	/***
	 * @param json      要解析的json串
	 * @param key       要取的key
	 * @param separator key之间的分隔符
	 * @return 返回一个Integer
	 */
	public static Integer getInteger(String json, String key, String separator) {
		String keyArray[] = StringUtils.split(key, separator);

		if (keyArray == null)
			return null;

		for (String string : keyArray) {
			json = getString(json, string);
		}
		return Integer.valueOf(json);
	}

	/***
	 * @param json 要解析的json串
	 * @param key  要取的key
	 * @return 返回一个Long
	 */
	public static Long getLong(String json, String key) {
		return JSONObject.parseObject(json).getLong(key);
	}

	/***
	 * @param json      要解析的json串
	 * @param key       要取的key
	 * @param separator key之间的分隔符
	 * @return 返回一个Long
	 */
	public static Long getLong(String json, String key, String separator) {
		String keyArray[] = StringUtils.split(key, separator);

		if (keyArray == null)
			return null;

		for (String string : keyArray) {
			json = getString(json, string);
		}
		return Long.valueOf(json);
	}

	/***
	 * @param json 要解析的json串
	 * @param key  要取的key
	 * @return 返回一个Double
	 */
	public static Double getDouble(String json, String key) {
		return JSONObject.parseObject(json).getDouble(key);
	}

	/***
	 * @param json      要解析的json串
	 * @param key       要取的key
	 * @param separator key之间的分隔符
	 * @return 返回一个Double
	 */
	public static Double getDouble(String json, String key, String separator) {
		String keyArray[] = StringUtils.split(key, separator);

		if (keyArray == null)
			return null;

		for (String string : keyArray) {
			json = getString(json, string);
		}
		return Double.valueOf(json);
	}

	//根据类型转换数据
	public static List<Map<String,String>> totalTs(String result){


		List<Map<String,String>> mapList = new ArrayList<>();


		List<String> list1 = JsonUtilBack.getList(result,"aggregations,count,buckets",",");
		//一共循环四层
		int totalTC = 0;
		for (String line1: list1) {
			Map<String,String> map = new HashMap<>();
			Map maps =  JSONObject.parseObject(line1);
			map.put("id",maps.get("key").toString());

			totalTC += Integer.valueOf(maps.get("doc_count").toString());

			int totalData = 0;
			List<String> list2 = JsonUtilBack.getList(line1,"count,buckets",",");
			for (String line2: list2) {
				List<String> list3 = JsonUtilBack.getList(line2,"count,buckets",",");
				for (String line3: list3) {
					List<String> list4 = JsonUtilBack.getList(line3,"count,buckets",",");

					for (String line4: list4) {
						Map map4 =  JSONObject.parseObject(line4);
						log.info("dddsss::"+JSONObject.toJSONString(map4));
						JSONObject jsonObject = JSONObject.parseObject(map4.get("count").toString());
						totalData = totalData + Integer.valueOf(jsonObject.get("value").toString());//总条数

					}
				}

			}
			map.put("totalTs",totalData+"");
			mapList.add(map);
		}

		int zongTs3 = 0;
		for (Map<String,String> map2 : mapList){
			zongTs3 += Integer.valueOf(map2.get("totalTs"));
		}
		log.info("总条数：："+zongTs3);
		log.info("总条次：："+totalTC);
		return mapList;
	}
    //根据类型转换数据(获取条数条次)（按地域，按广告类别专用）
    public static List<Map<String,String>>  totalTsTc(String result){
        List<Map<String,String>> mapList = new ArrayList<>();
        List<String> list1 = JsonUtilBack.getList(result,"aggregations,cityCount,buckets",",");
        //一共循环四层
        int totalTC = 0;
        //一共循环四层
        for (String line1: list1) {
            Map<String,String> map = new HashMap<>();
            Map maps =  JSONObject.parseObject(line1);
            map.put("id",maps.get("key").toString());
            int totalData = 0;
            totalTC += Integer.valueOf(maps.get("doc_count").toString());
            List<String> list2 = JsonUtilBack.getList(line1,"count,buckets",",");
            for (String line2: list2) {
                List<String> list3 = JsonUtilBack.getList(line2,"count,buckets",",");
                for (String line3: list3) {
                    List<String> list4 = JsonUtilBack.getList(line3,"count,buckets",",");
                    for (String line4: list4) {
                        List<String> list5 = JsonUtilBack.getList(line4,"count,buckets",",");
                        for(String line5: list5){
                            Map map5 =  JSONObject.parseObject(line5);
                          /*  JSONObject jsonObject = JSONObject.parseObject(map5.get("count").toString());*/
                         /*   totalTc = totalTc + Integer.valueOf(map5.get("doc_count").toString());
                            totalTs = totalTs + Integer.valueOf(jsonObject.get("value").toString());//总条数*/
                            log.info("dddsss::"+JSONObject.toJSONString(map5));
                            JSONObject jsonObject = JSONObject.parseObject(map5.get("count").toString());
                            totalData = totalData + Integer.valueOf(jsonObject.get("value").toString());//总条数
                        }
                    }
                }
            }
            map.put("totalTs",totalData+"");
            mapList.add(map);
        }
        int zongTs3 = 0;
        for (Map<String,String> map2 : mapList){
            zongTs3 += Integer.valueOf(map2.get("totalTs"));
        }
        log.info("总条数：："+zongTs3);
        log.info("总条次：："+totalTC);
        return mapList;
    }



}
