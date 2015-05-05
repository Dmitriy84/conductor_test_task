package com.conductor.steps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

public class EndUserSteps extends ScenarioSteps {
	@Step
	public JSONObject getLast(final String url) throws MalformedURLException,
			UnsupportedEncodingException, IOException, JSONException, Exception {
		return _send(url + "/last");
	}

	@Step
	public Map<String, Long> collectChars(final String field) {
		return Arrays.stream(field.split("")).collect(
				Collectors.groupingBy(c -> c, Collectors.counting()));
	}

	@Step
	public void print(final Map<String, Long> map, final String format) {
		map.forEach((k, v) -> {
			log.info(format.replace("{character}", k).replace("{counter}",
					v.toString()));
		});
	}

	private JSONObject _send(final String url) throws IOException,
			MalformedURLException, JSONException, UnsupportedEncodingException,
			Exception {
		final HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		log.info("calling URL: " + url);
		final int status = conn.getResponseCode();
		try (final InputStream istream = status == 200 ? conn.getInputStream()
				: conn.getErrorStream()) {
			try (final Stream<String> lines = new BufferedReader(
					new InputStreamReader(istream, "UTF-8")).lines()) {
				final String xml = lines.collect(Collectors.joining());
				if (status != 200)
					throw new Exception("Response status: " + status
							+ "/nDetails: " + xml);
				log.info("Response body: " + xml);
				return XML.toJSONObject(xml);
			}
		}
	}

	private final Logger log = Logger.getLogger(getClass());
	private static final long serialVersionUID = 4413415143679879733L;
}