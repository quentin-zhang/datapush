package com.zlst.data.dao;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.elasticsearch.search.aggregations.metrics.SumAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: dashboard dao
 * @author: Quentin Zhang
 * @create: 2020-10-15 10:50
 **/
@Component
public class DashboardDao {
    @Autowired
    RestHighLevelClient client;

    public List<Map<String, Object>> getList(String startTime, String endTime) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");
            //按直播分组
            TermsAggregationBuilder aggregationLive = AggregationBuilders.terms("group_by_OLD_ID")
                    .field("OLD_ID.keyword");
//                    .order(BucketOrder.aggregation("group_by_PLAY_DATE ", false)).size(1000);
            //按直播房间主分组
            TermsAggregationBuilder aggregationLiveOwnerName = aggregationLive.subAggregation(AggregationBuilders.terms("group_by_SOURCE_NAME")
                    .field("SOURCE_NAME.keyword"));
            //直播平台分组
            TermsAggregationBuilder aggregationMediaName = aggregationLiveOwnerName.subAggregation(AggregationBuilders.terms("group_by_MEDIA_NAME")
                    .field("MEDIA_NAME.keyword"));
            //直播日期
            TermsAggregationBuilder aggregationPlayDate = aggregationMediaName.subAggregation(AggregationBuilders.terms("group_by_PLAY_DATE")
                    .field("PLAY_DATE.keyword"));
            //直播时长
            aggregationPlayDate.subAggregation(AggregationBuilders.terms("group_by_PLAY_LEAN")
                    .field("PLAY_LEAN.keyword"));

            //聚合带货数量
            SumAggregationBuilder sumGoodsCount = AggregationBuilders.sum("sum_by_GOODS_COUNT")
                    .field("GOODS_COUNT");
            SumAggregationBuilder sumViewerCount = AggregationBuilders.sum("sum_by_VIEWER_COUNT")
                    .field("VIEWER_COUNT");
            SumAggregationBuilder sumWatchCount = AggregationBuilders.sum("sum_by_WATCH_COUNT")
                    .field("WATCH_COUNT");
            //直播分组绑定聚合
            aggregationLive.subAggregation(sumGoodsCount);
            aggregationLive.subAggregation(sumViewerCount);
            aggregationLive.subAggregation(sumWatchCount);

            searchSourceBuilder.query(timeRangeBuilder);
            searchSourceBuilder.aggregation(aggregationLive);
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            Aggregations agg = searchResponse.getAggregations();
            Terms terms = agg.get("group_by_OLD_ID");

            //按直播ID循环
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> infoMap = new HashMap<>();
                String liveOwnerName = "";
                long advertCount = 0;
                double goodsCount =0;
                double viewerCount =0;
                double watchCount = 0;
                String mediaName = "";
                String playDate = "";
                String playLean = "";

                Terms sourceTerms = bucket.getAggregations().get("group_by_SOURCE_NAME");

                Terms.Bucket sourceBucket0 = sourceTerms.getBuckets().get(0);

                //直播主
                liveOwnerName = sourceBucket0.getKeyAsString();
                //广告数
                advertCount = sourceBucket0.getDocCount();
                Sum goodsCountAgg = bucket.getAggregations().get("sum_by_GOODS_COUNT");
                goodsCount = goodsCountAgg.getValue();
                //观看人数
                Sum viewerCountAgg = bucket.getAggregations().get("sum_by_VIEWER_COUNT");
                viewerCount = viewerCountAgg.getValue();
                //观看次数
                Sum watchCountAgg = bucket.getAggregations().get("sum_by_WATCH_COUNT");
                watchCount = watchCountAgg.getValue();
                //直播平台
                Terms mediaNameAgg = bucket.getAggregations().get("group_by_MEDIA_NAME");
                mediaName = mediaNameAgg.getBuckets().get(0).getKeyAsString();
                //直播日期
                Terms playDateAgg = bucket.getAggregations().get("group_by_PLAY_DATE");
                playDate = playDateAgg.getBuckets().get(0).getKeyAsString();
                //直播时长
                Terms playLeanAgg = bucket.getAggregations().get("group_by_PLAY_LEAN");
                playLean = playLeanAgg.getBuckets().get(0).getKeyAsString();

