import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbElement;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;


public class DigestAuthentication_ComputeCookie extends MbJavaComputeNode {

	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");
		MbOutputTerminal alt = getOutputTerminal("alternate");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			
			MbElement mb = inMessage.getRootElement();
			MbElement ResponseElement = mb.getFirstElementByPath("HTTPResponseHeader").getFirstChild();
			String SetCookie="NoCookies";
			SetCookie= getcookies(ResponseElement);
			MbMessage outmb = outAssembly.getGlobalEnvironment();
		    outmb.evaluateXPath("?Cookies[set-value('" + SetCookie	+ "')]");
		
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			System.out.print(e);
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(),
					null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}
	 public static String getcookies(MbElement WWWAuthenticationElement) {
			String CookiesVal="NoCookies";
			String nodeName="";
			String mb1="";
			 
				
				try {
			
					while(WWWAuthenticationElement != null){
						 nodeName = WWWAuthenticationElement.getName();
						if ("Set-Cookie".equalsIgnoreCase(nodeName)){
							mb1 = WWWAuthenticationElement.getValueAsString().toString();
							CookiesVal = mb1.substring(0, mb1.indexOf(";"));
					        }
						WWWAuthenticationElement=WWWAuthenticationElement.getNextSibling();
					}


				} catch (MbException e) {
					e.printStackTrace();
				}
		 
	
			return CookiesVal;
	 }
}
