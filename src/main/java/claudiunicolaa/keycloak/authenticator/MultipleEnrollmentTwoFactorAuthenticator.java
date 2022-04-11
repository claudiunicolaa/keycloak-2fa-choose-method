package claudiunicolaa.keycloak.authenticator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;


//@JsonIgnoreProperties(value = { "required" })
class TwoFactorAuthAttribute {
	String type;
	Boolean required;

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Check if the two factor enrollment is needed.
	 *
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
}

/**
 * @author Claudiu Nicola, https://claudiunicola.xyz, @claudiunicolaa
 */
public class MultipleEnrollmentTwoFactorAuthenticator implements Authenticator {

	private static final String TPL_CODE = "multiple-enrollment.ftl";

	private static final List<String> AVAILABLE_METHODS = Arrays.asList("sms", "app");

	@Override
	public void authenticate(AuthenticationFlowContext context) {
//		KeycloakSession session = context.getSession();
		UserModel user = context.getUser();
		String twoFactorAuthAttrRaw =  user.getFirstAttribute("two_factor_auth");
		try {
			TwoFactorAuthAttribute userAttribute = TwoFactorAuthAttribute.FromRawJSON(twoFactorAuthAttrRaw);
			// the two-factor authentication multiple enrollment form needs to be shown if the user has not enrolled yet
			if (userAttribute.enrollmentNeedsToBeMade()) {
				context.challenge(context.form().setAttribute("realm", context.getRealm()).createForm(TPL_CODE));
				return;
			}

			// skip the authenticator if above conditions are not met
			context.success();
		} catch (Exception e) {
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
				context.form().setError("error", e.getMessage())
					.createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
		}
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		String chosenMethod = context.getHttpRequest().getDecodedFormParameters().getFirst("method");
		if (chosenMethod == null) {
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
				context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
			return;
		}

		if (!AVAILABLE_METHODS.contains(chosenMethod)) {
			context.failureChallenge(AuthenticationFlowError.INTERNAL_ERROR,
				context.form().createErrorPage(Response.Status.INTERNAL_SERVER_ERROR));
			return;
		}

		// @todo: save the chosen method in the user's attributes
		UserModel user = context.getUser();
		user.setAttribute("me", List.of(chosenMethod));

		context.success();
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
