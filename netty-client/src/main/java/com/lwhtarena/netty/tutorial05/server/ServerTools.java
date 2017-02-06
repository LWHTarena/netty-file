package com.lwhtarena.netty.tutorial05.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import com.alibaba.fastjson.JSONObject;

import com.lwhtarena.netty.tutorial05.module.BaseMsg;
import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * 服务端 工具
 */
public class ServerTools {
	/**最小延迟*/
	private static final int MIN_TIME = 2000;
	
	/** 服务端 连接列表*/
    private static Map<String,SocketChannel> connMap=new ConcurrentHashMap<String, SocketChannel>();
  
    /**  连接匹配池  用于限制指定连接接入 */
    private static List<String> conns = new ArrayList<String>();
    
    /**数据存取队列Map*/
 	private static final Map<String, LinkedBlockingDeque<JSONObject>> queryDatas = new HashMap<String, LinkedBlockingDeque<JSONObject>>();
 	
    static{
    	conns.add("001");
    	conns.add("002");
    	conns.add("003");
    	conns.add("004");
    }
    
    /**
     * 新增/修改 一个连接
     * @param clientId
     * @param socketChannel
     */
    public static void addConnMap(String clientId,SocketChannel socketChannel){
    	if(conns.contains(clientId)){
    		connMap.put(clientId,socketChannel);
    	}
    }
    /**
     * 根据ID 返回一个连接
     * @param clientId
     * @return
     */
    public static Channel getConnMap(String clientId){
       return connMap.get(clientId);
    }
    
    /**
     * 根据ID 检查一个连接是否存在
     * @param clientId
     * @return
     */
    public static boolean checkConnMap(String clientId){
    	return connMap.containsKey(clientId);
    }
    /**
     * 向指定连接发送信息
     * @param clientId
     * @param msg
     * @return
     */
    public static boolean sendConnMsg(BaseMsg msg){
    	if(checkConnMap(msg.getClientId())){
    		SocketChannel channel = (SocketChannel) getConnMap(msg.getClientId());
    		channel.writeAndFlush(msg);
    		System.out.println(" 向["+msg.getClientId()+"]发送信息发送成功!..");
    		return true;
    	}
    	System.out.println(" 向["+msg.getClientId()+"]信息发送失败..");
    	return false;
    }
    
    /**
     * 清除一个连接
     * @param socketChannel
     */
    public static void removeConnMap(SocketChannel socketChannel){
        for (Map.Entry<String,SocketChannel> entry:connMap.entrySet()){
            if (entry.getValue()==socketChannel){
            	connMap.remove(entry.getKey());
            }
        }
    }
    
    /**
     * 新增一个查询数据队列
     * 设置超时时间小于2000 则默认为2000
     * @param queryId
     * @param time 毫秒
     */
    public static LinkedBlockingDeque<JSONObject> addQueryDeque(String queryId,int time){
    	LinkedBlockingDeque<JSONObject> deque = new LinkedBlockingDeque<JSONObject>();
    	//新增查询队列
    	queryDatas.put(queryId, deque);
    	//启动超时定时器
    	if(time<MIN_TIME){
    		time = MIN_TIME;
    	}
    	new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// 超时处理， 指定时间后 模拟添加一条数据
				appendQueryDequeData(queryId, new JSONObject());
			}
		}, time);
    	return deque;
    }
    
    /**
     * 根据查询ID 返回一个查询队列
     * @param queryId
     * @return
     */
    public static LinkedBlockingDeque<JSONObject> getQueryDeque(String queryId){
    	if(queryDatas.containsKey(queryId)){
    		return queryDatas.get(queryId);
    	}
    	return null;
    }
    
    /**
     * 根据查询ID 清除一个查询队列
     * @param queryId
     */
    public static void removeQueryDeque(String queryId){
    	if(queryDatas.containsKey(queryId)){
    		queryDatas.remove(queryId);
    	}
    }
    
    /**
     * 向指定队列添加数据
     * @param queryId
     * @param jsonData
     */
    public static void appendQueryDequeData(String queryId,JSONObject jsonData){
    	if(queryDatas.containsKey(queryId)){
    		queryDatas.get(queryId).add(jsonData);
    	}
    }

    
    public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
}
