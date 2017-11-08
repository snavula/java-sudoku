package com.sneha.sudoku;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sneha.jsudoku.Grid;
import com.sneha.jsudoku.InvalidIndexException;
import com.sneha.jsudoku.InvalidValueException;

public class GridTest {

	@Test
	public void testExpert1() throws InvalidIndexException,
			InvalidValueException {
		testSolve(
				"--9--6/27--3/--4----9/-2/8-1/7-3-1-62/68---9-13/----83--2/4--2--7",
				"159826374/278934156/364175298/526397841/841652937/793418625/682749513/917583462/435261789");
	}

	@Test
	public void testExpert2() throws InvalidIndexException,
			InvalidValueException {
		testSolve(
				"-9-3--2--/--------5/--2-4-3--/-54-2--3-/--6-351--/1--4-----/64-----98/-3----65-/---9-----/",
				"495381267/317692845/862547319/754128936/926735184/183469572/641253798/239874651/578916423");
	}

	@Test
	public void testEasy() throws InvalidIndexException, InvalidValueException {
		testSolve(
				"83---4259/--973518-/-4-9--376/-56--7921/128---6--/7-4-1--3-/6-3421--5/-125-84-3/-8-69-7-2",
				"837164259/269735184/541982376/356847921/128359647/794216538/673421895/912578463/485693712");
		testSolve(
				"5-6-3-1--/8--7-129-/2---684-3/--4------/37-8-2--4/--85-----/---42----/--1--7---/45----7-9",
				"596234178/843751296/217968453/924673815/375812964/168549327/739425681/681397542/452186739");
		testSolve(
				"---5-8/73--9/--472-6-1/-4--1-97/-7---613/9----5-8/-------6/-92---4-7/---8",
				"129568743/736194528/584723691/648312975/275986134/913475286/351247869/892631457/467859312");
		testSolve(
				"2-1--8/--67-94/78-----5/--------9/------8-5/---2-1-3-/--21-69-7/-----3-6-/5-4---2-3",
				"241538796/356719482/789642351/428365179/613974825/975281634/832156947/197423568/564897213");
		testSolve(
				"2-1--8/--67-94/78-----5/--------9/------8-5/---2-1-3-/--21-69-7/-----3-6-/5-4---2-3",
				"241538796/356719482/789642351/428365179/613974825/975281634/832156947/197423568/564897213");
	}

	/*
	 * seeds a grid with provided start state, solves it and compares with the provided result
	 * @param start
	 * @param result
	 * @throws InvalidIndexException
	 * @throws InvalidValueException
	 */
	private void testSolve(String start, String result)
			throws InvalidIndexException, InvalidValueException {
		System.out.println("New Puzzle.");
		Grid g = new Grid(start);
		System.out.println(g);
		g.solve();
		assertEquals(new Grid(result), g);
	}

}
