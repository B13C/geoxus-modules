package com.geoxus.modules.ethereum.controller.frontend;

import cn.hutool.core.lang.Dict;
import com.geoxus.core.common.util.GXResultUtils;
import com.geoxus.modules.ethereum.service.UWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController("frontendEthController")
@RequestMapping("/eth/frontend")
@ConditionalOnExpression(value = "${eth-enable:true}")
public class EthController {
    @Autowired
    private UWalletService uWalletService;

    @PostMapping("/get-block-chain-balance")
    public GXResultUtils getBlockChainBalance(@RequestBody Dict param) {
        if (null == param.getStr("secret") || !"brt159357macron258963147".equalsIgnoreCase(param.getStr("secret"))) {
            return GXResultUtils.ok().putData(Dict.create());
        }
        final Dict chainBalance = uWalletService.getBlockChainBalance(param);
        return GXResultUtils.ok().putData(chainBalance);
    }

    @GetMapping("/hello")
    public String hello() throws URISyntaxException {
        return "Hello";
    }
}
