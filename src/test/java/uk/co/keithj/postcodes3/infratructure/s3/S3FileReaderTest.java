package uk.co.keithj.postcodes3.infratructure.s3;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.keithj.postcodes3.infrastructure.s3.S3FileReader;

@RunWith(MockitoJUnitRunner.class)
public class S3FileReaderTest {

	private final static String DATALINE = "ST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\rST7 2YB\n\rSK4 2HD\n\r";

	private TestS3FileReader testSubject;

	@Mock
	private BufferedReader mockBufferedReader;

	@Before
	public void setUp() {

	}

	@Test
	@Ignore
	public void shouldReadFile() {
		S3FileReader testSubject = new S3FileReader();
		String postcodesFilename = "E:/dev/git/Postcode-S3/testData/postcodes.csv";

		try (FileInputStream fis = new FileInputStream(postcodesFilename)) {

			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

			int chunkSize = 100;
			String remainingLine = "";
			for (int startPosition = 0; startPosition < 1000; startPosition += chunkSize) {
				System.out.println("startPosition: " + startPosition);
				remainingLine = testSubject.processInputStreamIntoTextLines(reader, startPosition, chunkSize,
						remainingLine);
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	@Test
	@Ignore
	public void shouldReadFileText100BlankPreviousLines() throws Exception {
		TestS3FileReader testSubject = new TestS3FileReader();
		// Given
		int startPosition = 0;
		int chunkSize = 31;
		String remainingLineInput = "";

		// When
		String remainingLine = testSubject.processInputStreamIntoTextLines(mockBufferedReader, startPosition, chunkSize,
				remainingLineInput);

		// Then
		assertEquals("SK4 ", remainingLine);
	}

	@Test
	// @Ignore
	public void shouldReadFileText100BlankPreviousLines2() throws Exception {
		TestS3FileReader testSubject = new TestS3FileReader();
		// Given
		int startPosition = 31;
		int chunkSize = 31;
		String remainingLineInput = "ST4 ";

		// When
		String remainingLine = testSubject.processInputStreamIntoTextLines(mockBufferedReader, startPosition, chunkSize,
				remainingLineInput);

		// Then
		assertEquals("\rST7 2YB", remainingLine);
	}

	@Test
	// @Ignore
	public void shouldReadFileText100BlankPreviousLines3() throws Exception {
		TestS3FileReader testSubject = new TestS3FileReader();
		// Given
		int startPosition = 32;
		int chunkSize = 31;
		String remainingLineInput = "ST4 2";

		// When
		String remainingLine = testSubject.processInputStreamIntoTextLines(mockBufferedReader, startPosition, chunkSize,
				remainingLineInput);

		// Then
		// assertEquals("\rST7 2YB", remainingLine);
		assertEquals("\r", remainingLine);
	}

	private String getCharacters(int startPosition, int chunkSize) {
		return DATALINE.substring(startPosition, startPosition + chunkSize);
	}

	class TestS3FileReader extends S3FileReader {
		@Override
		protected char[] getDataCharArray(BufferedReader reader, int startPosition, int chunkSize) {
			return getCharacters(startPosition, chunkSize).toCharArray();

		}
	}
}
