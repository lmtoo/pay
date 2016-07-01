package com.somnus.pay.payment.thirdPay.alipay.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.somnus.pay.payment.thirdPay.alipay.config.AlipayConfig;
import com.somnus.pay.payment.thirdPay.alipay.sign.MD5;
import com.somnus.pay.payment.thirdPay.alipay.sign.RSA;
import com.somnus.pay.payment.util.Constants;


/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {

	private final static Logger LOGGER = LoggerFactory.getLogger(AlipayNotify.class);
	
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "http://notify.alipay.com/trade/notify_query.do?";
    private static final String HTTPS_VERIFY_NEW_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params) {

        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
    	String responseTxt = "true";
    	String oper = params.get("oper");
		if(params.get("notify_id") != null && params.get("payFrom") == null && (StringUtils.isBlank(oper) || !oper.equals("refund"))) {
			String notify_id = params.get("notify_id");
			responseTxt = verifyResponse(notify_id,AlipayConfig.partner,HTTPS_VERIFY_URL);
		}
		if(params.get("payFrom") !=null && params.get("payFrom").equals(Constants.PAY_FROM_APP_B5M)){
			params.remove("payFrom");
		}
	    String sign = "";
		if (params.get("sign") != null) {
			params.remove("oper");
			sign = params.get("sign");
		}
	    boolean isSign = getSignVeryfy(params, sign, AlipayConfig.key, AlipayConfig.sign_type);

        //写日志记录（若要调试，请取消下面两行注释）
//        String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
//	    AlipayCore.logResult(sWord);

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @param key 商户支付宝公钥key
     * @param signType 签名类型
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> params, String sign, String key, String signType) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = AlipayCore.paraFilter(params);
        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        if(AlipayConfig.sign_type.equals(signType)){
        	isSign = MD5.verify(preSignStr, sign, key, AlipayConfig.input_charset);
        }else if(AlipayConfig.SIGN_TYPE_RSA.equals(signType)){
            isSign = RSA.verify(preSignStr, sign, key, AlipayConfig.input_charset);
        }
        return isSign;
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     * @param notify_id 通知校验ID
     * @param partnerId 商户号
     * @param url 查询地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private static String verifyResponse(String notify_id,String partnerId,String url) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfy_url = url + "partner=" + partnerId + "&notify_id=" + notify_id;
        LOGGER.info("支付宝验证返回URL:[{}]", veryfy_url);
        return checkUrl(veryfy_url);
    }

    /**
    * 获取远程服务器ATN结果
    * @param urlvalue 指定URL路径地址
    * @return 服务器ATN结果
    * 验证结果集：
    * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 
    * true 返回正确信息
    * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
    */
    private static String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }
        return inputLine;
    }

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @param partnerId 商户号
     * @param alipayPublicKey 商户支付宝公钥key
     * @return
     */
    public static boolean verifyRSA(Map<String, String> params,String partnerId,String alipayPublicKey) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = AlipayConfig.VERIFY_RESPONSE_TRUE;
        if(params.get(AlipayConfig.NOTIFY_ID) != null) {
            String notify_id = params.get(AlipayConfig.NOTIFY_ID);
            responseTxt = verifyResponse(notify_id,partnerId,HTTPS_VERIFY_NEW_URL);
        }
        String sign = "";
        if(params.get(AlipayConfig.SIGN_KEY) != null) {
            sign = params.get(AlipayConfig.SIGN_KEY);
        }
        boolean isSign = getSignVeryfy(params, sign, alipayPublicKey, AlipayConfig.SIGN_TYPE_RSA);
        if (isSign && responseTxt.equals(AlipayConfig.VERIFY_RESPONSE_TRUE)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证消息是否是支付宝发出的合法消息
     * @param params 通知返回来的参数数组
     * @param partnerId 商户号
     * @param alipayPublicKey 商户支付宝公钥key
     * @return
     */
    public static boolean verifyMD5(Map<String, String> params,String partnerId,String alipayPublicKey) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = AlipayConfig.VERIFY_RESPONSE_TRUE;
        if(params.get(AlipayConfig.NOTIFY_ID) != null) {
            String notify_id = params.get(AlipayConfig.NOTIFY_ID);
            responseTxt = verifyResponse(notify_id,partnerId,HTTPS_VERIFY_NEW_URL);
        }
        String sign = "";
        if(params.get(AlipayConfig.SIGN_KEY) != null) {
            sign = params.get(AlipayConfig.SIGN_KEY);
        }
        boolean isSign = getSignVeryfy(params, sign, alipayPublicKey, AlipayConfig.SIGN_TYPE_MD5);
        if (isSign && responseTxt.equals(AlipayConfig.VERIFY_RESPONSE_TRUE)) {
            return true;
        } else {
            return false;
        }
    }

}
