package GameOfLife;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

public class UtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {

		CellMatrix cellMatrix=Utils.initMatrixFromFile("D:/Program Files/Eclipse/workspace/GameOfLife/case_0.txt");
		int height=cellMatrix.getHeight();
		int weight=cellMatrix.getWidth();
		int duration=cellMatrix.getDuration();
		int transfromNum=cellMatrix.getTransfromNum();
		assertEquals(10,height);
		assertEquals(10,weight);
		assertEquals(200,duration);
		assertEquals(0,transfromNum);
		
	}

}
