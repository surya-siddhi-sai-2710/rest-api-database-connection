package com.example.demo.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Cars;
import com.example.demo.service.CarService;


@Component
public class CarsRoute extends RouteBuilder{
	
	@Autowired
	private CarService carService;		//bean name
	
	@Override
	public void configure() throws Exception{
		
		restConfiguration().bindingMode(RestBindingMode.auto);
		
		rest("/cars")
		//route 1
		.get("/view/all").to("direct:view-all-cars")
		//route 2
		.get("/view/{modelno}").to("direct:view-car-by-modelno")
		//route 3
		.post("/add").type(Cars.class).consumes("application/json").to("direct:add-car");
		
		from("direct:view-all-cars")
		.log("something-${body}")
//		.bean(carService,"viewAllCars");
		.to("bean:carService?method=viewAllCars");
		from("direct:view-car-by-modelno").bean(carService,"viewCarByModelno(${header.modelno})");
		from("direct:add-car").log("${body}").bean(carService,"addCars");
	}
}
