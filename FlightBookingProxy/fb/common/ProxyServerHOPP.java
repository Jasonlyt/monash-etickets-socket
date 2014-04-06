package fb.common;

import java.io.IOException;

public class ProxyServerHOPP implements ProxyServerSevice {
	protected ProxyClientHOPP pcHOPPtoCea, pcHOPPtoAc, pcHOPPtoQan;

	public ProxyServerHOPP() {
		String server = "localhost";
		pcHOPPtoCea = null;
		pcHOPPtoAc = null;
		pcHOPPtoQan = null;
		try {
			pcHOPPtoCea = new ProxyClientHOPP(server,FlightBookingConstants.PORT_CEA);
			pcHOPPtoAc = new ProxyClientHOPP(server,FlightBookingConstants.PORT_AC);
			pcHOPPtoQan = new ProxyClientHOPP(server,FlightBookingConstants.PORT_QAN);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] queryResp(String str) {
		String[] ceaResp = pcHOPPtoCea.queryReq(str);
		String[] acResp = pcHOPPtoAc.queryReq(str);
		String[] qanResp = pcHOPPtoQan.queryReq(str);
		String[] result = new String[ceaResp.length+acResp.length+qanResp.length];
		System.arraycopy(ceaResp, 0, result, 0, ceaResp.length);
		System.arraycopy(acResp, 0, result, ceaResp.length, acResp.length);
		System.arraycopy(qanResp, 0, result, ceaResp.length+acResp.length, qanResp.length);
		return result;
	}

	@Override
	public boolean regResp(String str) {
		boolean flag = false;
		if(str.toUpperCase().startsWith(FlightBookingConstants.CEA))
			flag = pcHOPPtoCea.regReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.AC))
			flag = pcHOPPtoAc.regReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.QAN))
			flag = pcHOPPtoQan.regReq(str);
		else{
			System.out.println("Unrecognized Airline!");
		}
		return flag;
	}

	@Override
	public boolean orderResp(String str) {
		boolean flag = false;
		if(str.toUpperCase().startsWith(FlightBookingConstants.CEA))
			flag = pcHOPPtoCea.orderReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.AC))
			flag = pcHOPPtoAc.orderReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.QAN))
			flag = pcHOPPtoQan.orderReq(str);
		else{
			System.out.println("Unrecognized FID!");
		}
		return flag;
	}

	@Override
	public String[] checkOrders(String str) {
		String[] result = null;
		if(str.toUpperCase().startsWith(FlightBookingConstants.CEA))
			result = pcHOPPtoCea.checkReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.AC))
			result = pcHOPPtoAc.checkReq(str);
		else if(str.toUpperCase().startsWith(FlightBookingConstants.QAN))
			result = pcHOPPtoQan.checkReq(str);
		else
			System.out.println("Unrecognized airline! >>>"+str+"<<<");
		return result;
	}

	@Override
	public void quit() {
		pcHOPPtoCea.quit();
		pcHOPPtoAc.quit();
		pcHOPPtoQan.quit();
	}

}
