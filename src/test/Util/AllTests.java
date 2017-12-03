package test.Util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.JPEG.TestDCTMatrix;
import test.JPEG.TestHuffmanCode;

@RunWith(Suite.class)
@SuiteClasses({ TestBitConverter.class, TestHuffmanCode.class, TestDCTMatrix.class })
public class AllTests {

}
