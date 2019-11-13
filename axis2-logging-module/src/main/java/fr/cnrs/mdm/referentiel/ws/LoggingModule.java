package fr.cnrs.mdm.referentiel.ws;

import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.description.AxisDescription;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.engine.Phase;
import org.apache.axis2.modules.Module;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;

public class LoggingModule implements Module {

	public static final String CALLER_NAME = "CallerName";

	private static Logger LOGGER = LogManager.getLogger(LoggingModule.class);

	public void init(ConfigurationContext configContext, AxisModule module) throws AxisFault {

	}

	public void shutdown(ConfigurationContext configurationContext) throws AxisFault {

	}

	public void engageNotify(AxisDescription axisDescription) throws AxisFault {

	}

	@Override
	public void applyPolicy(Policy arg0, AxisDescription arg1) throws AxisFault {

	}

	public boolean canSupportAssertion(Assertion assertion) {

		return true;
	}

	public static ConfigurationContext getDefaultConfigurationContext(String callerName) {
		ConfigurationContext configurationContext = null;
		try {

			configurationContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(null, null);
			configurationContext.getAxisConfiguration().getOutFlowPhases().add(new Phase("loggingPhase"));
			configurationContext.getAxisConfiguration().getInFlowPhases().add(0, new Phase("loggingPhase"));
			configurationContext.getAxisConfiguration().getInFaultFlowPhases().add(0, new Phase("loggingPhase"));
			configurationContext.getAxisConfiguration().getOutFaultFlowPhases().add(new Phase("loggingPhase"));
			configurationContext.setProperty(CALLER_NAME, callerName);
		} catch (Throwable e) {
			LOGGER.warn(e);
		}

		return configurationContext;
	}

	public static void engage(ServiceClient serviceClient) {
		try {
			serviceClient.engageModule("logging");

		} catch (Throwable e) {
			LOGGER.warn(e);
		}
	}

}
