package fr.cnrs.mdm.referentiel.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Singleton that return the tiers properties file.
 * 
 * @author MGU
 * 
 */
public class PropertiesUtils {

	/**
	 * Log4J properties filename.
	 */
	private static final String LOG4J_PROPS_FILENAME = "log4j2.xml";

	/**
	 * EBX home system variable
	 */
	private static final String EBX_HOME = "ebx.home";

	/**
	 * Tiers properties filename.
	 */
	private static final String TIERS_PROPS_FILENAME = "logging.module.properties";

	/**
	 * Log4j logger.
	 */
	private final static Logger LOGGER = LogManager.getLogger(PropertiesUtils.class);

	/**
	 * properties file name.
	 */
	private static final String PROPERTIES_FILENAME = "logging.module.properties";

	/**
	 * The properties.
	 */
	private Properties props = null;

	private static PropertiesUtils _instance = null;

	public static PropertiesUtils getInstance() {
		if (_instance == null) {
			_instance = new PropertiesUtils();
			loadLog4jConfiguration();
		}

		return _instance;
	}

	private PropertiesUtils() {

		// initialize properties
		props = new Properties();

		try {

			// load our log4j properties / configuration file
			InputStream inputStream;

			String tiersPropsFilename = ((String) System.getProperty(EBX_HOME)) + File.separator + TIERS_PROPS_FILENAME;
			File file = new File(tiersPropsFilename);

			if (file.exists()) {
				// use one who is in ebxHome repository
				inputStream = new FileInputStream(tiersPropsFilename);
			} else {
				inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
			}
			props.load(inputStream);
		} catch (IOException e) {
			LOGGER.fatal(e);
		}
	}

	/**
	 * Return the value matching the key.
	 * 
	 * @param key
	 *            the key to search for
	 * @return the value matching the key, returns null if the key is not found
	 */
	public String getValue(final String key) {
		String result = null;

		if (key != null) {
			result = props.getProperty(key.trim());
		}

		return result;
	}

	private static void loadLog4jConfiguration() {

		// initialize Log4J
		final Properties logProperties = new Properties();

		try {

			// load our log4j properties / configuration file
			InputStream inputStream;

			String log4jPropsFilename = ((String) System.getProperty(EBX_HOME)) + File.separator + LOG4J_PROPS_FILENAME;
			File file = new File(log4jPropsFilename);

			if (file.exists()) {
				// use one who is in ebxHome repository
				inputStream = new FileInputStream(log4jPropsFilename);

			} else {
				// if not exit in ebxHome repository use who is in war file
				log4jPropsFilename = LOG4J_PROPS_FILENAME;
				inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(log4jPropsFilename);

				// Get file from resources folder if we can't use it in ebxHome repository

				 file = new File(PropertiesUtils.class.getClassLoader().getResource(log4jPropsFilename).getFile());

			}

			logProperties.load(inputStream);

			// PropertyConfigurator.configure(logProperties);

			LoggerContext context = (LoggerContext) LogManager.getContext(false);

			context.setConfigLocation(file.toURI());

			LogManager.getLogger(PropertiesUtils.class).info("Log4J File Loaded!");

		} catch (IOException e) {
			LOGGER.fatal(e);
		}

	}

}
