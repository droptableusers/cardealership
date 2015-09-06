package io.interact.cardealership.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

	private String brand;
	private String model;
	private int engineSize;
	private String fuelType;
	private String bodyStyle;
	private String transmission;
	private float fuelEconomy;
	private int emissions;
	private int emissionClass;
	private int power;
	private int doors;
	private int seats;
	private String interior;
	private String interiorColour;
	private int price;
	private String exteriorColour;
	private String registration;
	private int mileage;
}
