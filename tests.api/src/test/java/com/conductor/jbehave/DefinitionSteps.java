package com.conductor.jbehave;

import java.util.TreeMap;

import net.thucydides.core.annotations.Steps;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.conductor.steps.EndUserSteps;

public class DefinitionSteps {
	@Given("url for beacon api calls is '$url'")
	public void setURL(final String url) {
		_url = url;
	}

	@Given("root element is '$root'")
	public void setRoot(final String root) {
		_root = root;
	}

	@Given("I am going to parse '$field' field")
	public void setParseField(final String field) {
		_field = field;
	}

	@When("the most recent event from the randomness beacon was retrieved")
	public void gotRecent() throws Exception {
		_event = endUser.getLast(_url);
	}

	@Then("print counter of characters with format '$format'")
	public void printResults(final String format) throws Exception {
		endUser.print(_collectChars(_event), format);
	}

	@Then(value = "print counter of characters with format '$format' from '$from' to '$to'", priority = 1)
	public void printResultsFromTo(final String format, final String from,
			final String to) throws Exception {
		final TreeMap<String, Long> map = new TreeMap<>();
		long to_mills = endUser.parseDate(to), timeStamp = 0;
		JSONObject event = endUser.getCurrent(_url, endUser.parseDate(from));
		while (true) {
			timeStamp = Long.parseLong(_getFieldValue(event, "timeStamp"));
			if (timeStamp > to_mills)
				break;
			endUser.joinMaps(map, _collectChars(event));
			event = endUser.getNext(_url, timeStamp);
			endUser.print(map, format);
		}
	}

	@Steps
	EndUserSteps endUser;
	private final Logger log = Logger.getLogger(getClass());
	private static String _url, _root, _field;
	private static JSONObject _event;

	private TreeMap<String, Long> _collectChars(final JSONObject obj)
			throws Exception {
		return new TreeMap<>(endUser.collectChars(_getFieldValue(obj, _field)));
	}

	private String _getFieldValue(final JSONObject obj, final String field) {
		try {
			final JSONObject root = (JSONObject) obj.get(_root);
			try {
				final String value = root.get(field).toString();
				Assert.assertFalse("empty '" + field + "' field",
						StringUtils.isBlank(value));
				log.info(field + " = " + value);
				return value;
			} catch (final JSONException e) {
				Assert.fail("node '" + field + "' was not found");
			}
		} catch (final JSONException e) {
			Assert.fail("unexpected root element '" + _root + "'");
		}
		return "";
	}
}