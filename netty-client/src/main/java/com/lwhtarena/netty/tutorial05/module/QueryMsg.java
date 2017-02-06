package com.lwhtarena.netty.tutorial05.module;

import com.alibaba.fastjson.JSONObject;
import com.lwhtarena.netty.tutorial05.module.enums.MsgType;
import com.lwhtarena.netty.tutorial05.module.enums.QueryType;
import com.lwhtarena.netty.tutorial05.server.ServerTools;

public class QueryMsg extends BaseMsg{
	
	private static final long serialVersionUID = -4161243660761537875L;
	//查询ID
	private String queryId;
	/** 设备Id */
	private String equId;
	/** 查询类型 */
	private QueryType queryType;
	/** 查询参数 */
	private String params;
	/** 查询结果 */
	private JSONObject resultData;
	/**
	 * 查询指定信息
	 * @param clientId 客户端ID
	 * @param equId 查询ID等
	 * @param queryType 查询类型
	 */
	public QueryMsg(String clientId, String equId, QueryType queryType, String params) {
		super(clientId);
		this.setType(MsgType.QUERY);
		this.queryId = ServerTools.get32UUID();
		this.equId = equId;
		this.queryType = queryType;
		this.params = params;
	}
	

	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getEquId() {
		return equId;
	}

	public void setEquId(String equId) {
		this.equId = equId;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public JSONObject getResultData() {
		return resultData;
	}

	public void setResultData(JSONObject resultData) {
		this.resultData = resultData;
	}
	public QueryType getQueryType() {
		return queryType;
	}
	public void setQueryType(QueryType queryType) {
		this.queryType = queryType;
	}
	
	
}
