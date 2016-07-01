package com.somnus.pay.payment.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * <pre>
 * 处理结果信息
 * </pre>
 *
 * @author masanbao
 * @version $ Msg.java, v 0.1 2015年1月28日 下午4:47:56 masanbao Exp $
 * @since   JDK1.6
 */
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    private int               code             = -1; // 返回状态码
    private boolean           ok;                   // 返回状态
    private Object            data;                 // 返回信息对象

    public Msg() {
        super();
    }

    public Msg(boolean ok, Object data) {
        super();
        this.ok = ok;
        this.data = data;
    }

    public Msg(int code, boolean ok, Object data) {
        super();
        this.code = code;
        this.ok = ok;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Msg [code=" + code + ", ok=" + ok + ",  data=" + data + "]";
    }


    public String toJSONString() {
        return JSONObject.toJSONString(this);
    }
}
