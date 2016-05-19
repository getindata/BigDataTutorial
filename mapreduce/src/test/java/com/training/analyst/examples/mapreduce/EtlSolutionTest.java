package com.training.analyst.examples.mapreduce;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.training.analyst.examples.mapreduce.EtlSolution.EtlSolutionMapper;

public class EtlSolutionTest {
	private static final String timestamp = "2013-08-14 11:45:32";
	private EtlSolutionMapper etlSolutionMapper;

	@Before
	public void init() {
		etlSolutionMapper = new EtlSolutionMapper();
	}

	@Test
	public void shouldRemoveBraces() {
		Assert.assertTrue(etlSolutionMapper.removeBraces("[" + timestamp + "]").equals(timestamp));
	}

	@Test
	public void shouldCheckDurationRange() {
		Assert.assertTrue(etlSolutionMapper.isDurationCorrect("241"));
		Assert.assertTrue(etlSolutionMapper.isDurationCorrect("419"));
		Assert.assertTrue(!etlSolutionMapper.isDurationCorrect("12"));
		Assert.assertTrue(etlSolutionMapper.isDurationCorrect("408"));
		Assert.assertTrue(!etlSolutionMapper.isDurationCorrect("10262"));
	}

	@Test
	public void shouldJoinStrings() {
		Assert.assertTrue(etlSolutionMapper.join("a", "b", "c", "d", "e").toString().equals("a\tb\tc\td\te"));
	}
}
