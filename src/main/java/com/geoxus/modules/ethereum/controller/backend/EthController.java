package com.geoxus.modules.ethereum.controller.backend;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("backendEthController")
@RequestMapping("/eth/backend")
@ConditionalOnExpression(value = "${eth-enable:true}")
public class EthController {

}