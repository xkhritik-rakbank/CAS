package com.newgen.custom.rakbank.interfaces;

import com.newgen.custom.rakbank.resources.concrete.IntegrationEntity;
import com.newgen.custom.rakbank.resources.concrete.ResponseEntity;
import com.newgen.mcap.core.external.basic.interfaces.AbstractedFunctionality;
import com.newgen.mcap.core.external.resources.concrete.StreamedResource;

public interface RakBank extends AbstractedFunctionality  {
	public ResponseEntity callWebService(IntegrationEntity request) throws Exception;
	public ResponseEntity getIdList(IntegrationEntity requestobject) throws Exception;
	//gaurav-s added 
	public ResponseEntity getAlocData(IntegrationEntity requestobject) throws Exception;
	//public ResponseEntity getOperationName(IntegrationEntity requestobject) throws Exception;
	public ResponseEntity exceuteSelect(IntegrationEntity requestobject) throws Exception;
	public StreamedResource fetchBUCStatus(StreamedResource bucData) throws Exception;
	//26022018
	public ResponseEntity  ExecuteOnDb(IntegrationEntity request) throws Exception;
	public ResponseEntity  checkVersion(IntegrationEntity request) throws Exception;
	public ResponseEntity getRetentionTime(IntegrationEntity request) throws Exception;
}
