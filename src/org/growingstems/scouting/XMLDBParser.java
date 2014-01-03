/*
 * Copyright 2013 Daniel Logan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.growingstems.scouting;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XMLDBParser {

	public static List<Map<String, String>> extractRows(String lookupCol,
			String lookupVal, String rawXML) throws XmlPullParserException, IOException {
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();

		XmlPullParser parser = Xml.newPullParser();

		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(new StringReader(rawXML));
		int eventType = parser.getEventType();
		Stack<String> tags = new Stack<String>();

		Map<String, String> curRow = null;
		String name;

		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				tags.push(parser.getName());
				if (parser.getName().compareToIgnoreCase("row") == 0) {
					curRow = new TreeMap<String, String>();
				}
				break;
			case XmlPullParser.TEXT:
				if (curRow != null
						&& (tags.peek().compareToIgnoreCase("row") != 0)) {
					curRow.put(tags.peek(), parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				boolean cont = true;
				while (cont) {
					name = tags.pop();
					if (name.compareToIgnoreCase("row") == 0) {
						if (lookupCol == null || lookupVal == null)
							ret.add(curRow);
						else if (curRow.containsKey(lookupCol)
								&& (curRow.get(lookupCol).compareToIgnoreCase(
										lookupVal) == 0)) {
							ret.add(curRow);
						}
						curRow = null;
					}
					if (name.compareTo(parser.getName()) == 0) {
						cont = false;
					}
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}

		return ret;
	}

	public static List<String> extractColumn(String colName, String rawXML)
			throws XmlPullParserException, IOException {
		List<String> ret = new ArrayList<String>();

		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(new StringReader(rawXML));
		int eventType = parser.getEventType();
		Stack<String> tags = new Stack<String>();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				tags.push(parser.getName());
				break;
			case XmlPullParser.TEXT:
				if (tags.peek().compareToIgnoreCase(colName) == 0) {
					ret.add(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				while (tags.pop() != parser.getName())
					;
				break;
			default:
				break;
			}
			eventType = parser.next();
		}

		return ret;
	}

	public static String dataLookupByValue(String colLookup,
			String valueLookup, String colOfInterest, String rawXML)
			throws XmlPullParserException, IOException {

		XmlPullParser parser = Xml.newPullParser();
		parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
		parser.setInput(new StringReader(rawXML));
		int eventType = parser.getEventType();
		Stack<String> tags = new Stack<String>();
		String lookup = "";
		String interest = "";
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				tags.push(parser.getName());
				break;
			case XmlPullParser.TEXT:
				if (tags.peek().compareToIgnoreCase(colLookup) == 0) {
					if (interest != "" && lookup.compareTo(valueLookup) == 0)
						return interest;
					lookup = parser.getText();
				} else if (tags.peek().compareToIgnoreCase(colOfInterest) == 0) {
					if (lookup != "" && lookup.compareTo(valueLookup) == 0)
						return parser.getText();
					interest = parser.getText();
				}
				break;
			case XmlPullParser.END_TAG:
				while (tags.pop() != parser.getName())
					;
				if (!tags.contains("row")) {
					interest = lookup = "";
				}
				break;
			default:
				break;
			}
			eventType = parser.next();
		}

		return "";
	}

}
