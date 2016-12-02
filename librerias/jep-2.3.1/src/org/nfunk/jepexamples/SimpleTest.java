/*****************************************************************************

JEP - Java Math Expression Parser 2.3.1
      January 26 2006
      (c) Copyright 2004, Nathan Funk and Richard Morris
      See LICENSE.txt for license information.

*****************************************************************************/

package org.nfunk.jepexamples;
import org.nfunk.jep.*;

/**
 * A seven line program for testing whether the JEP library can be found
 * by the compiler and at run-time.<br>
 * Upon successful compilation and running of the program, the program should
 * print out one line: "1+2 = 3.0"
 */
public class SimpleTest {
	public static void main(String args[]) {
		JEP myParser = new JEP();
		myParser.parseExpression("1+2");
		System.out.println("1+2 = " + myParser.getValue());
	}
}
