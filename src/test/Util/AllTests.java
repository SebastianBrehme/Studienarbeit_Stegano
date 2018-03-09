package test.Util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.JPEG.TestBitData;
import test.JPEG.TestDCTMatrix;
import test.JPEG.TestHuffmanDecode;
import test.JPEG.TestHuffmanEncode;
import test.JPEG.TestImageData;

@RunWith(Suite.class)
@SuiteClasses({ TestBitConverter.class, TestHuffmanDecode.class, TestDCTMatrix.class,
	TestImageData.class, TestHuffmanEncode.class, TestBitData.class, TestPathgenerator.class})
public class AllTests {

}
