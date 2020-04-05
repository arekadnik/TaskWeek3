package stadnik.arkadiusz.week3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import stadnik.arkadiusz.week3.model.Car;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//
//@RestController
//@RequestMapping("/cars")
@Controller
public class CarController {

    private List<Car> carList;

    public CarController() {
        carList = new ArrayList<>();
        carList.add(new Car(1, "Opel", "Vectra", "blue"));
        carList.add(new Car(2, "Seat", "Ibiza", "white"));
        carList.add(new Car(3, "Citroen", "c4", "red"));
    }

    @GetMapping("/cars")
    public String getCarList(Model model) {
        model.addAttribute("cars", carList);
        return "carsList";
    }

    @GetMapping(value = "cars/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public String getCardByID(@PathVariable long id, Model model) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            model.addAttribute("cars", first);
            return "carsList";
        }
        return "emptyList";
    }

    @GetMapping(value = "color/{color}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> getCarByColor(@PathVariable String color) {
        for (Car car : carList) {
            if (car.getColor().equals(color)) {
                return new ResponseEntity<Car>(car, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> addNewCar(@RequestBody Car car) {
        boolean add = carList.add(car);
        if (add) {
            return new ResponseEntity<Car>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Car>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> modifyCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity<Car>(newCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping(value = "/modify/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> changeCarParam(@RequestBody String color, @PathVariable int id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            first.get().setColor(color);
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }

    @DeleteMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Car> removeCarByID(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
