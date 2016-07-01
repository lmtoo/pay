package com.somnus.pay.payment.thirdPay.shouxin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.somnus.pay.payment.util.PageCommonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.exception.B5mException;
import com.somnus.pay.exception.StatusCode;
import com.somnus.pay.payment.enums.PayChannel;
import com.somnus.pay.payment.enums.PaymentOrderType;
import com.somnus.pay.payment.exception.PayException;
import com.somnus.pay.payment.exception.PayExceptionCode;
import com.somnus.pay.payment.pojo.PaymentOrder;
import com.somnus.pay.payment.pojo.PaymentResult;
import com.somnus.pay.payment.thirdPay.PaymentChannelHandler;
import com.somnus.pay.payment.thirdPay.RequestParameter;
import com.somnus.pay.payment.thirdPay.RequestType;
import com.somnus.pay.payment.thirdPay.shouxin.config.ShouXinConfig;
import com.somnus.pay.payment.thirdPay.shouxin.util.RSA_MD5;
import com.somnus.pay.payment.util.HTMLUtil;
import com.somnus.pay.payment.util.MD5Util;
import com.somnus.pay.payment.util.WebUtil;
import com.somnus.pay.utils.Assert;

/**
 * @description: ${TODO}
 * Copyright 2011-2015 B5M.COM. All rights reserved
 * @author: 方东白
 * @version: 1.0
 * @createdate: 2015/12/23
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2015/12/23       bai          1.0             Why & What is modified
 */
