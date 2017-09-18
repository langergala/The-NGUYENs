package mockdemo;

import java.time.LocalDate;

public class DateFactory implements IDateFactory {

	@Override
	public LocalDate now() {
		return LocalDate.now();
	}

}
