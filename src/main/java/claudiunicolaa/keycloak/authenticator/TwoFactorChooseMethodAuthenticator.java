package claudiunicolaa.keycloak.authenticator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


class TwoFactorAuthAttribute {
	String type;
	Boolean required;

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isAppType() {
		return type.equals("app");
	}

	/**
	 * Check if the two factor enrollment is needed.
	 * @return true if required=true and type is blank
	 */
	public Boolean enrollmentNeedsToBeMade() {
		return required && type.isBlank();
	}

	public static TwoFactorAuthAttribute FromRawJSON(String rawJson) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(
			rawJson,
			TwoFactorAuthAttribute.class
		);
	}

	@Override
	public String toString() {
		return String.format("{\"required\":%b,\"type\":\"%s\"}", required, type);
	}
}

/**
 * @author Claudiu Nicola, https://claudiunicola.xyz, @claudiunicolaa
 */
public class TwoFactorChooseMethodAuthenticator implements Authenticator {

	private static final String TPL_CODE = "two-factor-choose-method.ftl";

	private static final String USER_ATTRIBUTE_NAME = "two_factor_auth";

	private static final List<String> AVAILABLE_METHODS = Arrays.asList("sms", "app");

	private static final Logger LOG = Logger.getLogger(String.valueOf(TwoFactorChooseMethodAuthenticator.class));

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		UserModel user = context.getUser();
		String twoFactorAuthAttrRaw =  user.getFirstAttribute(USER_ATTRIBUTE_NAME);
		// skip the authenticator if the user attribute is not present
		if (twoFactorAuthAttrRaw == null) {
			context.success();
			return;
		}
		try {
			TwoFactorAuthAttribute userAttribute = TwoFactorAuthAttribute.FromRawJSON(twoFactorAuthAttrRaw);
			if (userAttribute.enrollmentNeedsToBeMade()) {
				context.challenge(context.form().setAttribute("realm", context.getRealm()).createForm(TPL_CODE));
				return;
			}

			// skip the authenticator if above conditions are not met
			context.success();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		String chosenMethod = context.getHttpRequest().getDecodedFormParameters().getFirst("method");
		if (chosenMethod == null) {
			LOG.error("The chosen method is equal with null. It should be a string value.");
			context.failureChallenge(
				AuthenticationFlowError.INTERNAL_ERROR,
				context.form()
					.setAttribute("realm", context.getRealm())
					.setError("twoFactorChooseMethodAuthMethodInvalid")
					.createErrorPage(Response.Status.INTERNAL_SERVER_ERROR)
			);
			return;
		}

		if (!AVAILABLE_METHODS.contains(chosenMethod)) {
			LOG.error(String.format("Chosen method: %s. Available methods: %s", chosenMethod, AVAILABLE_METHODS));
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
				context.form()
					.setAttribute("realm", context.getRealm())
					.setError("twoFactorChooseMethodAuthMethodInvalid")
					.createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
			return;
		}

		UserModel user = context.getUser();
		String twoFactorAuthAttrRaw =  user.getFirstAttribute(USER_ATTRIBUTE_NAME);
		try {
			TwoFactorAuthAttribute userAttribute = TwoFactorAuthAttribute.FromRawJSON(twoFactorAuthAttrRaw);
			userAttribute.setType(chosenMethod);
			user.setAttribute(USER_ATTRIBUTE_NAME, Collections.singletonList(userAttribute.toString()));
			// set CONFIGURE OTP as required user action if the chosen method is app
			if (userAttribute.isAppType()) {
				user.addRequiredAction(UserModel.RequiredAction.CONFIGURE_TOTP);
			}
			context.success();
			LOG.info(String.format(
				"User id=%s has enrolled in the two-factor authentication with method=%s",
				user.getId(),
				chosenMethod
			));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean requiresUser() {
		return true;
	}

	@Override
	public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
		return true;
	}

	@Override
	public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
	}

	@Override
	public void close() {
	}
}
