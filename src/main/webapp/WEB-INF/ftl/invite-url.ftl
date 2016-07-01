<table style="font-family:simsun;font-size:12px;margin:0 auto;padding:0;width:680px;color:#333333;" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" style=" height:64px; line-height:64px;border-bottom:4px solid #eb8226;">
				<tr>
					<td style="width:340px;"><a href="http://www1.b5m.com" target="_blank"><img src="http://www1.b5m.com:80/images/images/logo_170_53_08.png"/></a></td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td style="padding:40px 45px;">
			<p style="margin:0;line-height:26px;font-size:15px;font-weight:bold;">亲爱的用户<span style="color:#eb7e00;"></span>：</p>
			<p style="margin:0;line-height:26px;">感谢您注册帮5买！请点击下面的链接验证您的Email：</p>
			
			<p style="margin:0;line-height:26px;">
				<table style="text-align: center;">
					<tr style="font-size:14px;font-weight:bold;">
						<td width="30%">花名</td>
						<td>邀请链接</td>
					<tr>
					<#list res as inviteCode>
						<tr style="background:#efefef">
							<td>${inviteCode.name}</td>
							<td style="padding:6px 0"><a href="${inviteCode.url}" target="_blank" style="color:#eb7e00; text-decoration:underline;">${inviteCode.url}</a></td>
						</tr>
					</#list>
				</table>
			</p>
			<p style="margin:0;line-height:26px;">（若链接无法访问，请将其复制粘贴至浏览器新窗口中打开）</p>
			<p style="margin:0;line-height:26px;">轻松发现  轻松选择</p>
			<p style="margin:0;line-height:26px;">帮5买，帮你告别网购选择困难症！</p>
		</td>
	</tr>
	<tr>
		<td style="">
			<div style="background:#f4f4f4;border-top:1px solid #f8f8f8;padding:20px 0;text-align:center;color:#949494;">
				<p style="margin:0;line-height:22px;">这是一封系统自动发出的邮件，请勿回复。</p>
				<p style="margin:0;line-height:22px;">若有疑问，请发信至<a href="mailto:kefu@b5m.com" style="color:#949494;text-decoration:none;">kefu@b5m.com</a>。</p>
				<p style="margin:0;line-height:22px;">非常感谢！</p>
			</div>
		</td>
	</tr>
</table>