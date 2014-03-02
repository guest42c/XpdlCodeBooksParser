package br.com.samuraidev.XpdlParser;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		XpdlParser parser = new XpdlParser();
		System.out.println("\n\nadvise_of_charge.xpdl");
		parser.parse("resources/xpdl/advise_of_charge.xpdl");

		System.out.println("\n\nbest_path_forecast.xpdl");
		parser.parse("resources/xpdl/best_path_forecast.xpdl");

		System.out.println("\n\nbill_delayed_payment.xpdl");
		parser.parse("resources/xpdl/bill_delayed_payment.xpdl");

		System.out.println("\n\nbilling_advice_of_charge.xpdl");
		parser.parse("resources/xpdl/billing_advice_of_charge.xpdl");
	}
}
