package chargingstation;

import chargingstation.config.kafka.KafkaProcessor;

// import java.text.DateFormat;
// import java.text.SimpleDateFormat;
// import java.util.Calendar;

// import com.fasterxml.jackson.databind.DeserializationFeature;
// import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//import ch.qos.logback.core.joran.util.beans.BeanUtil;

@Service
public class PolicyHandler{
    @Autowired 
    ManagementRepository managementRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayFinished_Mount(@Payload PayFinished payFinished){
        // Payment로 부터 결재 완료 정보를 받는다.
        if(!payFinished.validate()) return;

        System.out.println("$$$$$ listener Mount : " + payFinished.toJson() + "$$$$$");

        if (payFinished.getOrderStatus().equals("PAY_FINISHED")) {
            Management management = new Management();
            BeanUtils.copyProperties(payFinished, management);
            management.setOrderStatus("MOUNT_REQUESTED");
            managementRepository.save(management);

            System.out.println("$$$$$ wheneverPayFinished_Mount $$$$$");
        }

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCanceled_MountCancel(@Payload PayCanceled payCanceled){
        // Payment로 부터 결재 취소 정보를 받는다.
        if(!payCanceled.validate()) return;

        System.out.println("$$$$$ listener MountCancel : " + payCanceled.toJson() + "$$$$$");

        if (payCanceled.getOrderStatus().equals("PAY_CANCELED")){
            Management management = managementRepository.findByOrderId(payCanceled.getOrderId());

            if (management != null) {
                //management.setOrderStatus("MOUNT_CANCEL_REQUESTED");
                managementRepository.delete(management);

                System.out.println("$$$$$ wheneverPayCanceled_MountCancel $$$$$");
            }
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}