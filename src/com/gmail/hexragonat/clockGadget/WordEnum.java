package com.gmail.hexragonat.clockGadget;


public enum WordEnum
{
	$_ITS(
			new int[]{0, 1, 2},
			new int[]{0, 0, 0}),
	H_NOON(
			new int[]{7, 8, 9, 10},
			new int[]{4, 4, 4, 4}), //12pm
	H_ONE(
			new int[]{0, 1, 2},
			new int[]{9, 9, 9}),
	H_TWO(
			new int[]{8, 9, 10},
			new int[]{7, 7, 7}),
	H_THREE(
			new int[]{6, 7, 8, 9, 10},
			new int[]{9, 9, 9, 9, 9}),
	H_FOUR(
			new int[]{0, 1, 2, 3},
			new int[]{10, 10, 10, 10}),
	H_FIVE(
			new int[]{0, 1, 2, 3},
			new int[]{7, 7, 7, 7}),
	H_SIX(
			new int[]{3, 4, 5},
			new int[]{9, 9, 9}),
	H_SEVEN(
			new int[]{1, 2, 3, 4, 5},
			new int[]{4, 4, 4, 4, 4}),
	H_EIGHT(
			new int[]{6, 7, 8, 9, 10},
			new int[]{8, 8, 8, 8, 8}),
	H_NINE(
			new int[]{4, 5, 6, 7},
			new int[]{7, 7, 7, 7}),
	H_TEN(
			new int[]{8, 9, 10},
			new int[]{6, 6, 6}),
	H_ELEVEN(
			new int[]{0, 1, 2, 3, 4, 5},
			new int[]{8, 8, 8, 8, 8, 8}),
	H_MIDNIGHT(
			new int[]{0, 1, 2, 3, 4, 5, 6, 7},
			new int[]{6, 6, 6, 6, 6, 6, 6, 6}), //12am


	M_FIVE(
			new int[]{7, 8, 9, 10},
			new int[]{2, 2, 2, 2}),
	M_TEN(
			new int[]{1, 2, 3},
			new int[]{1, 1, 1}),
	M_QUARTER(      //A
			new int[]{4, 4, 5, 6, 7, 8, 9, 10},
			new int[]{0, 1, 1, 1, 1, 1, 1, 1}),
	M_TWENTY(
			new int[]{0, 1, 2, 3, 4, 5},
			new int[]{2, 2, 2, 2, 2, 2}),
	M_TWENTYFIVE(    //FIVE      //TWENTY
			new int[]{0, 1, 2, 3, 4, 5, 7, 8, 9, 10},
			new int[]{2, 2, 2, 2, 2, 2, 2, 2, 2, 2}),
	M_HALF(
			new int[]{6, 7, 8, 9},
			new int[]{0, 0, 0, 0}),


	P_TIL(
			new int[]{3, 4, 5},
			new int[]{3, 3, 3}),
	P_PAST(
			new int[]{6, 7, 8, 9},
			new int[]{3, 3, 3, 3}),
	P_OCLOCK(
			new int[]{5, 6, 7, 8, 9, 10},
			new int[]{10, 10, 10, 10, 10, 10});

	final int size;
	final int[] xOf;
	final int[] yOf;
	private boolean active = false;

	WordEnum(int[] xOf, int[] yOf)
	{
		if (xOf.length != yOf.length)
		{
			throw new IllegalArgumentException("Problem with loading!");
		}
		size = (xOf.length + yOf.length) / 2;
		this.xOf = xOf;
		this.yOf = yOf;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean isActive)
	{
		active = isActive;
	}
}
