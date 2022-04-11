package claudiunicolaa.keycloak.authenticator;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.Arrays;
import java.util.List;

/**
 * @author Claudiu Nicola, https://claudiunicola.xyz, @claudiunicolaa
 */
public class TwoFactorChooseMethodAuthenticatorFactory implements AuthenticatorFactory {

	@Override
	public String getId() {
		return "2fa-choose-method-authenticator";
	}

	@Override
	public String getDisplayType() {
		return "Choose Two Factor Authentication method";
	}

	@Override
	public String getHelpText() {
		return "";
	}

	@Override
	public String getReferenceCategory() {
		return "otp";
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	@Override
	public boolean isUserSetupAllowed() {
		return false;
	}

	@Override
	public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
		return new AuthenticationExecutionModel.Requirement[] {
			AuthenticationExecutionModel.Requirement.REQUIRED,
			AuthenticationExecutionModel.Requirement.ALTERNATIVE,
			AuthenticationExecutionModel.Requirement.DISABLED,
		};
	}

	@Override
	public List<ProviderConfigProperty> getConfigProperties() {
		return Arrays.asList();
	}

	@Override
	public Authenticator create(KeycloakSession session) {
		return new TwoFactorChooseMethodAuthenticator();
	}

	@Override
	public void init(Config.Scope config) {
	}

	@Override
	public void postInit(KeycloakSessionFactory factory) {
	}

	@Override
	public void close() {
	}

}
