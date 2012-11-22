package com.zack.test;

import com.zack.utilities.Utilities;

public class UtilitiesTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int i = Utilities.getCourtesy(24.7f);
		System.out.println("Utilities.getCourtesy(24.7f) = " + i);

		i = Utilities.getCourtesy(25.0f);
		System.out.println("Utilities.getCourtesy(25.0f) = " + i);

		i = Utilities.getCourtesy(25.1f);
		System.out.println("Utilities.getCourtesy(25.1f) = " + i);

	}

}
