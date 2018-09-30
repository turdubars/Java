import java.math.*;
import java.util.*;

public class BigIntMain {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		try {
			BigInt a = new BigInt(scan.next());
			BigInt b = new BigInt(scan.next());
			System.out.println(a.divide(b));

		} catch (Exception e) {
			System.out.println("Incorrect BigInt");
		}
	}

}
