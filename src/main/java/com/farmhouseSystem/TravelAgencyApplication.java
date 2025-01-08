package com.farmhouseSystem;

//import com.lvxing.travel_agency.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@Slf4j
@ServletComponentScan
public class TravelAgencyApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelAgencyApplication.class, args);

        log.info("项目启动成功....");
    }

}
