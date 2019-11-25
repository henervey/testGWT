package fr.cnrs.mdm.referentiel.ws;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LoggingHandler extends AbstractHandler {

	private static final Boolean IS_ACTIVATED = Boolean.parseBoolean(PropertiesUtils.getInstance().getValue("axis2.logger.activated"));

	public InvocationResponse invoke(MessageContext messageContext) throws AxisFault {

		if (IS_ACTIVATED) {

			Logger logger = LoggerFactory.getLogger(messageContext);
			Level level = LoggerFactory.getPriority(messageContext);

			logger.log(level, "Message ID: " + messageContext.getLogCorrelationID());
			
			// here  log request and response SOAP in  file (In Out flow)
			
			logger.log(level, messageContext.getEnvelope().toString());
		}

		return InvocationResponse.CONTINUE;
	}

}
