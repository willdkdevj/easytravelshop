/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.infotera.easytravel;

import br.com.infotera.common.util.Utils;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author William Dias
 */
@SpringBootApplication
public class EasyTravelShopApplication {

    public static String nrVersao = "Easy Travel Shop ON versão ";

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        Utils.setTpAmbiente("H");
        if (args.length > 0)
            Utils.setTpAmbiente(args[0]);

        nrVersao += EasyTravelShopApplication.class.getPackage().getImplementationVersion();
        SpringApplication.run(EasyTravelShopApplication.class, args);
    }
}
