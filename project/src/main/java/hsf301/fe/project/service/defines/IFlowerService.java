package hsf301.fe.project.service.defines;

import hsf301.fe.project.pojo.Flower;

import java.util.List;

public interface IFlowerService {

    List<Flower> getAllFlowers();
    List<Flower> getFlowersBySellerId(int sellerId);

    Flower getFlowerById(int flowerId);

    Flower saveFlower(Flower flower);
    Flower updateFlower(int flowerId, Flower flowerDetails);

    void deleteFlower(int flowerId);

}
