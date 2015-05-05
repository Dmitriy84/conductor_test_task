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
	public void printResults(final String format) {
		try {
			final JSONObject jsonObject = (JSONObject) _event.get(_root);
			try {
				final String field = jsonObject.get(_field).toString();
				Assert.assertFalse("empty '" + _field + "' field",
						StringUtils.isBlank(field));
				log.info(_field + " = " + field);
				endUser.print(
						new TreeMap<String, Long>(endUser.collectChars(field)),
						format);
			} catch (final JSONException e) {
				Assert.fail("node '" + _field + "' was not found");
			}
		} catch (final JSONException root) {
			Assert.fail("unexpected root element '" + _root + "'");
		}
	}

	@Then(value = "print counter of characters with format '$format' from '$from' to '$to'", priority = 1)
	public void printResultsFromTo(final String format, final String from,
			final String to) {
		
		System.out.println();
	}

	@Steps
	EndUserSteps endUser;
	private final Logger log = Logger.getLogger(getClass());
	private String _url, _root, _field;
	private JSONObject _event;
}