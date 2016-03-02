package org.test;

import org.junit.Test;

public class TestLucene {

	@Test
	public void testIndex(){
		HelloLucence hl = new HelloLucence();
		hl.index();
	}
	@Test
	public void testSearch(){
		HelloLucence hl = new HelloLucence();
		hl.searcher();
	}
}
