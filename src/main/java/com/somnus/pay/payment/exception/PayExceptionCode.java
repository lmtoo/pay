package com.somnus.pay.payment.exception;

import com.somnus.pay.exception.StatusCode;


/**
 * 支付系统异常状态码
 * <pre>
 *
 2000=成功
 40000000=客户端错误
 40000001=参数错误
 40000002=请求超时
 40000003=未授权
 40000004=重复提交
 50000000=服务端错误
 50000001=服务忙，请稍后重试
 50000002=请求处理失败
 50080001=支付侧DB配置操作出错
 50080002=补通知操作出错
 50080003=超级帮钻抵扣失败(无响应)
 50080004=请求帮钻结算系统失败
 50080005=超级帮钻抵扣失败
 50080006=发现多条可疑支付记录
 50080007=支付过程已完成，无需重复支付
 50080008=支付过程已失败，禁止重新支付
 50080009=前后两次超级帮钻抵现额不一致
 50080010=前后两次支付金额不一致
 50080011=支付失败
 50080012=签名为空
 50080013=签名长度超出限制
 50080014=验签失败,请返回到我的订单重新付款
 50080015=计算签名失败
 50080016=订单格式校验失败
 50080017=该订单已经正在支付中
 50080018=支付类型不可为空
 50080019=支付密码校验失败
 50080020=支付密码错误
 50080021=不支持合并支付

 50081001=密码错误次数过多，账户被锁定，一小时后重试
 50081002=支付密码不正确
 50081003=您还没有设置支付密码
 50081004=您已设置过支付密码
 50081005=修改支付密码失败
 50081006=重置密码失败
 50081007=重置免密额度失败
 50081008=密码错误次数过多，账户被锁定，一小时后重试
 50081009=密码错误次数过多，账户被锁定
 50081010=加载配置
 50081011=设置超级帮钻支付密码失败
 50081012=发送设置超级帮钻支付密码短信失败
 50081013=检查是否设置过帮钻支付密码出错

 50081014=支付密码不能为空
 50081015=新支付密码和旧支付密码不能相同

 50081016=密码错误次数过多，账户被锁定，一小时后重试

 50081017=密码错误次数过多，账户被锁定，n分钟后重试

 50082001=帮钻支付额度超过预期限额
 50082002=支付侧DB配置操作出错
 50082003=清空支付信息缓存操作出错
 50082004=支付信息超时，请稍后再试

 50081010=不支持的支付回调类型
 50081011=不支持的支付渠道
 50081012=支付回调参数为空
 50081013=支付渠道为空
 50081014=支付回调类型为空
 50081015=支付回调请求处理失败
 50081016=解析微信支付回调参数(XML)失败

 50080003001=检查首信通知返回的参数中的签名是否正确发生错误

 50080001053=订单创建时间不能为空
 50080003003=订单数据为空

 * </pre>
 * 
 * @author 丹青生
 *
 * @date 2015-8-25
 */
public class PayExceptionCode {
	
