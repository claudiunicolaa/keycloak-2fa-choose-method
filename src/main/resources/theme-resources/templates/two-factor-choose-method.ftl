<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=true; section>
	<#if section = "header">
			${msg("twoFactorChooseMethodAuthTitle",realm.displayName)}
		<#elseif section = "show-username">
			<div class="${properties.kcFormGroupClass!}">
				<div id="kc-username">
					<label id="kc-attempted-username">${msg("twoFactorChooseMethodAuthTitle")}</label>
					<a id="reset-login" href="${url.loginRestartFlowUrl}">
						<div class="kc-login-tooltip">
							<i class="${properties.kcResetFlowIcon!}"></i>
							<span class="kc-tooltip-text">${msg("restartLoginTooltip")}</span>
						</div>
					</a>
				</div>
			</div>
		<#elseif section = "info" >
			${msg("twoFactorChooseMethodAuthInstruction")}
		<#elseif section = "form">
			<form id="kc-two-factor-choose-method-auth-form" class="${properties.kcFormClass!}" action="${url.loginAction}" method="post">
				<div class="${properties.kcFormGroupClass!}">
				<div class="${properties.kcLabelWrapperClass!}">
						<label for="app" class="${properties.kcFormHeaderClass!}">Authenticator App</label>
						${msg("twoFactorChooseMethodAuthAppSummary")}
						<div class="${properties.kcTooltipClass!}">
							<i class="${properties.kcTooltipIcon!}"></i>
							<span class="kc-tooltip-text">${msg("twoFactorChooseMethodAuthAppDetails")}</span>
						</div>
					<div class="${properties.kcInputWrapperClass!}">
						<input type="radio" id="app" name="method" value="app" class="${properties.kcInputClass!}"/>
					</div>
				</div>

					<div class="${properties.kcLabelWrapperClass!}">
						<label for="sms" class="${properties.kcFormHeaderClass!}">SMS</label>
						${msg("twoFactorChooseMethodAuthSmsSummary")}
						<div class="${properties.kcTooltipClass!}">
							<i class="${properties.kcTooltipIcon!}"></i>
							<span class="kc-tooltip-text">${msg("twoFactorChooseMethodAuthAppDetails")}</span>
						</div>
					</div>
					<div class="${properties.kcInputWrapperClass!}">
						<input type="radio" id="sms" name="method" value="sms" class="${properties.kcInputClass!}"/>
					</div>	
				<div class="${properties.kcFormGroupClass!} ${properties.kcFormSettingClass!}">
					<div id="kc-form-buttons" class="${properties.kcFormButtonsClass!}">
						<input class="${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!} ${properties.kcButtonBlockClass!} ${properties.kcButtonLargeClass!}" type="submit" value="${msg("doSubmit")}"/>
					</div>
				</div>
			</form>
	</#if>
</@layout.registrationLayout>
