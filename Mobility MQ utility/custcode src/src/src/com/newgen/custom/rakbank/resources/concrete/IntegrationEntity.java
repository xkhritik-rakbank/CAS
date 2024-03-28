package com.newgen.custom.rakbank.resources.concrete;

import java.util.List;

import com.newgen.mcap.core.external.basic.interfaces.Saveable;
import com.newgen.mcap.core.external.resources.abstracted.Resource;

public class IntegrationEntity extends Resource implements Saveable{

	String callname;
	String inputjsonstring;
	String operationtype;


	public String getCallname() {
		return callname;
	}

	public void setCallname(String callname) {
		this.callname = callname;
	}

	public String getInputjsonstring() {
		return inputjsonstring;
	}

	public void setInputjsonstring(String inputjsonstring) {
		this.inputjsonstring = inputjsonstring;
	}

	public String getOperationtype() {
		return operationtype;
	}

	public void setOperationtype(String operationtype) {
		this.operationtype = operationtype;
	}

	@Override
	public void decryptObject() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void encryptObject() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveOrUpdate() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends Saveable> List<T> search() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Saveable> T searchByNaturalKeys() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
