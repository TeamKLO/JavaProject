package projectpackage;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class test {
 public static void main(String[] a) {
	LocalDateTime now = LocalDateTime.now();
	DateTimeFormatter b = DateTimeFormatter.ofPattern("HH:mm:dd");
	now.format(b);

	 System.out.println(now.format(b));
 }
}
