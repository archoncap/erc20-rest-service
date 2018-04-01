package io.blk.erc20.Controller;

import io.blk.erc20.ContractService;
import io.blk.erc20.Request.ConsumerRequest;
import io.blk.erc20.Request.ContractSpecification;
import io.blk.erc20.Response.ConsumerEventResponse;
import io.blk.erc20.Response.TransactionResponse;
import io.blk.erc20.Response.TransferEventResponse;
import io.blk.erc20.Service.ConsumerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api("ERC-20 token standard API")
@RestController
@RequestMapping("/consumer")
public class ConsumerController {
    private final ConsumerService ConsumerService;

    @Autowired
    public ConsumerController(ConsumerService ConsumerService) {
        this.ConsumerService = ConsumerService;
    }

    @ApiOperation(
            value = "Deploy Consumer Contract",
            notes = "Returns hex encoded contract address")
    @RequestMapping(value = "/deploy", method = RequestMethod.POST)
    String deploy(
            HttpServletRequest request) throws Exception {
        return ConsumerService.deploy();
    }


    @ApiOperation("Get token name")
    @RequestMapping(value = "/{contractAddress}/callContract", method = RequestMethod.GET)
    TransactionResponse<ConsumerEventResponse> callContract(@PathVariable String contractAddress) throws Exception {
        return ConsumerService.callContract(contractAddress);
    }

    @ApiOperation(
            value = "CallInfoFeed Contract",
            notes = "Returns hex encoded contract address")
    @RequestMapping(value = "/setFeed", method = RequestMethod.POST)
    String setFeed(
            HttpServletRequest request,
            @RequestBody ConsumerRequest consumerRequest) throws Exception {
        return ConsumerService.setFeed(
                consumerRequest.getConsumerAddr(),
                consumerRequest.getInfoFeedAddr());
    }
}
