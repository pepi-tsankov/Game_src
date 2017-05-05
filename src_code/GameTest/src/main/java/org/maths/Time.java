package org.maths;

public class Time {
	private static int times;
	private static long time;
	/**
	 * sets the variables for the time() function to work
	 */
	public static void timeSetup(){
		times=0;
		time=System.currentTimeMillis();
	}
	/**
	 * prints the time since the timeSetup() function was calld <p>
	 * and how much times you've called the time() functon
	 */
	public static void time(){
		System.out.println(""+(times++)+" ; "+(System.currentTimeMillis()-time)); 
	}
}
