package com.conductor.tests;

import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import com.conductor.steps.EndUserSteps;

public class CharactersCalculationTests {
	@Test
	public void calculate_chars_positive() {
		TreeMap<String, Long> actual = _get_data("86539ABA17CBDFB52A703C36E69EB2B356C7EF935A8ADB36D5874694ADB33D269D4FC639718EF2E49DCCD362742309A3A7F091D45B919C8BFFE64BA49964471E");
		TreeMap<String, Long> expected = new TreeMap<String, Long>() {
			{
				put("0", 3L);
				put("1", 5L);
				put("2", 6L);
				put("3", 12L);
				put("4", 10L);
				put("5", 6L);
				put("6", 11L);
				put("7", 8L);
				put("8", 5L);
				put("9", 13L);
				put("A", 9L);
				put("B", 10L);
				put("C", 7L);
				put("D", 9L);
				put("E", 7L);
				put("F", 7L);
			}
		};
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected.equals(actual));
	}

	@Test
	public void calculate_chars_empty_input() {
		TreeMap<String, Long> actual = _get_data("");
		TreeMap<String, Long> expected = null;
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected == actual);
	}

	@Test
	public void calculate_chars_null_input() {
		TreeMap<String, Long> actual = _get_data(null);
		TreeMap<String, Long> expected = null;
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected == actual);
	}

	@Test
	public void calculate_chars_wrong_symbols() throws Exception {
		String data = "fd jbntrehm430 584hg84mhb 85t43hbm38trhfLFISBHDSOIFNBFDIONBSDFOPV SDVIERVNERINPVPIEORVA";
		TreeMap<String, Long> actual = null;
		TreeMap<String, Long> expected = null;
		try {
			actual = _get_data(data);
			throw new Exception("Not expected calculation for string:" + data);
		} catch (UnsupportedOperationException uoe) {
		}
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected == actual);
	}

	@Test
	public void calculate_chars_less_input_length() throws Exception {
		String data = "86539ABA17CBDFB52A703C36E69EB2B356C7EF935A8ADB36D5874694ADB33D269D4FC639718EF2E49DCCD362742309A3A7F091D45B919C8BFFE64BA49964471";
		TreeMap<String, Long> actual = null;
		TreeMap<String, Long> expected = null;
		try {
			actual = _get_data(data);
			throw new Exception("Not expected calculation for string:" + data);
		} catch (UnsupportedOperationException uoe) {
		}
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected == actual);
	}

	@Test
	public void calculate_chars_greater_input_length() throws Exception {
		String data = "86539ABA17CBDFB52A703C36E69EB2B356C7EF935A8ADB36D5874694ADB33D269D4FC639718EF2E49DCCD362742309A3A7F091D45B919C8BFFE64BA49964471E3";
		TreeMap<String, Long> actual = null;
		TreeMap<String, Long> expected = null;
		try {
			actual = _get_data(data);
			throw new Exception("Not expected calculation for string:" + data);
		} catch (UnsupportedOperationException uoe) {
		}
		Assert.assertTrue(
				"Not expected calculation of characters: " + expected,
				expected == actual);
	}

	private TreeMap<String, Long> _get_data(String input) {
		return _steps.collectChars(input);
	}

	private EndUserSteps _steps = new EndUserSteps();
}