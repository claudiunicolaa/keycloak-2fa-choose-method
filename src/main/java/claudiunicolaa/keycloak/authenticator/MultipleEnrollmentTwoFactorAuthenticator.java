package claudiunicolaa.keycloak.authenticator;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.*;


/**
 * @author Claudiu Nicola, https://claudiunicola.xyz, @claudiunicolaa
 */
public class MultipleEnrollmentTwoFactorAuthenticator implements Authenticator {

	private static final String TPL_CODE = "multiple-enrollment.ftl";

	@Override
	public void authenticate(AuthenticationFlowContext context) {
		// @todo
	}

	@Override
	public void action(AuthenticationFlowContext context) {
		// @todo
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
