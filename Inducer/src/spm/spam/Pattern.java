package spm.spam;

/**
 * Implementation of a pattern found by the TKS algorithm.
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
 * @see AlgoTKS
*  @see Prefix
*  @author Philippe Fournier-Viger  & Antonio Gomariz
 */
public class Pattern implements Comparable<Pattern>{
	
	Prefix prefix;
	int support;

	public Pattern(Prefix prefix, int suppport) {
		this.prefix = prefix;
		this.support = suppport;
//		if(prefix != null){
//			System.out.println("pat prefix" + prefix.toString() + " " + support);
//		}
	}

	public int compareTo(Pattern o) {
		if(o == this){
			return 0;
		}
		int compare = this.support - o.support;
		if(compare !=0){
			return compare;
		}

		return this.hashCode() - o.hashCode();
	}

}
