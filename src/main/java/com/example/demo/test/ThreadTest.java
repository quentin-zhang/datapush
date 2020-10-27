package com.example.demo.test;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadTest {

    private volatile  static boolean initFlag = false;


    public static void refresh()
    {
        log.info("刷新initFlagd的值");
        initFlag = true;
        log.info("刷新成功");
    }

    public static void loadData()
    {
        while (!initFlag){
            log.info("一直在循环");
        }
        log.info("线程:"+Thread.currentThread().getName() +"当前线程秀喊道initFlag的状态的改变");
    }

    public static  void main(String[] arg){
        Thread threadA = new Thread(()->{
            loadData();
        },"threadA");
        threadA.start();
        try {
            threadA.sleep(500);
        }catch (InterruptedException e){
            e.fillInStackTrace();
        }
        Thread threadB = new Thread(()->{
            refresh();
        },"threadB");
        threadB.start();

    }




}
