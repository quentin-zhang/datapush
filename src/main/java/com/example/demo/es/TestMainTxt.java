package com.example.demo.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

@Slf4j
public class TestMainTxt {

    //elasticsearch.hosts: ["http://172.16.0.22:10200","http://172.16.0.18:10200","http://172.16.0.14:10200"]
//	private static RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("172.16.0.22", 10200),new HttpHost("172.16.0.18", 10200),new HttpHost("172.16.0.14", 10200))
//			.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
//				public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
//					return requestConfigBuilder.setConnectTimeout(5000) // 连接超时（默认为1秒）
//							.setSocketTimeout(60000);// 套接字超时（默认为30秒）
//				}
//			}).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
//				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
//					return httpClientBuilder
//							.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());// 线程数
//				}
//			});

//    private static RestHighLevelClient client =  new RestHighLevelClient(
//            RestClient.builder(new HttpHost("172.16.0.22", 10200),new HttpHost("172.16.0.18", 10200),new HttpHost("172.16.0.14", 10200)));
	/*	private static RestHighLevelClient client =  new RestHighLevelClient(
		RestClient.builder(new HttpHost("192.168.0.83", 10200),new HttpHost("192.168.0.150", 10200),new HttpHost("192.168.0.148", 10200)));


			.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
        public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
            return requestConfigBuilder.setConnectTimeout(5000) // 连接超时（默认为1秒）
                    .setSocketTimeout(60000);// 套接字超时（默认为30秒）
        }
    }).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
        public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
            return httpClientBuilder
                    .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());// 线程数
        }
    });


    private static RestHighLevelClient client =  new RestHighLevelClient(
            RestClient.builder(new HttpHost("192.168.0.83", 10200),new HttpHost("192.168.0.150", 10200),new HttpHost("192.168.0.148", 10200)));
*/
	/*private static RestClientBuilder restClientBuilder = RestClient.builder(new HttpHost("192.168.1.230", 10200),new HttpHost("192.168.1.50", 10200),new HttpHost("192.168.0.45", 10200))
			.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
				public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
					return requestConfigBuilder.setConnectTimeout(5000) // 连接超时（默认为1秒）
							.setSocketTimeout(60000);// 套接字超时（默认为30秒）
				}
			}).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
					return httpClientBuilder
							.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());// 线程数
				}
			});


	private static RestHighLevelClient client =  new RestHighLevelClient(
			RestClient.builder(new HttpHost("192.168.1.230", 10200),new HttpHost("192.168.1.45", 10200),new HttpHost("192.168.1.50", 10200)));*/
	/*public static void main(String[] args) {

		TestMainTxt tm = new TestMainTxt();
		// TODO Auto-generated method stub
		File f = new File("D:/javaTest/data/151/");
		File[] list = f.listFiles();
		for (int i = 0; i < list.length; i++) {
			String fileName = list[i].getName();
			if (fileName.indexOf(".") > 0) {
				test(list[i]);
			}
		}
	}*/


//	public static void test1(String dd) {
//
//		TestMainTxt tm = new TestMainTxt();
//		// TODO Auto-generated method stub
//		File folder = new File("/home/develop/database/nxxx线下宁夏");
//		File[] subFolder1 = folder.listFiles();
//		for(int k = 0 ;k < subFolder1.length;k++) {
//			File fk = subFolder1[k];
//			File[] subFolder = fk.listFiles();
//			for(int j = 0 ;j < subFolder.length;j++) {
//				File f = subFolder[j];
//				File[] list = f.listFiles();
//				for (int i = 0; i < list.length; i++) {
//					String fileName = list[i].getName();
//					System.out.println(fileName);
//					//if(fileName.indexOf("ALL")>0){
//					if (fileName.indexOf(".") > 0) {
//						Thread t = new Thread(tm.new TestThread(list[i]));
//						t.start();
//					}
//					//}
//				}
//			}
//		}
//	}

//	/**
//	 * 循环文件夹
//	 */
//	public static void main(String[] args) {
//
//	}


//	class TestThread implements  Runnable{
//		private File f ;
//		public TestThread(File f){
//			this.f = f;
//		}
//		public  void run() {
//
//			// TODO Auto-generated method stub
//			try {
//
//				BulkRequest request = new BulkRequest();
//				BufferedReader reader = new BufferedReader(new FileReader(f));
//				String line = null;
//				int count = 0;
//				int test = 0;
//				while ((line = reader.readLine()) != null) {
//					Map map  = JSONObject.parseObject(line);
//
//					if (map.containsKey("ILLEGAL_CODE") && map.get("ILLEGAL_CODE") != null) {
//						/*if (map.get("ILLEGAL_CODE").toString().startsWith("[")) {
//							map.put("ILLEGAL_CODE", map.get("ILLEGAL_CODE").toString().substring(1, map.get("ILLEGAL_CODE").toString().length()-1));
//							//} else {
//							// String[] dds = obj.get("ILLEGAL_CODE").toString().split("、");
//							//ad.put("ILLEGAL_CODE", Arrays.toString(dds));
//						}else{*/
//							map.put("ILLEGAL_CODE",map.get("ILLEGAL_CODE").toString());
//						//}
//					}else{
//						map.put("ILLEGAL_CODE","");
//					}
//					/*map.put("BRAND","");
//					map.put("REMARKS1","");
//					map.put("REMARKS2","");
//					map.put("REMARKS3","");
//					map.put("REMARKS4","");
//					map.put("REMARKS5","");
//					map.put("REMARKS6","");
//					map.put("REMARKS7","");
//					map.put("REMARKS8","");
//					map.put("REMARKS9","");
//					map.put("REMARKS10","");*/
//					//map.put("INSERT_TIME","2020-09-08 00:00:00");
//                    map.put("CUSTOMER_ID","161");
//
//
//					try {
//						request.add(new IndexRequest("product_xx_data").source(JSONObject.toJSONString(map), XContentType.JSON));
//						if (++count % 1000 == 0) {
//							BulkResponse ddd =client.bulk(request, RequestOptions.DEFAULT);
//							request = new BulkRequest();
//							System.out.println(f.getName()+":"+count);
//						}
//					}catch (Exception e) {
//						System.out.println(f.getName());
//
//						System.out.println();
//						e.printStackTrace();
//					}
//                 /*   System.out.println("测试行数：："+test);
//                    System.out.println("测试语句：："+o.toJSONString().replace("'",""));*/
//
//				}
//				client.bulk(request, RequestOptions.DEFAULT);
//				reader.close();
//
//			} catch (Exception e) {
//				System.out.println();
//				e.printStackTrace();
//			}
//		}
//	}


//	public static void main(String[] arg){
//		/*
//		123	单县市场监督管理局
//		120	国家市场监督管理总局
//		141	郑州市市场监督管理局
//		143	南阳市市场监督管理局
//		142	濮阳市市场监督管理局*/
//		int[] aa = {16076938,1383133,1319757,1232894,234602};
//		System.out.println(aa[0]);
//		int b =0;
//		int c =0;
//		while (b++<aa.length){
//			System.out.println("b::"+b);
//			c += aa[b-1];
//		}
//		System.out.println(c);
//		System.out.println("剩余："+(73880421-c));
//	}
}
