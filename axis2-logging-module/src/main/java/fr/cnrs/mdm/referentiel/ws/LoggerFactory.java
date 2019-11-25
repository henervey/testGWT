package fr.cnrs.mdm.referentiel.ws;

import org.apache.axis2.context.MessageContext;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerFactory {

	private static final String COMMON_PREFIX = PropertiesUtils.getInstance().getValue("axis2.logger.prefix");

	private static final String IN_FLOW = PropertiesUtils.getInstance().getValue("axis2.logger.inflow");
	private static final String OUT_FLOW = PropertiesUtils.getInstance().getValue("axis2.logger.outflow");

	private static final String SERVICE_NAME_KEY_PREFIX = "axis2.logger.referentiel.servicename.";

	public static Logger getLogger(MessageContext messageContext) {

		// Get information from message context
		int flowType = messageContext.getFLOW();

		String callerName = (String) messageContext.getConfigurationContext().getProperty(LoggingModule.CALLER_NAME);
		if (callerName == null || callerName.trim().isEmpty()) {
			callerName = messageContext.getAxisService().getName();
		}

		// Build appendername
		String appenderName = COMMON_PREFIX;
		if (flowType == MessageContext.IN_FLOW || flowType == MessageContext.IN_FAULT_FLOW) {
			appenderName += IN_FLOW;
		}
		if (flowType == MessageContext.OUT_FLOW || flowType == MessageContext.OUT_FAULT_FLOW) {
			appenderName += OUT_FLOW;
		}

		String serviceLoggerName = PropertiesUtils.getInstance().getValue(SERVICE_NAME_KEY_PREFIX + callerName);

		if (serviceLoggerName != null) {
			appenderName += serviceLoggerName;
		}

		return LogManager.getLogger(appenderName);
	}

	public static Level getPriority(MessageContext messageContext) {

		if (messageContext.isFault()) {

			return Level.ERROR;

		} else {
			return Level.INFO;
		}
	}

}
