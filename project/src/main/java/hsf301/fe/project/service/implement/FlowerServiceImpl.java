package hsf301.fe.project.service.implement;

import hsf301.fe.project.pojo.Flower;
import hsf301.fe.project.repository.FlowerRepository;
import hsf301.fe.project.service.defines.IFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FlowerServiceImpl implements IFlowerService {

    @Autowired
    private FlowerRepository flowerRepository;


    // Chỉ đọc, không cần transaction ghi
    @Override
    @Transactional(readOnly = true)
    public List<Flower> getAllFlowers() {
        return flowerRepository.findAll();
    }

    // Chỉ đọc, không cần transaction ghi
    @Override
    @Transactional(readOnly = true)
    public Optional<Flower> getFlowerById(int flowerId) {
        return flowerRepository.findById(flowerId);
    }

    // Thêm mới, cần transaction để đảm bảo dữ liệu được ghi đúng
    @Override
    @Transactional
    public Flower saveFlower(Flower flower) {
        return flowerRepository.save(flower);
    }

    // Cập nhật, cần transaction để đảm bảo dữ liệu được ghi đúng hoặc hoàn tác khi lỗi
    @Override
    @Transactional
    public Flower updateFlower(int flowerId, Flower flowerDetails) {
        Optional<Flower> flowerOptional = flowerRepository.findById(flowerId);
        if (flowerOptional.isPresent()) {
            Flower flower = flowerOptional.get();
            flower.setName(flowerDetails.getName());
            flower.setType(flowerDetails.getType());
            flower.setQuantity(flowerDetails.getQuantity());
            flower.setCondition(flowerDetails.getCondition());
            flower.setPrice(flowerDetails.getPrice());
            flower.setSeller(flowerDetails.getSeller());
            return flowerRepository.save(flower);
        }
        return null; // hoặc throw exception nếu muốn xử lý lỗi
    }

    // Xóa, cần transaction để đảm bảo tính nhất quán dữ liệu
    @Override
    @Transactional
    public void deleteFlower(int flowerId) {
        flowerRepository.deleteById(flowerId);
    }
}
