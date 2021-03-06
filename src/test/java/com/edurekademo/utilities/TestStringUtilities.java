package com.edurekademo.utilities;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import com.edurekademo.utilities.StringUtilities;

import junit.framework.Assert;

public class TestStringUtilities {

	StringUtilities stringUtil = new StringUtilities();
	
	/**
	 * Test for given a comma separated string and type, returns an ArrayList of specific types
	 */
	@Test
	public void testConvertStringToListnull() {
		String strParamValueList = "peter,Jhonson,july";
		String type = "";
		List<Object> stringToList = stringUtil.convertStringToList(strParamValueList, type);
		Assert.assertEquals(3,stringToList.size());
	}

	/**
	 * Test for given a variable list of String parameters, forms a hash map
	 */
	@Test
	public void testCreateParameterList() {

		HashMap<String, Object> hMap = stringUtil.createParameterList("value0=0","value2=2","value3=3","value4=4","value5=5");
		Assert.assertEquals(true,hMap.keySet().contains("value0"));
	}
}
