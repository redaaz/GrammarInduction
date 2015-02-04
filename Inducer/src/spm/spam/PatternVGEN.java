package spm.spam;

/**
 * Implementation of a pattern found by the VGEN algorithm.
 * <br/><br/>
 * 
 * Copyright (c) 2013 Philippe Fournier-Viger, Antonio Gomariz
 *  <br/><br/>
 *  
 * This file is part of the SPMF DATA MINING SOFTWARE
 * (http://www.philippe-fournier-viger.com/spmf).
 * 
 *  <br/><br/>
 * SPMF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <br/><br/>
 * 
 * SPMF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <br/><br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with SPMF. If not, see <http://www.gnu.org/licenses/>.
 * 
 * @see AlgoVGEN
*  @see Prefix
*  @author Philippe Fournier-Viger  & Antonio Gomariz
 */
public class PatternVGEN implements Comparable<PatternVGEN>{
	
	public PrefixVGEN getPrefix() {
		return prefix;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	PrefixVGEN prefix;
	public Bitmap bitmap;

	public PatternVGEN(PrefixVGEN prefix, Bitmap bitmap) {
		this.prefix = prefix;
		this.bitmap = bitmap;
//		if(prefix != null){
//			System.out.println("pat prefix" + prefix.toString() + " " + support);
//		}
	}

	public int compareTo(PatternVGEN o) {
		if(o == this){
			return 0;
		}
//		int compare = o.prefix.sumOfEvenItems + o.prefix.sumOfOddItems
//				- this.prefix.sumOfEvenItems - this.prefix.sumOfOddItems;
		int compare = o.bitmap.sidsum
				- this.bitmap.sidsum;
		if(compare !=0){
			return compare;
		}

		return this.hashCode() - o.hashCode();
	}

	public int getAbsoluteSupport() {
		return bitmap.getSupport();
	}

}
