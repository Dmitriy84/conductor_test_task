package com.conductor.steps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.junit.Assert;

public class EndUserSteps extends ScenarioSteps {
	@Step
	public JSONObject getLast(final String url) throws MalformedURLException,
			UnsupportedEncodingException, IOException, JSONException, Exception {
		return _send(url + "/last");
	}

	@Step
	public JSONObject getCurrent(final String url, final long timestamp)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		return _send(url + "/" + timestamp);
	}

	@Step
	public JSONObject startChain(final String url, final long timestamp)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		return _send(url + "/start-chain/" + timestamp);
	}

	@Step
	public JSONObject getNext(final String url, final long timestamp)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		return _send(url + "/next/" + timestamp);
	}

	@Step
	public JSONObject getPrevious(final String url, final long timestamp)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException, JSONException, Exception {
		return _send(url + "/previous/" + timestamp);
	}

	@Step
	public TreeMap<String, Long> collectChars(final String field) {
		if (StringUtils.isBlank(field))
			return null;
		Pattern p = Pattern.compile("[^A-F\\d]");
		Matcher m = p.matcher(field);
		if (m.find())
			throw new UnsupportedOperationException("Unexpected character: "
					+ m.group());
		if (field.length() != 128)
			throw new UnsupportedOperationException(
					"Unexpected Output Value length: " + field.length());
		return new TreeMap<>(Stream.of(field.split("")).collect(
				Collectors.groupingBy(c -> c, Collectors.counting())));
	}

	@Step
	public void print(final Map<String, Long> map, final String format) {
		map.forEach((k, v) -> log.info(format.replace("{character}", k)
				.replace("{counter}", v.toString())));
	}

	@Step
	public long parseDate(final String date) {
		final Matcher m = PERIOD_PATTERN.matcher(date);
		DateTime d = new DateTime();
		boolean flag = false;
		try {
			while (m.find()) {
				flag = true;
				final String group = m.group(0);
				final String[] pair = group.split(" ");
				final int value = Integer.parseInt(pair[0]);
				log.info("Found date period: " + group);
				switch (pair[1].toLowerCase()) {
				case "month":
				case "months":
					d = d.minusMonths(value);
					break;
				case "day":
				case "days":
					d = d.minusDays(value);
					break;
				case "hour":
				case "hours":
					d = d.minusHours(value);
					break;
				case "minute":
				case "minutes":
					d = d.minusMinutes(value);
					break;
				default:
					throw new UnsupportedOperationException(
							"cannot parse period: " + pair[1]);
				}
			}
		} catch (final Exception e) {
			Assert.fail("can not parse date '" + date + "'/nDetails: "
					+ e.getMessage());
		}
		Assert.assertTrue("cannot parse date: " + date, flag);
		log.info("... created date: " + d);
		return d.getMillis() / 1000;
	}

	public void joinMaps(final TreeMap<String, Long> map,
			final TreeMap<String, Long> collected) {
		collected.entrySet().forEach(
				e -> map.put(e.getKey(),
						map.getOrDefault(e.getKey(), 0L) + e.getValue()));
	}

	private JSONObject _send(final String url) throws IOException,
			MalformedURLException, JSONException, UnsupportedEncodingException,
			Exception {
		final HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		try {
			log.info("calling URL: " + url);
			final int status = conn.getResponseCode();
			try (final InputStream istream = status == 200 ? conn
					.getInputStream() : conn.getErrorStream()) {
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
		} finally {
			conn.disconnect();
		}
	}

	private final Logger log = Logger.getLogger(getClass());
	private static final long serialVersionUID = 4413415143679879733L;
	private static final Pattern PERIOD_PATTERN = Pattern
			.compile("[\\d]+ [a-zA-Z]+ ");
}