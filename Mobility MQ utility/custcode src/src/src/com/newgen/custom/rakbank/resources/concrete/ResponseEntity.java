package com.newgen.custom.rakbank.resources.concrete;

import com.newgen.mcap.core.external.resources.abstracted.Resource;

public class ResponseEntity extends Resource{

	String outputjsonstring;

	public String getOutputjsonstring() {
		return outputjsonstring;
	}

	public void setOutputjsonstring(String outputjsonstring) {
		this.outputjsonstring = outputjsonstring;
	}
}
