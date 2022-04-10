<#import "template.ftl" as layout>
<#if section = "header">
		${msg("multipleEnrollmentAuthTitle",realm.displayName)}
	<#elseif section = "form">
		<form id="kc-mutiple-enrollment-two-factor-auth-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
			<div class="${properties.kcFormGroupClass!}">
				<div class="${properties.kcLabelWrapperClass!}">
					<label for="method-sms" class="${properties.kcLabelClass!}">SMS</label>
				</div>
				<div class="${properties.kcInputWrapperClass!}">
					<input type="radio" id="method-sms" name="method" class="${properties.kcInputClass!}"/>
				</div>

				<div class="${properties.kcLabelWrapperClass!}">
					<label for="method-app" class="${properties.kcLabelClass!}">Authenticator App (Google Authenticator, Free Authenticator)</label>
				</div>
				<div class="${properties.kcInputWrapperClass!}">
					<input type="radio" id="method-app" name="method" class="${properties.kcInputClass!}"/>
				</div>
			</div>
			<div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
				<div id="kc-form-options" class="${properties.kcFormOptionsClass!}">
					<div class="${properties.kcFormOptionsWrapperClass!}">
						<span><a href="${url.loginUrl}">${kcSanitize(msg("backToLogin"))?no_esc}</a></span>
					</div>
				</div>

				<div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
					<input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
				</div>
			</div>
		</form>
	<#elseif section = "info" >
		${msg("multipleEnrollmentAuthInstruction")}
	</#if>
