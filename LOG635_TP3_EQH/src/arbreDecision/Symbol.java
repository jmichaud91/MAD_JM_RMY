package arbreDecision;

// Class for the symbol ">", "<", ">=" or "<="
public class Symbol
{
	boolean isRightBranch; 
	String symbol;
	public Symbol(String symbol)
	{
		this.symbol = symbol;
		
	}
	
	public boolean hasEquality()
	{
		return symbol.contains("=");
	}
	
	public boolean isGreaterThan()
	{
		return symbol.contains(">");
	}
	
	public boolean isRightBranch()
	{
		return isRightBranch;
	}
	public void setIsRightBranch(boolean isRightBranch)
	{
		this.isRightBranch = isRightBranch;
	}
	
	public String getStringValue()
	{
		return symbol;
	}
}