	public static final StatusCode SERVER_BUSY = new StatusCode("50080000000");
	public static final StatusCode PAYMENT_CONFIG_OPER_ERROR = new StatusCode("50080000001");
	public static final StatusCode PAYMENT_REPAIR_NOTIFICATION_ERROR = new StatusCode("50080000002");
	public static final StatusCode SUPER_BEAN_TRADE_NO_RESPONSE = new StatusCode("50080000003");
	public static final StatusCode REQUEST_BPS_SERVICE_ERROR = new StatusCode("50080000004");
	public static final StatusCode SUPER_BEAN_TRADE_FAILED = new StatusCode("50080000005");
	public static final StatusCode MULTI_PAYMENT_ORDER = new StatusCode("50080000006");
	public static final String REPEAT_PAY_SUCCESS_ORDER_CODE = "50080000007";
	public static final StatusCode REPEAT_PAY_SUCCESS_ORDER = new StatusCode(REPEAT_PAY_SUCCESS_ORDER_CODE);
	public static final String FORBIDDEN_PAY_ERROR_ORDER_CODE = "50080000008";
	public static final StatusCode FORBIDDEN_PAY_ERROR_ORDER = new StatusCode(FORBIDDEN_PAY_ERROR_ORDER_CODE);
	public static final StatusCode SUPER_BEAN_AMOUNT_DIFFERENCE = new StatusCode("50080000009");
	public static final StatusCode ORDER_AMOUNT_DIFFERENCE = new StatusCode("50080000010");
	public static final StatusCode PAY_FAILED = new StatusCode("50080000011");
	public static final StatusCode SIGN_IS_BLANK = new StatusCode("50080000012");
	public static final StatusCode SIGN_TOO_LONG = new StatusCode("50080000013");
	public static final StatusCode SIGN_VALIDATE_FAILED = new StatusCode("50080000014");
	public static final StatusCode SIGN_MD5_FAILED = new StatusCode("50080000015");
	public static final StatusCode ORDER_VALIDATE_FAILED = new StatusCode("50080000016");
	public static final StatusCode ORDER_IS_PAYING = new StatusCode("50080000017");
	public static final StatusCode THIRD_PAY_TYPE_NULL = new StatusCode("50080000018");
	public static final StatusCode PAY_PASSWORD_VALIDATE_FAILED = new StatusCode("50080000019");
	public static final StatusCode PAY_PASSWORD_ERROR = new StatusCode("50080000020");
	public static final StatusCode NOT_SUPPORT_COMBINED_PAY = new StatusCode("50080000021");
	public static final StatusCode BOCOMB2C_INIT_ERROR = new StatusCode("50080000022");
	public static final StatusCode STANDBY_PAYMENT_ERROR = new StatusCode("50080000023");
	public static final StatusCode STANDBY_ORDERID_ERROR = new StatusCode("50080000024");
	public static final StatusCode COMBINE_LOSE_PAYMENT = new StatusCode("50080000025");
	public static final StatusCode STANDBY_USERID_ERROR = new StatusCode("50080000026");
	public static final StatusCode STANDBY_SUBJECT_ERROR = new StatusCode("50080000027");
	public static final StatusCode STANDBY_AMOUNT_ERROR = new StatusCode("50080000028");
	public static final StatusCode PAYMENT_SOURCE_IS_NULL = new StatusCode("50080000029");
	public static final StatusCode PAYMENT_SOURCE_IS_INVALID = new StatusCode("50080000030");
	public static final StatusCode PAYMENT_PARAM_ERROR = new StatusCode("50080000031");
	public static final StatusCode PAYPASSWORD_OVER_LIMIT_ERROR = new StatusCode("50080001001");
	public static final StatusCode PAYPASSWORD_ERROR = new StatusCode("50080001002");
	public static final StatusCode PAYPASSWORD_NOT_SET = new StatusCode("50080001003");
	public static final StatusCode PAYPASSWORD_ALREADY_SET = new StatusCode("50080001004");
	public static final StatusCode UPDATE_PAYPASSWORD_ERROR = new StatusCode("50080001005");
	public static final StatusCode RESET_PAYPASSWORD_ERROR = new StatusCode("50080001006");
	public static final StatusCode RESET_FREEQUOTA_PAYPASSWORD_ERROR = new StatusCode("50080001007");
	public static final StatusCode PAYPASSWORD_ERROR_TOMANY = new StatusCode("50080001008");
	public static final StatusCode PAYPASSWORD_ERROR_TOMANY_LOCK = new StatusCode("50080001009");
	public static final StatusCode PAYPASSWORD_SETTING_ERROR = new StatusCode("50080001011");
	public static final StatusCode PAYPASSWORD_SENDSMS_ERROR = new StatusCode("50080001012");
	public static final StatusCode PAYPASSWORD_CHECK_ERROR = new StatusCode("50080001013");
	public static final StatusCode PAYPASSWORD_CANT_BE_NULL = new StatusCode("50080001014");
	public static final StatusCode NEW_AND_OLD_PAYPASSWORD_CANT_BE_SAME = new StatusCode("50080001015");
	public static final StatusCode PASSWORD_ERROR_ONE_HOUR = new StatusCode("50080001016");
	public static final String PASSWORD_ERROR_A_FEW_MINUTES_CODE = "50080001017";
	public static final StatusCode PASSWORD_ERROR_A_FEW_MINUTES = new StatusCode(PASSWORD_ERROR_A_FEW_MINUTES_CODE);
	public static final StatusCode UNSUPPORTED_REQUEST_TYPE = new StatusCode("50080001018");
	public static final StatusCode UNSUPPORTED_PAY_CHANNEL = new StatusCode("50080001019");
	public static final StatusCode PAYMENT_PARAMETER_IS_NULL = new StatusCode("50080001020");
	public static final StatusCode PAY_CHANNEL_IS_NULL = new StatusCode("50080001021");
	public static final StatusCode REQUEST_TYPE_IS_NULL = new StatusCode("50080001022");
	public static final StatusCode CALLBACK_FAIL = new StatusCode("50080001023");
	public static final StatusCode CALLBACK_WX_PARAMETER_PARSE_ERROR = new StatusCode("50080001024");
	public static final StatusCode USER_ID_ERROR = new StatusCode("50080001025");
	public static final StatusCode ASYNC_TASK_NAME_ERROR = new StatusCode("50080001026");
	public static final StatusCode EVENT_PAYMENT_ORDER_ERROR = new StatusCode("50080001027");
	public static final StatusCode SIGN_ERROR = new StatusCode("50080001028");
	public static final StatusCode CHECKSIGN_ERROR = new StatusCode("50080003001");
	public static final StatusCode ALI_CALLBACK_PARAMETER_CONVERT_ERROR = new StatusCode("50080001029");
	public static final StatusCode ALI_ORDER_QUERY_ERROR = new StatusCode("50080001030");
	public static final StatusCode BOCOM_CALLBACK_PARAMETER_IS_NULL = new StatusCode("50080001031");
	public static final StatusCode PAYMENT_HANDLER_NOT_FOUND = new StatusCode("50080001032");
	public static final StatusCode CALLBACK_PROCESS_ERROR = new StatusCode("50080001033");
	public static final StatusCode CUSTOMS_DECLARATION_ERROR = new StatusCode("50080003002");
	public static final StatusCode CALLBACK_ID_PARAMETER_IS_NULL = new StatusCode("50080001034");
	public static final StatusCode CALLBACK_UPDATE_STATUS_FAILED = new StatusCode("50080001035");
	public static final StatusCode DAO_QUERY_RESULT_IS_NOT_EXPECTED = new StatusCode("50080001036");
	public static final StatusCode EXPECTED_RECORD_IS_NOT_EXIST = new StatusCode("50080001037");
	public static final StatusCode QUERY_PARAMETER_IS_NULL = new StatusCode("50080001038");
	public static final StatusCode CALLBACK_RECORD_NOT_EXIST = new StatusCode("50080001039");
	public static final StatusCode UNSUPPORTED_OPERATION = new StatusCode("50080001040");
	public static final StatusCode QUERY_ORDER_PAY_RESULT_ERROR = new StatusCode("50080001041");
	public static final StatusCode CONFIRM_ORDER_PAY_RESULT_ERROR = new StatusCode("50080001042");
	public static final StatusCode ORDER_PAYMENT_SOURCE_NOT_FOUND = new StatusCode("50080001043");
	public static final StatusCode NOTIFY_CLIENT_RECORD_NOT_FOUND = new StatusCode("50080001044");
	public static final StatusCode NOTIFY_CLIENT_RECORD_REPEAT = new StatusCode("50080001045");
	public static final StatusCode NOTIFY_ID_PARAMETER_IS_NULL = new StatusCode("50080001046");
	public static final StatusCode PAY_RESULT_IS_NOT_SUCCESS = new StatusCode("50080001047");
	public static final StatusCode UNSUPPORTED_NOTIFY_TYPE = new StatusCode("50080001048");
	public static final StatusCode NOTIFY_RECORD_NOT_EXIST = new StatusCode("50080001049");
	public static final StatusCode PAYMENT_ORDER_RECORD_NOT_EXIST = new StatusCode("50080001050");
	public static final StatusCode SWITCH_RECORD_NOT_EXIST = new StatusCode("50080001051");
	public static final StatusCode PAYMENT_CHANNEL_SERVICE_IS_UNABLE = new StatusCode("50080001052");
	public static final StatusCode CREATE_TIME_IS_NULL = new StatusCode("50080001053");
	public static final StatusCode BZ_LIMIT_ERROR = new StatusCode("50080002001");
	public static final StatusCode ERROR_PAYMENT_CONFIG_OPER = new StatusCode("50080002002");
	public static final StatusCode ERROR_CANCEL_PAYMENT_OPER= new StatusCode("50080002003");
	public static final StatusCode ERRRO_PAYMENT_INFO= new StatusCode("50080002004");
	public static final StatusCode PAYMENT_RESULT_ERROR = new StatusCode("50080001054");
	public static final StatusCode ORDER_IS_NULL = new StatusCode("50080003003");
	public static final StatusCode BG_CONFIG_IS_NULL = new StatusCode("50080003004");
	public static final StatusCode SEND_NOTIFY_ERROR = new StatusCode("50080002005");
	public static final StatusCode METAQ_SERVICE_ERROR = new StatusCode("50080002006");
	public static final StatusCode HTTP_NOTIFY_ERROR = new StatusCode("50080002007");
	public static final StatusCode EVENT_LISTENER_HANDLE_ERROR = new StatusCode("50080001055");
	public static final StatusCode THIRD_TRADE_NO_IS_NULL = new StatusCode("50080001056");
	public static final StatusCode THIRD_PAY_TYPE_ERROR = new StatusCode("50080001057");
	public static final StatusCode THIRD_PAY_ERROR = new StatusCode("50080002008");
	public static final StatusCode CALLBACK_PARAMETER_CONVERT_ERROR = new StatusCode("50080002009");
	public static final StatusCode PAYMENT_SOURCE_OPER_ERROR = new StatusCode("50080002010");
}
