package uk.co.keithj.postcodes3.infrastructure.s3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class S3FileReader {

	public String processInputStreamIntoTextLines(BufferedReader reader, int startPosition, int contentLength,
			String remainingLine) {

		System.out.println("startPosition: " + startPosition + ", contentLength: " + contentLength + ", remainingLine: "
				+ remainingLine);

		char[] dataChar = getDataCharArray(reader, startPosition, contentLength);
		if (dataChar == null) {
			return "";
		}

		String dataString = remainingLine + new String(dataChar);
		System.out.println("dataString: '" + dataString + "'");

		String[] lines = dataString.split("\\r?\\n");
		System.out.println("lines.length: " + lines.length);

		String lastLine = lines[lines.length - 1];
		lines = removeLastLine(lines);

		System.out.println("lastLine: '" + lastLine + "'");

		List<String> postcodesList = Arrays.asList(lines);

		processPostcodes(postcodesList);

		System.out.println("------------------------------------------------------------");
		return lastLine;
	}

	private String[] removeLastLine(String[] lines) {
		String[] newLines = new String[lines.length - 1];
		for (int i = 0; i < newLines.length; i++) {
			newLines[i] = lines[i];
		}
		return newLines;
	}

	protected char[] getDataCharArray(BufferedReader reader, int startPosition, int contentLength) {
		char[] dataChar = new char[contentLength];
		try {
			int length = reader.read(dataChar, startPosition, contentLength);
			if (length == -1) {
				return null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return dataChar;
	}

	public void processPostcodes(List<String> postcodes) {
		System.out.println("Chunk:");
		postcodes.stream().forEach(System.out::println);
	}
}
