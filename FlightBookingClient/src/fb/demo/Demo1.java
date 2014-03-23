package fb.demo;

public class Demo1 {
	public static void main(String[] args) {
		String line = "query shanghai melbourne";
		losePrefix(line,"query");
	}

	public static String losePrefix(String str, String prefix){
		int index = prefix.length();
		String ret = str.substring(index).trim();
		System.out.println(ret);
		String[] city = ret.split(" ");
		for (String s : city) {
			System.out.println(s);
		}
		return ret;
	}
}