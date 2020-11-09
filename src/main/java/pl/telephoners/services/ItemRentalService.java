//package pl.telephoners.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//import pl.telephoners.models.ItemRentalInfo;
//import pl.telephoners.repositories.ItemRentalInfoRepository;
//
//import java.time.LocalDate;
//
//@Service
//public class ItemRentalService {
//
//    @Autowired
//    private ItemRentalInfoRepository itemRentalInfoRepository;
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void start(){
//        ItemRentalInfo itemRentalInfo = new ItemRentalInfo();
////        itemRentalInfo.setFrom(LocalDate.now());
////        itemRentalInfo.setTo(LocalDate.now());
//        itemRentalInfoRepository.save(itemRentalInfo);
//
//    }
//
//
//}
