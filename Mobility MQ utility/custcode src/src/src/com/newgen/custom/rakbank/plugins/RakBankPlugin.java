package com.newgen.custom.rakbank.plugins;

import java.util.ArrayList;
import java.util.List;

import com.newgen.custom.rakbank.factory.RakBankFactory;
import com.newgen.mcap.core.external.basic.interfaces.AbstractedFunctionality;
import com.newgen.mcap.core.external.basic.interfaces.Configurable;
import com.newgen.mcap.core.external.solutionhooks.interfaces.SolutionEventHandler;
import com.newgen.mcap.core.external.solutionhooks.interfaces.SolutionInterface;
import com.newgen.mcap.core.external.utils.MobileCaptureConstants;

public class RakBankPlugin implements SolutionInterface{

	@Override
	public String getSolutionVersion() throws Exception {
		return MobileCaptureConstants.SOLUTION_VERSION;
	}

	@Override
	public List<Configurable> getConfigurables() throws Exception {
		Configurable factory = new RakBankFactory();	
		ArrayList<Configurable> configurables = new ArrayList<Configurable>();
		configurables.add(factory);
		return configurables;
	}

	@Override
	public List<SolutionEventHandler> getSolutionEventHandlers()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AbstractedFunctionality> getAbstractedFunctionalities()
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


}
