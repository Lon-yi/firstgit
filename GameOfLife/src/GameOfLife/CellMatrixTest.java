package GameOfLife;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CellMatrixTest {
	private CellMatrix cellMatrix ;	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	@Test
	public void testFindLifedNum() {
		cellMatrix = Utils.initMatrixFromFile("case_test.txt");//从文件中初始化细胞类
	    int[][] resultmatrix = {{1,3,2,1},{2,2,4,3},{2,3,4,2},{0,2,2,2}};//预期结果
		for(int y = 0;y<cellMatrix.getHeight();y++)
		{
			for(int x = 0;x<cellMatrix.getWidth();x++)
			{
				int num = cellMatrix.findLifedNum(y, x);
				assertEquals(resultmatrix[y][x],num);
			}
		}
	}
	@Test
	public void testTransform() {
		cellMatrix = Utils.initMatrixFromFile("case_test.txt");//从文件中初始化细胞类
		int[][] resultmatrix = {{0,1,1,0},{0,1,0,1},{0,1,0,1},{0,0,1,1}};//预期结果
		cellMatrix.transform();
		for(int y = 0;y<cellMatrix.getHeight();y++)
		{
			for(int x = 0;x<cellMatrix.getWidth();x++)
			{
				assertEquals(resultmatrix[y][x],cellMatrix.getMatrix()[y][x]);
			}
		}
	}

}
