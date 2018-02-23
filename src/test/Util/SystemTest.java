package test.Util;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import main.JPEG.DCTMatrix;
import main.JPEG.FileParser;
import main.JPEG.HuffmanDecode;
import main.JPEG.HuffmanEncode;
import main.LSB.DCTJsteg;
import main.LSB.DCTMatrixLSB;
import main.LSB.HiddenMessage;

@RunWith(Parameterized.class)
public class SystemTest {

	@Parameters
	public static List<Object> data(){
		return Arrays.asList(new Object[] {
				"8auf8BK.jpg","huff_simple0.jpg", "TestImage.jpg", "TestImage_new.jpg", "TestImage2.jpg", "TestImage3.jpg", "TestImage512_512.jpg", 
				"TestImage640_360.jpeg", "TestImage640_360_new.jpg", "TestImage720_1280.jpg", "TestImage1920_1080.jpg", "TestImage1920_1080_2.jpg",
				"TestImage1920_1080_3.jpg", "TestImage1920_1080_4.jpg", "TestImage1920_1080_5.jpg", "TestImage2368_4208.jpeg", "TestImage3613_2078.JPG",
				"TestImage4160_3120.jpg","TestImage6000_3376.jpeg", "whatsapp.jpg"
				//"cat.jpg","cat_new.jpg", "mona.jpg", "mona_new.jpg","TestImage.jpg", 
				//"TestImage2.jpg","whatsapp.jpg","whatsapp_new.jpg", "fabi.jpg","fabi_new.jpg", "MiddleImage.jpg", "BigImage.jpg"
		});
	}
	
	private String path;
	
	public SystemTest(String path) {
		this.path = path;
	}	
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRealImages() throws IOException {
		System.out.println(path);
		FileParser fileparser = new FileParser();
		fileparser.setFilePath("TestImages"+File.separator+path);
				
		fileparser.readFileBytes();
		fileparser.createHuffmanTables();

		HuffmanDecode hc = fileparser.processImageData();
		
		HuffmanEncode huffencode = new HuffmanEncode(hc.getDecodedData(), hc.huffmanTable, hc.imageData);
		huffencode.encode();
		fileparser.writeFileBytes(huffencode);
	}

}
