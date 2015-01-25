package edu.buffalo.cse.irf14.analysis.TokenFilters;

import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

public class SpecialCharsFilter extends TokenFilter {

	private static Pattern patternWS = Pattern.compile("[^\\w\\s\\-.:]");
	private static Pattern patternDHD =Pattern.compile("[\\d]+\\-[\\d]+");
	private static Pattern patternTime = Pattern.compile("[\\d]+:[\\d]+:[\\d]+");

	public SpecialCharsFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub

	}

	public void applyFilter() throws TokenizerException
	{
		try
		{
			String str = getCurrentStream().getCurrent().getTermText();
			if(str != null && str.length()!=0) {
				str = patternWS.matcher(str).replaceAll("").replaceAll("_", "").replaceAll("$", "");
				if( str.indexOf("-") !=0 && !patternDHD.matcher(str).matches() ) {
					str = str.replaceAll("-", "");
				}
				//if it is in time format.
				if( !patternTime.matcher(str).matches() ){
					str = str.replace(":", "");
				}
				getCurrentStream().getCurrent().setTermText(str);
			}
		}
		catch(Exception e){

		}
	}

}
