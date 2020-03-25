package stadnik.arkadiusz.week3;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    public CarApi() {
        carList = new ArrayList<>();
        carList.add(new Car(1, "Opel", "Vectra", "blue"));
        carList.add(new Car(2, "Seat", "Ibiza", "white"));
        carList.add(new Car(3, "Citroen", "c4", "red"));
    }

    @GetMapping
    public ResponseEntity<List<Car>> getCars() {
        return new ResponseEntity<>(carList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCardByID(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("color/{color}")
    public ResponseEntity<Car> getCarByColor(@PathVariable String color) {
        for (Car car : carList) {
            if (car.getColor().equals(color)) {
                return new ResponseEntity<Car>(car, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Car> addNewCar(@RequestBody Car car) {
        boolean add = carList.add(car);
        if (add) {
            return new ResponseEntity<Car>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Car>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<Car> modifyCar(@RequestBody Car newCar) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity<Car>(newCar, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/modify//{id}")
    public ResponseEntity<Car> changeCarParam(@RequestBody String modifyCarColor, @PathVariable int id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            first.get().setColor(modifyCarColor);
            return new ResponseEntity<Car>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Car> removeCarByID(@PathVariable long id) {
        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
