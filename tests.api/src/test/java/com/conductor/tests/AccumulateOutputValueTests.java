package com.conductor.tests;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.TreeMap;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.conductor.jbehave.Sentences;
import com.conductor.steps.EndUserSteps;

public class AccumulateOutputValueTests {
	@BeforeClass
	public static void setUp() throws Exception {
		_sentences.setURL("https://beacon.nist.gov/rest/record");
		_sentences.setParseField("outputValue");
		_sentences.setRoot("record");
		_sentences.endUser = new EndUserSteps();
		_sentences.gotRecent();
	}

	private static Sentences _sentences = new Sentences();

	@Test
	public void accumulate_output_value_positive()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}",
				"3 months 1 day 1 hour 45 minutes ago",
				"3 months 1 day 1 hour 40 minutes ago");
		Assert.assertTrue("Nothing was found for period", actual.entrySet()
				.size() > 0);
	}

	@Test
	public void accumulate_output_value_from_is_after_to()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}",
				"3 months 1 day 1 hour 40 minutes ago",
				"3 months 1 day 1 hour 45 minutes ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_wrong_from()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual;
		try {
			actual = _sentences.collectCharsFromTo("{character}, {counter}",
					"xutmymgfvngf", "3 months 1 day 1 hour 45 minutes ago");
		} catch (AssertionError ae) {
			return;
		}
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_wrong_to()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual;
		try {
			actual = _sentences.collectCharsFromTo("{character}, {counter}",
					"3 months 1 day 1 hour 45 minutes ago", "xutmymgfvngf");
		} catch (AssertionError ae) {
			return;
		}
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_empty_to()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual;
		try {
			actual = _sentences.collectCharsFromTo("{character}, {counter}",
					"3 months 1 day 1 hour 45 minutes ago", "");
		} catch (AssertionError ae) {
			return;
		}
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_empty_from()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual;
		try {
			actual = _sentences.collectCharsFromTo("{character}, {counter}",
					"", "3 months 1 day 1 hour 45 minutes ago");
		} catch (AssertionError ae) {
			return;
		}
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_zero_period()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}", "30 minutes ago", "30 minutes ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() == 0);
	}

	@Test
	public void accumulate_output_value_only_minutes()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}", "45 minutes ago", "40 minutes ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() > 0);
	}

	@Test
	public void accumulate_output_value_only_hours()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}", "2 hours ago", "1 hour ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() > 0);
	}

	@Test
	public void accumulate_output_value_only_days()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}", "2 days ago", "1 day ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() > 0);
	}

	@Test
	public void accumulate_output_value_only_months()
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		TreeMap<String, Long> actual = _sentences.collectCharsFromTo(
				"{character}, {counter}", "4 months ago", "3 months ago");
		Assert.assertTrue("Unexpected collected characters found", actual
				.entrySet().size() > 0);
	}
}