public abstract class AbstractShouXinHandler extends PaymentChannelHandler{
	
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractShouXinHandler.class);

    protected String vMid;
    protected String vMd5KeyInfo;

    public AbstractShouXinHandler(PayChannel payChannel,String vMid,String vMd5KeyInfo) {
        this(vMid,vMd5KeyInfo,payChannel);
    }

    public AbstractShouXinHandler(String vMid,String vMd5KeyInfo,PayChannel...payChannel) {
        super(payChannel);
        this.vMid = vMid;
        this.vMd5KeyInfo = vMd5KeyInfo;
    }

    @Override
    public String handleNotify(Map<String, String> data) {
        checkSign(data);
        return ShouXinConfig.NOTIFY_NOTE_SUCCESS;
    }

    @Override
    public String handleReturn(Map<String, String> data) {
        checkSign(data);
        return getOrderIdByOID(data.get(ShouXinConfig.OUT_TRADE_NO));
    }

    /**
     * 检查通知参数中的签名是否正确
     * @param data 通知参数
     */
    protected void checkSign(Map<String, String> data) {
        Assert.isTrue(MapUtils.isNotEmpty(data), PayExceptionCode.PAYMENT_PARAM_ERROR);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("检查首信通知返回的参数中的签名是否正确,data:[{}]]", data);
        }
        boolean verifyResult = false;
        String v_md5info = data.get("v_md5info");
        String v_mac = data.get("v_mac");
        String v_md5money = data.get("v_md5money");
        String v_sign = data.get("v_sign");
        Assert.hasText(v_sign, PayExceptionCode.SIGN_VALIDATE_FAILED);
        String signSource = data.get("v_oid") + data.get("v_pstatus") + data.get("v_amount") + data.get("v_moneytype");
        try {
            boolean isNotify = this.getRequestParameter().getType() == RequestType.NOTIFY;
            boolean isReturn = !isNotify;
            String info;
            if(isNotify){
                info = data.get("v_oid") + URLEncoder.encode(data.get("v_pmode"), "utf-8") + data.get("v_pstatus") + URLEncoder.encode(data.get("v_pstring"),"utf-8")  + data.get("v_count");
                signSource += data.get("v_count") ;
			}else {
				info = data.get("v_oid") + data.get("v_pstatus") + URLEncoder.encode(data.get("v_pstring"), "utf-8") + URLEncoder.encode(data.get("v_pmode"), "utf-8");
			}
            String publicKey = ShouXinConfig.CER_PATH;   //public1024.key的路径
            String signString = v_sign;
            RSA_MD5 rsaMD5 = new RSA_MD5();
            int code = rsaMD5.PublicVerifyMD5(publicKey,signString,signSource);
            if(verifyResult = code == 0){
            	String digestString;
            	if(isReturn && StringUtils.isNotBlank(v_md5info)){
            		MD5Util infomd5 = new MD5Util("") ;
            		infomd5.hmac_Md5(info, vMd5KeyInfo) ;
            		byte b[]= infomd5.getDigest();
            		digestString = MD5Util.stringify(b);
            		if(digestString.equals(v_md5info)){
            			verifyResult = true;
            		}else{
            			verifyResult = false;
            		}
            	}
            	if(isNotify && StringUtils.isNotBlank(v_mac)){
            		MD5Util infomd5 = new MD5Util ("") ;
            		infomd5.hmac_Md5(info, vMd5KeyInfo) ;
            		byte b[]= infomd5.getDigest();
            		digestString = MD5Util.stringify(b);
            		if(digestString.equals(v_mac)){
            			verifyResult = true;
            		}else{
            			verifyResult = false;
            		}
            	}
            	if(StringUtils.isNotBlank(v_md5money)){
            		MD5Util infomd5 = new MD5Util ("") ;
            		String monery = data.get("v_amount") + data.get("v_moneytype");
            		infomd5.hmac_Md5(monery, vMd5KeyInfo) ;
            		byte b[]= infomd5.getDigest();
            		digestString = MD5Util.stringify(b);
            		if(digestString.equals(v_md5money)){
            			verifyResult = true;
            		}else{
            			verifyResult = false;
            		}
            	}
            }
        } catch (B5mException e){
            throw e;
        }  catch (Exception e) {
            throw new PayException(new StatusCode(PayExceptionCode.CHECKSIGN_ERROR.getCode(), "检查首信通知返回的参数中的签名是否正确发生错误"));
        }
        boolean isB5MQuerySkipVerify = StringUtils.isNotBlank(data.get("isB5MQuerySkipVerify"));
        if(isB5MQuerySkipVerify){
            verifyResult = isB5MQuerySkipVerify;
        }
        Assert.isTrue(verifyResult, PayExceptionCode.SIGN_VALIDATE_FAILED);
    }

    @Override
    public String getFailedResponse(RequestParameter<?, ?> requestParameter) {
        return ShouXinConfig.NOTIFY_NOTE_FAIL;
    }

    @Override
    public PaymentResult[] convert(Map<String, String> data) {
    	LOGGER.info("首信易支付转换数据格式:{}", data);
        String v_oid = data.get("v_oid");//订单编号
        String v_pmode = data.get("v_pmode");//支付方式
        String v_pstatus = data.get("v_pstatus");////支付结果 v_pstatus=1:支付成功 v_pstatus=3:支付失败
        String v_pstring = data.get("v_pstring");//支付结果信息说明
        String v_amount = data.get("v_amount");//订单金额
        String v_moneytype = data.get("v_moneytype");//币种
        //分割订单
        String[] resultoid = v_oid.split("\\|_\\|");
        String[] resultpmode = v_pmode.split("\\|_\\|");
        String[] resultstatus = v_pstatus.split("\\|_\\|");
        String[] resultpstring = v_pstring.split("\\|_\\|");
        String[] resultamount = v_amount.split("\\|_\\|");
        String[] resultmoneytype = v_moneytype.split("\\|_\\|");
        PaymentResult[] paymentResults = new PaymentResult[resultoid.length];
        for(int i = 0; i < resultoid.length ;i++){
            Map<String, String> payParamsMap = new HashMap<String, String>();
            payParamsMap.put("v_oid",resultoid[i]);
            payParamsMap.put("v_pmode",resultpmode[i]);
            payParamsMap.put("v_pstatus",resultstatus[i]);
            payParamsMap.put("v_pstring",resultpstring[i]);
            payParamsMap.put("v_amount",resultamount[i]);
            payParamsMap.put("v_moneytype",resultmoneytype[i]);
            payParamsMap.put("isB5MQuerySkipVerify", "true");
            PaymentResult paymentResult = notifyMap2PaymentResult(payParamsMap);
            paymentResults[i] = paymentResult;
        }
        return paymentResults;
    }

    public PaymentResult notifyMap2PaymentResult(Map<String, String> notifyMap) {
        // 是否是合并付款
        int isCombined = 0;
        try {
            isCombined = Integer.valueOf(notifyMap.get(ShouXinConfig.V_RCVTEL));
        } catch (Exception e) {
            isCombined = 0;
        }
        PaymentResult paymentResult = new PaymentResult();
        paymentResult.setIsCombined(isCombined);
        String returnOID = notifyMap.get(ShouXinConfig.OUT_TRADE_NO);
        String orderId = getOrderIdByOID(returnOID);
        paymentResult.setOrderId(orderId);
        paymentResult.setTradeStatus(notifyMap.get(ShouXinConfig.PAY_RESULT));
        paymentResult.setThirdTradeNo(notifyMap.get(ShouXinConfig.TRADE_NO));
        paymentResult.setPrice(Double.valueOf(notifyMap.get(ShouXinConfig.TOTAL_AMOUNT)));
        paymentResult.setPayInfo(notifyMap.get(ShouXinConfig.PAY_RESULT_INFO));
        Integer payStatus = ShouXinConfig.TRADE_SUCCESS.equals(paymentResult.getTradeStatus()) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue();
        if(this.getRequestParameter().getType() == RequestType.NOTIFY){
            payStatus = ShouXinConfig.NOTIFY_TRADE_SUCCESS.equals(paymentResult.getTradeStatus()) ? PaymentOrderType.SCCUESS.getValue() : PaymentOrderType.FAIL.getValue();
        }
        paymentResult.setStatus(payStatus);
        if(null != returnOID && null !=(returnOID.split("-")) && returnOID.split("-").length > 4){
            paymentResult.setChannel(PayChannel.valueOf(Integer.valueOf(returnOID.substring(returnOID.lastIndexOf("-")+1))));
        }
        return paymentResult;
    }

    /**
     * 统一获取支付系统的支付号方法（兼容新老规则）
     * @param returnOID
     * @return
     */
    protected String getOrderIdByOID(String returnOID){
        String orderId = StringUtils.isBlank(returnOID)?"":returnOID.substring(ShouXinConfig.ORDER_PREFIX_LENGTH);
        if(orderId.indexOf("-") > 0){
            orderId = orderId.substring(0,orderId.indexOf("-"));
        }
        return orderId;
    }

    @Override
	public String handleOrder(RequestParameter<PaymentOrder, String> parameter) {
        LOGGER.info("[{}]的参数[{}]", parameter.getChannel(), parameter.getData());
        PaymentOrder payOrder = parameter.getData();
        HashMap<String,String> requestMap = new HashMap<String,String> ();
        String v_mid = this.vMid;  //商户编号，签约由易支付分配。444是测试商户编号。
        Assert.notNull(payOrder.getCreateTime(), PayExceptionCode.CREATE_TIME_IS_NULL);
        String ddate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()); //支付日期作为流水号
        String ddate1= new SimpleDateFormat("yyyyMMdd").format(payOrder.getCreateTime()); //订单首次创建日期
        //订单编号，订单编号的格式:yyyymmdd(订单创建日期)-v_mid-流水号(当前支付时间)-支付号-虚拟支付渠道(3位)。流水号可以按照自己的订单规则生成，但是要保证每一次提交，订单号是唯一值，否则会出错
        String v_oid = ddate1+"-"+v_mid+"-"+ddate+"-"+payOrder.getOrderId()+"-"+parameter.getChannel().getValue();
        String v_amount = payOrder.getAmount()+""; //订单金额,小数点后保留2位 例如12.34
        String v_ymd = ddate1;        //订单日期,订单提交的日期，可获取系统时间，格式为：yyyymmdd
        String v_orderstatus = ShouXinConfig.V_ORDERSTATUS_1;	//配货状态，0-未配齐，1-已配齐。
        String v_moneytype = ShouXinConfig.V_MONEYTYPE_RMB;  //币种。0为人民币
        String v_url = PageCommonUtil.getRootPath(WebUtil.getRequest(), true) + "/callback/" + parameter.getChannel().name() + "/return.htm";
        String MD5Key= this.vMd5KeyInfo; //MD5Key,签约后由商户自定义一个16位的数字字母组合作为密钥，区分大小写，发到kf@payeasenet.com.说明商户编号，公司名称和密钥。
        String digestString = "";
        String v_idtype = ShouXinConfig.V_IDTYPE;
        String v_idnumber = ShouXinConfig.V_IDNUMBER;
        String v_idcountry = ShouXinConfig.V_IDCOUNTRY;
        String v_idname = ""; //身份证姓名
        String v_ordername = ""; //订货人姓名，建议用商户编号代替
        String v_rcvname= ""; //收货人姓名，建议用商户编号代替
        String v_rcvaddr = ""; //收货人地址，建议用商户编号代替
        String v_rcvtel = payOrder.getIsCombined()?"1":"0";  	//收货人电话，建议用商户编号代替 (做临时扩展字段)
        String v_rcvpost = "";		//收货人邮编，建议用商户编号代替

        //加签
        try{
            MD5Util md5 = new MD5Util("") ;
            md5.hmac_Md5(v_moneytype+v_ymd+v_amount+v_rcvname+v_oid+v_mid+v_url,MD5Key) ;
            byte b[]= md5.getDigest();
            digestString = MD5Util.stringify(b);
        }catch (Exception e){
            LOGGER.error("ShouXinPay sign error", e);
        }

        requestMap.put("v_mid", v_mid);
        requestMap.put("v_oid",v_oid);
        requestMap.put("v_rcvname",v_rcvname);
        requestMap.put("v_rcvaddr",v_rcvaddr);
        requestMap.put("v_rcvtel",v_rcvtel);
        requestMap.put("v_rcvpost",v_rcvpost);
        requestMap.put("v_amount",v_amount);
        requestMap.put("v_ymd",ddate1);
        requestMap.put("v_orderstatus",v_orderstatus);
        requestMap.put("v_ordername",v_ordername);
        requestMap.put("v_moneytype",v_moneytype);
        requestMap.put("v_md5info",digestString);
        requestMap.put("v_url",v_url);
        requestMap.put("v_producttype",ShouXinConfig.V_PRODUCTTYPE);
        requestMap.put("v_idtype",v_idtype);
        requestMap.put("v_idnumber",v_idnumber);
        requestMap.put("v_idname",v_idname);
        requestMap.put("v_idcountry",v_idcountry);
        requestMap.put("v_idaddress",v_rcvaddr);
        requestMap.put("v_userref", parameter.getData().getUserId());

        String requestURL = ShouXinConfig.V_REQUEST_COMMON_ADDR;
        if(StringUtils.isNotBlank(parameter.getData().getDefaultBank()) && !ShouXinConfig.SX_PAY_KEY.equals(parameter.getData().getDefaultBank())){
            requestMap.put("v_pmode",parameter.getData().getDefaultBank()); //网银支付时使用
            requestURL = ShouXinConfig.V_REQUEST_BANK_ADDR;
        }
        return HTMLUtil.createSubmitHtml(requestURL, requestMap, null, null);
	}
    
}
