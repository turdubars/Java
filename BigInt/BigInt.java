import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BigInt {
	private ArrayList<Integer> digits;
	private boolean isPositive = true;

	public BigInt() {
		digits = new ArrayList<Integer>();
	}

	public BigInt(String s) throws Exception {
		digits = new ArrayList<Integer>();

		String number;
		// checking the sign of a number
		if (s.charAt(0) == '-') {
			isPositive = false;
			number = s.substring(1);
		} else if (s.charAt(0) == '+') {
			isPositive = true;
			number = s.substring(1);
		} else {
			isPositive = true;
			number = s;
		}
		// adding string of number into ArrayList
		for (int i = 0; i < number.length(); i++) {
			char ch = number.charAt(i);
			if (ch < '0' || ch > '9') {
				throw new Exception("Incorrect BigInt: " + s);
			}
			digits.add(ch - '0');
		}
	}
	@Override
	public String toString() {
		StringBuilder r = new StringBuilder();
		for (Integer d : digits) {
			r.append(d);
		}
		return (isPositive) ? r.toString() : "-" + r.toString();
	}

	public BigInt add(BigInt other) throws Exception {
		BigInt result = new BigInt();

		identicalSize(other);
		// if signs are equal
		if (isPositive == other.isPositive) {
			result.isPositive = isPositive;
			int carry = 0;
			for (int i = digits.size() - 1; i >= 0; i--) {
				int s = carry;
				s += digits.get(i) + other.digits.get(i);
				carry = s / 10;
				result.digits.add(0, s % 10);
			}
			if (carry != 0) {
				result.digits.add(0, carry);
			}
		}
		// comparing absolute values
		else {
			BigInt bigger = this;
			BigInt smaller = other;
			result.isPositive = isPositive;
			boolean otherSign = other.isPositive;
			this.isPositive = true;
			other.isPositive = true;
			if (compareTo(other) < 0) {
				bigger = other;
				smaller = this;
				result.isPositive = otherSign;
			}
			int carry = 0;
			for (int i = bigger.digits.size() - 1; i >= 0; i--) {
				int s1 = bigger.digits.get(i);
				s1 -= carry;
				carry = 0;
				int s2 = smaller.digits.get(i);
				if (s1 < s2) {
					s1 += 10;
					carry = 1;
				}
				s1 -= s2;
				result.digits.add(0, s1);
			}
		}
		removeZero(result);
		return result;
	}

	public BigInt subtract(BigInt other) throws Exception {
		// subtracting means to change the sign of second number
		changeSign(other);
		return add(other);
	}

	public BigInt multiply(BigInt other) throws Exception {
		// checking bigger number by absolute value
		BigInt result = new BigInt();
		BigInt bigger = this;
		BigInt smaller = other;
		boolean thisSign = isPositive;
		boolean otherSign = other.isPositive;
		isPositive = true;
		other.isPositive = true;
		if (compareTo(other) < 0) {
			bigger = other;
			smaller = this;
		}
		ArrayList<Integer> product;
		BigInt temp = new BigInt();
		for (int i = smaller.digits.size() - 1; i >= 0; i--) {
			int carry = 0;
			product = new ArrayList<Integer>();
			// shifting to left by adding zero
			for (int z = i; z < smaller.digits.size() - 1; z++) {
				product.add(0);
			}
			for (int j = bigger.digits.size() - 1; j >= 0; j--) {
				int mult = carry;
				mult += bigger.digits.get(j) * smaller.digits.get(i);
				carry = mult / 10;
				product.add(0, mult % 10);
			}
			if (carry != 0)
				product.add(0, carry);
			temp.digits = product;
			result = result.add(temp);
		}
		// checking signs
		if (thisSign == otherSign) {
			result.isPositive = true;
		} else {
			result.isPositive = false;
		}
		return result;
	}

	public BigInt divide(BigInt other) throws Exception {
		if (other.digits.get(0) == 0) {
			throw new ArithmeticException("division by zero");
		}
		// saving signs to use them for answer
		boolean thisSign = isPositive;
		boolean otherSign = other.isPositive;
		// making signs positive to make it easy for comparing and checking characters
		isPositive = true;
		other.isPositive = true;

		BigInt result = new BigInt();
		BigInt part = new BigInt();
		
		boolean isDivisible = false; // case for adding zeros
		for (int i = 0; i < this.digits.size(); i++) {
			part.digits.add(digits.get(i));
			removeZero(part);
			if (part.compareTo(other) < 0) {
				isDivisible = true;
			}
			boolean isBigger = part.compareTo(other) >= 0;
			// adding zeros by checking cases
			if (isDivisible && !isBigger) {
				result.digits.add(0);
			} else {
				isBigger = part.compareTo(other) >= 0;
				if (isBigger) {
					isDivisible = false;
					int counter = 0;
					while (isBigger) {
						part = part.subtract(other);
						removeZero(other);
						counter++;
						isBigger = part.compareTo(other) >= 0;
					}
					// if there is no remainder make it true
					if (part.digits.get(0) == 0) {
						isDivisible = true;
					}
					result.digits.add(counter);
				}
			}
		}
		removeZero(result);
		if (thisSign == otherSign)
			result.isPositive = true;
		else
			result.isPositive = false;
		return result;
	}
	
	public BigInt remainder(BigInt other) throws Exception {
		BigInt product = this.divide(other);
		BigInt productOfAnswer = product.multiply(other);
		return this.subtract(productOfAnswer);
	}

	public void changeSign(BigInt b) {
		if (b.isPositive) {
			b.isPositive = false;
		} else {
			b.isPositive = true;
		}
	}

	// checking sizes of numbers and make them equal by adding zeros
	public void identicalSize(BigInt other) {
		removeZero(this);
		removeZero(other);
		while (this.digits.size() != other.digits.size()) {
			if (this.digits.size() < other.digits.size())
				digits.add(0, 0);
			if (other.digits.size() < this.digits.size())
				other.digits.add(0, 0);
		}
	}

	public void removeZero(BigInt number) {
		for (int i = 0; i < number.digits.size(); i++) {
			if (number.digits.get(i) == 0) {
				if (number.digits.size() == 1) {
					number.isPositive = true;
					break;
				}
				number.digits.remove(i);
				i--;
			} else {
				break;
			}
		}
	}

	public int compareTo(BigInt other) {
		int result = 0;

		if (isPositive && !other.isPositive)
			return 1;
		else if (!isPositive && other.isPositive)
			return -1;
		else {
			if (digits.size() < other.digits.size()) {
				result = -1;
			} else if (digits.size() > other.digits.size()) {
				result = 1;
			} else {
				for (int i = 0; i < digits.size(); i++) {
					int compared = Integer.compare(digits.get(i), other.digits.get(i));
					if (compared != 0) {
						result = compared;
						break;
					}
				}
			}
			if (isPositive)
				return result;
			else
				return -result;
		}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		try {
			BigInt a = new BigInt(scan.next());
			BigInt b = new BigInt(scan.next());
			System.out.println(a.remainder(b));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
