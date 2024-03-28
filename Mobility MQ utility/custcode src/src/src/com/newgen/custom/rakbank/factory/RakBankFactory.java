package com.newgen.custom.rakbank.factory;

import java.io.InputStream;
import java.util.ArrayList;

import com.newgen.mcap.core.external.basic.interfaces.Configurable;
import com.newgen.mcap.core.external.configuration.entities.concrete.Configuration;
import com.newgen.mcap.core.external.factory.entities.asbtracted.Factory;
import com.newgen.mcap.core.external.logging.concrete.LogMe;
import com.newgen.mcap.core.external.utils.GenericUtils;

public class RakBankFactory extends Factory {
	@Override
	public Configuration loadConfigurationFromXML() throws Exception {
		InputStream inputStream = null;
		Configuration configuration = null;
		try {
			inputStream = this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"EssentialRakBank.xml");
			configuration = GenericUtils.getConfigurationFromInputStream(inputStream);
		} catch (Exception e) {
			LogMe.logMe(LogMe.LOG_LEVEL_ERROR, e);
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
		return configuration;
	}

	@Override
	public Configuration loadConfigurationFromXML(String xmlString)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generateCheckSumForXML() throws Exception {
		String toReturn = null;
		InputStream inputStream = null;
		try {
			inputStream = this
					.getClass()
					.getClassLoader()
					.getResourceAsStream(
							"EssentialRakBank.xml");
			
			toReturn = GenericUtils.generateSHA1Checksum(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			// Logs to be added
		} finally {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
		}
		return toReturn;
	}

	@Override
	public String generateCheckSumForXML(String xmlString) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isConfigurationSaveable() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int configurationRefreshInterval() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isPropogationToDevicesRequired() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Configurable buildConfigurable() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList targetDestinations() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