                infoMap.put("liveOwnerName",liveOwnerName);
                infoMap.put("advertCount",advertCount);
                infoMap.put("goodsCount",goodsCount);
                infoMap.put("viewerCount",viewerCount);
                infoMap.put("watchCount",watchCount);
                infoMap.put("mediaName",mediaName);
                infoMap.put("playDate",playDate);
                infoMap.put("playLean",playLean);

                mapList.add(infoMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    public List<Map<String, Object>> adDetailList(String startTime, String endTime,String oldName) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");

            QueryBuilder termBuilder = QueryBuilders.termQuery("OLD_ID.keyword",oldName);


            searchSourceBuilder.query(QueryBuilders.boolQuery().must(termBuilder).must(timeRangeBuilder));
            searchSourceBuilder.size(50);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();

            for (SearchHit hit : searchResponse.getHits()) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                Map<String, Object> infoMap = new HashMap<>();
                infoMap.put("liveOwnerName",sourceAsMap.get("SOURCE_NAME"));
                infoMap.put("adName",sourceAsMap.get("AD_NAME"));
                infoMap.put("cityName",sourceAsMap.get("CITY_NAME"));
                infoMap.put("illegalContent",sourceAsMap.get("ILLEGAL_CONTENT"));
                infoMap.put("adTypeName",sourceAsMap.get("AD_TYPE_NAME"));
                infoMap.put("illegalCode",sourceAsMap.get("ILLEGAL_CODE"));
                infoMap.put("ftpPath",sourceAsMap.get("FTP_PATH"));
                mapList.add(infoMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }
        //观看人数
    public List<Map<String, Object>> rankbyviewersList(String startTime, String endTime) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");

            //按直播房间主分组
            TermsAggregationBuilder aggregationLiveOwnerName = AggregationBuilders.terms("group_by_SOURCE_NAME")
                    .field("SOURCE_NAME.keyword")
                    .order(BucketOrder.aggregation("sum_by_VIEWER_COUNT ", false)).size(1000);
            //聚合观看数量
            SumAggregationBuilder sumViewerCount = AggregationBuilders.sum("sum_by_VIEWER_COUNT")
                    .field("VIEWER_COUNT");

            //直播分组绑定聚合
            aggregationLiveOwnerName.subAggregation(sumViewerCount);

            searchSourceBuilder.query(timeRangeBuilder);
            searchSourceBuilder.aggregation(aggregationLiveOwnerName);
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            Aggregations agg = searchResponse.getAggregations();
            Terms terms = agg.get("group_by_SOURCE_NAME");

            //按直播ID循环
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> infoMap = new HashMap<>();
                //直播主
                String liveOwnerName = bucket.getKeyAsString();
                double viewerCount =0;

                //观看人数
                Sum viewerCountAgg = bucket.getAggregations().get("sum_by_VIEWER_COUNT");
                viewerCount = viewerCountAgg.getValue();

                infoMap.put("liveOwnerName",liveOwnerName);
                infoMap.put("viewerCount",viewerCount);

                mapList.add(infoMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //观看次数
    public List<Map<String, Object>> rankByWatchCountList(String startTime, String endTime) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");

            //按直播房间主分组
            TermsAggregationBuilder aggregationLiveOwnerName = AggregationBuilders.terms("group_by_SOURCE_NAME")
                    .field("SOURCE_NAME.keyword")
                    .order(BucketOrder.aggregation("sum_by_WATCH_COUNT ", false)).size(1000);
            //观看次数
            SumAggregationBuilder sumWatchCount = AggregationBuilders.sum("sum_by_WATCH_COUNT")
                    .field("WATCH_COUNT");

            //直播分组绑定聚合
            aggregationLiveOwnerName.subAggregation(sumWatchCount);

            searchSourceBuilder.query(timeRangeBuilder);
            searchSourceBuilder.aggregation(aggregationLiveOwnerName);
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            Aggregations agg = searchResponse.getAggregations();
            Terms terms = agg.get("group_by_SOURCE_NAME");

            //按直播ID循环
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> infoMap = new HashMap<>();
                //直播主
                String liveOwnerName = bucket.getKeyAsString();
                double watchCount = 0;

                //观看人数
                Sum watchCountAgg = bucket.getAggregations().get("sum_by_WATCH_COUNT");
                watchCount = watchCountAgg.getValue();

                infoMap.put("liveOwnerName",liveOwnerName);
                infoMap.put("viewerCount",watchCount);

                mapList.add(infoMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //广告类别违法
    public List<Map<String, Object>> rankByAdTypeList(String startTime, String endTime) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");

            //按广告类型分组
            //noinspection AlibabaLowerCamelCaseVariableNaming
            TermsAggregationBuilder aggregationAdType = AggregationBuilders.terms("group_by_AD_TYPE_NAME")
                    .field("AD_TYPE_NAME.keyword");
//                    .order(BucketOrder.aggregation("sum_by_WATCH_COUNT ", false)).size(1000);
            //按直播房间住分组
            TermsAggregationBuilder aggregationLiveOwnerName = AggregationBuilders.terms("group_by_SOURCE_NAME")
                    .field("SOURCE_NAME.keyword");

            //直播分组绑定聚合
            aggregationAdType.subAggregation(aggregationLiveOwnerName);

            searchSourceBuilder.query(timeRangeBuilder);
            searchSourceBuilder.aggregation(aggregationAdType);
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            Aggregations agg = searchResponse.getAggregations();
            Terms terms = agg.get("group_by_AD_TYPE_NAME");

            //按直播ID循环
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> adTypeMap = new HashMap<>();
                String adTypeName = bucket.getKeyAsString();
                Terms sourceNameAgg = bucket.getAggregations().get("group_by_SOURCE_NAME");
                List<Map<String, Object>> ownerList = new ArrayList<>();
                for (Terms.Bucket sourceNameBucket : sourceNameAgg.getBuckets()) {
                    Map<String, Object> ownerMap = new HashMap<>();
                    String liveOwnerName = sourceNameBucket.getKeyAsString();
                    double adCount = sourceNameBucket.getDocCount();
                    ownerMap.put("liveOwnerName",liveOwnerName);
                    ownerMap.put("adCount",adCount);
                    ownerList.add(ownerMap);
                }
                adTypeMap.put(adTypeName,ownerList);
                mapList.add(adTypeMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    //按条数违法
    public List<Map<String, Object>> rankByAdCountList(String startTime, String endTime) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            SearchRequest searchRequest = new SearchRequest("product_live_2020");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder timeRangeBuilder = QueryBuilders.rangeQuery("BEGIN_TIME")
                    .gte(startTime)
                    .lte(endTime).format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").timeZone("+08:00");

            //按直播房间主分组
            TermsAggregationBuilder aggregationLiveOwnerName = AggregationBuilders.terms("group_by_SOURCE_NAME")
                    .field("SOURCE_NAME.keyword")
                    .order(BucketOrder.count(false)).size(1000);

            searchSourceBuilder.query(timeRangeBuilder);
            searchSourceBuilder.aggregation(aggregationLiveOwnerName);
            searchSourceBuilder.size(0);
            searchRequest.source(searchSourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            Aggregations agg = searchResponse.getAggregations();
            Terms terms = agg.get("group_by_SOURCE_NAME");

            //按直播ID循环
            for (Terms.Bucket bucket : terms.getBuckets()) {
                Map<String, Object> infoMap = new HashMap<>();
                //直播主
                String liveOwnerName = bucket.getKeyAsString();
                //广告条次
                double adCount = bucket.getDocCount();

                infoMap.put("liveOwnerName",liveOwnerName);
                infoMap.put("adCount",adCount);
                mapList.add(infoMap);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapList;
    }
}
