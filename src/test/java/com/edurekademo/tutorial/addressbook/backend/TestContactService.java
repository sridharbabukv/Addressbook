package com.edurekademo.tutorial.addressbook.backend;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

public class TestContactService {
	/**
	 * Test for Saving all the records internally
	 */
	@Test
	public void test() {
		ContactService contactSearch = ContactService.createDemoService();
		assertEquals(100, contactSearch.count());

	}

	
	/**
	 * Test for Search Names
	 */
	@Test
	public void filter() {

		ContactService contactSearch = ContactService.createDemoService();
		List<Contact> search = contactSearch.findAll("peeetr"); /*peter*/
		assertEquals(7, search.size());
	}
}
