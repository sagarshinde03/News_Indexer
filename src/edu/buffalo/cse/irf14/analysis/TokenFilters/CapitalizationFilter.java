package edu.buffalo.cse.irf14.analysis.TokenFilters;

import java.util.regex.Pattern;

import edu.buffalo.cse.irf14.analysis.Token;
import edu.buffalo.cse.irf14.analysis.TokenFilter;
import edu.buffalo.cse.irf14.analysis.TokenStream;
import edu.buffalo.cse.irf14.analysis.TokenizerException;

public class CapitalizationFilter extends TokenFilter {

	private static Pattern patternCaps = Pattern.compile("[A-Z]+");
	private static Pattern patternNotCaps = Pattern.compile("[^A-Z]+");
	private static Pattern patternCamel =Pattern.compile("[A-Z][a-z]+");

	public CapitalizationFilter(TokenStream stream) {
		super(stream);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void applyFilter() throws TokenizerException {
		// TODO Auto-generated method stub
		String currentToken = getCurrentStream().getCurrent().getTermText();
		try
		{
			if( patternCaps.matcher(currentToken).matches() ) {
				boolean isAllCaps = true;

				Token strPrevious = getCurrentStream().previous();
				if( strPrevious != null ) {
					if(patternNotCaps.matcher(strPrevious.getTermText()).matches()){
						isAllCaps = false;
						getCurrentStream().next();
					}else {
						getCurrentStream().next();
						Token strNext = getCurrentStream().next();
						if( strNext!=null && patternNotCaps.matcher(strNext.getTermText()).matches()) {
							isAllCaps = false;
							getCurrentStream().previous();
						}
					}
				} else {
					Token strNext = getCurrentStream().next();
					if( strNext!=null && patternNotCaps.matcher(strNext.getTermText()).matches()) {
						isAllCaps = false;
						getCurrentStream().previous();
					}
				}
				if(isAllCaps) {
					getCurrentStream().getCurrent().setTermText(
							getCurrentStream().getCurrent().getTermText().toLowerCase());
				}
			}
			else if( patternCamel.matcher(currentToken).matches() ) {
				Token strPrevious = getCurrentStream().previous();
				if( strPrevious!=null && !strPrevious.getTermText().endsWith(".") ) {
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(getCurrentStream().next());
					if(patternCamel.matcher(getCurrentStream().next().getTermText()).matches()){
						strBuffer.append(" "+getCurrentStream().getCurrent().getTermText());
						getCurrentStream().remove();
						getCurrentStream().previous(); 
						getCurrentStream().getCurrent().setTermText(strBuffer.toString());
					} else {
						getCurrentStream().previous();
						getCurrentStream().getCurrent().setTermText(
								getCurrentStream().getCurrent().getTermText().toLowerCase());
					}	
				} else if( strPrevious==null ) {
					getCurrentStream().getCurrent().setTermText(
							getCurrentStream().getCurrent().getTermText().toLowerCase());
				} else {
					getCurrentStream().next();
					getCurrentStream().getCurrent().setTermText(
							getCurrentStream().getCurrent().getTermText().toLowerCase());
				}
			}
		} catch ( Exception exception ) {
			
		}
	}

}
